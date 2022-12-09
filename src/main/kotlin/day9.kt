import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/9] */
class Ropes : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val rope = Array(if (part2) 10 else 2) { Coordinate(0, 0) }.toMutableList()
        val visited = mutableSetOf(Coordinate(0, 0))

        readInputLines("9.txt").map { row -> row.split(" ").let { it[0] to it[1].toInt() } }
            .forEach { (direction, steps) ->
                repeat(steps) {
                    rope[0] = when (direction) {
                        "R" -> rope[0].right()
                        "L" -> rope[0].left()
                        "D" -> rope[0].down()
                        "U" -> rope[0].up()
                        else -> error("Illegal direction $direction")
                    }

                    rope.indices.zipWithNext { prev, next ->
                        val touching = rope[prev].adjacent8().toSet() + rope[prev]
                        if (rope[next] !in touching) {
                            val candidates =
                                if (rope[next].x == rope[prev].x || rope[next].y == rope[prev].y) rope[next].adjacent().values else rope[next].adjacentDiagonally()
                            rope[next] = candidates.first { it in touching }
                        }
                    }
                    visited.add(rope.last())
                }
            }

        return visited.size
    }
}

fun main() {
    print(Ropes().run(part2 = true))
}
