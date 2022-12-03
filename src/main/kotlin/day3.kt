import utils.readInputLines

/** [https://adventofcode.com/2021/day/3] */
class Bags : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        fun Char.priority() = this - if (this.isLowerCase()) 'a' - 1 else 'A' - 27
        val bags = readInputLines("3.txt")
        return if (part2) {
            bags.chunked(3).sumOf { group ->
                group[0].first { it in group[1] && it in group[2] }.priority()
            }
        } else {
            bags.sumOf { bag ->
                val firstHalf = bag.substring(0, bag.length / 2)
                val secondHalf = bag.substring(bag.length / 2)
                firstHalf.first { it in secondHalf }.priority()
            }
        }
    }
}

fun main() {
    print(Bags().run(part2 = true))
}
