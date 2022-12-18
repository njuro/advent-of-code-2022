import utils.readInputLines

/** [https://adventofcode.com/2021/day/18] */
class Cubes : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        data class Cube(val x: Int, val y: Int, val z: Int) {
            fun adjacent() = setOf(
                copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1), copy(z = z + 1), copy(z = z - 1)
            )
        }

        val cubes = readInputLines("18.txt").map {
            val (x, y, z) = it.split(",").map(String::toInt)
            Cube(x, y, z)
        }.toMutableSet()

        val xRange = cubes.minOf { it.x }..cubes.maxOf { it.x }
        val yRange = cubes.minOf { it.y }..cubes.maxOf { it.y }
        val zRange = cubes.minOf { it.z }..cubes.maxOf { it.z }

        fun Cube.isAirPocket(): Boolean {
            val seen = mutableSetOf<Cube>()
            val queue = mutableListOf(this)
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (current.x !in xRange || current.y !in yRange || current.z !in zRange) {
                    return false
                }
                current.adjacent().filter { it !in cubes && it !in seen }.forEach {
                    queue.add(it)
                    seen.add(it)
                }
            }

            return true
        }

        return cubes.sumOf { cube -> cube.adjacent().count { it !in cubes && if (part2) !it.isAirPocket() else true } }
    }
}

fun main() {
    print(Cubes().run(part2 = true))
}
