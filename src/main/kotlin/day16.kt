import utils.readInputLines

/** [https://adventofcode.com/2021/day/16] */
class Day16 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        data class Valve(val label: String, val flowRate: Int, val tunnels: Set<String>)
        data class State(
            val minute: Int,
            val valve: String,
            val opening: Boolean,
            val opened: Set<String>,
            val flowRate: Int,
            val totalFlow: Int
        )

        val pattern = Regex("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)")
        val valves = readInputLines("16.txt").map {
            val (label, flowRate, tunnels) = pattern.matchEntire(it)!!.destructured
            Valve(label, flowRate.toInt(), tunnels.split(", ").toSet())
        }.associateBy { it.label }

        val queue = mutableSetOf(
            State(
                minute = 1,
                valve = "AA",
                opening = false,
                opened = emptySet(),
                flowRate = 0,
                totalFlow = 0
            )
        )
        val results = mutableSetOf<Int>()
        while (queue.isNotEmpty()) {
            val current = queue.first().also { queue.remove(it) }
            val valve = valves.getValue(current.valve)

            val nextTotalFlow = current.totalFlow + current.flowRate
            var nextFlowRate = current.flowRate
            val nextOpened = current.opened.toMutableSet()

            if (current.minute > 30) {
                results.add(nextTotalFlow)
                continue
            }

            if (current.opening) {
                nextOpened.add(current.valve)
                nextFlowRate += valve.flowRate
            }

            if (!current.opening && current.valve !in current.opened && valve.flowRate > 0) {
                queue.add(
                    State(
                        minute = current.minute + 1,
                        valve = current.valve,
                        opening = true,
                        opened = nextOpened,
                        flowRate = nextFlowRate,
                        totalFlow = nextTotalFlow
                    )
                )
            }

            valve.tunnels.forEach { nextValve ->
                queue.add(
                    State(
                        minute = current.minute + 1,
                        valve = nextValve,
                        opening = false,
                        opened = nextOpened,
                        flowRate = nextFlowRate,
                        totalFlow = nextTotalFlow
                    )
                )
            }
        }

        return results.max()
    }
}

fun main() {
    print(Day16().run(part2 = false))
}
