import kotlin.math.absoluteValue
import kotlin.math.sign

private fun Point2d.distanceTo(other: Point2d) = (other.x - x).absoluteValue + (other.y - y).absoluteValue
private fun Point2d.drawLine(end: Point2d): List<Point2d> {
  val xDirection = (end.x - x).sign
  val yDirection = (end.y - y).sign
  val numberOfPoints = maxOf((x - end.x).absoluteValue, (y - end.y).absoluteValue)
  return (1..numberOfPoints).scan(this) { lastPoint, _ -> Point2d(lastPoint.x + xDirection, lastPoint.y + yDirection) }
}

class Sensor(val position: Point2d, closestBeacon: Point2d) {
  val distanceToClosestBeacon = position.distanceTo(closestBeacon)
  fun rangeInRow(rowCoordinate: Int): IntRange? {
    val widthOfScan = distanceToClosestBeacon - (position.y - rowCoordinate).absoluteValue
    return ((position.x - widthOfScan)..(position.x + widthOfScan)).takeIf { it.first <= it.last }
  }

  fun isInRange(point: Point2d) = position.distanceTo(point) <= distanceToClosestBeacon
}

private fun List<IntRange>.reduce(): List<IntRange> = if (this.size <= 1) this else {
  val sorted = this.sortedBy { it.first }
  sorted.drop(1).fold(mutableListOf(sorted.first())) { acc, range ->
    val lastRange = acc.last()
    if (range.first <= lastRange.last) {
      acc[acc.lastIndex] = (lastRange.first..maxOf(lastRange.last, range.last))
    } else {
      acc.add(range)
    }
    acc

  }
}
  fun main() {
    val sensors = readInput("Day15").map {
      Sensor(
        Point2d(
          it.substringAfter("x=").substringBefore(',').toInt(),
          it.substringAfter("y=").substringBefore(':').toInt()
        ),
        Point2d(
          it.substringAfterLast("x=").substringBeforeLast(',').toInt(),
          it.substringAfterLast("y=").toInt()
        )
      )
    }

    fun part1() = sensors.mapNotNull { it.rangeInRow(2000000) }.reduce().sumOf { it.last - it.first }


    fun part2(): Long {
      val size = 0..4000000
      return sensors.firstNotNullOf { sensor ->
        val up = Point2d(sensor.position.x, sensor.position.y - sensor.distanceToClosestBeacon - 1)
        val down = Point2d(sensor.position.x, sensor.position.y + sensor.distanceToClosestBeacon + 1)
        val left = Point2d(sensor.position.x - sensor.distanceToClosestBeacon - 1, sensor.position.y)
        val right = Point2d(sensor.position.x + sensor.distanceToClosestBeacon + 1, sensor.position.y)

        (up.drawLine(right) + right.drawLine(down) + down.drawLine(left) + left.drawLine(up)).filter {
          it.x in size && it.y in size
        }.firstOrNull { point -> sensors.none { sensor -> sensor.isInRange(point) } }
      }.let { (it.x * 4000000L) + it.y }
    }



    part1().println()
    part2().println()
  }


