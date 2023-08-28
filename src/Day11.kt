class Monkey(
  var itemWorryLevels: ArrayList<Long>,
  private val inspection: (Long) -> Long,
  val test: Long,
  private val monkeyWhenTestTrue: Int,
  private val monkeyWhenTestFalse: Int,
) {
  var numberOfInspections = 0
    private set

  fun hasItems() = itemWorryLevels.isNotEmpty()
  fun inspect(item: Long, worriesReducer: (Long) -> Long): Long {
    return worriesReducer(inspection(item))
  }

  fun getMonkeyToThrowTo(item: Long) = if (item % test == 0L) {
    monkeyWhenTestTrue
  } else {
    monkeyWhenTestFalse
  }

  fun catchItem(item: Long) = itemWorryLevels.add(item)
  fun endInspection() {
    numberOfInspections += itemWorryLevels.size
    itemWorryLevels.clear()
  }

}

private fun parseInput(lines: List<String>): List<Monkey> = lines.filter { it.isNotEmpty() }.chunked(6).map { line ->
  val initialItems = line[1].substringAfter("Starting items: ").split(", ").map { it.toLong() }
  val inspection = parseInspectionTokens(line[2].substringAfter("Operation: new = ").split(" "))
  val divisibleBy = line[3].substringAfter("Test: divisible by ").toLong()
  val testTrueMonkey = line[4].substringAfter("If true: throw to monkey ").toInt()
  val testFalseMonkey = line[5].substringAfter("If false: throw to monkey ").toInt()
  Monkey(ArrayList(initialItems), inspection, divisibleBy, testTrueMonkey, testFalseMonkey)
}

private fun parseInspectionTokens(tokens: List<String>): (Long) -> Long {
  val rightToken = tokens[2]
  if (rightToken == "old") {
    return { it * it }
  }
  val op = tokens[1]
  if (op == "*") {
    return { it * rightToken.toLong() }
  }
  return { it + rightToken.toLong() }
}

private fun playKeepAway(monkeys: List<Monkey>, numberOfRounds: Int, worriesReducer: (Long) -> Long): Long {
  for (i in 1..numberOfRounds) {
    for (monkey in monkeys) {
      if (!monkey.hasItems()) {
        continue
      }
      monkey.itemWorryLevels.forEach() { item ->
        val newWorryLevel = monkey.inspect(item) { worriesReducer(it) }
        val monkeyToPassTheItem = monkey.getMonkeyToThrowTo(newWorryLevel)
        monkeys[monkeyToPassTheItem].catchItem(newWorryLevel)
      }
      monkey.endInspection()
    }
  }
  val monkeyBusiness = monkeys.map { it.numberOfInspections.toLong() }.sortedDescending().let {
    it[0] * it[1]
  }
  return monkeyBusiness
}

fun main() {
  fun part1(input: List<String>) = playKeepAway(parseInput(input), 20) { it / 3 }


  fun part2(input: List<String>): Long {
    val monkeys = parseInput(input)
    val productOfTests: Long = monkeys.map { it.test }.reduce(Long::times)
    return playKeepAway(monkeys, 10_000) { it % productOfTests }
  }

  val input = readInput("Day11")
  part1(input).println()
  part2(input).println()
}

