import utils.readInputBlock

/** [https://adventofcode.com/2021/day/6] */
class Packets : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        return readInputBlock("6.txt").withIndex().windowed(if (part2) 14 else 4).first {
            it.distinctBy(IndexedValue<Char>::value).size == it.size
        }.last().index + 1
    }
}

fun main() {
    print(Packets().run(part2 = false))
}
