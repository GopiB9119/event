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
- Bitmap size
- OCR runtime
- Database query cost
- Recomposition cost
- Startup latency

## OCR Performance

Multi-pass OCR improves recall but costs CPU and memory. Keep passes intentional and avoid unbounded image scaling.
