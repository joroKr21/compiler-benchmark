# Intersection Benchmarks

Cold benchmarks, one iteration, one fork, 10 min timeout.

* `Generic`, 9 fields, identical:
  ```md
  - shapeless_2.12 % 2.3.2:   93388.034 ms
  - Yinduction-heuristics:    87578.091 ms
  - faster-intersection:       6267.404 ms
  ```

* `LabelledGeneric`, 9 fields, identical:
  ```md
  - shapeless_2.12 % 2.3.2:     timed out
  - faster-intersection:      8319.167 ms
  ```

* `Generic`, 9 fields, reversed:
  ```md
  - shapeless_2.12 % 2.3.2: 107118.778 ms
  - Yinduction-heuristics:   84841.394 ms
  - faster-intersection:      7186.696 ms
  ```

* `LabelledGeneric`, 9 fields, reversed:
  ```md
  - faster-intersection:      7935.465 ms
  ```
