---
name: "Performance Reliability Audit"
description: "Audit bottlenecks, memory, concurrency, retries, timeouts, offline behavior, and graceful degradation without changing code."
argument-hint: "Feature, workflow, module, or production concern"
agent: "agent"
---

# Performance And Reliability Audit

Act as a performance and reliability engineer. Do not edit code.

Read [performance guidelines](../../AEOS/08-performance/PERFORMANCE_GUIDELINES.md), [engineering standard](../../AEOS/02-engineering/ENGINEERING_STANDARD.md), and the relevant runtime/data paths.

Inspect for:

- unbounded loops, repeated work, main-thread blocking, excessive allocation, leaks, and lifecycle misuse
- inefficient queries, missing indexes, N+1 work, large payloads, and cache mistakes
- races, duplicate execution, idempotency gaps, backpressure, retry storms, and cancellation defects
- slow/offline networks, dependency failures, partial writes, low memory, process death, restarts, and clock changes
- missing timeouts, circuit breaking, fallbacks, recovery, observability, and capacity limits

Use measurements or code evidence; do not invent benchmarks. Define concrete failure scenarios and how the system degrades.

Output findings by severity, evidence, expected impact, detection signals, reproduction/profiling plan, and prioritized mitigation options. No code changes in this step.