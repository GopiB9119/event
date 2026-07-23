import { spawnSync } from "node:child_process";
import { randomBytes } from "node:crypto";
import { existsSync, readFileSync, unlinkSync, writeFileSync } from "node:fs";
import { fileURLToPath } from "node:url";
import path from "node:path";

const scriptsDirectory = path.dirname(fileURLToPath(import.meta.url));
const functionsDirectory = path.resolve(scriptsDirectory, "..");
const firebaseCli = path.join(
  functionsDirectory,
  "node_modules",
  "firebase-tools",
  "lib",
  "bin",
  "firebase.js"
);
const firebaseConfig = path.resolve(functionsDirectory, "..", "..", "firebase.json");
const localEnvironmentFile = path.join(functionsDirectory, ".env.local");
const originalLocalEnvironment = existsSync(localEnvironmentFile)
  ? readFileSync(localEnvironmentFile)
  : null;
const emulatorPidsBeforeRun = listWindowsTestEmulatorPids();
const inviteTokenKey = randomBytes(32).toString("hex");
const duplicateHashKey = randomBytes(32).toString("hex");
const executionMode = process.argv[2] || "backend";
const testCommand = commandForMode(executionMode);
const {
  FIREBASE_CONFIG: ignoredFirebaseConfig,
  GCLOUD_PROJECT: ignoredGcloudProject,
  GOOGLE_APPLICATION_CREDENTIALS: ignoredGoogleCredentials,
  GOOGLE_CLOUD_PROJECT: ignoredGoogleCloudProject,
  PRESENCE_DATABASE_URL: ignoredPresenceDatabaseUrl,
  ...sanitizedEnvironment
} = process.env;
const pathEnvironmentKey = Object.keys(sanitizedEnvironment)
  .find((key) => key.toLowerCase() === "path") || "PATH";
sanitizedEnvironment[pathEnvironmentKey] = [
  path.dirname(process.execPath),
  sanitizedEnvironment[pathEnvironmentKey]
].filter(Boolean).join(path.delimiter);

writeTestEnvironment(localEnvironmentFile, originalLocalEnvironment);
let result;
try {
  result = spawnSync(
    process.execPath,
    [
      firebaseCli,
      "--config",
      firebaseConfig,
      "--project",
      "demo-community-ledger",
      "emulators:exec",
      "--only",
      "auth,database,functions,firestore",
      testCommand
    ],
    {
      cwd: functionsDirectory,
      env: {
        ...sanitizedEnvironment,
        npm_config_offline: "true",
        npm_config_update_notifier: "false",
        FUNCTIONS_DISCOVERY_TIMEOUT: executionMode === "android" ? "180" : "60",
        INVITE_TOKEN_KEY: inviteTokenKey,
        DUPLICATE_HASH_KEY: duplicateHashKey
      },
      stdio: "inherit"
    }
  );
} finally {
  restoreLocalEnvironment(localEnvironmentFile, originalLocalEnvironment);
  await stopNewWindowsTestEmulators(emulatorPidsBeforeRun);
}

if (result.error) {
  throw result.error;
}
process.exit(result.status ?? 1);

function commandForMode(mode) {
  if (mode === "backend") return "npm test";
  if (mode === "android") {
    return "node scripts/runAndroidInstrumentation.js";
  }
  throw new Error(`Unsupported emulator execution mode: ${mode}`);
}

function writeTestEnvironment(filePath, originalBytes) {
  const existingLines = originalBytes == null
    ? []
    : originalBytes.toString("utf8").split(/\r?\n/u);
  const retainedLines = existingLines
    .filter((line) => !/^\s*(?:FUNCTIONS_REGION|PRESENCE_DATABASE_URL|INVITE_TOKEN_KEY|DUPLICATE_HASH_KEY)\s*=/u.test(line));
  while (retainedLines.at(-1) === "") retainedLines.pop();
  retainedLines.push("FUNCTIONS_REGION=us-central1", "");
  writeFileSync(filePath, retainedLines.join("\n"), {
    encoding: "utf8",
    mode: 0o600
  });
}

function restoreLocalEnvironment(filePath, originalBytes) {
  if (originalBytes == null) {
    if (existsSync(filePath)) unlinkSync(filePath);
    return;
  }
  writeFileSync(filePath, originalBytes, { mode: 0o600 });
}

function listWindowsTestEmulatorPids() {
  if (process.platform !== "win32") return new Set();

  const command = [
    "$processes = Get-CimInstance Win32_Process | Where-Object {",
    "  $_.Name -eq 'java.exe' -and (",
    "    $_.CommandLine -like '*cloud-firestore-emulator-v*.jar*--port 8080*--project_id demo-community-ledger*' -or",
    "    $_.CommandLine -like '*firebase-database-emulator-v*.jar*--port 9000*--functions_emulator_port 5001*'",
    "  )",
    "}",
    "$processes | Select-Object -ExpandProperty ProcessId"
  ].join("\n");
  const query = spawnSync(
    "powershell.exe",
    ["-NoProfile", "-NonInteractive", "-Command", command],
    { encoding: "utf8", windowsHide: true }
  );
  if (query.error) throw query.error;
  if (query.status !== 0) {
    throw new Error(`Could not inspect Firebase emulator processes: ${query.stderr.trim()}`);
  }

  return new Set(
    query.stdout
      .split(/\r?\n/u)
      .map((value) => Number.parseInt(value.trim(), 10))
      .filter(Number.isInteger)
  );
}

async function stopNewWindowsTestEmulators(existingPids) {
  if (process.platform !== "win32") return;

  const createdPids = [...listWindowsTestEmulatorPids()]
    .filter((pid) => !existingPids.has(pid));
  for (const pid of createdPids) {
    try {
      process.kill(pid);
    } catch (error) {
      if (error?.code !== "ESRCH") throw error;
    }
  }

  for (let attempt = 0; attempt < 20; attempt += 1) {
    const remainingPids = [...listWindowsTestEmulatorPids()]
      .filter((pid) => createdPids.includes(pid));
    if (remainingPids.length === 0) return;
    await new Promise((resolve) => setTimeout(resolve, 100));
  }
  throw new Error(`Firebase emulator processes did not stop: ${createdPids.join(", ")}`);
}