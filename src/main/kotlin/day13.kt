import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import utils.readInputBlock
import utils.readInputLines

/** [https://adventofcode.com/2021/day/13] */
class Signals : AdventOfCodeTask {
    enum class Result { IN_ORDER, OUT_OF_ORDER, INDECISIVE }

    override fun run(part2: Boolean): Any {
        fun JsonElement.coerceToArray(): JsonArray = if (this is JsonArray) this else JsonArray(listOf(this))

        fun checkOrder(left: JsonElement, right: JsonElement): Result {
            println("Comparing $left vs $right")
            return when {
                left is JsonPrimitive && right is JsonPrimitive -> {
                    when {
                        left.int < right.int -> Result.IN_ORDER
                        left.int > right.int -> Result.OUT_OF_ORDER
                        else -> Result.INDECISIVE
                    }
                }

                left is JsonArray && right is JsonArray -> {
                    var index = 0
                    while (true) {
                        if (index >= left.size && index >= right.size) {
                            return Result.INDECISIVE
                        }
                        if (index >= right.size) {
                            return Result.OUT_OF_ORDER
                        }
                        if (index >= left.size) {
                            return Result.IN_ORDER
                        }
                        val valueResult = checkOrder(left[index], right[index])
                        if (valueResult != Result.INDECISIVE) {
                            return valueResult
                        }
                        index++
                    }
                    return Result.INDECISIVE
                }

                else -> checkOrder(left.coerceToArray(), right.coerceToArray())
            }
        }


        if (!part2) {
            return readInputBlock("13.txt").split("\n\n").map { it.lines() }
                .mapIndexedNotNull { index, (left, right) ->
                    println("PAIR ${index + 1}")
                    if (checkOrder(
                            Json.decodeFromString(left),
                            Json.decodeFromString(right)
                        ) != Result.OUT_OF_ORDER
                    ) index + 1 else null
                }.sum()
        }

        val comparator: Comparator<JsonElement> = Comparator { e1, e2 ->
            when (checkOrder(e1, e2)) {
                Result.IN_ORDER -> -1
                Result.OUT_OF_ORDER -> 1
                Result.INDECISIVE -> 0
            }
        }
        val dividers: List<JsonElement> = listOf("[[2]]", "[[6]]").map(Json::decodeFromString)
        return readInputLines("13.txt").filter { it.isNotBlank() }
            .map<String, JsonElement> { Json.decodeFromString(it) }.toMutableList().apply {
            addAll(dividers)
        }.sortedWith(comparator).let { (it.indexOf(dividers.first()) + 1) * (it.indexOf(dividers.last()) + 1) }

    }
}

fun main() {
    print(Signals().run(part2 = true))
}
