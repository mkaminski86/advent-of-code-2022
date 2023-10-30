private data class ValueWithOriginalPosition(val originalIndex: Int, val value: Long)

private fun List<ValueWithOriginalPosition>.getCoordinates(): Long {
  val zeroPosition = indexOfFirst { it.value == 0L }
  return listOf(1000, 2000, 3000).sumOf { this[(zeroPosition + it) % size].value }
}

private fun MutableList<ValueWithOriginalPosition>.decrypt() {
  indices.forEach { originalIndex ->
    val index = indexOfFirst { it.originalIndex == originalIndex }
    val moved = removeAt(index)
    add((index + moved.value).mod(size), moved)
  }
}

fun main() {
  fun part1(): Long {
    val numbers = readInput("Day20").mapIndexed { index, number -> ValueWithOriginalPosition(index, number.toLong()) }
      .toMutableList()
    numbers.decrypt()
    return numbers.getCoordinates()
  }


  fun part2(): Long {
    val numbers =
      readInput("Day20").mapIndexed { index, number -> ValueWithOriginalPosition(index, number.toLong() * 811589153) }
        .toMutableList()
    repeat(10) {
      numbers.decrypt()
    }
    return numbers.getCoordinates()
  }


  part1().println()
  part2().println()
}


