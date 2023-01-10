private fun Char.priority() =
  when (this) {
    in 'a'..'z' -> (this - 'a') + 1
    in 'A'..'Z' -> (this - 'A') + 27
    else -> throw Exception("Out of range")
  }

fun main() {
  fun part1(input: List<String>) = input.map {
    val mid = it.length / 2
    val firstCompartment = it.substring(0, mid).toMutableList()
    val secondCompartment = it.substring(mid).toMutableList()
    firstCompartment.retainAll(secondCompartment)
    firstCompartment.first()
  }.sumOf { it.priority() }

  fun part2(input: List<String>) = input.chunked(3).map {
    val first = it[0].asIterable()
    val second = it[1].asIterable()
    val third = it[2].asIterable()
    first.intersect(second.toSet()).intersect(third.toSet()).first()
  }.sumOf { it.priority() }

  val input = readInput("Day03")
  part1(input).println()
  part2(input).println()
}


