# Kotlin Temporal Flows

This library contains a set of tools to operate Kotlin flows using time based operators inspired
by [Akka Streams - Timer
Driven Operators](https://doc.akka.io/docs/akka/current/stream/operators/index.html#timer-driven-operators).

- For installation, take a look at [Installation Guide](guide/installation.md).
- For examples, take a look at [Examples](guide/examples.md).
- For contribution, take a look at [Contribution Guide](guide/contribution.md).

To see the full documentation, check our [API Reference](api/index.md).
## Use Cases

### Taking elements from a `flow` for a given time

```kotlin
val dataFlow: Flow<Int> = getFlow(...) // Assume this is some flow that fetches data from other sources async

val collectedData = dataFlow
    .takeWithin(10.seconds) // Get data from the flow for at most 10 seconds
    .toList()
```

Note that `takeWithin` will not throw any exceptions or terminate early if the deadlien is met. This method will just
stop flow from emitting after 10 seconds. For stronger guarantees you can wrap your code using `withTimeout`.
