private fun calculateTotalScore(input: List<String>, scoreMapping: Map<String, Int>) = input.sumOf { scoreMapping[it] ?: 0 }
fun main() {
  fun part1(input: List<String>): Int {
    val scoreMapping = mapOf(
      "A X" to 1 + 3,
      "A Y" to 2 + 6,
      "A Z" to 3 + 0,
      "B X" to 1 + 0,
      "B Y" to 2 + 3,
      "B Z" to 3 + 6,
      "C X" to 1 + 6,
      "C Y" to 2 + 0,
      "C Z" to 3 + 3,
    )

    return calculateTotalScore(input, scoreMapping)
  }

  fun part2(input: List<String>): Int {
    val scoreMapping = mapOf(
      "A X" to 3 + 0,
      "A Y" to 1 + 3,
      "A Z" to 2 + 6,
      "B X" to 1 + 0,
      "B Y" to 2 + 3,
      "B Z" to 3 + 6,
      "C X" to 2 + 0,
      "C Y" to 3 + 3,
      "C Z" to 1 + 6,
    )

    return calculateTotalScore(input, scoreMapping)
  }

  val input = readInput("Day02")
  part1(input).println()
  part2(input).println()
}
