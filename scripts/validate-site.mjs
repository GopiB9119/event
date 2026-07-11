import fs from "node:fs";
import path from "node:path";

const root = path.resolve(process.argv[2] || "site");
const errors = [];

function walk(directory) {
  return fs.readdirSync(directory, { withFileTypes: true }).flatMap((entry) => {
    const target = path.join(directory, entry.name);
    return entry.isDirectory() ? walk(target) : [target];
  });
}

const files = walk(root);
const htmlFiles = files.filter((file) => file.endsWith(".html"));
const localTargetPattern = /(?:href|src)=["']([^"']+)["']/g;

for (const file of htmlFiles) {
  const html = fs.readFileSync(file, "utf8");
  for (const match of html.matchAll(localTargetPattern)) {
    const value = match[1];
    if (/^(#|https?:|mailto:|tel:|data:|javascript:)/i.test(value)) continue;

    const localPath = value.split(/[?#]/, 1)[0];
    let target = path.resolve(path.dirname(file), localPath);
    if (value.endsWith("/")) target = path.join(target, "index.html");
    if (!fs.existsSync(target)) {
      errors.push(`${path.relative(root, file)} -> missing ${value}`);
    }
  }
}

const requiredFiles = [
  "index.html",
  "privacy/index.html",
  "terms/index.html",
  "contact/index.html",
  "join/index.html",
  "assets/screenshots/dashboard.png",
  "assets/screenshots/create-event.png",
  "assets/screenshots/trust-center.png",
  "releases/latest.json"
];

for (const required of requiredFiles) {
  if (!fs.existsSync(path.join(root, required))) {
    errors.push(`required file missing: ${required}`);
  }
}

const manifestPath = path.join(root, "releases", "latest.json");
if (fs.existsSync(manifestPath)) {
  try {
    const release = JSON.parse(fs.readFileSync(manifestPath, "utf8"));
    if (release.schemaVersion !== 1) errors.push("release schemaVersion must be 1");
    if (typeof release.available !== "boolean") errors.push("release available must be boolean");

    if (release.available) {
      const trustedAsset = /^https:\/\/github\.com\/GopiB9119\/event\/releases\/download\/[A-Za-z0-9._-]+\/[A-Za-z0-9._-]+\.apk$/;
      if (!Number.isInteger(release.versionCode) || release.versionCode <= 0) {
        errors.push("published release requires a positive integer versionCode");
      }
      if (!release.versionName || typeof release.versionName !== "string") {
        errors.push("published release requires versionName");
      }
      if (!trustedAsset.test(release.downloadUrl || "")) {
        errors.push("published release downloadUrl must be an official GitHub Release APK asset");
      }
      if (!/^[A-Fa-f0-9]{64}$/.test(release.sha256 || "")) {
        errors.push("published release requires a SHA-256 digest");
      }
    } else if (release.sha256 || release.publishedAt) {
      errors.push("unpublished release must not expose checksum or publishedAt metadata");
    }
  } catch (error) {
    errors.push(`release manifest is not valid JSON: ${error.message}`);
  }
}

console.log(`SITE_ROOT=${root}`);
console.log(`HTML_FILES=${htmlFiles.length}`);
console.log(`VALIDATION_ERRORS=${errors.length}`);
for (const error of errors) console.error(error);

if (errors.length) process.exit(1);