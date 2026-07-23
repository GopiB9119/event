Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$script:IdPattern = '^[a-z0-9]+(?:-[a-z0-9]+)*$'
$script:GeneratedRelativePath = "AEOS\16-behavior-system\generated"
$script:ForbiddenAuthorityTerms = @(
    "deploy",
    "publish",
    "delete data",
    "rotate secrets",
    "spend money",
    "expand authority"
)
$script:AllowedTemplateTokens = @(
    "subject",
    "artifact",
    "evidence",
    "invariant",
    "boundary",
    "risk",
    "failureMechanism",
    "recoveryAction",
    "decisionAxis",
    "mentalMechanism",
    "governanceControl",
    "decisionOwner",
    "challengeOwner",
    "handoffReceiver"
)

function Get-AeosTextSha256 {
    param([Parameter(Mandatory)][AllowEmptyString()][string]$Text)

    $algorithm = [System.Security.Cryptography.SHA256]::Create()
    try {
        $bytes = [System.Text.Encoding]::UTF8.GetBytes($Text)
        return (($algorithm.ComputeHash($bytes) | ForEach-Object { $_.ToString("x2") }) -join "")
    } finally {
        $algorithm.Dispose()
    }
}

function Assert-NoDuplicateJsonProperties {
    param([Parameter(Mandatory)][string]$Json)

    $stack = New-Object System.Collections.Stack
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
                throw "Behavior catalog JSON contains an unterminated string."
            }

            $nextIndex = $index + 1
            while ($nextIndex -lt $Json.Length -and [char]::IsWhiteSpace($Json[$nextIndex])) {
                $nextIndex++
            }
            if ($nextIndex -lt $Json.Length -and $Json[$nextIndex] -eq ':' -and $stack.Count -gt 0) {
                $frame = $stack.Peek()
                if ($frame.type -eq "object") {
                    $rawPropertyName = $Json.Substring($stringStart, $index - $stringStart)
                    if ($rawPropertyName.Contains('\')) {
                        throw "Escaped JSON property names are not supported by the behavior catalog."
                    }
                    if (-not $frame.keys.Add($rawPropertyName)) {
                        throw "Behavior catalog JSON contains duplicate property '$rawPropertyName'."
                    }
                }
            }
        } else {
            switch ($character) {
                '{' {
                    $keys = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::Ordinal)
                    $stack.Push([pscustomobject]@{ type = "object"; keys = $keys })
                }
                '[' { $stack.Push([pscustomobject]@{ type = "array"; keys = $null }) }
                '}' {
                    if ($stack.Count -eq 0 -or $stack.Peek().type -ne "object") {
                        throw "Behavior catalog JSON contains mismatched object delimiters."
                    }
                    [void]$stack.Pop()
                }
                ']' {
                    if ($stack.Count -eq 0 -or $stack.Peek().type -ne "array") {
                        throw "Behavior catalog JSON contains mismatched array delimiters."
                    }
                    [void]$stack.Pop()
                }
            }
        }
        $index++
    }

    if ($stack.Count -ne 0) {
        throw "Behavior catalog JSON contains unterminated objects or arrays."
    }
}

function Read-AeosBehaviorBlueprint {
    param([Parameter(Mandatory)][string]$Path)

    if (-not (Test-Path -LiteralPath $Path -PathType Leaf)) {
        throw "Behavior catalog blueprint is missing: $Path"
    }

    try {
        $json = Get-Content -LiteralPath $Path -Raw -Encoding UTF8
        Assert-NoDuplicateJsonProperties $json
        return $json | ConvertFrom-Json
    } catch {
        throw "Behavior catalog blueprint could not be parsed: $($_.Exception.Message)"
    }
}

function Assert-ExactProperties {
    param(
        [Parameter(Mandatory)]$Value,
        [Parameter(Mandatory)][string[]]$Expected,
        [Parameter(Mandatory)][string]$Context
    )

    $actual = @($Value.PSObject.Properties.Name)
    foreach ($name in $Expected) {
        if ($actual -notcontains $name) {
            throw "$Context is missing required property '$name'."
        }
    }
    foreach ($name in $actual) {
        if ($Expected -notcontains $name) {
            throw "$Context contains unsupported property '$name'."
        }
    }
}

function Assert-NonEmptyString {
    param(
        [AllowNull()]$Value,
        [Parameter(Mandatory)][string]$Context
    )

    if ($Value -isnot [string] -or [string]::IsNullOrWhiteSpace($Value)) {
        throw "$Context must be a non-empty string."
    }
}

function Assert-Integer {
    param(
        [AllowNull()]$Value,
        [Parameter(Mandatory)][string]$Context
    )

    if ($Value -isnot [int] -and $Value -isnot [long]) {
        throw "$Context must be a JSON integer."
    }
}

function Assert-StringArray {
    param(
        [AllowNull()]$Value,
        [Parameter(Mandatory)][string]$Context,
        [switch]$AllowEmpty
    )

    if ($Value -isnot [System.Array]) {
        throw "$Context must be a JSON array."
    }
    $values = @($Value)
    if (-not $AllowEmpty -and $values.Count -eq 0) {
        throw "$Context must contain at least one value."
    }
    $seen = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::Ordinal)
    foreach ($item in $values) {
        Assert-NonEmptyString $item "$Context item"
        if (-not $seen.Add($item)) {
            throw "$Context contains duplicate value '$item'."
        }
    }
}

function Assert-Id {
    param(
        [AllowNull()]$Value,
        [Parameter(Mandatory)][string]$Context
    )

    Assert-NonEmptyString $Value $Context
    if ($Value -notmatch $script:IdPattern) {
        throw "$Context '$Value' must use lowercase ASCII kebab-case."
    }
}

function Resolve-RepositorySourceFile {
    param(
        [Parameter(Mandatory)][string]$RepositoryRoot,
        [Parameter(Mandatory)][string]$RelativePath,
        [Parameter(Mandatory)][string]$Context
    )

    if ([System.IO.Path]::IsPathRooted($RelativePath)) {
        throw "$Context must be repository-relative."
    }
    $root = [System.IO.Path]::GetFullPath($RepositoryRoot).TrimEnd(
        [System.IO.Path]::DirectorySeparatorChar,
        [System.IO.Path]::AltDirectorySeparatorChar
    )
    $candidate = [System.IO.Path]::GetFullPath(
        (Join-Path $root $RelativePath.Replace('/', [System.IO.Path]::DirectorySeparatorChar))
    )
    $rootPrefix = $root + [System.IO.Path]::DirectorySeparatorChar
    if (-not $candidate.StartsWith($rootPrefix, [System.StringComparison]::OrdinalIgnoreCase)) {
        throw "$Context escapes the repository boundary."
    }
    if (-not (Test-Path -LiteralPath $candidate -PathType Leaf)) {
        throw "$Context is missing: $RelativePath"
    }
    return $candidate
}

function Assert-TemplateTokens {
    param(
        [Parameter(Mandatory)][string]$Template,
        [Parameter(Mandatory)][string]$Context,
        [string[]]$AdditionalAllowedTokens = @()
    )

    $allowed = @($script:AllowedTemplateTokens + $AdditionalAllowedTokens)
    foreach ($match in [regex]::Matches($Template, '\{\{([A-Za-z][A-Za-z0-9]*)\}\}')) {
        $token = $match.Groups[1].Value
        if ($allowed -notcontains $token) {
            throw "$Context contains unsupported template token '{{$token}}'."
        }
    }
    $withoutKnownTokens = [regex]::Replace($Template, '\{\{([A-Za-z][A-Za-z0-9]*)\}\}', '')
    if ($withoutKnownTokens.Contains('{{') -or $withoutKnownTokens.Contains('}}')) {
        throw "$Context contains malformed template syntax."
    }
}

function ConvertTo-TemplateMap {
    param([Parameter(Mandatory)]$Value)

    $map = @{}
    foreach ($property in $Value.PSObject.Properties) {
        if ($property.Value -is [string]) {
            $map[$property.Name] = [string]$property.Value
        }
    }
    return $map
}

