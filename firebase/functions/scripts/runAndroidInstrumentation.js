import { spawnSync } from "node:child_process";
import { readFileSync } from "node:fs";
import { fileURLToPath } from "node:url";
import path from "node:path";

const scriptsDirectory = path.dirname(fileURLToPath(import.meta.url));
const repositoryDirectory = path.resolve(scriptsDirectory, "..", "..", "..");
const sdkDirectory = resolveAndroidSdk(repositoryDirectory);
const adb = path.join(sdkDirectory, "platform-tools", process.platform === "win32" ? "adb.exe" : "adb");
const appApk = path.join(
  repositoryDirectory,
  "app",
  "build",
  "outputs",
  "apk",
  "direct",
  "debug",
  "app-direct-debug.apk"
);
const testApk = path.join(
  repositoryDirectory,
  "app",
  "build",
  "outputs",
  "apk",
  "androidTest",
  "direct",
  "debug",
  "app-direct-debug-androidTest.apk"
);
const testClass = "com.communityledger.app.shared.SharedFirebaseConvergenceInstrumentedTest";

const readyDevices = run(adb, ["devices"], { capture: true })
  .split(/\r?\n/u)
  .slice(1)
  .map((line) => line.trim().split(/\s+/u))
  .filter((parts) => parts.length >= 2 && parts[1] === "device")
  .map((parts) => parts[0]);
const emulators = readyDevices.filter((serial) => serial.startsWith("emulator-"));
if (emulators.length !== 1) {
  throw new Error(
    `Android convergence requires exactly one ready emulator; found ${emulators.length}.`
  );
}
const emulator = emulators[0];

run(adb, ["-s", emulator, "install", "-r", "-t", appApk]);
run(adb, ["-s", emulator, "install", "-r", "-t", testApk]);
const instrumentation = run(
  adb,
  [
    "-s",
    emulator,
    "shell",
    "am",
    "instrument",
    "-w",
    "-r",
    "-e",
    "class",
    testClass,
    "com.communityledger.app.dev.test/androidx.test.runner.AndroidJUnitRunner"
  ],
  { capture: true }
);
process.stdout.write(instrumentation);
if (
  !/OK \(1 test\)/u.test(instrumentation)
  || /FAILURES!!!|INSTRUMENTATION_FAILED|Process crashed/u.test(instrumentation)
) {
  throw new Error("Android convergence instrumentation did not pass exactly one test.");
}

function resolveAndroidSdk(rootDirectory) {
  const configured = process.env.ANDROID_SDK_ROOT || process.env.ANDROID_HOME;
  if (configured) return configured;

  const properties = readFileSync(path.join(rootDirectory, "local.properties"), "utf8");
  const line = properties.split(/\r?\n/u).find((value) => value.startsWith("sdk.dir="));
  if (!line) throw new Error("Android SDK location is not configured.");
  return line
    .slice("sdk.dir=".length)
    .replace(/\\:/gu, ":")
    .replace(/\\\\/gu, "\\");
}

function run(command, args, options = {}) {
  const result = spawnSync(command, args, {
    cwd: repositoryDirectory,
    encoding: "utf8",
    windowsHide: true,
    stdio: options.capture ? "pipe" : "inherit"
  });
  if (result.error) throw result.error;
  if (result.status !== 0) {
    const output = `${result.stdout || ""}${result.stderr || ""}`.trim();
    throw new Error(`${path.basename(command)} ${args.join(" ")} failed: ${output}`);
  }
  return options.capture ? result.stdout : "";
}