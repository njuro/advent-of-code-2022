import utils.readInputLines

/** [https://adventofcode.com/2021/day/2] */
class Weapons : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val points = mapOf(
            "A" to 1,
            "B" to 2,
            "C" to 3
        )
        val destroyers = mapOf(
            "A" to "C",
            "B" to "A",
            "C" to "B"
        )

        return readInputLines("2.txt").sumOf { row ->
            row.split(" ").let { (opponentWeapon, strategy) ->
                val myWeapon = when (strategy) {
                    "X" -> if (part2) destroyers[opponentWeapon]!! else "A"
                    "Y" -> if (part2) opponentWeapon else "B"
                    "Z" -> if (part2) destroyers.entries.first { it.value == opponentWeapon }.key else "C"
                    else -> error("invalid")
                }
                val resultScore = when {
                    destroyers[myWeapon] == opponentWeapon -> 6
                    myWeapon == opponentWeapon -> 3
                    else -> 0
                }
                resultScore + points[myWeapon]!!
            }
        }
    }
}

fun main() {
    print(Weapons().run(part2 = false))
}
