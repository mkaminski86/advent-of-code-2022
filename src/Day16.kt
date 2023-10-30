import com.github.shiguruikai.combinatoricskt.combinations
import com.github.shiguruikai.combinatoricskt.permutations

private data class ValveRoom(val name: String, val paths: List<String>, val flowRate: Int)


private fun calculateShortestPaths(rooms: Map<String, ValveRoom>): Map<String, Map<String, Int>> {
  val shortestPaths = rooms.values.associate {
    it.name to it.paths.associateWith { 1 }.toMutableMap()
  }.toMutableMap()
  shortestPaths.keys.permutations(3).forEach { (waypoint, from, to) ->
    shortestPaths[from, to] = minOf(
      shortestPaths[from, to], // Existing Path
      shortestPaths[from, waypoint] + shortestPaths[waypoint, to] // New Path
    )
  }
  val zeroFlowRooms = rooms.values.filter { it.flowRate == 0 || it.name == "AA" }.map { it.name }.toSet()
  shortestPaths.values.forEach { it.keys.removeAll(zeroFlowRooms) }
  val canGetToFromAA: Set<String> = shortestPaths.getValue("AA").keys
  return shortestPaths.filter { it.key in canGetToFromAA || it.key == "AA" }
}

private operator fun Map<String, MutableMap<String, Int>>.set(key1: String, key2: String, value: Int) {
  getValue(key1)[key2] = value
}

private operator fun Map<String, Map<String, Int>>.get(key1: String, key2: String, defaultValue: Int = 31000): Int =
  get(key1)?.get(key2) ?: defaultValue

private fun searchPaths(
  rooms: Map<String, ValveRoom>,
  cheapestPathCosts: Map<String, Map<String, Int>>,
  location: String,
  timeAllowed: Int,
  seen: Set<String> = emptySet(),
  timeTaken: Int = 0,
  totalFlow: Int = 0
): Int = cheapestPathCosts
  .getValue(location)
  .asSequence()
  .filterNot { (nextRoom, _) -> nextRoom in seen }
  .filter { (_, traversalCost) -> timeTaken + traversalCost + 1 < timeAllowed }
  .maxOfOrNull { (nextRoom, traversalCost) ->
    searchPaths(
      rooms,
      cheapestPathCosts,
      nextRoom,
      timeAllowed,
      seen + nextRoom,
      timeTaken + traversalCost + 1,
      totalFlow + ((timeAllowed - timeTaken - traversalCost - 1) * rooms.getValue(nextRoom).flowRate)
    )
  } ?: totalFlow

fun main() {
  val valves = readInput("Day16").map {
    val roomName = it.substringAfter(" ").substringBefore(" ")
    val paths = it.substringAfter("valve").substringAfter(" ").split(", ")
    val flowRate = it.substringAfter('=').substringBefore(';').toInt()
    ValveRoom(roomName, paths, flowRate)
  }.associateBy { it.name }

  val cheapestPathCosts = calculateShortestPaths(valves)

  fun part1() = searchPaths(valves, cheapestPathCosts, "AA", 30)


  fun part2() = cheapestPathCosts.keys.filter { it != "AA" }
    .combinations(cheapestPathCosts.size / 2)
    .map { it.toSet() }
    .maxOf { halfOfTheRooms ->
      searchPaths(valves, cheapestPathCosts, "AA", 26, halfOfTheRooms) +
          searchPaths(valves, cheapestPathCosts, "AA", 26, cheapestPathCosts.keys - halfOfTheRooms)
    }



  part1().println()
  part2().println()
}


