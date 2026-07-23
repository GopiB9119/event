import { createRequire } from "node:module";
import { readFileSync } from "node:fs";
import path from "node:path";
import { pathToFileURL } from "node:url";

const argumentsByName = parseArguments(process.argv.slice(2));
const repositoryRoot = path.resolve(requireArgument(argumentsByName, "repository-root"));
const schemaPath = confinedFile(repositoryRoot, requireArgument(argumentsByName, "schema"));
const documentPath = confinedFile(repositoryRoot, requireArgument(argumentsByName, "document"), true);
const requireFromToolchain = createRequire(
  pathToFileURL(path.join(repositoryRoot, "firebase", "functions", "package.json"))
);
const Ajv2020 = requireFromToolchain("ajv/dist/2020").default;
const ajvPackage = requireFromToolchain("ajv/package.json");

if (ajvPackage.version !== "8.20.0") {
  throw new Error(`AEOS schema validation requires Ajv 8.20.0; found ${ajvPackage.version}.`);
}

const schema = parseJson(schemaPath, "schema");
const document = parseJson(documentPath, "document");
const ajv = new Ajv2020({ allErrors: true, strict: true });
const validate = ajv.compile(schema);

if (!validate(document)) {
  const errors = validate.errors
    .slice(0, 20)
    .map((error) => `${error.instancePath || "/"} ${error.message}`)
    .join("\n");
  throw new Error(`AEOS behavior schema validation failed:\n${errors}`);
}

process.stdout.write("AEOS behavior catalog schema validation passed.\n");

function parseArguments(args) {
  const values = new Map();
  for (let index = 0; index < args.length; index += 2) {
    const name = args[index];
    const value = args[index + 1];
    if (!name?.startsWith("--") || value == null) {
      throw new Error("Arguments must be supplied as --name value pairs.");
    }
    const key = name.slice(2);
    if (values.has(key)) throw new Error(`Duplicate argument --${key}.`);
    values.set(key, value);
  }
  return values;
}

function requireArgument(values, name) {
  const value = values.get(name);
  if (!value) throw new Error(`Missing required argument --${name}.`);
  return value;
}

function confinedFile(root, requestedPath, allowTemporary = false) {
  const resolved = path.resolve(requestedPath);
  const repositoryPrefix = `${root}${path.sep}`.toLowerCase();
  const temporaryPrefix = `${path.resolve(process.env.TEMP || process.env.TMP || "")}${path.sep}`.toLowerCase();
  const normalized = resolved.toLowerCase();
  if (!normalized.startsWith(repositoryPrefix) && !(allowTemporary && normalized.startsWith(temporaryPrefix))) {
    throw new Error(`Schema input escapes its allowed root: ${requestedPath}`);
  }
  return resolved;
}

function parseJson(filePath, label) {
  try {
    return JSON.parse(readFileSync(filePath, "utf8"));
  } catch (error) {
    throw new Error(`Could not parse ${label} JSON at ${filePath}: ${error.message}`);
  }
}