import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/8] */
class Trees : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val trees = readInputLines("8.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, height -> Coordinate(x, y) to Character.getNumericValue(height) }
        }.toMap().withDefault { -1 }

        data class TreePosition(val viewDistance: Int, val blocked: Boolean) {
            infix fun combineWith(other: TreePosition) =
                TreePosition(viewDistance * other.viewDistance, blocked && other.blocked)
        }

        fun calculatePosition(
            initialCoordinate: Coordinate,
            nextCoordinate: (Coordinate) -> Coordinate,
            height: Int
        ): TreePosition =
            generateSequence(initialCoordinate to TreePosition(0, false)) { (current, position) ->
                val next = nextCoordinate(current)
                next to TreePosition(position.viewDistance + 1, trees.getValue(next) >= height)
            }.takeWhile { (current, _) -> current in trees }
                .run { firstOrNull { (_, position) -> position.blocked } ?: last() }.second

        return trees.map { (coordinate, height) ->
            val left = calculatePosition(coordinate, Coordinate::left, height)
            val right = calculatePosition(coordinate, Coordinate::right, height)
            val down = calculatePosition(coordinate, Coordinate::up, height)
            val up = calculatePosition(coordinate, Coordinate::down, height)

            listOf(left, right, down, up).reduce { pos1, pos2 -> pos1 combineWith pos2 }
        }.run { if (part2) maxOf { it.viewDistance } else count { !it.blocked } }
    }
}

fun main() {
    print(Trees().run(part2 = true))
}
