[CmdletBinding()]
param(
    [string]$Serial,
    [string]$ApkPath = ".\app\build\outputs\apk\debug\app-debug.apk",
    [string]$ExpectedSha256 = "83F237EB6877FD839F73879F3500853B3F2A48A263130B2724620505C0B468FE",
    [string]$OutputPath = ".\build\physical-device-preflight.json",
    [switch]$Install
)

$ErrorActionPreference = "Stop"
$packageName = "com.aistudio.communityledger.yrtqwx"

function Invoke-Adb {
    param([string[]]$Arguments)

    $output = & adb @Arguments 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "adb $($Arguments -join ' ') failed: $($output -join [Environment]::NewLine)"
    }
    return $output
}

if (-not (Get-Command adb -ErrorAction SilentlyContinue)) {
    Write-Error "adb was not found. Install Android platform-tools and add adb to PATH."
    exit 2
}

$resolvedApk = Resolve-Path $ApkPath -ErrorAction SilentlyContinue
if ($null -eq $resolvedApk) {
    Write-Error "APK not found: $ApkPath"
    exit 2
}

$apkFile = Get-Item $resolvedApk.Path
$apkHash = (Get-FileHash -Algorithm SHA256 $apkFile.FullName).Hash.ToUpperInvariant()
if ($ExpectedSha256 -and $apkHash -ne $ExpectedSha256.ToUpperInvariant()) {
    Write-Error "APK hash mismatch. Expected $ExpectedSha256 but found $apkHash."
    exit 3
}

$deviceLines = & adb devices -l 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Error "adb devices failed: $($deviceLines -join [Environment]::NewLine)"
    exit 2
}

$devices = @()
foreach ($line in $deviceLines) {
    if ($line -match '^([^\s]+)\s+(device|offline|unauthorized)(?:\s+(.*))?$') {
        $devices += [pscustomobject]@{
            Serial = $matches[1]
            State = $matches[2]
            Details = $matches[3]
        }
    }
}

$physicalDevices = @($devices | Where-Object {
    $_.Serial -notmatch '^emulator-' -and
    $_.Details -notmatch 'device:emu|model:sdk_gphone|product:sdk_gphone'
})

if ($Serial) {
    $selected = $physicalDevices | Where-Object { $_.Serial -eq $Serial } | Select-Object -First 1
    if ($null -eq $selected) {
        Write-Error "Physical device '$Serial' was not found. Connected physical devices: $($physicalDevices.Serial -join ', ')"
        exit 2
    }
} elseif ($physicalDevices.Count -eq 1) {
    $selected = $physicalDevices[0]
} elseif ($physicalDevices.Count -eq 0) {
    Write-Error "No physical Android device is connected. Enable USB debugging, connect one unlocked phone, and accept the RSA prompt."
    exit 2
} else {
    Write-Error "Multiple physical devices are connected. Re-run with -Serial. Devices: $($physicalDevices.Serial -join ', ')"
    exit 2
}

if ($selected.State -ne "device") {
    Write-Error "Device $($selected.Serial) is $($selected.State). Unlock it and accept the USB debugging prompt."
    exit 2
}

$serialArgs = @("-s", $selected.Serial)
$manufacturer = (Invoke-Adb ($serialArgs + @("shell", "getprop", "ro.product.manufacturer")) | Out-String).Trim()
$model = (Invoke-Adb ($serialArgs + @("shell", "getprop", "ro.product.model")) | Out-String).Trim()
$androidVersion = (Invoke-Adb ($serialArgs + @("shell", "getprop", "ro.build.version.release")) | Out-String).Trim()
$sdk = (Invoke-Adb ($serialArgs + @("shell", "getprop", "ro.build.version.sdk")) | Out-String).Trim()
$abi = (Invoke-Adb ($serialArgs + @("shell", "getprop", "ro.product.cpu.abi")) | Out-String).Trim()

$installedBefore = $false
$packagePath = & adb @serialArgs shell pm path $packageName 2>$null
if ($LASTEXITCODE -eq 0 -and ($packagePath | Out-String).Trim()) {
    $installedBefore = $true
}

if ($Install) {
    Write-Host "Installing with -r -t. Existing app data is preserved; this script never clears or uninstalls the app."
    Invoke-Adb ($serialArgs + @("install", "-r", "-t", $apkFile.FullName)) | Out-Host
}

$installedAfter = $false
$installedVersionCode = $null
$installedVersionName = $null
$packagePathAfter = & adb @serialArgs shell pm path $packageName 2>$null
if ($LASTEXITCODE -eq 0 -and ($packagePathAfter | Out-String).Trim()) {
    $installedAfter = $true
    $packageDump = Invoke-Adb ($serialArgs + @("shell", "dumpsys", "package", $packageName))
    foreach ($line in $packageDump) {
        if ($line -match 'versionCode=(\d+)') { $installedVersionCode = [int]$matches[1] }
        if ($line -match 'versionName=([^\s]+)') { $installedVersionName = $matches[1] }
    }
}

$record = [ordered]@{
    schemaVersion = 1
    capturedAtUtc = [DateTime]::UtcNow.ToString("o")
    device = [ordered]@{
        serialRedacted = if ($selected.Serial.Length -gt 4) { "..." + $selected.Serial.Substring($selected.Serial.Length - 4) } else { "redacted" }
        manufacturer = $manufacturer
        model = $model
        androidVersion = $androidVersion
        sdk = $sdk
        abi = $abi
    }
    candidate = [ordered]@{
        fileName = $apkFile.Name
        sizeBytes = $apkFile.Length
        sha256 = $apkHash
        expectedSha256 = $ExpectedSha256.ToUpperInvariant()
    }
    install = [ordered]@{
        requested = [bool]$Install
        installedBefore = $installedBefore
        installedAfter = $installedAfter
        installedVersionCode = $installedVersionCode
        installedVersionName = $installedVersionName
        dataCleared = $false
        appUninstalled = $false
    }
}

$outputDirectory = Split-Path -Parent $OutputPath
if ($outputDirectory -and -not (Test-Path $outputDirectory)) {
    New-Item -ItemType Directory -Path $outputDirectory -Force | Out-Null
}
$record | ConvertTo-Json -Depth 6 | Set-Content -Path $OutputPath -Encoding UTF8

Write-Host "Physical preflight passed."
Write-Host "Device: $manufacturer $model, Android $androidVersion (SDK $sdk), ABI $abi"
Write-Host "APK: $($apkFile.Name), $($apkFile.Length) bytes, SHA-256 $apkHash"
Write-Host "Evidence: $OutputPath"
Write-Host "Next: follow docs/Testing/PHYSICAL_DEVICE_LAUNCH_MATRIX.md and record observed results manually."