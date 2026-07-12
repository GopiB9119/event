# Performance Guidelines

## General Principles

- Measure before optimizing.
- Avoid blocking UI thread.
- Bound expensive work.
- Cache only with invalidation strategy.
- Prefer streaming or incremental processing for large data.

## Mobile-Specific Concerns

- Battery
- Memory pressure
- media and binary size
- background execution limits
- persistence query cost
- rendering and layout cost
- Startup latency

## Processing Pipelines

Each additional processing pass consumes time, memory, battery, or network capacity. Keep passes intentional, bound input and output size, and profile the real target environment before claiming an optimization.
