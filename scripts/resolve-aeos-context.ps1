[CmdletBinding()]
param(
    [string]$RepositoryRoot = (Split-Path -Parent $PSScriptRoot),
    [Parameter(Mandatory)][string[]]$SignalIds,
    [Parameter(Mandatory)][ValidateSet("understand", "plan", "implement", "debug", "review", "release")][string]$Phase,
    [switch]$Trusted
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$root = (Resolve-Path $RepositoryRoot).Path
Import-Module (Join-Path $root "scripts\AeosBehaviorSystem.psm1") -Force
$blueprint = Read-AeosBehaviorBlueprint (Join-Path $root "AEOS\16-behavior-system\source\catalog-blueprint.json")
$catalog = Expand-AeosBehaviorCatalog $blueprint $root
$record = Resolve-AeosBehaviorContext $catalog $SignalIds $Phase ([bool]$Trusted)
$record | ConvertTo-Json -Depth 10
if ($record.terminalState -ne "PASSED") { exit 2 }
exit 0
