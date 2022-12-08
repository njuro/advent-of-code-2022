import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/8] */
class Trees : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val trees = readInputLines("8.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, height -> Coordinate(x, y) to Character.getNumericValue(height) }
        }.toMap()

        data class TreePosition(val viewDistance: Int, val hidden: Boolean) {
            infix fun combineWith(other: TreePosition) =
                TreePosition(viewDistance * other.viewDistance, hidden && other.hidden)
        }

        fun calculatePosition(coordinate: Coordinate, height: Int, moveFn: (Coordinate) -> Coordinate): TreePosition {
            var viewDistance = 0
            var hidden = false
            var current = coordinate
            while (true) {
                val next = moveFn(current)
                if (next !in trees) {
                    break
                }
                if (trees.getValue(next) >= height) {
                    viewDistance += 1
                    hidden = true
                    break
                }
                viewDistance += 1
                current = next
            }

            return TreePosition(viewDistance, hidden)
        }

        return trees.map { (coordinate, height) ->
            val left = calculatePosition(coordinate, height, Coordinate::left)
            val right = calculatePosition(coordinate, height, Coordinate::right)
            val down = calculatePosition(coordinate, height, Coordinate::up)
            val up = calculatePosition(coordinate, height, Coordinate::down)

            listOf(left, right, down, up).reduce { pos1, pos2 -> pos1 combineWith pos2 }
        }.run { if (part2) maxOf { it.viewDistance } else count { !it.hidden } }
    }
}

fun main() {
    print(Trees().run(part2 = true))
}