function Resolve-Template {
    param(
        [Parameter(Mandatory)][string]$Template,
        [Parameter(Mandatory)][hashtable]$Values,
        [Parameter(Mandatory)][string]$Context
    )

    $result = $Template
    foreach ($match in [regex]::Matches($Template, '\{\{([A-Za-z][A-Za-z0-9]*)\}\}')) {
        $token = $match.Groups[1].Value
        if (-not $Values.ContainsKey($token) -or [string]::IsNullOrWhiteSpace([string]$Values[$token])) {
            throw "$Context cannot resolve template token '{{$token}}'."
        }
        $result = $result.Replace("{{$token}}", [string]$Values[$token])
    }
    if ($result.Contains('{{') -or $result.Contains('}}')) {
        throw "$Context contains an unresolved template token."
    }
    return $result
}

function Resolve-TemplateArray {
    param(
        [Parameter(Mandatory)]$Templates,
        [Parameter(Mandatory)][hashtable]$Values,
        [Parameter(Mandatory)][string]$Context
    )

    return @($Templates | ForEach-Object { Resolve-Template ([string]$_) $Values $Context })
}

function Get-ItemProvenance {
    param(
        [Parameter(Mandatory)]$Defaults,
        [Parameter(Mandatory)][string[]]$Sources
    )

    return [ordered]@{
        derivationMethod = $Defaults.derivationMethod
        evidenceLabel = $Defaults.evidenceLabel
        sources = @($Sources | Sort-Object -Unique)
        owner = $Defaults.owner
        reviewer = $Defaults.reviewer
        reviewedOn = $Defaults.reviewedOn
        invalidationTriggers = @($Defaults.invalidationTriggers)
    }
}

function Get-CommonAuthority {
    param([Parameter(Mandatory)][string]$Kind)

    $ceiling = if ($Kind -in @("behavior", "recovery")) { "bounded-local" } else { "read-only" }
    return [ordered]@{
        mutationCeiling = $ceiling
        forbiddenMutations = @($script:ForbiddenAuthorityTerms)
        approvalRequired = $false
        independentReviewRequired = $true
    }
}

function Get-CommonItemBase {
    param(
        [Parameter(Mandatory)][string]$Id,
        [Parameter(Mandatory)][string]$Kind,
        [Parameter(Mandatory)][string]$Title,
        [Parameter(Mandatory)][AllowEmptyCollection()][string[]]$DomainIds,
        [Parameter(Mandatory)][AllowEmptyCollection()][string[]]$ScopeIds,
        [Parameter(Mandatory)][string]$Statement,
        [Parameter(Mandatory)][string]$DomainDelta,
        [Parameter(Mandatory)][AllowEmptyCollection()][string[]]$SignalIds,
        [Parameter(Mandatory)][string[]]$Phases,
        [Parameter(Mandatory)][string[]]$RiskClasses,
        [Parameter(Mandatory)][string[]]$ProtectedInvariants,
        [Parameter(Mandatory)][string[]]$RequiredEvidence,
        [Parameter(Mandatory)][string[]]$StopConditions,
        [Parameter(Mandatory)]$Provenance
    )

    return [ordered]@{
        id = $Id
        kind = $Kind
        status = "active"
        itemVersion = 1
        title = $Title
        domainIds = @($DomainIds)
        scopeIds = @($ScopeIds)
        statement = $Statement
        domainDelta = $DomainDelta
        activation = [ordered]@{
            allOfSignals = @()
            anyOfSignals = @($SignalIds)
            noneOfSignals = @()
            phases = @($Phases | Sort-Object -Unique)
            riskClasses = @($RiskClasses | Sort-Object -Unique)
        }
        protectedInvariants = @($ProtectedInvariants)
        requiredEvidence = @($RequiredEvidence)
        stopConditions = @($StopConditions)
        authority = Get-CommonAuthority $Kind
        dependencies = @()
        conflicts = @()
        provenance = $Provenance
        supersedes = @()
        replacementIds = @()
    }
}

function Add-TypeFields {
    param(
        [Parameter(Mandatory)][System.Collections.Specialized.OrderedDictionary]$Base,
        [Parameter(Mandatory)][System.Collections.Specialized.OrderedDictionary]$Fields
    )

    foreach ($key in $Fields.Keys) {
        $Base[$key] = $Fields[$key]
    }
    return [pscustomobject]$Base
}

function Get-FailureRelationshipMap {
    param([Parameter(Mandatory)][string]$DomainId)

    return @{
        "premature-action" = [ordered]@{
            signals = @("signal-$DomainId-explicit-mission", "signal-$DomainId-repository-evidence")
            recoveries = @("recovery-$DomainId-reset-and-reconstruct")
        }
        "stale-context" = [ordered]@{
            signals = @("signal-$DomainId-repository-evidence")
            recoveries = @("recovery-$DomainId-reset-and-reconstruct")
        }
        "boundary-violation" = [ordered]@{
            signals = @("signal-$DomainId-constraint-risk")
            recoveries = @("recovery-$DomainId-escalate-and-contain")
        }
        "silent-failure" = [ordered]@{
            signals = @("signal-$DomainId-runtime-failure")
            recoveries = @("recovery-$DomainId-isolate-and-repair")
        }
        "unbounded-loop" = [ordered]@{
            signals = @("signal-$DomainId-runtime-failure", "signal-$DomainId-constraint-risk")
            recoveries = @("recovery-$DomainId-reset-and-reconstruct")
        }
        "evidence-overclaim" = [ordered]@{
            signals = @("signal-$DomainId-repository-evidence", "signal-$DomainId-constraint-risk")
            recoveries = @("recovery-$DomainId-reset-and-reconstruct")
        }
    }
}

function Get-RecoveryFailureIds {
    param(
        [Parameter(Mandatory)][string]$DomainId,
        [Parameter(Mandatory)][string]$RecoveryArchetypeId
    )

    switch ($RecoveryArchetypeId) {
        "reset-and-reconstruct" {
            return @(
                "failure-$DomainId-premature-action",
                "failure-$DomainId-stale-context",
                "failure-$DomainId-unbounded-loop",
                "failure-$DomainId-evidence-overclaim"
            )
        }
        "isolate-and-repair" { return @("failure-$DomainId-silent-failure") }
        "escalate-and-contain" { return @("failure-$DomainId-boundary-violation") }
        default { throw "Unknown recovery archetype '$RecoveryArchetypeId'." }
    }
}

