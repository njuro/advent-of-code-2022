import utils.readInputLines
import java.util.PriorityQueue

/** [https://adventofcode.com/2021/day/16] */
class Day16 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        data class Valve(val label: String, val flowRate: Int, val tunnels: Set<String>)

        val pattern = Regex("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)")

        val valves = readInputLines("16.txt").map {
            val (label, flowRate, tunnels) = pattern.matchEntire(it)!!.destructured
            Valve(label, flowRate.toInt(), tunnels.split(", ").toSet())
        }.associateBy { it.label }

        val valuable = valves.values.filter { it.flowRate > 0 }.map { it.label }
        fun distanceMap(start: String): Map<String, Int> {
            val queue = PriorityQueue<Pair<String, Int>> { p1, p2 -> p1.second - p2.second }.apply { offer(start to 0) }
            val seen = mutableSetOf(start)
            val distanceMap = mutableMapOf<String, Int>()
            while (queue.isNotEmpty()) {
                val (current, minutes) = queue.poll()
                if (current in valuable) {
                    distanceMap[current] = minutes
                }

                valves.getValue(current).tunnels.filter { it !in seen }.forEach {
                    seen.add(it)
                    queue.offer(it to minutes + 1)
                }
            }

            return distanceMap
        }

        val distances = (valuable + "AA").associateWith { distanceMap(it) }

        data class State(val minute: Int, val valve: String, val opened: Map<String, Int>)

        val queue = mutableListOf(State(minute = 0, valve = "AA", opened = emptyMap()))
        val results = mutableSetOf<Int>()
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val candidates = distances.getValue(current.valve)
                .filter { (valve, distance) -> valve !in current.opened && current.minute + distance < 30 }
            if (candidates.isEmpty()) {
                var totalFlow = 0
                current.opened.forEach { (valve, openedAt) ->
                    totalFlow += valves.getValue(valve).flowRate * (30 - openedAt)
                }

                results.add(totalFlow)
            }
            candidates.forEach { (nextValve, distance) ->
                val nextMinute = current.minute + distance + 1
                queue.add(
                    State(
                        minute = nextMinute,
                        valve = nextValve,
                        opened = current.opened + (nextValve to nextMinute)
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
