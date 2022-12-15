import utils.Coordinate
import utils.readInputLines
import kotlin.math.abs

/** [https://adventofcode.com/2021/day/15] */
class Sensors : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val pattern = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
        val sensors = readInputLines("15.txt").map {
            val (sX, sY, bX, bY) = pattern.matchEntire(it)!!.destructured.toList().map(String::toInt)
            Coordinate(sX, sY).let { sensor -> sensor to sensor.distanceTo(Coordinate(bX, bY)) }
        }

        return if (part2) {
            generateSequence(Coordinate(0, 0)) { point ->
                sensors.find { (center, distance) -> point.distanceTo(center) <= distance }
                    ?.let { (center, distance) ->
                        val next = Coordinate(center.x + distance + 1 - abs(center.y - point.y), point.y)
                        next.run { if (x > 4_000_000) Coordinate(0, y + 1) else this }
                    }
            }.last().run { x * 4_000_000L + y }
        } else sensors.flatMap { (sensor, distance) ->
            val base = Coordinate(sensor.x, 2_000_000)
            setOf(Coordinate::left, Coordinate::right).flatMap { move ->
                generateSequence(base) { move.invoke(it, 1) }.takeWhile { sensor.distanceTo(it) <= distance }
            }.map { it.x }
        }.distinct().size - 1
    }
}

fun main() {
    print(Sensors().run(part2 = false))
}
