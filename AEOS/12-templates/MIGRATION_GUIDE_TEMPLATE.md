# Migration Guide Template

## Summary

What is changing?

## Source Version

## Target Version

## Data Affected

- Tables:
- Files:
- Preferences:
- External systems:

## Migration Steps

1. ...

## Preservation Rules

What data must survive?

## Cleanup Rules

What data can be removed and why?

## Validation

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDebugKotlin
```

## Rollback Limits

What cannot be safely undone?

## User Impact

What might the user notice?
