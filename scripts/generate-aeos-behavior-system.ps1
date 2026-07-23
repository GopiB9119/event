[CmdletBinding()]
param(
    [string]$RepositoryRoot = (Split-Path -Parent $PSScriptRoot),
    [switch]$Check
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$root = (Resolve-Path $RepositoryRoot).Path
$modulePath = Join-Path $root "scripts\AeosBehaviorSystem.psm1"
$blueprintPath = Join-Path $root "AEOS\16-behavior-system\source\catalog-blueprint.json"
$generatedRoot = Join-Path $root "AEOS\16-behavior-system\generated"
Import-Module $modulePath -Force

$blueprint = Read-AeosBehaviorBlueprint $blueprintPath
$catalog = Expand-AeosBehaviorCatalog $blueprint $root
Test-AeosRoutingGoldenCases $catalog
$catalogJson = ConvertTo-DeterministicJson $catalog
$catalogHash = Get-AeosTextSha256 $catalogJson
$counts = Get-AeosBehaviorCounts $catalog
$countsRecord = [pscustomobject][ordered]@{
    schemaVersion = 1
    catalogVersion = $catalog.catalogVersion
    catalogSha256 = $catalogHash
    counts = $counts
}
$outputs = [ordered]@{}
$outputs["catalog.json"] = $catalogJson
$outputs["COUNTS.json"] = ConvertTo-DeterministicJson $countsRecord
$outputs["INDEX.md"] = Get-AeosIndexMarkdown $catalog $counts $catalogHash
$outputs["COLLABORATION.md"] = Get-AeosCollaborationMarkdown $catalog
foreach ($domain in @($catalog.domains | Sort-Object id)) {
    $outputs["domains\$($domain.id).md"] = Get-AeosDomainMarkdown $domain $catalog
}

if ($Check) {
    $expectedPaths = @($outputs.Keys | ForEach-Object { Join-Path $generatedRoot $_ })
    $actualFiles = if (Test-Path -LiteralPath $generatedRoot -PathType Container) {
        @(Get-ChildItem $generatedRoot -File -Recurse | Select-Object -ExpandProperty FullName)
    } else {
        @()
    }
    foreach ($key in $outputs.Keys) {
        $path = Join-Path $generatedRoot $key
        if (-not (Test-Path -LiteralPath $path -PathType Leaf)) { throw "Generated file is missing: $key" }
        $actual = Get-Content -LiteralPath $path -Raw -Encoding UTF8
        $actual = $actual -replace "`r`n", "`n"
        if ($actual -ne $outputs[$key]) { throw "Generated file is stale: $key" }
    }
    foreach ($path in $actualFiles) {
        if ($expectedPaths -notcontains $path) { throw "Orphan generated file exists: $path" }
    }
    Write-Output "AEOS behavior-system generated files are current."
    exit 0
}

if (Test-Path -LiteralPath $generatedRoot -PathType Container) {
    Remove-Item $generatedRoot -Recurse -Force
}
New-Item -ItemType Directory -Path (Join-Path $generatedRoot "domains") -Force | Out-Null
foreach ($key in $outputs.Keys) {
    $path = Join-Path $generatedRoot $key
    $resolvedPath = [System.IO.Path]::GetFullPath($path)
    $resolvedRoot = [System.IO.Path]::GetFullPath($generatedRoot) + [System.IO.Path]::DirectorySeparatorChar
    if (-not $resolvedPath.StartsWith($resolvedRoot, [System.StringComparison]::OrdinalIgnoreCase)) {
        throw "Generator output path escaped the fixed generated directory: $key"
    }
    Write-Utf8NoBomLf $path $outputs[$key]
}
Write-Output "AEOS behavior-system generated $($outputs.Count) files."
Write-Output "Catalog SHA-256: $catalogHash"
exit 0
