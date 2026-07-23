[CmdletBinding()]
param(
    [string]$RepositoryRoot = (Split-Path -Parent $PSScriptRoot),
    [switch]$SelfTest
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$contractRelativePath = "AEOS\14-playbooks\contracts\aeos-governance-validation.loop.json"
$expectedContractId = "aeos-governance-validation"
$supportedContractVersion = 5
$acceptedContractSemanticSha256 = "0d3ba3d1a42f36610e2ca9964d6999f3d51ffcc5aa1a27890ff702afa55c62c3"
$maximumTimeBudgetSeconds = 60

function ConvertTo-ProcessArgument {
    param([Parameter(Mandatory)][AllowEmptyString()][string]$Value)

    if ($Value.Contains('"')) {
        throw "Process arguments containing quote characters are not supported by this pilot."
    }

    if ($Value -match '\s') {
        return '"' + $Value + '"'
    }

    return $Value
}

function Get-TextSha256 {
    param([Parameter(Mandatory)][AllowEmptyString()][string]$Text)

    $algorithm = [System.Security.Cryptography.SHA256]::Create()
    try {
        $bytes = [System.Text.Encoding]::UTF8.GetBytes($Text)
        return (($algorithm.ComputeHash($bytes) | ForEach-Object { $_.ToString("x2") }) -join "")
    } finally {
        $algorithm.Dispose()
    }
}

function Assert-NoDuplicateTopLevelJsonProperties {
    param([Parameter(Mandatory)][string]$Json)

    $propertyNames = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::Ordinal)
    $objectDepth = 0
    $arrayDepth = 0
    $index = 0

    while ($index -lt $Json.Length) {
        $character = $Json[$index]
        if ($character -eq '"') {
            $stringStart = $index + 1
            $index++
            $escaped = $false
            while ($index -lt $Json.Length) {
                $stringCharacter = $Json[$index]
                if ($escaped) {
                    $escaped = $false
                } elseif ($stringCharacter -eq '\') {
                    $escaped = $true
                } elseif ($stringCharacter -eq '"') {
                    break
                }
                $index++
            }
            if ($index -ge $Json.Length) {
                throw "Loop contract JSON contains an unterminated string."
            }

            if ($objectDepth -eq 1 -and $arrayDepth -eq 0) {
                $nextIndex = $index + 1
                while ($nextIndex -lt $Json.Length -and [char]::IsWhiteSpace($Json[$nextIndex])) {
                    $nextIndex++
                }
                if ($nextIndex -lt $Json.Length -and $Json[$nextIndex] -eq ':') {
                    $rawPropertyName = $Json.Substring($stringStart, $index - $stringStart)
                    if ($rawPropertyName.Contains('\')) {
                        throw "Escaped top-level loop contract property names are not supported."
                    }
                    if (-not $propertyNames.Add($rawPropertyName)) {
                        throw "Loop contract contains duplicate top-level property '$rawPropertyName'."
                    }
                }
            }
        } else {
            switch ($character) {
                '{' { $objectDepth++ }
                '}' { $objectDepth-- }
                '[' { $arrayDepth++ }
                ']' { $arrayDepth-- }
            }
        }
        $index++
    }
}

function Assert-AcceptedLoopContract {
    param([Parameter(Mandatory)]$Contract)

    Assert-LoopContract $Contract
    $canonicalContract = $Contract | ConvertTo-Json -Compress -Depth 10
    $semanticHash = Get-TextSha256 $canonicalContract
    if ($semanticHash -ne $acceptedContractSemanticSha256) {
        throw "Loop contract semantic hash '$semanticHash' is not the reviewed contract anchor."
    }
}

function Assert-LoopContract {
    param([Parameter(Mandatory)]$Contract)

    $expectedProperties = @(
        "schemaVersion",
        "id",
        "version",
        "mission",
        "decisionState",
        "trigger",
        "scope",
        "nonGoals",
        "entryEvidence",
        "protectedInvariants",
        "permittedTools",
        "successPredicate",
        "iterationBudget",
        "timeBudgetSeconds",
        "retryAllowed",
        "evidencePathTemplate",
        "humanApprovalGates",
        "contractOwner",
        "amendmentAuthority",
        "failureAndEscalationOwner",
        "verifierIds"
    )
    $actualProperties = @($Contract.PSObject.Properties.Name)

    foreach ($propertyName in $expectedProperties) {
        if ($actualProperties -notcontains $propertyName) {
            throw "Loop contract is missing required property '$propertyName'."
        }
    }
    foreach ($propertyName in $actualProperties) {
        if ($expectedProperties -notcontains $propertyName) {
            throw "Loop contract contains unsupported property '$propertyName'."
        }
    }

    $requiredStrings = @(
        "id",
        "mission",
        "decisionState",
        "trigger",
        "successPredicate",
        "evidencePathTemplate",
        "contractOwner",
        "failureAndEscalationOwner"
    )
    foreach ($propertyName in $requiredStrings) {
        $value = $Contract.$propertyName
        if ($value -isnot [string] -or [string]::IsNullOrWhiteSpace($value)) {
            throw "Loop contract property '$propertyName' must be a non-empty string."
        }
    }

    $requiredArrays = @(
        "scope",
        "nonGoals",
        "entryEvidence",
        "protectedInvariants",
        "permittedTools",
        "amendmentAuthority",
        "verifierIds"
    )
    foreach ($propertyName in $requiredArrays) {
        if ($Contract.$propertyName -isnot [System.Array]) {
            throw "Loop contract property '$propertyName' must be a JSON array."
        }
        $values = @($Contract.$propertyName)
        if ($values.Count -eq 0) {
            throw "Loop contract property '$propertyName' must contain at least one value."
        }
        foreach ($value in $values) {
            if ($value -isnot [string] -or [string]::IsNullOrWhiteSpace($value)) {
                throw "Loop contract property '$propertyName' contains an empty or non-string value."
            }
        }
    }

    if ($Contract.humanApprovalGates -isnot [System.Array]) {
        throw "Loop contract property 'humanApprovalGates' must be a JSON array."
    }
    foreach ($value in @($Contract.humanApprovalGates)) {
        if ($value -isnot [string] -or [string]::IsNullOrWhiteSpace($value)) {
            throw "Loop contract property 'humanApprovalGates' contains an empty or non-string value."
        }
    }

    if ($Contract.schemaVersion -isnot [int] -or $Contract.schemaVersion -ne 1) {
        throw "Unsupported loop contract schemaVersion '$($Contract.schemaVersion)'."
    }
    if ($Contract.version -isnot [int] -or $Contract.version -ne $supportedContractVersion) {
        throw "Runner supports loop contract version $supportedContractVersion only."
    }
    if ($Contract.id -ne $expectedContractId) {
        throw "Runner only accepts loop contract '$expectedContractId'."
    }
    if ($Contract.decisionState -ne "TEST" -or $Contract.trigger -ne "goal") {
        throw "Governance pilot requires decisionState TEST and trigger goal."
    }
    if ($Contract.iterationBudget -isnot [int] -or $Contract.iterationBudget -ne 1) {
        throw "Governance pilot iterationBudget must remain 1."
    }
    if ($Contract.timeBudgetSeconds -isnot [int] -or $Contract.timeBudgetSeconds -lt 1 -or $Contract.timeBudgetSeconds -gt $maximumTimeBudgetSeconds) {
        throw "Governance pilot timeBudgetSeconds must be between 1 and $maximumTimeBudgetSeconds."
    }
    if ($Contract.retryAllowed -isnot [bool] -or $Contract.retryAllowed) {
        throw "Governance pilot retryAllowed must remain false."
    }
    if ($Contract.evidencePathTemplate -ne "build/aeos-loop-runs/{runId}.json") {
        throw "Governance pilot evidencePathTemplate is outside the fixed safe path."
    }

    $requiredInvariants = @(
        "read-only governance verification",
        "unchanged HEAD/status/content fingerprint",
        "fixed verifier allowlist",
        "safe persisted verifier output",
        "no blind retry"
    )
    foreach ($invariant in $requiredInvariants) {
        if (@($Contract.protectedInvariants) -notcontains $invariant) {
            throw "Loop contract is missing protected invariant '$invariant'."
        }
    }

    $verifierIds = @($Contract.verifierIds)
    $requiredVerifierIds = @("validator-self-test", "governance-validation")
    if ($verifierIds.Count -ne $requiredVerifierIds.Count) {
        throw "Governance pilot must select exactly the two required verifier IDs."
    }
    for ($index = 0; $index -lt $requiredVerifierIds.Count; $index++) {
        if ($verifierIds[$index] -ne $requiredVerifierIds[$index]) {
            throw "Unsupported verifier selection '$($verifierIds -join ', ')'."
        }
    }
}

function Read-LoopContract {
    param([Parameter(Mandatory)][string]$Path)

    if (-not (Test-Path -LiteralPath $Path -PathType Leaf)) {
        throw "Loop contract is missing: $Path"
    }

    try {
        $contractJson = Get-Content -LiteralPath $Path -Raw -Encoding UTF8
        Assert-NoDuplicateTopLevelJsonProperties $contractJson
        $contract = $contractJson | ConvertFrom-Json
    } catch {
        throw "Loop contract JSON could not be parsed: $($_.Exception.Message)"
    }

    Assert-AcceptedLoopContract $contract
    return $contract
}

function Get-VerifierDefinition {
    param(
        [Parameter(Mandatory)][string]$VerifierId,
        [Parameter(Mandatory)][string]$ValidatorPath,
        [Parameter(Mandatory)][string]$RepositoryRootPath
    )

    switch ($VerifierId) {
        "validator-self-test" {
            return [ordered]@{
                label = "validator-self-test"
                arguments = @("-NoProfile", "-NonInteractive", "-ExecutionPolicy", "Bypass", "-File", $ValidatorPath, "-RepositoryRoot", $RepositoryRootPath, "-SelfTest")
                safeStdoutPattern = '^AEOS validator self-test passed\.$'
            }
        }
        "governance-validation" {
            return [ordered]@{
                label = "governance-validation"
                arguments = @("-NoProfile", "-NonInteractive", "-ExecutionPolicy", "Bypass", "-File", $ValidatorPath, "-RepositoryRoot", $RepositoryRootPath)
                safeStdoutPattern = '^AEOS validation passed\.\r?\nPrompts: \d+ files, \d+ unique names\.\r?\nGovernance documents: \d+ files, local links resolved\.$'
            }
        }
        default {
            throw "Verifier ID '$VerifierId' is not in the runner allowlist."
        }
    }
}

function Test-SafeProcessOutput {
    param(
        [Parameter(Mandatory)][AllowEmptyString()][string]$Stdout,
        [Parameter(Mandatory)][AllowEmptyString()][string]$Stderr,
        [Parameter(Mandatory)][string]$SafeStdoutPattern
    )

    if (-not [string]::IsNullOrEmpty($Stderr)) {
        return $false
    }

    return [regex]::IsMatch(
        $Stdout,
        $SafeStdoutPattern,
        [System.Text.RegularExpressions.RegexOptions]::CultureInvariant
    )
}

function ConvertTo-EvidenceCheck {
    param(
        [Parameter(Mandatory)]$ProcessResult,
        [Parameter(Mandatory)][string]$SafeStdoutPattern
    )

    $outputPolicyPassed = Test-SafeProcessOutput `
        -Stdout $ProcessResult.stdout `
        -Stderr $ProcessResult.stderr `
        -SafeStdoutPattern $SafeStdoutPattern

    return [pscustomobject][ordered]@{
        label = $ProcessResult.label
        timedOut = $ProcessResult.timedOut
        exitCode = $ProcessResult.exitCode
        durationMilliseconds = $ProcessResult.durationMilliseconds
        outputPolicyPassed = $outputPolicyPassed
        stdoutCharacters = $ProcessResult.stdout.Length
        stderrCharacters = $ProcessResult.stderr.Length
        stdoutSha256 = Get-TextSha256 $ProcessResult.stdout
        stderrSha256 = Get-TextSha256 $ProcessResult.stderr
        stdout = if ($outputPolicyPassed) { $ProcessResult.stdout } else { $null }
        stderr = if ($outputPolicyPassed) { $ProcessResult.stderr } else { $null }
    }
}

function Resolve-LoopState {
    param(
        [bool]$PreflightPassed,
        [bool]$VerifierPassed,
        [bool]$TimedOut,
        [bool]$RetryAllowed,
        [int]$IterationsUsed,
        [int]$Budget,
        [bool]$RuntimeBlocked = $false
    )

    if (-not $PreflightPassed -or $RuntimeBlocked) {
        return "BLOCKED"
    }
    if ($TimedOut) {
        return "EXHAUSTED"
    }
    if ($VerifierPassed) {
        return "PASSED"
    }
    if ($RetryAllowed -and $IterationsUsed -ge $Budget) {
        return "EXHAUSTED"
    }
    if ($RetryAllowed) {
        return "RUNNING"
    }
    return "FAILED"
}

function Invoke-BoundedProcess {
    param(
        [Parameter(Mandatory)][string]$FilePath,
        [Parameter(Mandatory)][string[]]$Arguments,
        [Parameter(Mandatory)][ValidateRange(1, 2147483647)][int]$TimeoutMilliseconds,
        [Parameter(Mandatory)][string]$WorkingDirectory,
        [Parameter(Mandatory)][string]$TemporaryDirectory,
        [Parameter(Mandatory)][string]$Label
    )

    $stdoutPath = Join-Path $TemporaryDirectory "$Label.stdout.txt"
    $stderrPath = Join-Path $TemporaryDirectory "$Label.stderr.txt"
    Remove-Item $stdoutPath, $stderrPath -Force -ErrorAction SilentlyContinue

    $argumentLine = ($Arguments | ForEach-Object { ConvertTo-ProcessArgument $_ }) -join " "
    $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
    $process = $null
    $timedOut = $false

    try {
        $process = Start-Process `
            -FilePath $FilePath `
            -ArgumentList $argumentLine `
            -WorkingDirectory $WorkingDirectory `
            -WindowStyle Hidden `
            -RedirectStandardOutput $stdoutPath `
            -RedirectStandardError $stderrPath `
            -PassThru

        # Windows PowerShell 5.1 can expose a null ExitCode unless the handle is acquired before waiting.
        $processHandle = $process.Handle

        if (-not $process.WaitForExit($TimeoutMilliseconds)) {
            $timedOut = $true
            $process.Kill()
        }
        $process.WaitForExit()
    } finally {
        $stopwatch.Stop()
    }

    $stdout = if (Test-Path $stdoutPath) {
        $stdoutContent = Get-Content $stdoutPath -Raw -Encoding UTF8
        if ($null -eq $stdoutContent) { "" } else { $stdoutContent.TrimEnd() }
    } else {
        ""
    }
    $stderr = if (Test-Path $stderrPath) {
        $stderrContent = Get-Content $stderrPath -Raw -Encoding UTF8
        if ($null -eq $stderrContent) { "" } else { $stderrContent.TrimEnd() }
    } else {
        ""
    }
    $exitCode = if ($timedOut -or $null -eq $process) { $null } else { $process.ExitCode }

    Remove-Item $stdoutPath, $stderrPath -Force -ErrorAction SilentlyContinue

    return [pscustomobject][ordered]@{
        label = $Label
        timedOut = $timedOut
        exitCode = $exitCode
        durationMilliseconds = $stopwatch.ElapsedMilliseconds
        stdout = $stdout
        stderr = $stderr
    }
}

