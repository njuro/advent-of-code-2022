import utils.readInputLines
import kotlin.math.max

/** [https://adventofcode.com/2021/day/19] */
class Day19 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        data class Blueprint(
            val id: Int,
            val oreCost: Int,
            val clayCost: Int,
            val obsidianCost: Pair<Int, Int>,
            val geodeCost: Pair<Int, Int>
        )

        val blueprints = readInputLines("19.txt").map { row ->
            val data = Regex("\\d+").findAll(row).map { it.value.toInt() }.toList()
            Blueprint(data[0], data[1], data[2], data[3] to data[4], data[5] to data[6])
        }

        data class Inventory(
            val oreRobots: Int = 1,
            val clayRobots: Int = 0,
            val obsidianRobots: Int = 0,
            val geodeRobots: Int = 0,
            val ore: Int = 0,
            val clay: Int = 0,
            val obsidian: Int = 0,
            val geode: Int = 0
        )

        fun Blueprint.maxGeodes(limit: Int): Int {
            var maxGeode = 0
            val queue = mutableSetOf(0 to Inventory())
            while (queue.isNotEmpty()) {
                val (minute, inventory) = queue.first().also { queue.remove(it) }

                if (inventory.geode + (limit - minute) <= maxGeode) {
                    continue
                }

                if (minute == limit) {
                    maxGeode = max(maxGeode, inventory.geode)
                    continue
                }

                val nextInventory = inventory.copy(
                    ore = inventory.ore + inventory.oreRobots,
                    clay = inventory.clay + inventory.clayRobots,
                    obsidian = inventory.obsidian + inventory.obsidianRobots,
                    geode = inventory.geode + inventory.geodeRobots
                )

                if (inventory.ore >= geodeCost.first && inventory.obsidian >= geodeCost.second) {
                    queue.add(
                        minute + 1 to nextInventory.copy(
                            geodeRobots = nextInventory.geodeRobots + 1,
                            ore = nextInventory.ore - geodeCost.first,
                            obsidian = nextInventory.obsidian - geodeCost.second
                        )
                    )
                    continue
                }

                if (inventory.ore >= obsidianCost.first && inventory.clay >= obsidianCost.second) {
                    queue.add(
                        minute + 1 to nextInventory.copy(
                            obsidianRobots = nextInventory.obsidianRobots + 1,
                            ore = nextInventory.ore - obsidianCost.first,
                            clay = nextInventory.clay - obsidianCost.second
                        )
                    )
                }

                if (inventory.ore >= clayCost) {
                    queue.add(
                        minute + 1 to nextInventory.copy(
                            clayRobots = nextInventory.clayRobots + 1,
                            ore = nextInventory.ore - clayCost
                        )
                    )
                }

                if (inventory.ore >= oreCost) {
                    queue.add(
                        minute + 1 to nextInventory.copy(
                            oreRobots = nextInventory.oreRobots + 1,
                            ore = nextInventory.ore - oreCost
                        )
                    )
                }

                queue.add(minute + 1 to nextInventory)
            }

            println("Blueprint ${id} can open $maxGeode geodes")
            return maxGeode
        }

        return if (part2) blueprints.take(3).map { it.maxGeodes(32) }
            .reduce(Int::times) else blueprints.sumOf { it.id * it.maxGeodes(24) }
    }
}

fun main() {
    print(Day19().run(part2 = false))
}