function Expand-AeosBehaviorCatalog {
    param(
        [Parameter(Mandatory)]$Blueprint,
        [Parameter(Mandatory)][string]$RepositoryRoot
    )

    Assert-AeosBehaviorBlueprint $Blueprint $RepositoryRoot
    $domains = New-Object System.Collections.Generic.List[object]
    $scopes = New-Object System.Collections.Generic.List[object]
    $items = New-Object System.Collections.Generic.List[object]
    $routingRules = New-Object System.Collections.Generic.List[object]

    foreach ($domain in @($Blueprint.domains | Sort-Object id)) {
        $values = ConvertTo-TemplateMap $domain
        $provenance = Get-ItemProvenance $Blueprint.provenanceDefaults @($domain.source)
        $domains.Add([pscustomobject][ordered]@{
            id = $domain.id
            name = $domain.name
            status = "active"
            itemVersion = 1
            purpose = $domain.purpose
            boundaries = @($domain.boundary)
            invariants = @($domain.invariant)
            positiveSignals = @($domain.positiveSignals)
            negativeSignals = @($domain.negativeSignals)
            riskClasses = @($domain.riskClasses)
            defaultEvidence = @($domain.evidence)
            relatedDomainIds = @($domain.relatedDomainIds | Sort-Object)
            provenance = $provenance
        })

        $profile = @($Blueprint.applicabilityProfiles | Where-Object { $_.id -eq $domain.applicabilityProfileId })
        if ($profile.Count -ne 1) {
            throw "Domain '$($domain.id)' must resolve exactly one applicability profile."
        }
        $profile = $profile[0]
        $domainSignalIds = @($profile.signal | ForEach-Object { "signal-$($domain.id)-$_" })
        $failureRelationships = Get-FailureRelationshipMap $domain.id

        foreach ($archetypeId in @($profile.signal)) {
            $archetype = @($Blueprint.archetypes.signal | Where-Object { $_.id -eq $archetypeId })[0]
            $id = "signal-$($domain.id)-$archetypeId"
            $base = Get-CommonItemBase `
                -Id $id `
                -Kind "signal" `
                -Title "$($domain.name): $($archetype.name)" `
                -DomainIds @($domain.id) `
                -ScopeIds @() `
                -Statement (Resolve-Template $archetype.predicate $values "$id predicate") `
                -DomainDelta "For $($domain.name), this signal observes $($domain.subject) through $($domain.artifact) while rejecting stale or untrusted substitutes." `
                -SignalIds @() `
                -Phases @("understand", "plan", "implement", "debug", "review", "release") `
                -RiskClasses @($domain.riskClasses) `
                -ProtectedInvariants @($domain.invariant) `
                -RequiredEvidence @($domain.evidence) `
                -StopConditions @("Stop when the source is stale, untrusted, or below the required confidence.") `
                -Provenance $provenance
            $item = Add-TypeFields $base ([ordered]@{
                sourceType = $archetype.sourceType
                predicate = Resolve-Template $archetype.predicate $values "$id predicate"
                freshness = Resolve-Template $archetype.freshness $values "$id freshness"
                minimumConfidence = [double]$archetype.minimumConfidence
                negativeMatch = Resolve-Template $archetype.negativeMatch $values "$id negativeMatch"
            })
            $items.Add($item)
            $routingRules.Add([pscustomobject][ordered]@{
                id = "route-$($domain.id)-$archetypeId"
                priority = switch ($archetypeId) {
                    "runtime-failure" { 10 }
                    "constraint-risk" { 20 }
                    "explicit-mission" { 30 }
                    default { 40 }
                }
                trustedSignalIds = @($id)
                selectDomainIds = @($domain.id)
                excludeDomainIds = @()
                rationale = "A trusted $($archetype.name.ToLowerInvariant()) selects the $($domain.name) domain."
            })
        }

        foreach ($archetypeId in @($profile.behavior)) {
            $archetype = @($Blueprint.archetypes.behavior | Where-Object { $_.id -eq $archetypeId })[0]
            $id = "behavior-$($domain.id)-$archetypeId"
            $action = Resolve-Template $archetype.action $values "$id action"
            $base = Get-CommonItemBase `
                -Id $id `
                -Kind "behavior" `
                -Title "$($domain.name): $($archetype.name)" `
                -DomainIds @($domain.id) `
                -ScopeIds @() `
                -Statement $action `
                -DomainDelta "For $($domain.name), this behavior operates on $($domain.artifact), uses $($domain.evidence), and protects '$($domain.invariant)'." `
                -SignalIds $domainSignalIds `
                -Phases @($archetype.phases) `
                -RiskClasses @($domain.riskClasses) `
                -ProtectedInvariants @($domain.invariant) `
                -RequiredEvidence @($domain.evidence) `
                -StopConditions @("Stop when $($domain.governanceControl).", "Stop when evidence, rollback, or authority is unavailable.") `
                -Provenance $provenance
            $items.Add((Add-TypeFields $base ([ordered]@{
                trigger = Resolve-Template $archetype.trigger $values "$id trigger"
                action = $action
                successPredicate = Resolve-Template $archetype.success $values "$id success"
            })))
        }

        foreach ($archetypeId in @($profile.failure)) {
            $archetype = @($Blueprint.archetypes.failure | Where-Object { $_.id -eq $archetypeId })[0]
            $id = "failure-$($domain.id)-$archetypeId"
            $relationship = $failureRelationships[$archetypeId]
            $mechanism = Resolve-Template $archetype.mechanism $values "$id mechanism"
            $base = Get-CommonItemBase `
                -Id $id `
                -Kind "failure" `
                -Title "$($domain.name): $($archetype.name)" `
                -DomainIds @($domain.id) `
                -ScopeIds @() `
                -Statement $mechanism `
                -DomainDelta "In $($domain.name), this failure threatens '$($domain.invariant)' through $($domain.failureMechanism)." `
                -SignalIds @($relationship.signals) `
                -Phases @("understand", "plan", "implement", "debug", "review", "release") `
                -RiskClasses @($domain.riskClasses) `
                -ProtectedInvariants @($domain.invariant) `
                -RequiredEvidence @($domain.evidence) `
                -StopConditions @("Stop propagation when the failure is observed.", "Do not repair before the mechanism is reproduced or evidence is preserved.") `
                -Provenance $provenance
            $items.Add((Add-TypeFields $base ([ordered]@{
                cause = Resolve-Template $archetype.cause $values "$id cause"
                mechanism = $mechanism
                symptoms = Resolve-TemplateArray $archetype.symptoms $values "$id symptoms"
                impact = Resolve-Template $archetype.impact $values "$id impact"
                detectorSignalIds = @($relationship.signals)
                recoveryIds = @($relationship.recoveries)
            })))
        }

        foreach ($archetypeId in @($profile.recovery)) {
            $archetype = @($Blueprint.archetypes.recovery | Where-Object { $_.id -eq $archetypeId })[0]
            $id = "recovery-$($domain.id)-$archetypeId"
            $failureIds = Get-RecoveryFailureIds $domain.id $archetypeId
            $steps = Resolve-TemplateArray $archetype.steps $values "$id steps"
            $base = Get-CommonItemBase `
                -Id $id `
                -Kind "recovery" `
                -Title "$($domain.name): $($archetype.name)" `
                -DomainIds @($domain.id) `
                -ScopeIds @() `
                -Statement ($steps -join " ") `
                -DomainDelta "For $($domain.name), recovery targets $($domain.failureMechanism) in $($domain.artifact) and exits only with $($domain.evidence)." `
                -SignalIds $domainSignalIds `
                -Phases @("implement", "debug", "review", "release") `
                -RiskClasses @($domain.riskClasses) `
                -ProtectedInvariants @($domain.invariant) `
                -RequiredEvidence @($domain.evidence) `
                -StopConditions @("Stop at terminal state $($archetype.terminalState).", "Stop when rollback or escalation ownership is missing.") `
                -Provenance $provenance
            $items.Add((Add-TypeFields $base ([ordered]@{
                entryConditions = Resolve-TemplateArray $archetype.entry $values "$id entry"
                steps = $steps
                rollback = Resolve-Template $archetype.rollback $values "$id rollback"
                exitEvidence = Resolve-TemplateArray $archetype.exitEvidence $values "$id exitEvidence"
                escalationOwner = "Engineering coordinator or accountable $($domain.name) owner"
                terminalState = $archetype.terminalState
                failureIds = @($failureIds)
            })))
        }

        foreach ($archetypeId in @($profile.decision)) {
            $archetype = @($Blueprint.archetypes.decision | Where-Object { $_.id -eq $archetypeId })[0]
            $id = "decision-$($domain.id)-$archetypeId"
            $rule = Resolve-Template $archetype.rule $values "$id rule"
            $base = Get-CommonItemBase `
                -Id $id `
                -Kind "decision" `
                -Title "$($domain.name): $($archetype.name)" `
                -DomainIds @($domain.id) `
                -ScopeIds @() `
                -Statement $rule `
                -DomainDelta "For $($domain.name), this model decides $($domain.decisionAxis) using $($domain.evidence) and the constraint '$($domain.invariant)'." `
                -SignalIds $domainSignalIds `
                -Phases @("understand", "plan", "debug", "review", "release") `
                -RiskClasses @($domain.riskClasses) `
                -ProtectedInvariants @($domain.invariant) `
                -RequiredEvidence @($domain.evidence) `
                -StopConditions @("Stop when options, owner values, or falsifying evidence are missing.") `
                -Provenance $provenance
            $items.Add((Add-TypeFields $base ([ordered]@{
                question = Resolve-Template $archetype.question $values "$id question"
                inputs = Resolve-TemplateArray $archetype.inputs $values "$id inputs"
                options = Resolve-TemplateArray $archetype.options $values "$id options"
                decisionRule = $rule
                tradeoffs = Resolve-TemplateArray $archetype.tradeoffs $values "$id tradeoffs"
                falsifier = Resolve-Template $archetype.falsifier $values "$id falsifier"
                contraindications = Resolve-TemplateArray $archetype.contraindications $values "$id contraindications"
            })))
        }

        foreach ($archetypeId in @($profile.'mental-model')) {
            $archetype = @($Blueprint.archetypes.'mental-model' | Where-Object { $_.id -eq $archetypeId })[0]
            $id = "mental-model-$($domain.id)-$archetypeId"
            $mechanism = Resolve-Template $archetype.mechanism $values "$id mechanism"
            $base = Get-CommonItemBase `
                -Id $id `
                -Kind "mental-model" `
                -Title "$($domain.name): $($archetype.name)" `
                -DomainIds @($domain.id) `
                -ScopeIds @() `
                -Statement $mechanism `
                -DomainDelta "For $($domain.name), this model maps $($domain.mentalMechanism) onto $($domain.subject) and $($domain.artifact)." `
                -SignalIds $domainSignalIds `
                -Phases @("understand", "plan", "debug", "review") `
                -RiskClasses @($domain.riskClasses) `
                -ProtectedInvariants @($domain.invariant) `
                -RequiredEvidence @($domain.evidence) `
                -StopConditions @("Stop using the model when its counterexample or misuse warning applies.") `
                -Provenance $provenance
            $items.Add((Add-TypeFields $base ([ordered]@{
                mechanism = $mechanism
                domainMapping = Resolve-Template $archetype.mapping $values "$id mapping"
                predictions = Resolve-TemplateArray $archetype.predictions $values "$id predictions"
                counterexample = Resolve-Template $archetype.counterexample $values "$id counterexample"
                misuseWarning = Resolve-Template $archetype.misuseWarning $values "$id misuseWarning"
            })))
        }

        foreach ($archetypeId in @($profile.governance)) {
            $archetype = @($Blueprint.archetypes.governance | Where-Object { $_.id -eq $archetypeId })[0]
            $id = "governance-$($domain.id)-$archetypeId"
            $obligation = Resolve-Template $archetype.obligation $values "$id obligation"
            $base = Get-CommonItemBase `
                -Id $id `
                -Kind "governance" `
                -Title "$($domain.name): $($archetype.name)" `
                -DomainIds @($domain.id) `
                -ScopeIds @() `
                -Statement $obligation `
                -DomainDelta "For $($domain.name), this policy enforces $($domain.governanceControl) at $($domain.boundary)." `
                -SignalIds $domainSignalIds `
                -Phases @("plan", "implement", "debug", "review", "release") `
                -RiskClasses @($domain.riskClasses) `
                -ProtectedInvariants @($domain.invariant) `
                -RequiredEvidence @($domain.evidence) `
                -StopConditions @("Block the transition when enforcement, evidence, or exception authority is absent.") `
                -Provenance $provenance
            $items.Add((Add-TypeFields $base ([ordered]@{
                obligation = $obligation
                prohibition = Resolve-Template $archetype.prohibition $values "$id prohibition"
                enforcement = Resolve-Template $archetype.enforcement $values "$id enforcement"
                exceptionAuthority = Resolve-Template $archetype.exceptionAuthority $values "$id exceptionAuthority"
                auditEvidence = Resolve-TemplateArray $archetype.auditEvidence $values "$id auditEvidence"
            })))
        }
    }

    $collaborationProvenance = Get-ItemProvenance $Blueprint.provenanceDefaults @("AEOS/00-foundation/OPERATING_MODEL.md")
    foreach ($scope in @($Blueprint.collaborationScopes | Sort-Object id)) {
        $scopeValues = ConvertTo-TemplateMap $scope
        $scopes.Add([pscustomobject][ordered]@{
            id = $scope.id
            name = $scope.name
            purpose = $scope.purpose
            participants = @($scope.participants)
            artifact = $scope.artifact
            provenance = $collaborationProvenance
        })

        foreach ($archetype in @($Blueprint.archetypes.collaboration | Sort-Object id)) {
            $id = "collaboration-$($scope.id)-$($archetype.id)"
            $statement = Resolve-Template $archetype.statement $scopeValues "$id statement"
            $base = Get-CommonItemBase `
                -Id $id `
                -Kind "collaboration" `
                -Title "$($scope.name): $($archetype.name)" `
                -DomainIds @() `
                -ScopeIds @($scope.id) `
                -Statement $statement `
                -DomainDelta "For $($scope.name), this pattern coordinates $($scope.participants -join ', ') around $($scope.artifact)." `
                -SignalIds @() `
                -Phases @("understand", "plan", "review", "release") `
                -RiskClasses @("collaboration") `
                -ProtectedInvariants @("Authority, evidence, and handoff remain explicit for $($scope.name).") `
                -RequiredEvidence @("Accepted $($scope.artifact)", "Named decision and next owner") `
                -StopConditions @("Stop when ownership, conflict routing, or handoff evidence is missing.") `
                -Provenance $collaborationProvenance
            $items.Add((Add-TypeFields $base ([ordered]@{
                participants = @($scope.participants)
                sharedArtifact = $scope.artifact
                authoritySplit = Resolve-Template $archetype.authoritySplit $scopeValues "$id authoritySplit"
                handoff = Resolve-Template $archetype.handoff $scopeValues "$id handoff"
                conflictRoute = Resolve-Template $archetype.conflictRoute $scopeValues "$id conflictRoute"
                completionEvidence = @("Accepted $($scope.artifact)", "Documented decision, evidence, and next owner")
            })))
        }
    }

    $catalog = [pscustomobject][ordered]@{
        schemaVersion = 1
        catalogVersion = $Blueprint.catalogVersion
        kernelCompatibility = [ordered]@{
            minimum = $Blueprint.kernelCompatibility.minimum
            maximumExclusive = $Blueprint.kernelCompatibility.maximumExclusive
        }
        countFloors = [ordered]@{
            domain = [int]$Blueprint.countFloors.domain
            behavior = [int]$Blueprint.countFloors.behavior
            failure = [int]$Blueprint.countFloors.failure
            signal = [int]$Blueprint.countFloors.signal
            recovery = [int]$Blueprint.countFloors.recovery
            decision = [int]$Blueprint.countFloors.decision
            'mental-model' = [int]$Blueprint.countFloors.'mental-model'
            collaboration = [int]$Blueprint.countFloors.collaboration
            governance = [int]$Blueprint.countFloors.governance
        }
        domains = @($domains.ToArray())
        collaborationScopes = @($scopes.ToArray())
        items = @($items.ToArray() | Sort-Object id)
        routing = [ordered]@{
            maximumDomains = [int]$Blueprint.routing.maximumDomains
            maximumItems = [int]$Blueprint.routing.maximumItems
            minimumConfidence = [double]$Blueprint.routing.minimumConfidence
            rules = @($routingRules.ToArray() | Sort-Object priority, id)
            goldenCases = @($Blueprint.routing.goldenCases | Sort-Object id)
        }
    }

    Assert-AeosExpandedCatalog $catalog
    return $catalog
}

