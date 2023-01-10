fun main() {
  fun part1(input: List<String>): Int {
    return parseInput(input).first()
  }

  fun part2(input: List<String>): Int {
    return parseInput(input).take(3).sum()
  }

  val input = readInput("Day01")
  part1(input).println()
  part2(input).println()
}

private fun parseInput(input: List<String>): List<Int> {
  var totalCals = 0
  val totalCounts = mutableListOf<Int>()
  input.forEach {
    if (it.isEmpty()) {
      totalCounts.add(totalCals)
      totalCals = 0
    } else {
      totalCals += it.toInt()
    }
  }
  totalCounts.sortDescending()
  return totalCounts
}
