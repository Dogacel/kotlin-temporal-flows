package dogacel.flow.temporal

import java.time.Instant
import kotlin.time.Duration
import kotlin.time.toKotlinDuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.takeWhile

/**
 * Takes items from the flow until the given duration has passed.
 * The flow won't terminate if the duration is exceeded, it will just stop emitting items.
 *
 * @param T the type of the flow.
 * @param duration the duration to take items for.
 * @return a flow that emits until the given duration has passed.
 */
fun <T> Flow<T>.takeWithin(duration: Duration): Flow<T> {
    val start = Instant.now()

    return this.takeWhile {
        val now = Instant.now()
        val diff = java.time.Duration.between(start, now).toKotlinDuration()

        diff <= duration
    }
}
