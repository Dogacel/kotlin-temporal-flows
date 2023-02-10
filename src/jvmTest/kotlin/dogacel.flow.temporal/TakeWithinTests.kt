package dogacel.flow.temporal

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Timeout

class TakeWithinTests {
    @Test
    @Timeout(20)
    fun testTakeWithinSuccess() {
        val testFlow: Flow<Int> =
            flow {
                    while (true) {
                        emit(1)
                        delay(3000)
                    }
                }
                .takeWithin(10.seconds)

        val result = runBlocking { testFlow.toList() }

        assertContentEquals(listOf(1, 1, 1, 1), result)
    }
}
