import org.matheclipse.core.eval.ExprEvaluator
import utils.readInputLines

/** [https://adventofcode.com/2021/day/21] */
class Equations : AdventOfCodeTask {

    sealed class Node {
        abstract fun evaluate(): Long
    }

    data class Equation(val left: Node, val right: Node, val operation: String) : Node() {
        override fun evaluate(): Long {
            val leftValue = left.evaluate()
            val rightValue = right.evaluate()
            return when (operation) {
                "+" -> leftValue + rightValue
                "-" -> leftValue - rightValue
                "*" -> leftValue * rightValue
                "/" -> leftValue / rightValue
                else -> error("Invalid operation $operation")
            }
        }

        override fun toString(): String = "($left $operation $right)"
    }

    data class Literal(val value: Long) : Node() {
        override fun evaluate(): Long = value

        override fun toString(): String = value.toString()
    }

    data class Variable(val label: String) : Node() {
        override fun evaluate(): Long = throw UnsupportedOperationException()

        override fun toString(): String = label
    }

    data class RawEquation(val left: String, val right: String, val operation: String)

    override fun run(part2: Boolean): Any {
        val literals = mutableMapOf<String, Literal>()
        val equations = mutableMapOf<String, RawEquation>()
        readInputLines("21.txt").forEach {
            val (name, value) = it.split(": ")
            value.toLongOrNull()?.let { number -> literals[name] = Literal(number) } ?: run {
                val (left, operation, right) = value.split(" ")
                equations[name] = RawEquation(left, right, operation)
            }
        }

        fun RawEquation.toEquation(): Equation {
            fun resolve(label: String): Node = when {
                part2 && label == "humn" -> Variable(label)
                label in literals -> literals[label]!!
                else -> equations[label]!!.toEquation()
            }
            return Equation(resolve(left), resolve(right), operation)
        }

        val root = equations["root"]!!.toEquation()
        return if (part2) {
            val equation = root.copy(operation = "==").toString()
            val result = ExprEvaluator().eval("Solve($equation,humn)")
            result.toString().substringAfter("->").substringBefore("}}").toLong()
        } else root.evaluate()
    }
}

fun main() {
    print(Equations().run(part2 = true))
}
