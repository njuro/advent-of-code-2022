import utils.Coordinate
import utils.Direction.DOWN
import utils.Direction.LEFT
import utils.Direction.RIGHT
import utils.Direction.UP
import utils.readInputBlock

/** [https://adventofcode.com/2021/day/22] */
class Day22 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (rawMap, rawInstructions) = readInputBlock("22.txt").split("\n\n")
        val map = rawMap.lines().flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                if (c == ' ') null else Coordinate(x, y) to c
            }
        }.toMap()
        val instructions = Regex("(\\d+)|([RL])").findAll(rawInstructions).map { it.value }.toList()

        var position = Coordinate(map.filterKeys { it.y == 0 }.minOf { it.key.x }, 0)
        var direction = RIGHT
        instructions.forEach { instruction ->
            when (instruction) {
                "L" -> direction = direction.turnLeft()
                "R" -> direction = direction.turnRight()
                else -> {
                    for (step in 1..instruction.toInt()) {
                        var next = position.move(direction, offset = true)
                        if (next !in map) {
                            next = when (direction) {
                                RIGHT -> Coordinate(map.filterKeys { it.y == next.y }.minOf { it.key.x }, next.y)
                                DOWN -> Coordinate(next.x, map.filterKeys { it.x == next.x }.minOf { it.key.y })
                                LEFT -> Coordinate(map.filterKeys { it.y == next.y }.maxOf { it.key.x }, next.y)
                                UP -> Coordinate(next.x, map.filterKeys { it.x == next.x }.maxOf { it.key.y })
                            }
                        }
                        if (map.getValue(next) == '#') break
                        position = next
                    }
                }
            }
        }

        return (position.y + 1) * 1000 + (position.x + 1) * 4 + when (direction) {
            RIGHT -> 0
            DOWN -> 1
            LEFT -> 2
            UP -> 3
        }
    }
}

fun main() {
    print(Day22().run(part2 = false))
}
