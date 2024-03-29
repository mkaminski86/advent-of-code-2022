import kotlin.math.absoluteValue
import kotlin.math.sign

private fun Point2d.stepTowards(other: Point2d) = Point2d((other.x - x).sign + x, (other.y - y).sign + y)

private fun pointsTouching(p1: Point2d, p2: Point2d) = (p1.x - p2.x).absoluteValue <= 1 && (p1.y - p2.y).absoluteValue <= 1

private fun follow(input: List<String>, numberOfKnots: Int): Int {
  val rope = Array(numberOfKnots) { Point2d() }
  val tailVisits = mutableSetOf<Point2d>()

  val headPath = input.joinToString {
    val direction = it.substringBefore(" ")
    val steps = it.substringAfter(" ").toInt()
    direction.repeat(steps)
  }
  headPath.forEach { step ->
    when (step) {
      'U' -> rope[0] = rope[0].copy(y = rope[0].y + 1)
      'D' -> rope[0] = rope[0].copy(y = rope[0].y - 1)
      'L' -> rope[0] = rope[0].copy(x = rope[0].x - 1)
      'R' -> rope[0] = rope[0].copy(x = rope[0].x + 1)
    }
    rope.indices.windowed(2, 1) { (headIdx, tailIdx) ->
      if (!pointsTouching(rope[headIdx], rope[tailIdx])) {
        rope[tailIdx] = rope[tailIdx].stepTowards(rope[headIdx])
      }
    }

    tailVisits += rope.last()
  }
  return tailVisits.size
}

fun main() {
  fun part1(input: List<String>) = follow(input, 2)


  fun part2(input: List<String>) = follow(input, 10)

  val input = readInput("Day09")
  part1(input).println()
  part2(input).println()
}

