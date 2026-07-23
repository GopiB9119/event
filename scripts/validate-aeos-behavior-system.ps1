[CmdletBinding()]
param(
    [string]$RepositoryRoot = (Split-Path -Parent $PSScriptRoot),
    [switch]$SelfTest
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$root = (Resolve-Path $RepositoryRoot).Path
$modulePath = Join-Path $root "scripts\AeosBehaviorSystem.psm1"
$blueprintPath = Join-Path $root "AEOS\16-behavior-system\source\catalog-blueprint.json"
Import-Module $modulePath -Force

function Invoke-CatalogSchemaValidation {
    param([Parameter(Mandatory)]$Catalog)

    $nodePath = Join-Path $root "firebase\functions\node_modules\node\bin\node.exe"
    $schemaRunnerPath = Join-Path $root "scripts\validate-aeos-behavior-schema.mjs"
    $schemaPath = Join-Path $root "AEOS\16-behavior-system\schemas\catalog.schema.json"
    foreach ($requiredPath in @($nodePath, $schemaRunnerPath, $schemaPath)) {
        if (-not (Test-Path -LiteralPath $requiredPath -PathType Leaf)) {
            throw "AEOS behavior schema validation dependency is missing: $requiredPath"
        }
    }

    $temporaryPath = Join-Path ([System.IO.Path]::GetTempPath()) ("aeos-behavior-catalog-{0}.json" -f [guid]::NewGuid().ToString("N"))
    try {
        Write-Utf8NoBomLf $temporaryPath (ConvertTo-DeterministicJson $Catalog)
        $output = @(& $nodePath $schemaRunnerPath `
            --repository-root $root `
            --schema $schemaPath `
            --document $temporaryPath 2>&1)
        if ($LASTEXITCODE -ne 0) {
            throw (($output | ForEach-Object { $_.ToString() }) -join "`n")
        }
        return ($output | ForEach-Object { $_.ToString() }) -join "`n"
    } finally {
        if (Test-Path -LiteralPath $temporaryPath -PathType Leaf) {
            Remove-Item -LiteralPath $temporaryPath -Force
        }
    }
}

function Assert-Equal {
    param($Expected, $Actual, [string]$Message)
    if ($Expected -ne $Actual) { throw "$Message Expected '$Expected', got '$Actual'." }
}

if ($SelfTest) {
    $duplicateRejected = $false
    try {
        Assert-NoDuplicateJsonProperties '{"outer":{"id":"one","id":"two"}}'
    } catch {
        $duplicateRejected = $true
    }
    Assert-Equal $true $duplicateRejected "Nested duplicate JSON property was accepted."

    $escapedPropertyRejected = $false
    try {
        Assert-NoDuplicateJsonProperties '{"\u0069d":"one"}'
    } catch {
        $escapedPropertyRejected = $true
    }
    Assert-Equal $true $escapedPropertyRejected "Escaped JSON property name was accepted."

    $blueprint = Read-AeosBehaviorBlueprint $blueprintPath
    $catalog = Expand-AeosBehaviorCatalog $blueprint $root
    Assert-Equal "AEOS behavior catalog schema validation passed." `
        (Invoke-CatalogSchemaValidation $catalog) `
        "Generated catalog did not satisfy its published schema."
    $firstJson = ConvertTo-DeterministicJson $catalog
    $secondJson = ConvertTo-DeterministicJson (Expand-AeosBehaviorCatalog $blueprint $root)
    Assert-Equal (Get-AeosTextSha256 $firstJson) (Get-AeosTextSha256 $secondJson) "Catalog generation is not deterministic."
    Test-AeosRoutingGoldenCases $catalog

    $untrusted = Resolve-AeosBehaviorContext $catalog @("signal-tool-orchestration-repository-evidence") "implement" $false
    Assert-Equal "BLOCKED" $untrusted.terminalState "Untrusted routing input was accepted."
    $overBudget = Resolve-AeosBehaviorContext $catalog @(
        "signal-software-architecture-constraint-risk",
        "signal-security-constraint-risk",
        "signal-privacy-constraint-risk"
    ) "plan" $true
    Assert-Equal "BLOCKED" $overBudget.terminalState "Domain budget exhaustion was accepted."
    $valid = Resolve-AeosBehaviorContext $catalog @("signal-debugging-runtime-failure") "debug" $true
    Assert-Equal "PASSED" $valid.terminalState "Valid routing input did not pass."
    if ($valid.selectedItemIds.Count -gt 12) { throw "Resolver exceeded the twelve-item budget." }
    foreach ($requiredKind in @("signal", "failure", "recovery", "behavior", "governance")) {
        $selectedKinds = @($valid.selectedItemIds | ForEach-Object { ($_ -split '-', 2)[0] })
        if ($requiredKind -eq "mental-model") {
            $present = @($valid.selectedItemIds | Where-Object { $_ -like "mental-model-*" }).Count -gt 0
        } else {
            $present = @($catalog.items | Where-Object { $valid.selectedItemIds -contains $_.id -and $_.kind -eq $requiredKind }).Count -gt 0
        }
        if (-not $present) { throw "Valid debug context pack omitted required kind '$requiredKind'." }
    }

    $unknownPropertyRejected = $false
    $copy = $blueprint | ConvertTo-Json -Depth 30 | ConvertFrom-Json
    $copy | Add-Member -NotePropertyName command -NotePropertyValue "arbitrary-command"
    try {
        Assert-AeosBehaviorBlueprint $copy $root
    } catch {
        $unknownPropertyRejected = $true
    }
    Assert-Equal $true $unknownPropertyRejected "Unknown blueprint property was accepted."

    $scalarArrayRejected = $false
    $copy = $blueprint | ConvertTo-Json -Depth 30 | ConvertFrom-Json
    $copy.domains[0].riskClasses = "scalar"
    try {
        Assert-AeosBehaviorBlueprint $copy $root
    } catch {
        $scalarArrayRejected = $true
    }
    Assert-Equal $true $scalarArrayRejected "Scalar array substitute was accepted."

    $numericStringRejected = $false
    $copy = $blueprint | ConvertTo-Json -Depth 30 | ConvertFrom-Json
    $copy.countFloors.behavior = "500"
    try {
        Assert-AeosBehaviorBlueprint $copy $root
    } catch {
        $numericStringRejected = $true
    }
    Assert-Equal $true $numericStringRejected "Numeric string count floor was accepted."

    $pathEscapeRejected = $false
    $copy = $blueprint | ConvertTo-Json -Depth 30 | ConvertFrom-Json
    $copy.domains[0].source = "../outside.md"
    try {
        Assert-AeosBehaviorBlueprint $copy $root
    } catch {
        $pathEscapeRejected = $true
    }
    Assert-Equal $true $pathEscapeRejected "Repository provenance path escape was accepted."

    $expandedUnknownPropertyRejected = $false
    $copy = $catalog | ConvertTo-Json -Depth 30 | ConvertFrom-Json
    $copy.items[0] | Add-Member -NotePropertyName command -NotePropertyValue "arbitrary-command"
    try {
        [void](Invoke-CatalogSchemaValidation $copy)
    } catch {
        $expandedUnknownPropertyRejected = $true
    }
    Assert-Equal $true $expandedUnknownPropertyRejected "Unknown expanded catalog property was accepted."

    $duplicateDomainRejected = $false
    $copy = $blueprint | ConvertTo-Json -Depth 30 | ConvertFrom-Json
    $copy.domains[1].id = $copy.domains[0].id
    try {
        Assert-AeosBehaviorBlueprint $copy $root
    } catch {
        $duplicateDomainRejected = $true
    }
    Assert-Equal $true $duplicateDomainRejected "Duplicate domain ID was accepted."

    Write-Output "AEOS behavior-system self-test passed."
    exit 0
}

$blueprint = Read-AeosBehaviorBlueprint $blueprintPath
$catalog = Expand-AeosBehaviorCatalog $blueprint $root
[void](Invoke-CatalogSchemaValidation $catalog)
Test-AeosRoutingGoldenCases $catalog
$counts = Get-AeosBehaviorCounts $catalog

Write-Output "AEOS behavior-system validation passed."
Write-Output "Domains: $($counts['domain']). Items: $($counts['totalItems'])."
Write-Output "Behaviors: $($counts['behavior']). Failures: $($counts['failure']). Signals: $($counts['signal']). Recoveries: $($counts['recovery'])."
Write-Output "Decisions: $($counts['decision']). Mental models: $($counts['mental-model']). Collaboration: $($counts['collaboration']). Governance: $($counts['governance'])."
exit 0
