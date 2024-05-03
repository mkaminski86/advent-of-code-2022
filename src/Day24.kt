private data class Blizzard(val position: Point2d, val movingDirection: Point2d) {
  fun move(boundary: Point2d): Blizzard {
    var newPosition = position + movingDirection

    when {
      newPosition.x == 0 -> newPosition = Point2d(boundary.x, position.y)
      newPosition.x > boundary.x -> newPosition = Point2d(1, position.y)
      newPosition.y == 0 -> newPosition = Point2d(position.x, boundary.y)
      newPosition.y > boundary.y -> newPosition = Point2d(position.x, 1)
    }
    return copy(position = newPosition)
  }
}

private data class Valley(val limit: Point2d, val blizzards: Set<Blizzard>) {
  val blizzardPositions = blizzards.map { it.position }.toSet()

  fun canStayAt(destination: Point2d) = destination !in blizzardPositions
  fun inBounds(destination: Point2d) = destination.x in 1..limit.x && destination.y in 1..limit.y
  fun tick() = copy(blizzards = blizzards.map { it.move(limit) }.toSet())
}

private data class PathTo(val position: Point2d, val stepsToHere: Int) {
  fun next(position: Point2d = this.position) = PathTo(position, stepsToHere + 1)
}

private fun simulate(startState: Valley, startingPoint: Point2d, exitPoint: Point2d, stepsSoFar: Int = 0): Pair<Int, Valley> {
  val states = mutableMapOf(stepsSoFar to startState)
  val queue = mutableListOf(PathTo(startingPoint, stepsSoFar))
  val seen = mutableSetOf<PathTo>()
  while (queue.isNotEmpty()) {
    val currentPosition = queue.removeFirst()
    if (currentPosition !in seen) {
      seen += currentPosition
      val nextState = states.computeIfAbsent(currentPosition.stepsToHere+1) { key -> states.getValue(key - 1).tick() }

      if (nextState.canStayAt(currentPosition.position)) queue.add(currentPosition.next())

      val neighbours = currentPosition.position.getNonDiagonalNeighbours()
      if (exitPoint in neighbours) return Pair(currentPosition.stepsToHere + 1, nextState)

      neighbours.filter { (nextState.canStayAt(it) && nextState.inBounds(it)) }
        .forEach { neighbour -> queue.add(currentPosition.next(neighbour)) }
    }

  }
  throw IllegalStateException("Should not happen")
}

fun main() {
  val lines = readInput("Day24")
  val maxLowerRight = Point2d(lines.first().lastIndex - 1, lines.lastIndex - 1)
  val blizzards = lines.flatMapIndexed { y, row ->
    row.mapIndexedNotNull { x, char ->
      when (char) {
        'v' -> Blizzard(Point2d(x, y), Point2d(0, 1))
        '^' -> Blizzard(Point2d(x, y), Point2d(0, -1))
        '<' -> Blizzard(Point2d(x, y), Point2d(-1, 0))
        '>' -> Blizzard(Point2d(x, y), Point2d(1, 0))
        else -> null
      }
    }
  }.toSet()

  val initialState = Valley(maxLowerRight, blizzards)
  val startingPoint = Point2d(lines.first().indexOfFirst { it == '.' }, 0)
  val exitPoint = Point2d(lines.last().indexOfLast { it == '.' }, lines.lastIndex)

  fun part1() = simulate(initialState, startingPoint, exitPoint).first
  fun part2(): Int {
    val firstTrip = simulate(initialState, startingPoint, exitPoint)
    val secondTrip = simulate(firstTrip.second, exitPoint, startingPoint, firstTrip.first)
    return simulate(secondTrip.second, startingPoint, exitPoint, secondTrip.first).first
  }


  part1().println()
  part2().println()
}

