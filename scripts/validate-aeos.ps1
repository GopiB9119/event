[CmdletBinding()]
param(
    [string]$RepositoryRoot = (Split-Path -Parent $PSScriptRoot),
    [switch]$SelfTest
)

$ErrorActionPreference = "Stop"
$failures = New-Object System.Collections.Generic.List[string]

function Add-Failure {
    param([string]$Message)
    $failures.Add($Message)
}

function Get-FrontmatterFieldValue {
    param(
        [string]$Frontmatter,
        [string]$Field
    )

    $fieldMatch = [regex]::Match($Frontmatter, "(?m)^$([regex]::Escape($Field))\s*:\s*(.*)$")
    if (-not $fieldMatch.Success) {
        return $null
    }

    $fieldValue = $fieldMatch.Groups[1].Value.Trim()
    if ($fieldValue.Length -ge 2 -and (
        ($fieldValue.StartsWith('"') -and $fieldValue.EndsWith('"')) -or
        ($fieldValue.StartsWith("'") -and $fieldValue.EndsWith("'"))
    )) {
        $fieldValue = $fieldValue.Substring(1, $fieldValue.Length - 2).Trim()
    }

    if ([string]::IsNullOrWhiteSpace($fieldValue)) {
        return $null
    }

    return $fieldValue
}

