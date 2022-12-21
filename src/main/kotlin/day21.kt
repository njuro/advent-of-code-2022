import utils.readInputLines

/** [https://adventofcode.com/2021/day/21] */
class Day21 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        data class Equation(val left: String, val right: String, val operation: String)

        val literals = mutableMapOf<String, Long>()
        val equations = mutableMapOf<String, Equation>()
        readInputLines("21.txt").forEach {
            val (name, value) = it.split(": ")
            value.toLongOrNull()?.let { literal -> literals[name] = literal } ?: run {
                val (left, operation, right) = value.split(" ")
                equations[name] = Equation(left, right, operation)
            }
        }

        while (equations.isNotEmpty()) {
            equations.filterValues { it.left in literals && it.right in literals }.forEach { (name, equation) ->
                val leftValue = literals.getValue(equation.left)
                val rightValue = literals.getValue(equation.right)
                val result = when (equation.operation) {
                    "+" -> leftValue + rightValue
                    "-" -> leftValue - rightValue
                    "*" -> leftValue * rightValue
                    "/" -> leftValue / rightValue
                    else -> error("Invalid operation ${equation.operation}")
                }
                equations.remove(name)
                literals[name] = result
            }
        }

        return literals.getValue("root")
    }
}

fun main() {
    print(Day21().run(part2 = false))
}
