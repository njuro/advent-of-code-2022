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

    @Test
    fun day09() {
        runTaskTest(Ropes(), 6311, 2482)
    }

    @Test
    fun day10() {
        runTaskTest(
            Cycles(), 13060,
            """
            # # # # . . . # # . # . . # . # # # . . # . . # . # . . . . # # # . . # # # # . 
            # . . . . . . . # . # . . # . # . . # . # . . # . # . . . . # . . # . . . . # . 
            # # # . . . . . # . # . . # . # # # . . # . . # . # . . . . # . . # . . . # . . 
            # . . . . . . . # . # . . # . # . . # . # . . # . # . . . . # # # . . . # . . . 
            # . . . . # . . # . # . . # . # . . # . # . . # . # . . . . # . # . . # . . . . 
            # . . . . . # # . . . # # . . # # # . . . # # . . # # # # . # . . # . # # # # .
        """.trimIndent(),
        )
    }

    @Test
    fun day11() {
        runTaskTest(Monkeys(), 69918L, 19573408701L)
    }

    @Test
    fun day12() {
        runTaskTest(Paths(), 472, 465)
    }

    @Test
    fun day13() {
        runTaskTest(Signals(), 5330, 27648)
    }

    @Test
    fun day14() {
        runTaskTest(Sand(), 768, 26686)
    }

    @Test
    fun day15() {
        runTaskTest(Sensors(), 4879972, 12525726647448)
    }

    @Test
    fun day18() {
        runTaskTest(Cubes(), 3374, 2010)
    }

    @Test
    fun day19() {
        runTaskTest(Ores(), 1427, 4400)
    }

    @Test
    fun day21() {
        runTaskTest(Equations(), 169525884255464, 3247317268284)
    }

    private fun runTaskTest(task: AdventOfCodeTask, part1Result: Any, part2Result: Any) {
        assertEquals(part1Result, task.run())
        assertEquals(part2Result, task.run(part2 = true))
    }
}
