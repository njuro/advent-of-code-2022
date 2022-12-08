import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/8] */
class Trees : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val trees = readInputLines("8.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, height -> Coordinate(x, y) to Character.getNumericValue(height) }
        }.toMap()

        data class TreePosition(val viewDistance: Int, val blocked: Boolean) {
            fun combine(other: TreePosition) =
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
            }.first { (current, position) -> position.blocked || nextCoordinate(current) !in trees }.second

        return trees.map { (coordinate, height) ->
            listOf(
                calculatePosition(coordinate, Coordinate::left, height),
                calculatePosition(coordinate, Coordinate::right, height),
                calculatePosition(coordinate, Coordinate::up, height),
                calculatePosition(coordinate, Coordinate::down, height)
            ).reduce(TreePosition::combine)
        }.run { if (part2) maxOf { it.viewDistance } else count { !it.blocked } }
    }
}

fun main() {
    print(Trees().run(part2 = true))
}
