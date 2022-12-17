import utils.Coordinate
import utils.maxY
import utils.readInputBlock

/** [https://adventofcode.com/2021/day/17] */
class Day17 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val shapes = listOf(
            setOf(Coordinate(3, 0), Coordinate(4, 0), Coordinate(5, 0), Coordinate(6, 0)),
            setOf(Coordinate(3, 1), Coordinate(4, 1), Coordinate(4, 0), Coordinate(4, 2), Coordinate(5, 1)),
            setOf(Coordinate(3, 0), Coordinate(4, 0), Coordinate(5, 0), Coordinate(5, 1), Coordinate(5, 2)),
            setOf(Coordinate(3, 0), Coordinate(3, 1), Coordinate(3, 2), Coordinate(3, 3)),
            setOf(Coordinate(3, 0), Coordinate(3, 1), Coordinate(4, 0), Coordinate(4, 1))
        )
        val moveList = readInputBlock("17.txt").trim().toList()
        var moves = moveList.iterator()
        val map = mutableMapOf<Coordinate, Char>().withDefault { '.' }
        fun Coordinate.isValid(): Boolean = x in 1..7 && y > 0 && map.getValue(this) == '.'

        fun placeRock(turn: Int) {
            val top = map.filterValues { it == '#' }.maxOfOrNull { it.key.y } ?: 0
            var current = shapes[turn % shapes.size].map { Coordinate(it.x, it.y + top + 4) }
            while (true) {
                if (!moves.hasNext()) {
                    moves = moveList.iterator()
                }
                val horizontalChange =
                    if (moves.next() == '>') current.map { it.right() } else current.map { it.left() }
                if (horizontalChange.all { it.isValid() }) {
                    current = horizontalChange
                }
                val verticalChange = current.map { it.down() }
                if (verticalChange.any { !it.isValid() }) {
                    break
                }
                current = verticalChange
            }
            current.forEach { map[it] = '#' }
        }



        return if (part2) {
            -1
        } else {
            repeat(2022) { placeRock(it) }
            map.maxY()
        }
    }
}

fun main() {
    print(Day17().run(part2 = false))
}
