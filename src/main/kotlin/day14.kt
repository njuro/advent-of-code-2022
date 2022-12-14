import utils.Coordinate
import utils.maxY
import utils.readInputLines
import java.lang.Integer.max
import java.lang.Integer.min

/** [https://adventofcode.com/2021/day/14] */
class Sand : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val map =
            readInputLines("14.txt").map { it.split(" -> ") }.let { if (it.size == 1) it + listOf(it.single()) else it }
                .flatMap { line ->
                    line.map { it.split(",").let { (x, y) -> Coordinate(x.toInt(), y.toInt()) } }
                        .zipWithNext()
                        .flatMap { (start, end) ->
                            (min(start.x, end.x)..max(start.x, end.x)).flatMap { dx ->
                                (min(start.y, end.y)..max(start.y, end.y)).map { dy ->
                                    Coordinate(dx, dy) to '#'
                                }
                            }
                        }
                }.toMap().toMutableMap().withDefault { '.' }

        val start = Coordinate(500, 0)
        val floor = if (part2) map.maxY() + 2 else map.maxY()
        fun Coordinate.candidates() = listOf(copy(y = y + 1), copy(x = x - 1, y = y + 1), copy(x = x + 1, y = y + 1))
        fun Coordinate.isAvailable(): Boolean = if (part2 && y == floor) false else map.getValue(this) == '.'
        fun generateSand(): Coordinate? =
            generateSequence(start) { current ->
                current.candidates().firstOrNull { it.y <= floor && it.isAvailable() }
            }.last().takeIf { it.y < floor }

        do {
            val sand = generateSand() ?: break
            map[sand] = 'o'
        } while (sand != start)

        return map.values.count { it == 'o' }
    }
}

fun main() {
    print(Sand().run(part2 = true))
}
