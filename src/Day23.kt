private fun Point2d.getNeighbours() = setOf(
  copy(x = x - 1, y = y - 1),
  copy(y = y - 1),
  copy(x = x + 1, y = y - 1),
  copy(x = x - 1),
  copy(x = x + 1),
  copy(x = x - 1, y = y + 1),
  copy(y = y + 1),
  copy(x = x + 1, y = y + 1)
)

private fun tick(currentPositions: Set<Point2d>, movePossibilities: List<List<Point2d>>, round: Int): Set<Point2d> {
  val positionsAfterCurrentRound = currentPositions.toMutableSet()
  val possibleTransitions = currentPositions.filter { p -> p.getNeighbours().any { it in currentPositions } }
    .mapNotNull { p ->
      figureOutPossibleTransition(movePossibilities, round, p, currentPositions)
    }.toMap()
  val distinctDestinations = possibleTransitions.values.groupingBy { it }.eachCount().filter { it.value == 1 }.keys

  possibleTransitions.filter { (_, target) -> target in distinctDestinations }.forEach { (source, destination) ->
    positionsAfterCurrentRound.remove(source)
    positionsAfterCurrentRound.add(destination)

  }
  return positionsAfterCurrentRound
}

private fun figureOutPossibleTransition(
  movePossibilities: List<List<Point2d>>,
  round: Int,
  p: Point2d,
  currentPositions: Set<Point2d>
) = movePossibilities.indices.map { direction -> movePossibilities[(round + direction) % 4] }
  .firstNotNullOfOrNull { move ->
    if (move.none { offset -> (p + offset) in currentPositions }) p to (p + move.first())
    else null
  }

fun main() {
  val elvesStartingPositions = readInput("Day23").flatMapIndexed { y, row ->
    row.mapIndexedNotNull { x, char ->
      if (char == '#') Point2d(x, y) else null
    }
  }.toSet()

  val movementPropositions: List<List<Point2d>> =
    listOf(
      listOf(Point2d(0, -1), Point2d(-1, -1), Point2d(1, -1)),
      listOf(Point2d(0, 1), Point2d(-1, 1), Point2d(1, 1)),
      listOf(Point2d(-1, 0), Point2d(-1, -1), Point2d(-1, 1)),
      listOf(Point2d(1, 0), Point2d(1, -1), Point2d(1, 1)),
    )

  fun part1(): Int {
    val finalPositions =
      (0 until 10).fold(elvesStartingPositions) { acc, round -> tick(acc, movementPropositions, round) }
    val mapSize = ((finalPositions.maxOf { it.x } - finalPositions.minOf { it.x }) + 1) *
        ((finalPositions.maxOf { it.y } - finalPositions.minOf { it.y }) + 1)

    return mapSize - finalPositions.size
  }

  fun part2(): Int {
    var currentPositions = elvesStartingPositions
    var roundNumber = 0
    do {
      val previousPositions = currentPositions
      currentPositions = tick(previousPositions, movementPropositions, roundNumber++)

    }while (currentPositions != previousPositions)
    return roundNumber
  }

  part1().println()
  part2().println()
}


