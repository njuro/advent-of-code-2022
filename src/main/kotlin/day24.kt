import utils.Coordinate
import utils.Direction
import utils.readInputLines
import java.util.PriorityQueue

/** [https://adventofcode.com/2021/day/24] */
class Day24 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        data class Blizzard(val direction: Direction, val position: Coordinate)

        val blizzards = mutableListOf<Blizzard>()
        val map = readInputLines("24.txt")
        val maxY = map.size - 1
        val maxX = map.first().trimEnd().length - 1
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c == '.' || c == '#') {
                    return@forEachIndexed
                }
                val direction = when (c) {
                    '^' -> Direction.UP
                    '>' -> Direction.RIGHT
                    '<' -> Direction.LEFT
                    'v' -> Direction.DOWN
                    else -> error("Unknown direction $c")
                }
                blizzards.add(Blizzard(direction, Coordinate(x, y)))
            }
        }

        fun Coordinate.adjust(): Coordinate = when {
            x == 0 -> Coordinate(maxX - 1, y)
            x == maxX -> Coordinate(1, y)
            y == 0 -> Coordinate(x, maxY - 1)
            y == maxY -> Coordinate(x, 1)
            else -> this
        }

        fun Blizzard.move(): Blizzard = copy(position = position.move(direction, offset = true).adjust())
        fun List<Blizzard>.move(): List<Blizzard> = map { it.move() }
        operator fun List<Blizzard>.contains(position: Coordinate): Boolean = any { it.position == position }

        val start = Coordinate(1, 0)
        val target = Coordinate(maxX - 1, maxY - 1)
        val queue =
            PriorityQueue<Pair<Coordinate, Int>> { p1, p2 -> p1.second - p2.second }.apply { offer(start to 0) }
        val cache = mutableListOf(blizzards.move())
        val seen = mutableSetOf(start to 0)
        while (queue.isNotEmpty()) {
            val (current, steps) = queue.poll()
            if (current == target) {
                return steps + 1
            }
            if (steps == cache.size) {
                cache.add(cache.last().move())
            }
            val nextBlizzards = cache[steps]
            current.adjacent().filterValues { it !in nextBlizzards && (it.x in 1 until maxX) && (it.y in 1 until maxY) }
                .map { it.value to steps + 1 }
                .filter { it !in seen }
                .forEach {
                    seen.add(it)
                    queue.offer(it)
                }

            if (current !in nextBlizzards) {
                val next = current to steps + 1
                if (next !in seen) {
                    seen.add(next)
                    queue.offer(next)
                }
            }
        }

        error("Impossible")
    }
}

fun main() {
    print(Day24().run(part2 = false))
}
