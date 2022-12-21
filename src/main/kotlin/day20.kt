import utils.readInputLines

/** [https://adventofcode.com/2021/day/20] */
class Numbers : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        data class Number(val originalIndex: Int, val value: Long)

        val numbers = readInputLines("20.txt").mapIndexed { index, value ->
            Number(
                index,
                value.toLong().let { if (part2) it * 811589153 else it })
        }.toMutableList()

        repeat(if (part2) 10 else 1) {
            numbers.indices.forEach { index ->
                val currentIndex = numbers.indexOfFirst { it.originalIndex == index }
                val currentNumber = numbers.removeAt(currentIndex)
                numbers.add((currentIndex + currentNumber.value).mod(numbers.size), currentNumber)
            }
        }

        val indexOfZero = numbers.indexOfFirst { it.value == 0L }
        return setOf(1000, 2000, 3000).sumOf { numbers[(indexOfZero + it) % numbers.size].value }
    }
}

fun main() {
    print(Numbers().run(part2 = true))
}
