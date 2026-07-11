const RELEASE_MANIFEST_URL = "releases/latest.json";
const RELEASES_PAGE = "https://github.com/GopiB9119/event/releases";

const downloadButtons = [
  document.querySelector("#download-apk"),
  document.querySelector("#download-apk-secondary")
].filter(Boolean);

const releaseStatus = document.querySelector("#release-status");
const releaseDetail = document.querySelector("#release-detail");

function setDownloadTarget(url, label) {
  downloadButtons.forEach((button) => {
    button.href = url;
    button.textContent = `↓ ${label}`;
  });
}

function isTrustedRelease(release) {
  if (
    release?.schemaVersion !== 1 ||
    release?.available !== true ||
    !Number.isInteger(release.versionCode) ||
    release.versionCode <= 0 ||
    typeof release.versionName !== "string" ||
    !/^[A-Fa-f0-9]{64}$/.test(release.sha256 || "")
  ) {
    return false;
  }

  try {
    const url = new URL(release.downloadUrl);
    return url.protocol === "https:" &&
      url.hostname === "github.com" &&
      url.username === "" &&
      url.password === "" &&
      url.port === "" &&
      url.search === "" &&
      url.hash === "" &&
      /^\/GopiB9119\/event\/releases\/download\/[A-Za-z0-9._-]+\/[A-Za-z0-9._-]+\.apk$/.test(url.pathname);
  } catch {
    return false;
  }
}

async function loadReleaseStatus() {
  try {
    const response = await fetch(RELEASE_MANIFEST_URL, { cache: "no-store" });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);

    const release = await response.json();
    if (isTrustedRelease(release)) {
      setDownloadTarget(release.downloadUrl, `Download APK ${release.versionName}`);
      releaseStatus.textContent = `Latest signed beta: ${release.versionName} · Android ${release.minimumAndroid || "7.0+"}`;
      releaseDetail.textContent = `Version ${release.versionName}. Verify SHA-256 ${release.sha256} before installation.`;
      return;
    }

    setDownloadTarget(RELEASES_PAGE, "Download APK");
    releaseStatus.textContent = "Public release signing is still pending. The button opens the official release page.";
    releaseDetail.textContent = "No permanent release-signed APK is published yet. Do not install copies reposted by unknown third parties.";
  } catch (error) {
    setDownloadTarget(RELEASES_PAGE, "Open official releases");
    releaseStatus.textContent = "Release status could not be loaded. Use the official project release page.";
    releaseDetail.textContent = "Check release notes and SHA-256 on the official project page before installing.";
  }
}

loadReleaseStatus();
