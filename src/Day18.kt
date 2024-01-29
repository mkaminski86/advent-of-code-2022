data class Point3d(val x: Int, val y: Int, val z: Int) {
  fun neighbours() = setOf(
    copy(x = x - 1),
    copy(x = x + 1),
    copy(y = y - 1),
    copy(y = y + 1),
    copy(z = z - 1),
    copy(z = z + 1)
  )
}

class Range3d private constructor(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
  companion object {
    private fun range(points: Set<Point3d>, function: (Point3d) -> Int) =
      points.minOf(function) - 1..points.maxOf(function) + 1

    fun of(points: Set<Point3d>): Range3d {
      val xRange = range(points) { it.x }
      val yRange = range(points) { it.y }
      val zRange = range(points) { it.z }
      return Range3d(xRange, yRange, zRange)
    }
  }

  operator fun contains(element: Point3d) = element.x in xRange && element.y in yRange && element.z in zRange

  fun first() = Point3d(xRange.first, yRange.first, zRange.first)
}


fun main() {
  val cubes =
    readInput("Day18").map { it.split(',').let { (x, y, z) -> Point3d(x.toInt(), y.toInt(), z.toInt()) } }.toSet()

  fun part1() = cubes.sumOf { point -> 6 - point.neighbours().count { it in cubes } }


  fun part2(): Int {
    val ranges = Range3d.of(cubes)
    val queue = ArrayDeque<Point3d>().apply { add(ranges.first()) }
    val seenPoints = mutableSetOf<Point3d>()
    var outsideSides = 0
    while (queue.isNotEmpty()) {
      val point = queue.removeFirst()
      if (point !in seenPoints) {
        seenPoints += point
        point.neighbours().filter { it in ranges }.forEach { n ->
          if (n in cubes) outsideSides++
          else queue.addLast(n)
        }
      }
    }
    return outsideSides
  }


  part1().println()
  part2().println()
}