function Assert-AeosBehaviorBlueprint {
    param(
        [Parameter(Mandatory)]$Blueprint,
        [Parameter(Mandatory)][string]$RepositoryRoot
    )

    Assert-ExactProperties $Blueprint @(
        "schemaVersion",
        "catalogVersion",
        "kernelCompatibility",
        "countFloors",
        "provenanceDefaults",
        "applicabilityProfiles",
        "archetypes",
        "domains",
        "collaborationScopes",
        "routing"
    ) "Behavior blueprint"
    if ($Blueprint.schemaVersion -ne 1) { throw "Behavior blueprint schemaVersion must be 1." }
    if ($Blueprint.catalogVersion -notmatch '^[0-9]+\.[0-9]+\.[0-9]+$') { throw "Behavior blueprint catalogVersion is invalid." }

    Assert-ExactProperties $Blueprint.kernelCompatibility @("minimum", "maximumExclusive") "kernelCompatibility"
    Assert-ExactProperties $Blueprint.countFloors @("domain", "behavior", "failure", "signal", "recovery", "decision", "mental-model", "collaboration", "governance") "countFloors"
    $expectedFloors = [ordered]@{
        domain = 50; behavior = 500; failure = 300; signal = 200; recovery = 150
        decision = 100; 'mental-model' = 100; collaboration = 75; governance = 50
    }
    foreach ($key in $expectedFloors.Keys) {
        Assert-Integer $Blueprint.countFloors.$key "Behavior blueprint count floor '$key'"
        if ([int]$Blueprint.countFloors.$key -ne $expectedFloors[$key]) {
            throw "Behavior blueprint count floor '$key' must remain $($expectedFloors[$key])."
        }
    }

    Assert-ExactProperties $Blueprint.provenanceDefaults @("derivationMethod", "evidenceLabel", "owner", "reviewer", "reviewedOn", "invalidationTriggers") "provenanceDefaults"
    foreach ($property in @("derivationMethod", "evidenceLabel", "owner", "reviewer", "reviewedOn")) {
        Assert-NonEmptyString $Blueprint.provenanceDefaults.$property "provenanceDefaults.$property"
    }
    if ($Blueprint.provenanceDefaults.evidenceLabel -notin @("VERIFIED", "SUPPORTED", "ASSUMPTION", "UNKNOWN")) {
        throw "provenanceDefaults.evidenceLabel is unsupported."
    }
    Assert-StringArray $Blueprint.provenanceDefaults.invalidationTriggers "provenanceDefaults.invalidationTriggers"

    if ($Blueprint.applicabilityProfiles -isnot [System.Array] -or @($Blueprint.applicabilityProfiles).Count -eq 0) {
        throw "Behavior blueprint applicabilityProfiles must be a non-empty array."
    }
    $profileIds = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::Ordinal)
    foreach ($profile in @($Blueprint.applicabilityProfiles)) {
        Assert-ExactProperties $profile @("id", "behavior", "failure", "signal", "recovery", "decision", "mental-model", "governance") "applicability profile"
        Assert-Id $profile.id "applicability profile id"
        if (-not $profileIds.Add($profile.id)) { throw "Duplicate applicability profile '$($profile.id)'." }
        foreach ($kind in @("behavior", "failure", "signal", "recovery", "decision", "mental-model", "governance")) {
            Assert-StringArray $profile.$kind "applicability profile '$($profile.id)' $kind"
        }
    }

    Assert-ExactProperties $Blueprint.archetypes @("behavior", "failure", "signal", "recovery", "decision", "mental-model", "governance", "collaboration") "archetypes"
    $archetypePropertySets = @{
        behavior = @("id", "name", "trigger", "action", "success", "phases")
        failure = @("id", "name", "cause", "mechanism", "symptoms", "impact")
        signal = @("id", "name", "sourceType", "predicate", "freshness", "minimumConfidence", "negativeMatch")
        recovery = @("id", "name", "entry", "steps", "rollback", "exitEvidence", "terminalState")
        decision = @("id", "name", "question", "inputs", "options", "rule", "tradeoffs", "falsifier", "contraindications")
        'mental-model' = @("id", "name", "mechanism", "mapping", "predictions", "counterexample", "misuseWarning")
        governance = @("id", "name", "obligation", "prohibition", "enforcement", "exceptionAuthority", "auditEvidence")
        collaboration = @("id", "name", "statement", "authoritySplit", "handoff", "conflictRoute")
    }
    $expectedArchetypeCounts = @{
        behavior = 10; failure = 6; signal = 4; recovery = 3; decision = 2
        'mental-model' = 2; governance = 1; collaboration = 3
    }
    $archetypeIdsByKind = @{}
    foreach ($kind in $archetypePropertySets.Keys) {
        $entries = @($Blueprint.archetypes.$kind)
        if ($entries.Count -ne $expectedArchetypeCounts[$kind]) {
            throw "Archetype kind '$kind' must contain $($expectedArchetypeCounts[$kind]) entries."
        }
        $ids = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::Ordinal)
        foreach ($entry in $entries) {
            Assert-ExactProperties $entry $archetypePropertySets[$kind] "$kind archetype"
            Assert-Id $entry.id "$kind archetype id"
            Assert-NonEmptyString $entry.name "$kind archetype name"
            if (-not $ids.Add($entry.id)) { throw "Duplicate $kind archetype '$($entry.id)'." }
            foreach ($property in $entry.PSObject.Properties) {
                if ($property.Value -is [string]) {
                    Assert-NonEmptyString $property.Value "$kind archetype '$($entry.id)' $($property.Name)"
                    Assert-TemplateTokens $property.Value "$kind archetype '$($entry.id)' $($property.Name)"
                } elseif ($property.Value -is [System.Array]) {
                    Assert-StringArray $property.Value "$kind archetype '$($entry.id)' $($property.Name)"
                    foreach ($value in @($property.Value)) {
                        Assert-TemplateTokens $value "$kind archetype '$($entry.id)' $($property.Name)"
                    }
                }
            }
        }
        $archetypeIdsByKind[$kind] = @($ids)
    }
    foreach ($profile in @($Blueprint.applicabilityProfiles)) {
        foreach ($kind in @("behavior", "failure", "signal", "recovery", "decision", "mental-model", "governance")) {
            foreach ($id in @($profile.$kind)) {
                if ($archetypeIdsByKind[$kind] -notcontains $id) {
                    throw "Applicability profile '$($profile.id)' references unknown $kind archetype '$id'."
                }
            }
        }
    }

    $domainExpected = @(
        "id", "name", "purpose", "subject", "artifact", "evidence", "invariant", "boundary",
        "risk", "failureMechanism", "recoveryAction", "decisionAxis", "mentalMechanism",
        "governanceControl", "positiveSignals", "negativeSignals", "riskClasses",
        "relatedDomainIds", "source", "applicabilityProfileId"
    )
    if ($Blueprint.domains -isnot [System.Array] -or @($Blueprint.domains).Count -ne 50) {
        throw "Behavior blueprint must contain exactly 50 domains."
    }
    $domainIds = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::Ordinal)
    foreach ($domain in @($Blueprint.domains)) {
        Assert-ExactProperties $domain $domainExpected "domain"
        Assert-Id $domain.id "domain id"
        if (-not $domainIds.Add($domain.id)) { throw "Duplicate domain '$($domain.id)'." }
        foreach ($property in @("name", "purpose", "subject", "artifact", "evidence", "invariant", "boundary", "risk", "failureMechanism", "recoveryAction", "decisionAxis", "mentalMechanism", "governanceControl", "source", "applicabilityProfileId")) {
            Assert-NonEmptyString $domain.$property "domain '$($domain.id)' $property"
        }
        foreach ($property in @("positiveSignals", "negativeSignals", "riskClasses", "relatedDomainIds")) {
            Assert-StringArray $domain.$property "domain '$($domain.id)' $property"
        }
        if ($profileIds -notcontains $domain.applicabilityProfileId) {
            throw "Domain '$($domain.id)' references unknown applicability profile '$($domain.applicabilityProfileId)'."
        }
        [void](Resolve-RepositorySourceFile `
            -RepositoryRoot $RepositoryRoot `
            -RelativePath $domain.source `
            -Context "Domain '$($domain.id)' provenance source")
    }
    foreach ($domain in @($Blueprint.domains)) {
        foreach ($related in @($domain.relatedDomainIds)) {
            if ($domainIds -notcontains $related) {
                throw "Domain '$($domain.id)' references unknown related domain '$related'."
            }
        }
    }

    $scopeExpected = @("id", "name", "purpose", "participants", "artifact", "decisionOwner", "challengeOwner", "handoffReceiver")
    if ($Blueprint.collaborationScopes -isnot [System.Array] -or @($Blueprint.collaborationScopes).Count -ne 25) {
        throw "Behavior blueprint must contain exactly 25 collaboration scopes."
    }
    $scopeIds = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::Ordinal)
    foreach ($scope in @($Blueprint.collaborationScopes)) {
        Assert-ExactProperties $scope $scopeExpected "collaboration scope"
        Assert-Id $scope.id "collaboration scope id"
        if (-not $scopeIds.Add($scope.id)) { throw "Duplicate collaboration scope '$($scope.id)'." }
        foreach ($property in @("name", "purpose", "artifact", "decisionOwner", "challengeOwner", "handoffReceiver")) {
            Assert-NonEmptyString $scope.$property "collaboration scope '$($scope.id)' $property"
        }
        Assert-StringArray $scope.participants "collaboration scope '$($scope.id)' participants"
    }

    Assert-ExactProperties $Blueprint.routing @("maximumDomains", "maximumItems", "minimumConfidence", "rules", "goldenCases") "routing"
    Assert-Integer $Blueprint.routing.maximumDomains "routing.maximumDomains"
    Assert-Integer $Blueprint.routing.maximumItems "routing.maximumItems"
    if ([int]$Blueprint.routing.maximumDomains -ne 2) { throw "routing.maximumDomains must remain 2." }
    if ([int]$Blueprint.routing.maximumItems -ne 12) { throw "routing.maximumItems must remain 12." }
    if ([double]$Blueprint.routing.minimumConfidence -lt 0 -or [double]$Blueprint.routing.minimumConfidence -gt 1) { throw "routing.minimumConfidence is invalid." }
    if ($Blueprint.routing.rules -isnot [System.Array]) { throw "routing.rules must be an array." }
    if ($Blueprint.routing.goldenCases -isnot [System.Array] -or @($Blueprint.routing.goldenCases).Count -lt 6) { throw "routing.goldenCases must contain at least six cases." }
    foreach ($case in @($Blueprint.routing.goldenCases)) {
        Assert-ExactProperties $case @("id", "trusted", "phase", "signalIds", "expectedDomainIds", "expectedTerminalState", "reason") "routing golden case"
        Assert-Id $case.id "routing golden case id"
        if ($case.trusted -isnot [bool]) { throw "Golden case '$($case.id)' trusted must be boolean." }
        if ($case.phase -notin @("understand", "plan", "implement", "debug", "review", "release")) { throw "Golden case '$($case.id)' phase is invalid." }
        Assert-StringArray $case.signalIds "golden case '$($case.id)' signalIds" -AllowEmpty
        Assert-StringArray $case.expectedDomainIds "golden case '$($case.id)' expectedDomainIds" -AllowEmpty
        if ($case.expectedTerminalState -notin @("PASSED", "BLOCKED")) { throw "Golden case '$($case.id)' terminal state is invalid." }
        Assert-NonEmptyString $case.reason "golden case '$($case.id)' reason"
    }
}

function Assert-AeosExpandedCatalog {
    param([Parameter(Mandatory)]$Catalog)

    $domainIds = @($Catalog.domains | ForEach-Object { $_.id })
    $scopeIds = @($Catalog.collaborationScopes | ForEach-Object { $_.id })
    if ($domainIds.Count -ne 50 -or ($domainIds | Sort-Object -Unique).Count -ne 50) {
        throw "Expanded catalog must contain 50 unique domains."
    }
    if ($scopeIds.Count -ne 25 -or ($scopeIds | Sort-Object -Unique).Count -ne 25) {
        throw "Expanded catalog must contain 25 unique collaboration scopes."
    }

    $items = @($Catalog.items)
    $itemIds = @($items | ForEach-Object { $_.id })
    if (($itemIds | Sort-Object -Unique).Count -ne $itemIds.Count) {
        throw "Expanded catalog contains duplicate item IDs."
    }
    $itemsById = @{}
    foreach ($item in $items) {
        Assert-Id $item.id "catalog item id"
        $itemsById[$item.id] = $item
        if ($item.status -ne "active") { throw "Generated item '$($item.id)' must be active." }
        if ($item.statement.Contains('{{') -or $item.domainDelta.Contains('{{')) {
            throw "Generated item '$($item.id)' contains unresolved template syntax."
        }
        if ($item.authority.mutationCeiling -notin @("read-only", "bounded-local", "approval-gated")) {
            throw "Generated item '$($item.id)' has an invalid mutation ceiling."
        }
        foreach ($term in $script:ForbiddenAuthorityTerms) {
            if (@($item.authority.forbiddenMutations) -notcontains $term) {
                throw "Generated item '$($item.id)' does not preserve forbidden mutation '$term'."
            }
        }
        foreach ($domainId in @($item.domainIds)) {
            if ($domainIds -notcontains $domainId) { throw "Item '$($item.id)' references unknown domain '$domainId'." }
        }
        foreach ($scopeId in @($item.scopeIds)) {
            if ($scopeIds -notcontains $scopeId) { throw "Item '$($item.id)' references unknown scope '$scopeId'." }
        }
    }

    $counts = Get-AeosBehaviorCounts $Catalog
    foreach ($kind in @($Catalog.countFloors.Keys)) {
        $actual = [int]$counts[$kind]
        $minimum = [int]$Catalog.countFloors[$kind]
        if ($actual -lt $minimum) {
            throw "Catalog kind '$kind' has $actual active canonical entries; minimum is $minimum."
        }
    }
    if ($counts["behavior"] -gt 1000 -or $counts["failure"] -gt 500) {
        throw "Catalog exceeds the requested behavior or failure maximum."
    }

    foreach ($failure in @($items | Where-Object { $_.kind -eq "failure" })) {
        if (@($failure.detectorSignalIds).Count -eq 0 -or @($failure.recoveryIds).Count -eq 0) {
            throw "Failure '$($failure.id)' lacks detector or recovery relationships."
        }
        foreach ($id in @($failure.detectorSignalIds)) {
            if (-not $itemsById.ContainsKey($id) -or $itemsById[$id].kind -ne "signal") {
                throw "Failure '$($failure.id)' references invalid detector '$id'."
            }
        }
        foreach ($id in @($failure.recoveryIds)) {
            if (-not $itemsById.ContainsKey($id) -or $itemsById[$id].kind -ne "recovery") {
                throw "Failure '$($failure.id)' references invalid recovery '$id'."
            }
        }
    }
    foreach ($recovery in @($items | Where-Object { $_.kind -eq "recovery" })) {
        foreach ($id in @($recovery.failureIds)) {
            if (-not $itemsById.ContainsKey($id) -or $itemsById[$id].kind -ne "failure") {
                throw "Recovery '$($recovery.id)' references invalid failure '$id'."
            }
            if (@($itemsById[$id].recoveryIds) -notcontains $recovery.id) {
                throw "Recovery relationship between '$($recovery.id)' and '$id' is not symmetric."
            }
        }
    }
    foreach ($item in $items) {
        foreach ($conflict in @($item.conflicts)) {
            if (-not $itemsById.ContainsKey($conflict) -or @($itemsById[$conflict].conflicts) -notcontains $item.id) {
                throw "Conflict relationship for '$($item.id)' and '$conflict' is not symmetric."
            }
        }
    }

    $normalized = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::Ordinal)
    foreach ($item in $items) {
        $key = (($item.kind + "|" + $item.statement + "|" + $item.domainDelta).ToLowerInvariant() -replace '\s+', ' ').Trim()
        if (-not $normalized.Add($key)) {
            throw "Catalog contains an exact normalized semantic duplicate at '$($item.id)'."
        }
    }
}

function Get-AeosBehaviorCounts {
    param([Parameter(Mandatory)]$Catalog)

    $active = @($Catalog.items | Where-Object { $_.status -eq "active" })
    return [ordered]@{
        domain = @($Catalog.domains | Where-Object { $_.status -eq "active" }).Count
        behavior = @($active | Where-Object { $_.kind -eq "behavior" }).Count
        failure = @($active | Where-Object { $_.kind -eq "failure" }).Count
        signal = @($active | Where-Object { $_.kind -eq "signal" }).Count
        recovery = @($active | Where-Object { $_.kind -eq "recovery" }).Count
        decision = @($active | Where-Object { $_.kind -eq "decision" }).Count
        'mental-model' = @($active | Where-Object { $_.kind -eq "mental-model" }).Count
        collaboration = @($active | Where-Object { $_.kind -eq "collaboration" }).Count
        governance = @($active | Where-Object { $_.kind -eq "governance" }).Count
        totalItems = $active.Count
    }
}

function ConvertTo-DeterministicJson {
    param([Parameter(Mandatory)]$Value)

    $json = $Value | ConvertTo-Json -Depth 30
    return ($json -replace "`r`n", "`n").TrimEnd() + "`n"
}

function Resolve-AeosBehaviorContext {
    param(
        [Parameter(Mandatory)]$Catalog,
        [Parameter(Mandatory)][AllowEmptyCollection()][string[]]$SignalIds,
        [Parameter(Mandatory)][ValidateSet("understand", "plan", "implement", "debug", "review", "release")][string]$Phase,
        [Parameter(Mandatory)][bool]$Trusted
    )

    $record = [ordered]@{
        schemaVersion = 1
        catalogVersion = $Catalog.catalogVersion
        phase = $Phase
        trusted = $Trusted
        signalIds = @($SignalIds | Sort-Object -Unique)
        candidateDomainIds = @()
        selectedDomainIds = @()
        selectedItemIds = @()
        rejectedReasons = @()
        contextBudget = [ordered]@{
            maximumDomains = [int]$Catalog.routing.maximumDomains
            maximumItems = [int]$Catalog.routing.maximumItems
            usedDomains = 0
            usedItems = 0
        }
        authorityCeiling = "read-only"
        terminalState = "BLOCKED"
        stopReason = $null
    }

    if (-not $Trusted) {
        $record.stopReason = "Signals are not from a trusted typed source."
        return [pscustomobject]$record
    }
    if ($SignalIds.Count -eq 0) {
        $record.stopReason = "No trusted signals were supplied."
        return [pscustomobject]$record
    }

    $signalsById = @{}
    foreach ($signal in @($Catalog.items | Where-Object { $_.kind -eq "signal" })) {
        $signalsById[$signal.id] = $signal
    }
    $candidateDomains = New-Object 'System.Collections.Generic.HashSet[string]' ([System.StringComparer]::Ordinal)
    foreach ($signalId in @($SignalIds | Sort-Object -Unique)) {
        if (-not $signalsById.ContainsKey($signalId)) {
            $record.rejectedReasons += "Unknown or inactive signal '$signalId'."
            $record.stopReason = "One or more signals are unknown."
            return [pscustomobject]$record
        }
        $signal = $signalsById[$signalId]
        if ([double]$signal.minimumConfidence -lt [double]$Catalog.routing.minimumConfidence) {
            $record.rejectedReasons += "Signal '$signalId' is below the routing confidence floor."
            continue
        }
        foreach ($domainId in @($signal.domainIds)) { [void]$candidateDomains.Add($domainId) }
    }

    $candidateIds = @($candidateDomains | Sort-Object)
    $record.candidateDomainIds = $candidateIds
    if ($candidateIds.Count -eq 0) {
        $record.stopReason = "No domain met the confidence and trust requirements."
        return [pscustomobject]$record
    }
    if ($candidateIds.Count -gt [int]$Catalog.routing.maximumDomains) {
        $record.stopReason = "Candidate domains exceed the configured context budget."
        return [pscustomobject]$record
    }

    $phaseArchetypes = switch ($Phase) {
        "understand" {
            @(
                @{ kind = "signal"; archetype = "input" },
                @{ kind = "behavior"; archetype = "establish-state" },
                @{ kind = "failure"; archetype = "premature-action" },
                @{ kind = "decision"; archetype = "build-versus-test" },
                @{ kind = "mental-model"; archetype = "feedback-loop" },
                @{ kind = "governance"; archetype = "evidence-authority-policy" }
            )
        }
        "plan" {
            @(
                @{ kind = "signal"; archetype = "input" },
                @{ kind = "decision"; archetype = "build-versus-test" },
                @{ kind = "mental-model"; archetype = "weakest-link" },
                @{ kind = "behavior"; archetype = "protect-invariant" },
                @{ kind = "failure"; archetype = "boundary-violation" },
                @{ kind = "governance"; archetype = "evidence-authority-policy" }
            )
        }
        "implement" {
            @(
                @{ kind = "signal"; archetype = "input" },
                @{ kind = "behavior"; archetype = "minimize-change" },
                @{ kind = "behavior"; archetype = "validate-immediately" },
                @{ kind = "behavior"; archetype = "protect-invariant" },
                @{ kind = "failure"; archetype = "boundary-violation" },
                @{ kind = "governance"; archetype = "evidence-authority-policy" }
            )
        }
        "debug" {
            @(
                @{ kind = "signal"; archetype = "input" },
                @{ kind = "failure"; archetype = "silent-failure" },
                @{ kind = "recovery"; archetype = "isolate-and-repair" },
                @{ kind = "behavior"; archetype = "choose-falsifier" },
                @{ kind = "behavior"; archetype = "validate-immediately" },
                @{ kind = "governance"; archetype = "evidence-authority-policy" }
            )
        }
        "review" {
            @(
                @{ kind = "signal"; archetype = "input" },
                @{ kind = "failure"; archetype = "evidence-overclaim" },
                @{ kind = "behavior"; archetype = "validate-immediately" },
                @{ kind = "decision"; archetype = "build-versus-test" },
                @{ kind = "mental-model"; archetype = "weakest-link" },
                @{ kind = "governance"; archetype = "evidence-authority-policy" }
            )
        }
        "release" {
            @(
                @{ kind = "signal"; archetype = "input" },
                @{ kind = "governance"; archetype = "evidence-authority-policy" },
                @{ kind = "failure"; archetype = "evidence-overclaim" },
                @{ kind = "recovery"; archetype = "escalate-and-contain" },
                @{ kind = "behavior"; archetype = "validate-immediately" },
                @{ kind = "decision"; archetype = "build-versus-test" }
            )
        }
    }
    $signalsByDomain = @{}
    foreach ($signalId in @($SignalIds | Sort-Object -Unique)) {
        $signal = $signalsById[$signalId]
        foreach ($domainId in @($signal.domainIds)) {
            if (-not $signalsByDomain.ContainsKey($domainId)) { $signalsByDomain[$domainId] = @() }
            $signalsByDomain[$domainId] = @($signalsByDomain[$domainId] + $signalId | Sort-Object -Unique)
        }
    }
    $itemsById = @{}
    foreach ($item in @($Catalog.items)) { $itemsById[$item.id] = $item }
    $selected = New-Object System.Collections.Generic.List[object]
    foreach ($domainId in $candidateIds) {
        foreach ($selection in $phaseArchetypes) {
            if ($selected.Count -ge [int]$Catalog.routing.maximumItems) { break }
            $itemId = if ($selection.archetype -eq "input") {
                @($signalsByDomain[$domainId] | Select-Object -First 1)[0]
            } else {
                "$($selection.kind)-$domainId-$($selection.archetype)"
            }
            if ([string]::IsNullOrWhiteSpace($itemId) -or -not $itemsById.ContainsKey($itemId)) {
                $record.rejectedReasons += "Required $Phase pack item is missing for domain '$domainId': $itemId"
                $record.stopReason = "A required phase-pack item is missing."
                return [pscustomobject]$record
            }
            $item = $itemsById[$itemId]
            if (@($item.activation.phases) -notcontains $Phase) {
                $record.rejectedReasons += "Item '$itemId' is not active in phase '$Phase'."
                $record.stopReason = "A required phase-pack item is not applicable."
                return [pscustomobject]$record
            }
            if (@($selected | Where-Object { $_.id -eq $item.id }).Count -eq 0) { $selected.Add($item) }
        }
    }

    $record.selectedDomainIds = $candidateIds
    $record.selectedItemIds = @($selected | ForEach-Object { $_.id })
    $record.contextBudget.usedDomains = $candidateIds.Count
    $record.contextBudget.usedItems = $selected.Count
    $record.authorityCeiling = if (@($selected | Where-Object { $_.authority.mutationCeiling -eq "bounded-local" }).Count -gt 0) { "bounded-local" } else { "read-only" }
    $record.terminalState = "PASSED"
    $record.stopReason = "Context pack resolved within trust, authority, and size budgets."
    return [pscustomobject]$record
}

function Test-AeosRoutingGoldenCases {
    param([Parameter(Mandatory)]$Catalog)

    foreach ($case in @($Catalog.routing.goldenCases)) {
        $actual = Resolve-AeosBehaviorContext `
            -Catalog $Catalog `
            -SignalIds @($case.signalIds) `
            -Phase $case.phase `
            -Trusted ([bool]$case.trusted)
        if ($actual.terminalState -ne $case.expectedTerminalState) {
            throw "Golden case '$($case.id)' expected state '$($case.expectedTerminalState)', got '$($actual.terminalState)'."
        }
        $expectedDomains = @($case.expectedDomainIds | Sort-Object)
        $actualDomains = @($actual.selectedDomainIds | Sort-Object)
        if (($expectedDomains -join "|") -ne ($actualDomains -join "|")) {
            throw "Golden case '$($case.id)' expected domains '$($expectedDomains -join ',')', got '$($actualDomains -join ',')'."
        }
    }
}

function Get-AeosDomainMarkdown {
    param(
        [Parameter(Mandatory)]$Domain,
        [Parameter(Mandatory)]$Catalog
    )

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# $($Domain.name)")
    $lines.Add("")
    $lines.Add($Domain.purpose)
    $lines.Add("")
    $lines.Add("- **Domain ID:** ``$($Domain.id)``")
    $lines.Add("- **Boundary:** $($Domain.boundaries -join '; ')")
    $lines.Add("- **Invariant:** $($Domain.invariants -join '; ')")
    $lines.Add("- **Default evidence:** $($Domain.defaultEvidence -join '; ')")
    $lines.Add("- **Risk classes:** $($Domain.riskClasses -join ', ')")
    $lines.Add("")

    $domainItems = @($Catalog.items | Where-Object { @($_.domainIds) -contains $Domain.id } | Sort-Object kind, id)
    foreach ($kind in @("behavior", "failure", "signal", "recovery", "decision", "mental-model", "governance")) {
        $kindItems = @($domainItems | Where-Object { $_.kind -eq $kind })
        $lines.Add("## $((Get-Culture).TextInfo.ToTitleCase($kind.Replace('-', ' '))) ($($kindItems.Count))")
        $lines.Add("")
        foreach ($item in $kindItems) {
            $lines.Add("### ``$($item.id)``")
            $lines.Add("")
            $lines.Add("$($item.title). $($item.statement)")
            $lines.Add("")
            $lines.Add("**Domain delta:** $($item.domainDelta)")
            $lines.Add("")
        }
    }
    return ($lines -join "`n") + "`n"
}

function Get-AeosIndexMarkdown {
    param(
        [Parameter(Mandatory)]$Catalog,
        [Parameter(Mandatory)]$Counts,
        [Parameter(Mandatory)][string]$CatalogSha256
    )

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# AEOS Behavior Catalog Index")
    $lines.Add("")
    $lines.Add("Generated deterministically from the reviewed catalog blueprint. Generated rows are derived indexes; canonical counting semantics are governed by the catalog contract.")
    $lines.Add("")
    $lines.Add("- **Catalog version:** ``$($Catalog.catalogVersion)``")
    $lines.Add("- **Catalog SHA-256:** ``$CatalogSha256``")
    $lines.Add("- **Domains:** $($Counts['domain'])")
    $lines.Add("- **Behaviors:** $($Counts['behavior'])")
    $lines.Add("- **Failure patterns:** $($Counts['failure'])")
    $lines.Add("- **Context signals:** $($Counts['signal'])")
    $lines.Add("- **Recovery strategies:** $($Counts['recovery'])")
    $lines.Add("- **Decision models:** $($Counts['decision'])")
    $lines.Add("- **Mental models:** $($Counts['mental-model'])")
    $lines.Add("- **Collaboration patterns:** $($Counts['collaboration'])")
    $lines.Add("- **Governance policies:** $($Counts['governance'])")
    $lines.Add("")
    $lines.Add("## Domains")
    $lines.Add("")
    $lines.Add("| Domain | Purpose | Items |")
    $lines.Add("|---|---|---:|")
    foreach ($domain in @($Catalog.domains | Sort-Object name)) {
        $count = @($Catalog.items | Where-Object { @($_.domainIds) -contains $domain.id }).Count
        $lines.Add("| [$($domain.name)](domains/$($domain.id).md) | $($domain.purpose) | $count |")
    }
    $lines.Add("")
    $lines.Add("## Runtime")
    $lines.Add("")
    $lines.Add("The resolver loads at most two domains and twelve items. It accepts typed trusted signal IDs only and fails closed on ambiguity, stale or untrusted input, missing references, authority conflict, or budget overflow.")
    $lines.Add("")
    return ($lines -join "`n") + "`n"
}

function Get-AeosCollaborationMarkdown {
    param([Parameter(Mandatory)]$Catalog)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# AEOS Collaboration Patterns")
    $lines.Add("")
    foreach ($scope in @($Catalog.collaborationScopes | Sort-Object name)) {
        $lines.Add("## $($scope.name)")
        $lines.Add("")
        $lines.Add($scope.purpose)
        $lines.Add("")
        foreach ($item in @($Catalog.items | Where-Object { $_.kind -eq "collaboration" -and @($_.scopeIds) -contains $scope.id } | Sort-Object id)) {
            $lines.Add("### ``$($item.id)``")
            $lines.Add("")
            $lines.Add($item.statement)
            $lines.Add("")
            $lines.Add("- **Authority split:** $($item.authoritySplit)")
            $lines.Add("- **Handoff:** $($item.handoff)")
            $lines.Add("- **Conflict route:** $($item.conflictRoute)")
            $lines.Add("")
        }
    }
    return ($lines -join "`n") + "`n"
}

function Write-Utf8NoBomLf {
    param(
        [Parameter(Mandatory)][string]$Path,
        [Parameter(Mandatory)][AllowEmptyString()][string]$Text
    )

    $normalized = $Text -replace "`r`n", "`n"
    $directory = Split-Path -Parent $Path
    if (-not (Test-Path -LiteralPath $directory -PathType Container)) {
        New-Item -ItemType Directory -Path $directory -Force | Out-Null
    }
    $encoding = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText($Path, $normalized, $encoding)
}

Export-ModuleMember -Function @(
    "Assert-AeosBehaviorBlueprint",
    "Assert-AeosExpandedCatalog",
    "Assert-NoDuplicateJsonProperties",
    "ConvertTo-DeterministicJson",
    "Expand-AeosBehaviorCatalog",
    "Get-AeosBehaviorCounts",
    "Get-AeosCollaborationMarkdown",
    "Get-AeosDomainMarkdown",
    "Get-AeosIndexMarkdown",
    "Get-AeosTextSha256",
    "Read-AeosBehaviorBlueprint",
    "Resolve-AeosBehaviorContext",
    "Test-AeosRoutingGoldenCases",
    "Write-Utf8NoBomLf"
)