if ($SelfTest) {
    $validFrontmatter = "name: `"Valid Prompt`"`ndescription: test"
    if ((Get-FrontmatterFieldValue -Frontmatter $validFrontmatter -Field "name") -ne "Valid Prompt") {
        throw "Self-test failed: valid quoted frontmatter was not parsed."
    }

    foreach ($invalidFrontmatter in @("name:", "name: `"`"", "name: '   '")) {
        if ($null -ne (Get-FrontmatterFieldValue -Frontmatter $invalidFrontmatter -Field "name")) {
            throw "Self-test failed: empty frontmatter was accepted: $invalidFrontmatter"
        }
    }

    Write-Output "AEOS validator self-test passed."
    exit 0
}

$repositoryRootPath = (Resolve-Path $RepositoryRoot).Path
$aeosRoot = Join-Path $repositoryRootPath "AEOS"
$promptRoot = Join-Path $repositoryRootPath ".github\prompts"

$requiredPaths = @(
    "AEOS\AGENTS.md",
    "AEOS\README.md",
    "AEOS\MANIFEST.md",
    "AEOS\00-foundation\AI_CONSTITUTION.md",
    "AEOS\00-foundation\DECISION_INTELLIGENCE.md",
    "AEOS\00-foundation\OPERATING_MODEL.md",
    "AEOS\10-ai\AGENTIC_CODING.md",
    "AEOS\10-ai\CONTEXT_AND_MEMORY.md",
    "AEOS\10-ai\LOOP_ENGINEERING.md",
    "AEOS\07-security\HIGH_STAKES_MOBILE_SYSTEMS.md",
    "AEOS\11-prompts\PROMPT_LIBRARY.md",
    "AEOS\13-review\QUALITY_GATES.md",
    "AEOS\14-playbooks\GIT_RECOVERY_PLAYBOOK.md",
    "AEOS\14-playbooks\AEOS_VALIDATION_LOOP_PILOT.md",
    "AEOS\16-behavior-system\README.md",
    "AEOS\16-behavior-system\CATALOG_CONTRACT.md",
    "AEOS\16-behavior-system\RUNTIME_AND_ACTIVATION.md",
    "AEOS\13-review\BEHAVIOR_SYSTEM_QUALITY_GATES.md",
    "AEOS\14-playbooks\BEHAVIOR_SYSTEM_AUTHORING_PLAYBOOK.md",
    "AEOS\16-behavior-system\source\catalog-blueprint.json",
    "AEOS\16-behavior-system\generated\catalog.json",
    "AEOS\14-playbooks\contracts\aeos-governance-validation.loop.json",
    "scripts\invoke-aeos-governance-loop.ps1",
    "scripts\validate-aeos-behavior-system.ps1",
    "scripts\generate-aeos-behavior-system.ps1",
    "scripts\resolve-aeos-context.ps1"
)

foreach ($relativePath in $requiredPaths) {
    if (-not (Test-Path (Join-Path $repositoryRootPath $relativePath))) {
        Add-Failure "Missing required AEOS document: $relativePath"
    }
}

$loopRunnerPath = Join-Path $repositoryRootPath "scripts\invoke-aeos-governance-loop.ps1"
if (Test-Path $loopRunnerPath -PathType Leaf) {
    $loopPowerShellPath = Join-Path $PSHOME "powershell.exe"
    if (-not (Test-Path $loopPowerShellPath -PathType Leaf)) {
        Add-Failure "Windows PowerShell executable required by the AEOS loop pilot is missing from PSHOME."
    } else {
        $loopSelfTestOutput = @(& $loopPowerShellPath -NoProfile -NonInteractive -ExecutionPolicy Bypass -File $loopRunnerPath -RepositoryRoot $repositoryRootPath -SelfTest 2>&1)
        $loopSelfTestExitCode = $LASTEXITCODE
        $loopSelfTestText = ($loopSelfTestOutput | ForEach-Object { $_.ToString() }) -join "`n"
        if ($loopSelfTestExitCode -ne 0) {
            Add-Failure "AEOS governance loop self-test exited with code $loopSelfTestExitCode."
        }
        if ($loopSelfTestText -ne "AEOS governance loop self-test passed: states, successful process, and watchdog exhaustion.") {
            Add-Failure "AEOS governance loop self-test returned unexpected output."
        }
    }
}

$behaviorValidatorPath = Join-Path $repositoryRootPath "scripts\validate-aeos-behavior-system.ps1"
$behaviorGeneratorPath = Join-Path $repositoryRootPath "scripts\generate-aeos-behavior-system.ps1"
if (Test-Path $behaviorValidatorPath -PathType Leaf) {
    $behaviorSelfTestOutput = @(& $behaviorValidatorPath -RepositoryRoot $repositoryRootPath -SelfTest 2>&1)
    $behaviorSelfTestExitCode = $LASTEXITCODE
    $behaviorSelfTestText = ($behaviorSelfTestOutput | ForEach-Object { $_.ToString() }) -join "`n"
    if ($behaviorSelfTestExitCode -ne 0 -or $behaviorSelfTestText -ne "AEOS behavior-system self-test passed.") {
        Add-Failure "AEOS behavior-system self-test failed or returned unexpected output."
    }
    $behaviorValidationOutput = @(& $behaviorValidatorPath -RepositoryRoot $repositoryRootPath 2>&1)
    if ($LASTEXITCODE -ne 0) {
        Add-Failure "AEOS behavior-system validation failed."
    }
}
if (Test-Path $behaviorGeneratorPath -PathType Leaf) {
    $behaviorGenerationOutput = @(& $behaviorGeneratorPath -RepositoryRoot $repositoryRootPath -Check 2>&1)
    $behaviorGenerationText = ($behaviorGenerationOutput | ForEach-Object { $_.ToString() }) -join "`n"
    if ($LASTEXITCODE -ne 0 -or $behaviorGenerationText -ne "AEOS behavior-system generated files are current.") {
        Add-Failure "AEOS behavior-system generated artifacts are stale or invalid."
    }
}

$promptFiles = @(Get-ChildItem $promptRoot -Filter "*.prompt.md" -File | Sort-Object FullName)
if ($promptFiles.Count -eq 0) {
    Add-Failure "No workspace prompt files found under .github/prompts."
}

$promptNames = @{}
$requiredFrontmatterFields = @("name", "description", "argument-hint", "agent")

foreach ($promptFile in $promptFiles) {
    $relativePromptPath = $promptFile.FullName.Substring($repositoryRootPath.Length + 1)
    $lines = @(Get-Content $promptFile.FullName -Encoding UTF8)

    if ($lines.Count -lt 3 -or $lines[0].Trim() -ne "---") {
        Add-Failure "$relativePromptPath does not start with YAML frontmatter."
        continue
    }

    $closingDelimiter = -1
    for ($index = 1; $index -lt $lines.Count; $index++) {
        if ($lines[$index].Trim() -eq "---") {
            $closingDelimiter = $index
            break
        }
    }

    if ($closingDelimiter -lt 0) {
        Add-Failure "$relativePromptPath has no closing frontmatter delimiter."
        continue
    }

    $frontmatter = $lines[1..($closingDelimiter - 1)] -join "`n"
    $frontmatterValues = @{}
    foreach ($field in $requiredFrontmatterFields) {
        $fieldValue = Get-FrontmatterFieldValue -Frontmatter $frontmatter -Field $field
        if ($null -eq $fieldValue) {
            Add-Failure "$relativePromptPath is missing or has an empty frontmatter field '$field'."
            continue
        }

        $frontmatterValues[$field] = $fieldValue
    }

    if ($frontmatterValues.ContainsKey("name")) {
        $promptName = $frontmatterValues["name"]
        if ($promptNames.ContainsKey($promptName)) {
            Add-Failure "Duplicate prompt name '$promptName': $($promptNames[$promptName]) and $relativePromptPath"
        } else {
            $promptNames[$promptName] = $relativePromptPath
        }
    }

    $promptText = Get-Content $promptFile.FullName -Raw -Encoding UTF8
    if ($promptText.Contains('${input:')) {
        Add-Failure "$relativePromptPath contains an unresolved input placeholder."
    }
}

$governanceFiles = @()
$governanceFiles += Get-ChildItem $aeosRoot -Filter "*.md" -File -Recurse
$governanceFiles += $promptFiles
$governanceFiles += Get-ChildItem (Join-Path $repositoryRootPath ".ai") -Filter "*.md" -File
$governanceFiles += Get-Item (Join-Path $repositoryRootPath "AGENTS.md")
$governanceFiles += Get-Item (Join-Path $repositoryRootPath "prompts.md")
$governanceFiles = @($governanceFiles | Sort-Object FullName -Unique)

$universalAeosFiles = @(Get-ChildItem $aeosRoot -Filter "*.md" -File -Recurse | Where-Object {
    $_.FullName -notlike "$(Join-Path $aeosRoot 'project')\*"
})
$projectSpecificPatterns = @(
    "Community Ledger",
    "ReceiptParser",
    "ML Kit",
    "Robolectric",
    "\bRoom\b",
    '`memberId`',
    '`eventId`',
    "\bGemini\b"
)

foreach ($universalFile in $universalAeosFiles) {
    $relativeUniversalPath = $universalFile.FullName.Substring($repositoryRootPath.Length + 1)
    $universalContent = Get-Content $universalFile.FullName -Raw -Encoding UTF8
    foreach ($projectSpecificPattern in $projectSpecificPatterns) {
        if ($universalContent -match $projectSpecificPattern) {
            Add-Failure "Project-specific term '$projectSpecificPattern' found in universal AEOS document $relativeUniversalPath. Move it to AEOS/project or a repository overlay."
        }
    }
}

$markdownLinkPattern = '!?(?:\[[^\]]*\])\(([^)]+)\)'
foreach ($governanceFile in $governanceFiles) {
    $relativeSourcePath = $governanceFile.FullName.Substring($repositoryRootPath.Length + 1)
    $sourceDirectory = Split-Path $governanceFile.FullName
    $content = Get-Content $governanceFile.FullName -Raw -Encoding UTF8

    foreach ($match in [regex]::Matches($content, $markdownLinkPattern)) {
        $rawTarget = $match.Groups[1].Value.Trim()
        if ($rawTarget -match '^(https?:|mailto:|#)' -or $rawTarget -eq "") {
            continue
        }

        $targetWithoutAnchor = ($rawTarget -split '#', 2)[0]
        $targetWithoutQuery = ($targetWithoutAnchor -split '\?', 2)[0]
        $decodedTarget = [uri]::UnescapeDataString($targetWithoutQuery).Trim('<', '>')
        if ($decodedTarget -eq "") {
            continue
        }

        if ($decodedTarget.StartsWith("/")) {
            $resolvedTarget = Join-Path $repositoryRootPath $decodedTarget.TrimStart('/', '\')
        } else {
            $resolvedTarget = Join-Path $sourceDirectory $decodedTarget
        }

        if (-not (Test-Path $resolvedTarget)) {
            Add-Failure "Broken local link in $relativeSourcePath -> $rawTarget"
        }
    }
}

if ($failures.Count -gt 0) {
    $failures | ForEach-Object { Write-Error $_ }
    Write-Output "AEOS validation failed with $($failures.Count) error(s)."
    exit 1
}

Write-Output "AEOS validation passed."
Write-Output "Prompts: $($promptFiles.Count) files, $($promptNames.Count) unique names."
Write-Output "Governance documents: $($governanceFiles.Count) files, local links resolved."