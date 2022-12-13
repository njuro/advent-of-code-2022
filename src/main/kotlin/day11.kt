import utils.readInputBlock
import kotlin.math.floor

/** [https://adventofcode.com/2021/day/11] */
class Monkeys : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        data class Monkey(
            val items: MutableList<Long>,
            val inspect: (Long) -> Long,
            val testDivisor: Int,
            val testOk: Int,
            val testFail: Int
        ) {
            var inspectCount = 0L
        }

        val monkeys =
            readInputBlock("11.txt").split("\n\n").map { it.lines().filter(String::isNotBlank).drop(1) }.map { block ->
                val items = block[0].substringAfter(": ").split(", ").map(String::toLong).toMutableList()
                val inspect: ((Long) -> Long) = block[1].substringAfter(" = old ").split(" ").let { (sign, operand) ->
                    if (sign == "*") {
                        { value -> value * if (operand == "old") value else operand.toLong() }
                    } else {
                        { value -> value + if (operand == "old") value else operand.toLong() }
                    }
                }
                val testDivisor = block[2].substringAfter("divisible by ").toInt()
                val testOk = block[3].substringAfter("to monkey ").toInt()
                val testFail = block[4].substringAfter("to monkey ").toInt()
                Monkey(items, inspect, testDivisor, testOk, testFail)
            }

        val totalDivisor = monkeys.map(Monkey::testDivisor).reduce(Int::times)
        fun Monkey.turn() {
            while (items.isNotEmpty()) {
                inspectCount += 1
                val worryLevel =
                    inspect(items.removeFirst()).let { if (part2) it % totalDivisor else floor(it / 3.0).toLong() }
                val newMonkey = if (worryLevel % testDivisor == 0L) testOk else testFail
                monkeys[newMonkey].items.add(worryLevel)
            }
        }

        repeat(if (part2) 10_000 else 20) { monkeys.forEach(Monkey::turn) }
        return monkeys.map { it.inspectCount }.sortedDescending().take(2).run { first() * last() }
    }
}

fun main() {
    print(Monkeys().run(part2 = true))
}
