import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AdventOfCodeTasksTest {

    @Test
    fun day01() {
        runTaskTest(Snacks(), 70613, 205805)
    }

    @Test
    fun day02() {
        runTaskTest(Weapons(), 11906, 11186)
    }

    @Test
    fun day03() {
        runTaskTest(Bags(), 7990, 2602)
    }

    @Test
    fun day04() {
        runTaskTest(Camps(), 305, 811)
    }

    @Test
    fun day05() {
        runTaskTest(Crates(), "TQRFCBSJJ", "RMHFJNVFP")
    }

    @Test
    fun day06() {
        runTaskTest(Packets(), 1282, 3513)
    }

    @Test
    fun day07() {
        runTaskTest(Files(), 1644735, 1300850)
    }

    @Test
    fun day08() {
        runTaskTest(Trees(), 1825, 235200)
    }

    private fun runTaskTest(task: AdventOfCodeTask, part1Result: Any, part2Result: Any) {
        assertEquals(part1Result, task.run())
        assertEquals(part2Result, task.run(part2 = true))
    }
}
