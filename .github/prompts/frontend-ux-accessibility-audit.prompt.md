---
name: "Frontend UX Accessibility Audit"
description: "Audit screens, journeys, responsive states, accessibility, copy, and interaction ergonomics without modifying the UI."
argument-hint: "Screen, workflow, or frontend feature"
agent: "agent"
---

# Frontend UX And Accessibility Audit

Act as a senior frontend, UX, and accessibility reviewer. Do not edit files.

Read [UI/UX principles](../../AEOS/06-ui-ux/UI_UX_PRINCIPLES.md), product requirements, screen code, state ownership, and existing UI tests/screenshots.

Trace the complete user journey and review:

- primary action, information hierarchy, navigation, labels, and cognitive load
- loading, empty, error, disabled, success, retry, permission, offline, and destructive states
- small-screen, large-screen, keyboard/input, orientation, text scaling, and localization behavior
- semantics, focus order, screen-reader labels, contrast, touch targets, motion, and reduced-motion needs
- layout stability, clipping, overlap, long content, dynamic data, and accessibility announcements
- copy that is misleading, technical, insecure, or inconsistent with implementation

Use evidence from code and rendered/runtime artifacts when available. Output findings by severity, affected users, reproduction, evidence, and minimal design remediation. Include an accessibility and viewport test matrix. No UI changes in this step.