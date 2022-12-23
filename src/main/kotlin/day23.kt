import utils.Coordinate
import utils.readInputLines
import utils.toStringRepresentation

/** [https://adventofcode.com/2021/day/23] */
class Elves : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val map = readInputLines("23.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, c -> Coordinate(x, y) to c }
        }.toMap().toMutableMap().withDefault { '.' }

        val moves: MutableList<List<Coordinate.() -> Coordinate>> = mutableListOf(
            listOf({ copy(y = y - 1) }, { copy(x = x + 1, y = y - 1) }, { copy(x = x - 1, y = y - 1) }), // north
            listOf({ copy(y = y + 1) }, { copy(x = x + 1, y = y + 1) }, { copy(x = x - 1, y = y + 1) }), // south
            listOf({ copy(x = x - 1) }, { copy(x = x - 1, y = y - 1) }, { copy(x = x - 1, y = y + 1) }), // west
            listOf({ copy(x = x + 1) }, { copy(x = x + 1, y = y + 1) }, { copy(x = x + 1, y = y - 1) }), // east
        )

        fun moveElves(): Boolean {
            val candidates = map.filterValues { it == '#' }.keys.mapNotNull { elf ->
                if (elf.adjacent8().all { map.getValue(it) == '.' }) {
                    return@mapNotNull null
                }
                val next = moves.firstOrNull { transformations ->
                    transformations.map { it.invoke(elf) }.all { map.getValue(it) == '.' }
                }?.first()?.invoke(elf) ?: return@mapNotNull null
                elf to next
            }

            val valid = candidates.filter { (_, next) -> candidates.count { (_, position) -> position == next } == 1 }
            if (valid.isEmpty()) {
                return false
            }

            valid.forEach { (prev, next) ->
                map[prev] = '.'
                map[next] = '#'
            }

            moves.add(moves.removeFirst())
            return true
        }

        return if (part2) {
            generateSequence(0 to true) { (round, _) -> round + 1 to moveElves() }.dropWhile { (_, moved) -> moved }
                .first().first
        } else {
            repeat(10) { moveElves() }
            map.toStringRepresentation(offsetCoordinates = true).count { it == '.' }
        }

    }
}

fun main() {
    print(Elves().run(part2 = true))
}
