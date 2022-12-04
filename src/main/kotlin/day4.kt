import utils.readInputLines

/** [https://adventofcode.com/2021/day/4] */
class Camps : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        return readInputLines("4.txt").count { row ->
            val (first, second) = row.split(",")
                .map { it.split("-").let { range -> (range.first().toInt()..range.last().toInt()).toList() } }
            if (part2) first.any { it in second } else (first.containsAll(second) || second.containsAll(first))
        }
    }
}

fun main() {
    print(Camps().run(part2 = false))
}