function Get-RepositorySnapshot {
    param([Parameter(Mandatory)][string]$Root)

    $headOutput = @(& git -C $Root rev-parse --verify HEAD 2>$null)
    $headExitCode = $LASTEXITCODE
    $head = ($headOutput -join "").Trim()
    if ($headExitCode -ne 0 -or [string]::IsNullOrWhiteSpace($head)) {
        throw "Repository HEAD could not be read."
    }

    $statusOutput = @(& git -C $Root status --porcelain=v1 --untracked-files=all 2>$null)
    $statusExitCode = $LASTEXITCODE
    if ($statusExitCode -ne 0) {
        throw "Repository status could not be read."
    }
    $statusText = $statusOutput -join "`n"

    $fileListOutput = & git -C $Root ls-files --cached --others --exclude-standard -z 2>$null
    $fileListExitCode = $LASTEXITCODE
    if ($fileListExitCode -ne 0) {
        throw "Repository file list could not be read."
    }

    $fileListText = if ($null -eq $fileListOutput) { "" } else { [string]$fileListOutput }
    $filePaths = @($fileListText.Split([char]0, [System.StringSplitOptions]::RemoveEmptyEntries) | Sort-Object -Unique)
    $contentFingerprint = New-Object System.Text.StringBuilder

    foreach ($relativePath in $filePaths) {
        $fullPath = Join-Path $Root $relativePath
        [void]$contentFingerprint.Append($relativePath).Append([char]0)

        if (Test-Path -LiteralPath $fullPath -PathType Leaf) {
            $file = Get-Item -LiteralPath $fullPath
            $fileHash = (Get-FileHash -LiteralPath $fullPath -Algorithm SHA256).Hash.ToLowerInvariant()
            [void]$contentFingerprint.Append("FILE").Append([char]0)
            [void]$contentFingerprint.Append($file.Length).Append([char]0)
            [void]$contentFingerprint.Append($fileHash).Append([char]0)
        } elseif (Test-Path -LiteralPath $fullPath -PathType Container) {
            $nestedHeadOutput = @(& git -C $fullPath rev-parse --verify HEAD 2>$null)
            $nestedHeadExitCode = $LASTEXITCODE
            $nestedHead = if ($nestedHeadExitCode -eq 0) { ($nestedHeadOutput -join "").Trim() } else { "" }
            [void]$contentFingerprint.Append("DIRECTORY").Append([char]0)
            [void]$contentFingerprint.Append($nestedHead).Append([char]0)
        } else {
            [void]$contentFingerprint.Append("MISSING").Append([char]0)
        }
    }

    return [ordered]@{
        head = $head.Trim()
        workingTreeDirty = -not [string]::IsNullOrWhiteSpace($statusText)
        workingTreeStatusSha256 = Get-TextSha256 $statusText
        workingTreeContentSha256 = Get-TextSha256 $contentFingerprint.ToString()
        trackedAndUntrackedFileCount = $filePaths.Count
    }
}

