import utils.readInputBlock

/** [https://adventofcode.com/2021/day/1] */
class Snacks : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        return readInputBlock("1.txt")
            .split("\n\n")
            .map { it.split("\n").filter(String::isNotBlank).sumOf(String::toInt) }
            .sortedDescending().take(if (part2) 3 else 1).sum()
    }
}

fun main() {
    print(Snacks().run(part2 = true))
}
