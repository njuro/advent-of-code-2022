import utils.Coordinate
import utils.maxX
import utils.maxY
import utils.minX
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
        if (part2) {
            // TODO nasty hack
            (map.minX() - 1000..map.maxX() + 1000).forEach { map[Coordinate(it, floor)] = '#' }
        }
        fun Coordinate.candidates() = listOf(copy(y = y + 1), copy(x = x - 1, y = y + 1), copy(x = x + 1, y = y + 1))
        fun generateSand(): Coordinate? =
            generateSequence(start) { current ->
                current.candidates().firstOrNull { it.y <= floor && map.getValue(it) == '.' }
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
