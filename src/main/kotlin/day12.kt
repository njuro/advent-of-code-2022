import utils.Coordinate
import utils.readInputLines
import java.util.PriorityQueue

/** [https://adventofcode.com/2021/day/12] */
class Paths : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        lateinit var start: Coordinate
        lateinit var target: Coordinate
        val map = readInputLines("12.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                val position = Coordinate(x, y)
                val elevation = when (c) {
                    'S' -> 'a'.also { start = position }
                    'E' -> 'z'.also { target = position }
                    else -> c
                }
                Coordinate(x, y) to elevation
            }
        }.toMap()

        fun findPath(initial: Coordinate): Int {
            val queue =
                PriorityQueue<Pair<Coordinate, Int>> { p1, p2 -> p1.second - p2.second }.apply { offer(initial to 0) }
            val seen = mutableSetOf(initial)
            while (queue.isNotEmpty()) {
                val (current, steps) = queue.poll()
                if (current == target) {
                    return steps
                }
                current.adjacent().filterValues { it !in seen && it in map && map[it]!! - map[current]!! <= 1 }
                    .forEach {
                        seen.add(it.value)
                        queue.offer(it.value to steps + 1)
                    }
            }

            return Int.MAX_VALUE
        }

        return (if (part2) map.filterValues { it == 'a' }.keys else setOf(start)).minOf(::findPath)
    }
}

fun main() {
    print(Paths().run(part2 = true))
}
