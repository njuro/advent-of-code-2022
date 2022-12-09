import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/9] */
class Day9 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        var head = Coordinate(0, 0)
        var tail = Coordinate(0, 0)
        val visited = mutableSetOf(tail)

        readInputLines("9.txt").map { row -> row.split(" ").let { it[0] to it[1].toInt() } }
            .forEach { (direction, steps) ->
                repeat(steps) {
                    head = when (direction) {
                        "R" -> head.right()
                        "L" -> head.left()
                        "D" -> head.down()
                        "U" -> head.up()
                        else -> error("Illegal direction $direction")
                    }
                    val touching = head.adjacent8().toSet() + head
                    if (tail !in touching) {
                        val candidates =
                            if (tail.x == head.x || tail.y == head.y) tail.adjacent().values else tail.adjacentDiagonally()
                        tail = candidates.first { it in touching }
                        visited.add(tail)
                    }
                }
            }

        return visited.size
    }
}

fun main() {
    print(Day9().run(part2 = false))
}
