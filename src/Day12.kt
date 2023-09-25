import java.lang.IllegalStateException
import java.util.*

  private fun Point2d.getNonDiagonalNeighbours() = setOf<Point2d>(
    copy(x = x - 1),
    copy(x = x + 1),
    copy(y = y - 1),
    copy(y = y + 1)
  )

class Path(val lastPoint: Point2d, val costUpToHere: Int = 0) : Comparable<Path> {
  override fun compareTo(other: Path) = this.costUpToHere.compareTo(other.costUpToHere)
}

fun findShortestPath(
  start: Point2d,
  elevationsMap: Map<Point2d, Int>,
  canMove: (from: Point2d, to: Point2d) -> Boolean,
  isEnd: (Point2d) -> Boolean
): Int {
  val seenPoints = mutableSetOf<Point2d>()
  val queue = PriorityQueue<Path>().apply { add(Path(start, 0)) }
  while (queue.isNotEmpty()) {
    val current = queue.poll()
    if (current.lastPoint !in seenPoints) {
      seenPoints.add(current.lastPoint)
      val reachableNeighbors = current.lastPoint.getNonDiagonalNeighbours().filter { it in elevationsMap }
        .filter { canMove(current.lastPoint, it) }
      if (reachableNeighbors.any { isEnd(it) }) {
        return current.costUpToHere + 1
      }
      queue.addAll(reachableNeighbors.map { Path(it, current.costUpToHere + 1) })
    }
  }
  throw IllegalStateException("Should not happen")
}

fun main() {
  var start: Point2d? = null
  var end: Point2d? = null
  val elevationsMap = mutableMapOf<Point2d, Int>()
  readInput("Day12").mapIndexed { y, row ->
    row.mapIndexed { x, elevationChar ->
      val currentPoint = Point2d(x, y)
      val elevation = when (elevationChar) {
        'S' -> 0.also { start = currentPoint }
        'E' -> 25.also { end = currentPoint }
        else -> elevationChar - 'a'
      }
      elevationsMap[currentPoint] = elevation
    }
  }
  fun part1() = findShortestPath(
    start!!,
    elevationsMap,
    { from, to -> elevationsMap[to]!! - elevationsMap[from]!! <= 1 }
  ) { it == end }


  fun part2() = findShortestPath(
    end!!,
    elevationsMap,
    { from, to -> elevationsMap[from]!! - elevationsMap[to]!! <= 1 }
  ) { elevationsMap[it] == 0 }



  part1().println()
  part2().println()
}