function Assert-Equal {
    param(
        [Parameter(Mandatory)]$Expected,
        [Parameter(Mandatory)][AllowNull()]$Actual,
        [Parameter(Mandatory)][string]$Message
    )

    if ($Expected -ne $Actual) {
        throw "$Message Expected '$Expected', got '$Actual'."
    }
}

if ($SelfTest) {
    $selfTestRepositoryRoot = (Resolve-Path $RepositoryRoot).Path
    $selfTestContractPath = Join-Path $selfTestRepositoryRoot $contractRelativePath
    $selfTestContract = Read-LoopContract $selfTestContractPath

    $malformedBudgetContract = Get-Content -LiteralPath $selfTestContractPath -Raw -Encoding UTF8 | ConvertFrom-Json
    $malformedBudgetContract.timeBudgetSeconds = 0
    $malformedBudgetRejected = $false
    try {
        Assert-LoopContract $malformedBudgetContract
    } catch {
        $malformedBudgetRejected = $true
    }
    Assert-Equal $true $malformedBudgetRejected "Malformed budget contract was accepted."

    $unsafeVerifierContract = Get-Content -LiteralPath $selfTestContractPath -Raw -Encoding UTF8 | ConvertFrom-Json
    $unsafeVerifierContract.verifierIds = @("validator-self-test", "arbitrary-command")
    $unsafeVerifierRejected = $false
    try {
        Assert-LoopContract $unsafeVerifierContract
    } catch {
        $unsafeVerifierRejected = $true
    }
    Assert-Equal $true $unsafeVerifierRejected "Unsafe verifier contract was accepted."

    $scalarArrayContract = Get-Content -LiteralPath $selfTestContractPath -Raw -Encoding UTF8 | ConvertFrom-Json
    $scalarArrayContract.scope = "scalar scope"
    $scalarArrayRejected = $false
    try {
        Assert-LoopContract $scalarArrayContract
    } catch {
        $scalarArrayRejected = $true
    }
    Assert-Equal $true $scalarArrayRejected "Scalar array substitute was accepted."

    $alteredAuthorityContract = Get-Content -LiteralPath $selfTestContractPath -Raw -Encoding UTF8 | ConvertFrom-Json
    $alteredAuthorityContract.contractOwner = "Implementer"
    $alteredAuthorityRejected = $false
    try {
        Assert-AcceptedLoopContract $alteredAuthorityContract
    } catch {
        $alteredAuthorityRejected = $true
    }
    Assert-Equal $true $alteredAuthorityRejected "Unanchored authority change was accepted."

    $duplicatePropertyRejected = $false
    try {
        Assert-NoDuplicateTopLevelJsonProperties '{"id":"first","id":"second"}'
    } catch {
        $duplicatePropertyRejected = $true
    }
    Assert-Equal $true $duplicatePropertyRejected "Duplicate top-level contract property was accepted."

    $unknownPropertyContract = Get-Content -LiteralPath $selfTestContractPath -Raw -Encoding UTF8 | ConvertFrom-Json
    $unknownPropertyContract | Add-Member -NotePropertyName "command" -NotePropertyValue "arbitrary-command"
    $unknownPropertyRejected = $false
    try {
        Assert-LoopContract $unknownPropertyContract
    } catch {
        $unknownPropertyRejected = $true
    }
    Assert-Equal $true $unknownPropertyRejected "Unknown contract property was accepted."

    Assert-Equal "BLOCKED" (Resolve-LoopState $false $false $false $false 0 1) "Preflight classification failed."
    Assert-Equal "PASSED" (Resolve-LoopState $true $true $false $false 1 1) "Pass classification failed."
    Assert-Equal "FAILED" (Resolve-LoopState $true $false $false $false 1 1) "Verifier failure classification failed."
    Assert-Equal "RUNNING" (Resolve-LoopState $true $false $false $true 1 2) "Retry classification failed."
    Assert-Equal "EXHAUSTED" (Resolve-LoopState $true $false $false $true 2 2) "Iteration exhaustion classification failed."
    Assert-Equal "EXHAUSTED" (Resolve-LoopState $true $false $true $false 1 1) "Timeout classification failed."
    Assert-Equal "BLOCKED" (Resolve-LoopState $true $false $false $false 1 1 $true) "Runtime drift classification failed."
    Assert-Equal $true (Test-SafeProcessOutput "safe output" "" '^safe output$') "Safe-output acceptance failed."
    Assert-Equal $false (Test-SafeProcessOutput "unexpected output" "" '^safe output$') "Unexpected stdout was accepted."
    Assert-Equal $false (Test-SafeProcessOutput "safe output" "unexpected stderr" '^safe output$') "Unexpected stderr was accepted."

    $repositorySnapshot = Get-RepositorySnapshot $selfTestRepositoryRoot
    if ($repositorySnapshot.head -notmatch '^[0-9a-fA-F]{40,64}$') {
        throw "Repository snapshot self-test returned an invalid HEAD."
    }
    if ($repositorySnapshot.workingTreeStatusSha256 -notmatch '^[0-9a-f]{64}$') {
        throw "Repository snapshot self-test returned an invalid status fingerprint."
    }
    if ($repositorySnapshot.workingTreeContentSha256 -notmatch '^[0-9a-f]{64}$') {
        throw "Repository snapshot self-test returned an invalid content fingerprint."
    }
    if ($repositorySnapshot.trackedAndUntrackedFileCount -le 0) {
        throw "Repository snapshot self-test returned no files."
    }

    $temporaryDirectory = Join-Path ([System.IO.Path]::GetTempPath()) "aeos-loop-self-test-$([guid]::NewGuid().ToString('N'))"
    New-Item -ItemType Directory -Path $temporaryDirectory -Force | Out-Null
    try {
        $powerShellPath = Join-Path $PSHOME "powershell.exe"
        $passingProcess = Invoke-BoundedProcess `
            -FilePath $powerShellPath `
            -Arguments @("-NoProfile", "-NonInteractive", "-Command", "exit 0") `
            -TimeoutMilliseconds 15000 `
            -WorkingDirectory $temporaryDirectory `
            -TemporaryDirectory $temporaryDirectory `
            -Label "passing-process"
        Assert-Equal $false $passingProcess.timedOut "Passing subprocess timed out."
        Assert-Equal 0 $passingProcess.exitCode "Passing subprocess exit code failed."

        $timedProcess = Invoke-BoundedProcess `
            -FilePath $powerShellPath `
            -Arguments @("-NoProfile", "-NonInteractive", "-Command", 'while ($true) { }') `
            -TimeoutMilliseconds 500 `
            -WorkingDirectory $temporaryDirectory `
            -TemporaryDirectory $temporaryDirectory `
            -Label "timed-process"
        Assert-Equal $true $timedProcess.timedOut "Watchdog timeout was not enforced."
    } finally {
        Remove-Item $temporaryDirectory -Recurse -Force -ErrorAction SilentlyContinue
    }

    Write-Output "AEOS governance loop self-test passed: states, successful process, and watchdog exhaustion."
    return
}

