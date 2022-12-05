import utils.readInputBlock

/** [https://adventofcode.com/2021/day/5] */
class Crates : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (initial, instructions) = readInputBlock("5.txt").split("\n\n").map { it.lines().dropLast(1) }
        val crates = mutableMapOf<Int, ArrayDeque<Char>>().withDefault { ArrayDeque() }
        initial.forEach { row ->
            row.forEachIndexed { index, c ->
                if (c == '[') {
                    val column = index / 4 + 1
                    crates[column] = crates.getValue(column).apply { addLast(row[index + 1]) }
                }
            }
        }

        val pattern = Regex("move (\\d+) from (\\d+) to (\\d+)")
        instructions.forEach { instruction ->
            val (amount, from, to) = pattern.matchEntire(instruction)!!.destructured.toList().map(String::toInt)
            val buffer = (1..amount).map { crates.getValue(from).removeFirst() }
            crates.getValue(to).addAll(0, if (part2) buffer else buffer.reversed())
        }

        return crates.toSortedMap().map { it.value.first() }.joinToString("")
    }
}

fun main() {
    print(Crates().run(part2 = true))
}
