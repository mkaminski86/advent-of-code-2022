import java.lang.IllegalStateException
import kotlin.math.absoluteValue

operator fun Point2d.plus(other: Point2d) = Point2d(x + other.x, y + other.y)

private fun <T> List<T>.nth(n: Int) = this[n % size]
private fun Set<Point2d>.moveToStart(ceilingHeight: Int) = map { it + Point2d(2, ceilingHeight - 4) }.toSet()
private operator fun Set<Point2d>.times(point: Point2d) = this.map { it + point }.toSet()
private operator fun IntRange.contains(set: Set<Point2d>) = set.all { it.x in this }
private fun Set<Point2d>.minY() = minOf { it.y }
private fun Set<Point2d>.height() = minY().absoluteValue
private fun Set<Point2d>.normalizedCaveCeiling(): List<Int> =
  groupBy { it.x }
    .entries
    .sortedBy { it.key }
    .map { pointList -> pointList.value.minBy { point -> point.y } }
    .let {
      val normalTo = this.minY()
      it.map { point -> normalTo - point.y }
    }

val shapes = listOf(
  setOf(Point2d(0, 0), Point2d(1, 0), Point2d(2, 0), Point2d(3, 0)),
  setOf(Point2d(1, 0), Point2d(0, -1), Point2d(1, -1), Point2d(2, -1), Point2d(1, -2)),
  setOf(Point2d(0, 0), Point2d(1, 0), Point2d(2, 0), Point2d(2, -1), Point2d(2, -2)),
  setOf(Point2d(0, 0), Point2d(0, -1), Point2d(0, -2), Point2d(0, -3)),
  setOf(Point2d(0, 0), Point2d(1, 0), Point2d(0, -1), Point2d(1, -1))
)
val cave: MutableSet<Point2d> = (0..6).map { Point2d(it, 0) }.toMutableSet()
val down = Point2d(0, 1)
val up = Point2d(0, -1)
var jetCounter: Int = 0
var blockCounter: Int = 0

fun simulate(jets: List<Point2d>) {
  var shape = shapes.nth(blockCounter++).moveToStart(cave.minY())
  do {
    val jet = jets.nth(jetCounter++)
    val jettedShape = shape * jet
    if (jettedShape in (0..6) && jettedShape.intersect(cave).isEmpty()) {
      shape = jettedShape
    }
    shape = shape * down
  } while (shape.intersect(cave).isEmpty())
  cave += (shape * up)
}

private fun calculateHeight(jets: List<Point2d>): Long {
  val targetBlockCount = 1000000000000L - 1

  data class State(val ceiling: List<Int>, val blockMod: Int, val jetMod: Int)

  val seen: MutableMap<State, Pair<Int, Int>> = mutableMapOf()
  while (true) {
    simulate(jets)
    val state = State(cave.normalizedCaveCeiling(), blockCounter % shapes.size, jetCounter % jets.size)
    if (state in seen) {
      // Fast forward
      val (blockCountAtLoopStart, heightAtLoopStart) = seen.getValue(state)
      val blocksPerLoop: Long = blockCounter - 1L - blockCountAtLoopStart
      val totalLoops: Long = (targetBlockCount - blockCountAtLoopStart) / blocksPerLoop
      val remainingBlocksFromClosestLoopToGoal: Long =
        (targetBlockCount - blockCountAtLoopStart) - (totalLoops * blocksPerLoop)
      val heightGainedSinceLoop = cave.height() - heightAtLoopStart
      repeat(remainingBlocksFromClosestLoopToGoal.toInt()) {
        simulate(jets)
      }
      return cave.height() + (heightGainedSinceLoop * (totalLoops - 1))
    }
    seen[state] = blockCounter - 1 to cave.height()
  }
}

fun main() {
  val jets = readInput("Day17")[0].map {
    when (it) {
      '<' -> Point2d(-1, 0)
      '>' -> Point2d(1, 0)
      else -> throw IllegalStateException()
    }
  }

  fun part1(): Int {
    repeat(2022) {
      simulate(jets)
    }
    return cave.height()
  }


  fun part2() = calculateHeight(jets)


  part1().println()
  part2().println()
}