$repositoryRootPath = (Resolve-Path $RepositoryRoot).Path
$contractPath = Join-Path $repositoryRootPath $contractRelativePath
$loopContract = Read-LoopContract $contractPath
$contractId = $loopContract.id
$contractVersion = [int]$loopContract.version
$iterationBudget = [int]$loopContract.iterationBudget
$timeBudgetSeconds = [int]$loopContract.timeBudgetSeconds
$validatorPath = Join-Path $repositoryRootPath "scripts\validate-aeos.ps1"
$powerShellPath = Join-Path $PSHOME "powershell.exe"
$runId = "$contractId-$([DateTime]::UtcNow.ToString('yyyyMMddTHHmmssfffZ'))-$([guid]::NewGuid().ToString('N').Substring(0, 8))"
$relativeEvidencePath = $loopContract.evidencePathTemplate.Replace("{runId}", $runId).Replace('\', '/')
$evidenceDirectory = Split-Path (Join-Path $repositoryRootPath $relativeEvidencePath)
$evidencePath = Join-Path $evidenceDirectory "$runId.json"
$evidenceContractPath = $relativeEvidencePath
$evidencePathIgnored = $false
$runStopwatch = [System.Diagnostics.Stopwatch]::StartNew()
$checks = New-Object System.Collections.Generic.List[object]
$preflightPassed = $true
$preflightError = $null
$startingSnapshot = $null
$completedSnapshot = $null
$sourceUnchanged = $null
$runtimeBlocked = $false

try {
    if (-not (Test-Path $validatorPath -PathType Leaf)) {
        throw "Allowlisted verifier is missing: scripts/validate-aeos.ps1"
    }
    if (-not (Test-Path $powerShellPath -PathType Leaf)) {
        throw "Windows PowerShell executable is missing from PSHOME."
    }

    & git -C $repositoryRootPath check-ignore --quiet -- $relativeEvidencePath
    if ($LASTEXITCODE -ne 0) {
        $evidenceDirectory = Join-Path ([System.IO.Path]::GetTempPath()) "aeos-loop-runs"
        $evidencePath = Join-Path $evidenceDirectory "$runId.json"
        $evidenceContractPath = "<OS_TEMP>/aeos-loop-runs/$runId.json"
        throw "Repository evidence path is not ignored; blocked evidence will use OS temp storage."
    }
    $evidencePathIgnored = $true
    $startingSnapshot = Get-RepositorySnapshot $repositoryRootPath
} catch {
    $preflightPassed = $false
    $preflightError = $_.Exception.Message
}

$timedOut = $false
$verifierPassed = $false

if ($preflightPassed) {
    $checkDefinitions = @($loopContract.verifierIds | ForEach-Object {
        Get-VerifierDefinition `
            -VerifierId $_ `
            -ValidatorPath $validatorPath `
            -RepositoryRootPath $repositoryRootPath
    })

    $verifierPassed = $true
    foreach ($checkDefinition in $checkDefinitions) {
        $remainingMilliseconds = ($timeBudgetSeconds * 1000) - [int]$runStopwatch.ElapsedMilliseconds
        if ($remainingMilliseconds -le 0) {
            $timedOut = $true
            $verifierPassed = $false
            break
        }

        $processResult = Invoke-BoundedProcess `
            -FilePath $powerShellPath `
            -Arguments $checkDefinition.arguments `
            -TimeoutMilliseconds $remainingMilliseconds `
            -WorkingDirectory $repositoryRootPath `
            -TemporaryDirectory ([System.IO.Path]::GetTempPath()) `
            -Label "$runId-$($checkDefinition.label)"
        $checkResult = ConvertTo-EvidenceCheck `
            -ProcessResult $processResult `
            -SafeStdoutPattern $checkDefinition.safeStdoutPattern
        $checks.Add($checkResult)

        if ($checkResult.timedOut) {
            $timedOut = $true
            $verifierPassed = $false
            break
        }
        if ($checkResult.exitCode -ne 0) {
            $verifierPassed = $false
            break
        }
        if (-not $checkResult.outputPolicyPassed) {
            $verifierPassed = $false
            break
        }
    }
}

if ($preflightPassed) {
    try {
        $completedSnapshot = Get-RepositorySnapshot $repositoryRootPath
        $sourceUnchanged = (
            $startingSnapshot.head -eq $completedSnapshot.head -and
            $startingSnapshot.workingTreeStatusSha256 -eq $completedSnapshot.workingTreeStatusSha256 -and
            $startingSnapshot.workingTreeContentSha256 -eq $completedSnapshot.workingTreeContentSha256
        )
        if (-not $sourceUnchanged) {
            $runtimeBlocked = $true
            $verifierPassed = $false
        }
    } catch {
        $runtimeBlocked = $true
        $verifierPassed = $false
        $preflightError = "Completion snapshot failed: $($_.Exception.Message)"
    }
}

$runStopwatch.Stop()
$terminalState = Resolve-LoopState `
    -PreflightPassed $preflightPassed `
    -VerifierPassed $verifierPassed `
    -TimedOut $timedOut `
    -RetryAllowed $false `
    -IterationsUsed $(if ($preflightPassed) { 1 } else { 0 }) `
    -Budget $iterationBudget `
    -RuntimeBlocked $runtimeBlocked

$contractRecord = [ordered]@{}
foreach ($property in $loopContract.PSObject.Properties) {
    $contractRecord[$property.Name] = $property.Value
}
$contractRecord["acceptedSemanticSha256"] = $acceptedContractSemanticSha256
$contractRecord["resolvedPermittedMutations"] = @($evidenceContractPath)

$record = [ordered]@{
    schemaVersion = 5
    runId = $runId
    contract = $contractRecord
    startedAtUtc = [DateTime]::UtcNow.Subtract($runStopwatch.Elapsed).ToString("o")
    completedAtUtc = [DateTime]::UtcNow.ToString("o")
    durationMilliseconds = $runStopwatch.ElapsedMilliseconds
    source = [ordered]@{
        starting = $startingSnapshot
        completed = $completedSnapshot
        unchangedDuringRun = $sourceUnchanged
    }
    evidenceStorage = [ordered]@{
        contractPath = $evidenceContractPath
        repositoryPathIgnored = $evidencePathIgnored
    }
    iterationsUsed = $(if ($preflightPassed) { 1 } else { 0 })
    checks = $checks.ToArray()
    preflightError = $preflightError
    terminalState = $terminalState
    residualRisk = @(
        "This pilot validates governance structure, not semantic correctness of every policy.",
        "The Windows PowerShell adapter is local and manual; no scheduler or event listener is implemented."
    )
}

New-Item -ItemType Directory -Path $evidenceDirectory -Force | Out-Null
$record | ConvertTo-Json -Depth 10 | Set-Content $evidencePath -Encoding UTF8

Write-Output "AEOS_LOOP_STATE=$terminalState"
Write-Output "AEOS_LOOP_RUN_ID=$runId"
Write-Output "AEOS_LOOP_EVIDENCE=$(if ($evidencePathIgnored) { $relativeEvidencePath } else { $evidencePath })"

switch ($terminalState) {
    "PASSED" { exit 0 }
    "FAILED" { exit 2 }
    "EXHAUSTED" { exit 3 }
    "BLOCKED" { exit 4 }
    default { exit 5 }
}
