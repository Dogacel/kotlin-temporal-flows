package dogacel.flow.temporal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.takeWhile
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

fun <T> Flow<T>.takeWithin(duration: Duration): Flow<T> {
    val start = Instant.now()

    return this.takeWhile {
        val now = Instant.now()
        val diff = java.time.Duration.between(start, now).toKotlinDuration()

        diff <= duration
    }
}