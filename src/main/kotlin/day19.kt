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

        return blueprints.sumOf { blueprint ->
            var maxGeode = 0
            val queue = mutableSetOf(0 to Inventory())
            while (queue.isNotEmpty()) {
                val (minute, inventory) = queue.first().also { queue.remove(it) }

                if (minute == 24) {
                    maxGeode = max(maxGeode, inventory.geode)
                    continue
                }

                val nextInventory = inventory.copy(
                    ore = inventory.ore + inventory.oreRobots,
                    clay = inventory.clay + inventory.clayRobots,
                    obsidian = inventory.obsidian + inventory.obsidianRobots,
                    geode = inventory.geode + inventory.geodeRobots
                )

                if (inventory.ore >= blueprint.geodeCost.first && inventory.obsidian >= blueprint.geodeCost.second) {
                    queue.add(
                        minute + 1 to nextInventory.copy(
                            geodeRobots = nextInventory.geodeRobots + 1,
                            ore = nextInventory.ore - blueprint.geodeCost.first,
                            obsidian = nextInventory.obsidian - blueprint.geodeCost.second
                        )
                    )
                }

                if (inventory.ore >= blueprint.obsidianCost.first && inventory.clay >= blueprint.obsidianCost.second) {
                    queue.add(
                        minute + 1 to nextInventory.copy(
                            obsidianRobots = nextInventory.obsidianRobots + 1,
                            ore = nextInventory.ore - blueprint.obsidianCost.first,
                            clay = nextInventory.clay - blueprint.obsidianCost.second
                        )
                    )
                }

                if (inventory.ore >= blueprint.clayCost) {
                    queue.add(
                        minute + 1 to nextInventory.copy(
                            clayRobots = nextInventory.clayRobots + 1,
                            ore = nextInventory.ore - blueprint.clayCost
                        )
                    )
                }

                if (inventory.ore >= blueprint.oreCost) {
                    queue.add(
                        minute + 1 to nextInventory.copy(
                            oreRobots = nextInventory.oreRobots + 1,
                            ore = nextInventory.ore - blueprint.oreCost
                        )
                    )
                }

                queue.add(minute + 1 to nextInventory)
            }

            println("Blueprint ${blueprint.id} can open $maxGeode geodes")
            blueprint.id * maxGeode
        }
    }
}

fun main() {
    print(Day19().run(part2 = false))
}
