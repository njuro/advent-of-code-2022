import utils.Coordinate
import utils.Direction
import utils.readInputLines
import java.util.PriorityQueue

/** [https://adventofcode.com/2021/day/24] */
class Blizzards : AdventOfCodeTask {
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
        val target = Coordinate(maxX - 1, maxY)

        data class State(val position: Coordinate, val steps: Int, val phase: Int)

        val initial = State(start, 0, 0)
        val queue = PriorityQueue<State> { p1, p2 -> p1.steps - p2.steps }.apply { offer(initial) }
        val cache = mutableListOf(blizzards.move())
        val seen = mutableSetOf(initial)
        while (queue.isNotEmpty()) {
            val (current, steps, phase) = queue.poll()
            if ((current.y == 0 || current.y == maxY) && current != target && current != start) {
                continue
            }
            var nextPhase = phase
            if (current == target) {
                if (phase == 2 || !part2) {
                    return steps
                } else if (phase == 0) {
                    nextPhase = 1
                }
            }
            if (current == start && phase == 1) {
                nextPhase = 2
            }

            if (steps == cache.size) {
                cache.add(cache.last().move())
            }
            val nextBlizzards = cache[steps]
            (current.adjacent().values + current).filter { it !in nextBlizzards && (it.x in 1 until maxX) && (it.y in 0..maxY) }
                .map { State(it, steps + 1, nextPhase) }
                .filter { it !in seen }
                .forEach {
                    seen.add(it)
                    queue.offer(it)
                }
        }

        error("Impossible")
    }
}

fun main() {
    print(Blizzards().run(part2 = false))
}
