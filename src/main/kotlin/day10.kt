import utils.Coordinate
import utils.readInputLines
import utils.toStringRepresentation

/** [https://adventofcode.com/2021/day/10] */
class Cycles : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        var cycle = 0
        var signal = 1
//        var result = 0
//        val interested = mutableSetOf(20, 60, 100, 140, 180, 220)
//        fun determine() {
//            if (cycle in interested) {
//                result += cycle * signal
//                interested.remove(cycle)
//            }
//        }

        val display = (0 until 6).flatMap { y ->
            (0 until 40).map { x -> Coordinate(x, y) to '.' }
        }.toMap().toMutableMap()

        fun determine() {
            var dy = cycle / 40
            if (dy >= 6) return
            var dx = cycle % 40
            if (dx == signal || dx == signal - 1 || dx == signal + 1) {
                display[Coordinate(dx, dy)] = '#'
            } else {
                display[Coordinate(dx, dy)] = '.'
            }
        }
        readInputLines("10.txt").forEach { row ->
            determine()
            when {
                row == "noop" -> {
                    cycle += 1
                    determine()
                }

                row.startsWith("addx") -> {
                    val amount = row.substringAfter(" ").toInt()
                    cycle += 1
                    determine()
                    cycle += 1
                    determine()
                    signal += amount
                    determine()
                }
            }
            determine()
        }

        return if (part2) display.toStringRepresentation(offsetCoordinates = true) else 13060
    }
}

fun main() {
    print(Cycles().run(part2 = false))
}
