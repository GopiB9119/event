# Community Ledger Agent Entry Point

This file is the entry point for AI agents working on Community Ledger.

Before changing code, read:

0. [AEOS/AGENTS.md](AEOS/AGENTS.md)
1. [prompts.md](prompts.md)
2. [.ai/AI_CONSTITUTION.md](.ai/AI_CONSTITUTION.md)
3. [.ai/RECEIPT_OCR_PLAYBOOK.md](.ai/RECEIPT_OCR_PLAYBOOK.md)
4. [.ai/QUALITY_GATES.md](.ai/QUALITY_GATES.md)
5. [.ai/SECURITY_PLAYBOOK.md](.ai/SECURITY_PLAYBOOK.md)

AEOS is the universal engineering and decision system. The `.ai/` documents are Community Ledger project overlays: receipt OCR rules, ledger-specific validation, and security constraints. When both apply, satisfy AEOS first and then the stricter project-specific rule; the files are complementary, not parallel constitutions.

## Prime Directive

Protect ledger integrity.

Every receipt, member, transaction, JSON file, and calculation must be traceable to real user input or real ML Kit OCR output. Do not invent financial data. Do not hide uncertainty. Do not save weak evidence as fact.

## Required Workflow

1. Identify the exact behavior being changed.
2. Read the owning code path.
3. State the failure mode or desired invariant.
4. Make the smallest safe edit.
5. Validate with both `compileDirectDebugKotlin` and `compilePlayDebugKotlin` at minimum.
6. Report remaining risk honestly.

For non-trivial missions, start with [AEOS/11-prompts/PROMPT_LIBRARY.md](AEOS/11-prompts/PROMPT_LIBRARY.md). Discovery and specialist audits remain read-only; implementation begins only after Definition of Ready.

## Forbidden Runtime Patterns

- Dummy receipt data.
- Random receipt values.
- Filename-based receipt extraction.
- Gemini/cloud OCR fallback.
- Silent ledger mutations from uncertain OCR.
- Claiming invite links are cryptographically secure.

## Validation Command

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDirectDebugKotlin :app:compilePlayDebugKotlin
```
