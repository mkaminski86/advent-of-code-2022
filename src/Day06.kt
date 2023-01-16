private fun findMarkerIndex(input: List<String>, distinctChars: Int): Int {
  val inputChars = input[0].toCharArray()
  val startSequenceBuffer = mutableListOf<Char>()
  var endOfStartSequenceIndex = -1
  for (index in inputChars.indices) {
    val char = inputChars[index]
    if (startSequenceBuffer.contains(char)) {
      val indexInStartSequence = startSequenceBuffer.indexOfFirst { it == char }
      var i = 0
      while (i <= indexInStartSequence) {
        startSequenceBuffer.removeFirst()
        i++
      }
    }
    startSequenceBuffer.add(char)
    if (startSequenceBuffer.size == distinctChars) {
      endOfStartSequenceIndex = index
      break
    }
  }
  return endOfStartSequenceIndex + 1
}

fun main() {
  fun part1(input: List<String>) = findMarkerIndex(input, 4)

  fun part2(input: List<String>) = findMarkerIndex(input, 14)

  val input = readInput("Day06")
  part1(input).println()
  part2(input).println()
}

