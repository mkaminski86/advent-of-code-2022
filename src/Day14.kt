import kotlin.math.absoluteValue
import kotlin.math.sign

private fun Point2d.drawLine(end: Point2d): List<Point2d> {
  val xDirection = (end.x - x).sign
  val yDirection = (end.y - y).sign
  val numberOfPoints = maxOf((x - end.x).absoluteValue, (y - end.y).absoluteValue)
  return (1..numberOfPoints).scan(this) { lastPoint, _ -> Point2d(lastPoint.x + xDirection, lastPoint.y + yDirection) }
}

private fun Point2d.stepDown() = Point2d(x, y + 1)
private fun Point2d.stepDiagonalLeft() = Point2d(x - 1, y + 1)
private fun Point2d.stepDiagonalRight() = Point2d(x + 1, y + 1)

private fun dropSand(initialSandSource: Point2d, filledPoints: MutableSet<Point2d>, voidLevel: Int): Int {
  var start = initialSandSource
  var noOfSandPartsComeToRest = 0
  while (true) {
    val next =
      listOf(start.stepDown(), start.stepDiagonalLeft(), start.stepDiagonalRight()).firstOrNull { it !in filledPoints }
    start = when {
      next == null && start == initialSandSource -> return noOfSandPartsComeToRest
      next == null -> {
        filledPoints.add(start)
        noOfSandPartsComeToRest++
        initialSandSource
      }

      next.y == voidLevel -> return noOfSandPartsComeToRest
      else -> next
    }
  }
}

private fun init() = readInput("Day14").flatMap { row ->
  row.split(" -> ").map {
    val coordsRaw = it.split(",")
    Point2d(coordsRaw.first().toInt(), coordsRaw.last().toInt())
  }.zipWithNext().flatMap { (start, end) -> start.drawLine(end) }
}

fun main() {
  val filledPoints = init()

  val sandSource = Point2d(500, 0)
  val depthOfCave = filledPoints.maxOf { it.y }

  fun part1() = dropSand(sandSource, filledPoints.toMutableSet(), depthOfCave + 1)


  fun part2(): Int {
    val mutableFilledPoints = filledPoints.toMutableSet()
    val minX = mutableFilledPoints.minOf { it.x }
    val maxX = mutableFilledPoints.maxOf { it.x }
    mutableFilledPoints.addAll(
      Point2d(minX - depthOfCave, depthOfCave + 2).drawLine(
        Point2d(
          maxX + depthOfCave,
          depthOfCave + 2
        )
      )
    )
    return dropSand(sandSource, mutableFilledPoints, depthOfCave + 3) + 1
  }



  part1().println()
  part2().println()
}

