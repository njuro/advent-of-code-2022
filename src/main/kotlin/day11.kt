import kotlin.math.floor

/** [https://adventofcode.com/2021/day/11] */
class Monkeys : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        data class Monkey(val items: MutableList<Long>, val operation: (Long) -> Long, val test: (Long) -> Int) {
            var inspectCount = 0L
        }

        val totalDivisor = 19 * 2 * 3 * 17 * 13 * 7 * 5 * 11
        val monkeys = listOf(
            Monkey(
                items = mutableListOf(74, 73, 57, 77, 74),
                operation = { it * 11 },
                test = { if (it % 19 == 0L) 6 else 7 }
            ),
            Monkey(
                items = mutableListOf(99, 77, 79),
                operation = { it + 8 },
                test = { if (it % 2 == 0L) 6 else 0 }
            ),
            Monkey(
                items = mutableListOf(64, 67, 50, 96, 89, 82, 82),
                operation = { it + 1 },
                test = { if (it % 3 == 0L) 5 else 3 }
            ),
            Monkey(
                items = mutableListOf(88),
                operation = { it * 7 },
                test = { if (it % 17 == 0L) 5 else 4 }
            ),
            Monkey(
                items = mutableListOf(80, 66, 98, 83, 70, 63, 57, 66),
                operation = { it + 4 },
                test = { if (it % 13 == 0L) 0 else 1 }
            ),
            Monkey(
                items = mutableListOf(81, 93, 90, 61, 62, 64),
                operation = { it + 7 },
                test = { if (it % 7 == 0L) 1 else 4 }
            ),
            Monkey(
                items = mutableListOf(69, 97, 88, 93),
                operation = { it * it },
                test = { if (it % 5 == 0L) 7 else 2 }
            ),
            Monkey(
                items = mutableListOf(59, 80),
                operation = { it + 6 },
                test = { if (it % 11 == 0L) 2 else 3 }
            )
        )

        fun Monkey.turn() {
            while (items.isNotEmpty()) {
                inspectCount += 1
                val worryLevel =
                    operation(items.removeFirst()).let { if (part2) it % totalDivisor else floor(it / 3.0).toLong() }
                monkeys[test(worryLevel)].items.add(worryLevel)
            }
        }

        repeat(if (part2) 10_000 else 20) {
            monkeys.forEach { it.turn() }
        }

        return monkeys.map { it.inspectCount }.sortedDescending().take(2).let { it.first() * it.last() }
    }
}

fun main() {
    print(Monkeys().run(part2 = true))
}
