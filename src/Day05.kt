private fun buildStacks(input: List<String>): Map<Int, ArrayDeque<Char>> {
  val stackRows = input.takeWhile { it.contains('[') }
  return (1 until stackRows.last().length step 4).map { index ->
    stackRows
      .mapNotNull { it.getOrNull(index) }
      .filter { !it.isWhitespace() }
  }.mapIndexed { index, stack -> index + 1 to  ArrayDeque(stack.reversed())}.toMap()
}
private fun moveCrates(numberOfElements: Int, sourceStack: ArrayDeque<Char>, targetStack: ArrayDeque<Char>, reversed: Boolean) {
  val moving = sourceStack.takeLast(numberOfElements)
  var i = 0
  while (i < numberOfElements) {
    sourceStack.removeLast()
    i++
  }
  targetStack.addAll(if (reversed) moving.reversed() else moving)
}

fun solve(input: List<String>, reversed: Boolean): String {
  val instructionRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()
  val stacks = buildStacks(input)
  input.dropWhile { !it.startsWith("move") }.forEach { instruction ->
    val (numberOfElements, sourceStackRaw, targetStackRaw) = instructionRegex.find(instruction)?.destructured
      ?: throw Exception()
    val sourceStack = sourceStackRaw.toInt()
    val targetStack = targetStackRaw.toInt()
    if (stacks[sourceStack] == null || stacks[targetStack] == null) {
      throw Exception("Stack(s) don't exist")
    }
    moveCrates(numberOfElements.toInt(), stacks[sourceStack]!!, stacks[targetStack]!!, reversed)
  }
  return stacks.values.filter { it.isNotEmpty() }.map {it.removeLast() }.joinToString("")
}
fun main() {
  fun part1(input: List<String>) = solve(input, true)
  fun part2(input: List<String>) = solve(input, false)

  val input = readInput("Day05")
  part1(input).println()
  part2(input).println()
}

