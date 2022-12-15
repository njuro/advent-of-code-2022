import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/15] */
class Day15 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val pattern = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
        val sensors = readInputLines("15.txt").associate {
            val (sX, sY, bX, bY) = pattern.matchEntire(it)!!.destructured.toList().map(String::toInt)
            Coordinate(sX, sY) to Coordinate(bX, bY)
        }

        return sensors.entries.flatMap { (sensor, beacon) ->
            val maxDistance = sensor.distanceTo(beacon)
            val base = Coordinate(sensor.x, 2_000_000)
            setOf(Coordinate::left, Coordinate::right).flatMap { move ->
                generateSequence(base) { move.invoke(it, 1) }.takeWhile { sensor.distanceTo(it) <= maxDistance }
            }
        }.distinct().size - 1
    }
}

fun main() {
    print(Day15().run(part2 = false))
}
