private fun mapInputToRangePairs(input: List<String>) = input.map {
  val rawRanges = it.split(",").flatMap { singleRange -> singleRange.split("-") }.map { str -> str.toInt() }
  Pair(rawRanges[0]..rawRanges[1], rawRanges[2]..rawRanges[3])
}

private fun IntRange.containsRange(other: IntRange) = other.first in this && other.last in this

private fun IntRange.overlaps(other: IntRange) = other.first in this || other.last in this
fun main() {
  fun part1(input: List<String>) =
    mapInputToRangePairs(input).filter { it.first.containsRange(it.second) || it.second.containsRange(it.first) }.size

  fun part2(input: List<String>) =
    mapInputToRangePairs(input).filter { it.first.overlaps(it.second) || it.second.overlaps(it.first) }.size

  val input = readInput("Day04")
  part1(input).println()
  part2(input).println()
}
