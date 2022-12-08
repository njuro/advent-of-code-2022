import utils.Coordinate
import utils.maxX
import utils.maxY
import utils.readInputLines

/** [https://adventofcode.com/2021/day/8] */
class Trees : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val trees = readInputLines("8.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, height -> Coordinate(x, y) to Character.getNumericValue(height) }
        }.toMap()
        val maxX = trees.maxX()
        val maxY = trees.maxY()

        return trees.map { (pos, height) ->
            val left =
                (1..pos.x).firstOrNull { trees.getValue(Coordinate(pos.x - it, pos.y)) >= height }?.let { it to true }
                    ?: (pos.x to false)
            val right = (1..(maxX - pos.x)).firstOrNull { trees.getValue(Coordinate(pos.x + it, pos.y)) >= height }
                ?.let { it to true } ?: (maxX - pos.x to false)
            val down = (1..(maxY - pos.y)).firstOrNull { trees.getValue(Coordinate(pos.x, pos.y + it)) >= height }
                ?.let { it to true } ?: (maxY - pos.y to false)
            val up =
                (1..pos.y).firstOrNull { trees.getValue(Coordinate(pos.x, pos.y - it)) >= height }?.let { it to true }
                    ?: (pos.y to false)
            setOf(left, right, down, up).reduce { d1, d2 ->
                d1.first * d2.first to (d1.second && d2.second)
            }
        }.let { if (part2) it.maxOf { it.first } else it.count { !it.second } }
    }
}

fun main() {
    print(Trees().run(part2 = false))
}
