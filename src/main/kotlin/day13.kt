import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import utils.readInputLines

/** [https://adventofcode.com/2021/day/13] */
class Signals : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        fun JsonElement.coerceToArray(): JsonArray = if (this is JsonArray) this else JsonArray(listOf(this))

        fun checkOrder(left: JsonElement, right: JsonElement): Int {
            return when {
                left is JsonPrimitive && right is JsonPrimitive -> left.int.compareTo(right.int)
                left is JsonArray && right is JsonArray -> {
                    val leftIterator = left.iterator()
                    val rightIterator = right.iterator()
                    while (leftIterator.hasNext() || rightIterator.hasNext()) {
                        if (!rightIterator.hasNext()) return 1
                        if (!leftIterator.hasNext()) return -1

                        val valueComparison = checkOrder(leftIterator.next(), rightIterator.next())
                        if (valueComparison != 0) return valueComparison
                    }
                    return 0
                }

                else -> checkOrder(left.coerceToArray(), right.coerceToArray())
            }
        }

        operator fun JsonElement.compareTo(other: JsonElement) = checkOrder(this, other)

        return readInputLines("13.txt").filter(String::isNotBlank).map<String, JsonElement>(Json::decodeFromString)
            .run {
                if (part2) {
                    val dividers = listOf("[[2]]", "[[6]]").map<String, JsonElement>(Json::decodeFromString)
                    toMutableList().apply { addAll(dividers) }.sortedWith(::checkOrder)
                        .let { list -> dividers.map { list.indexOf(it) + 1 }.reduce(Int::times) }
                } else {
                    chunked(2).mapIndexedNotNull { index, (left, right) ->
                        if (left <= right) index + 1 else null
                    }.sum()
                }
            }
    }
}

fun main() {
    print(Signals().run(part2 = true))
}
