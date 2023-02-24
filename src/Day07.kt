import java.util.UUID
import kotlin.Exception

private fun calculateDirSizes(lines: List<String>): Map<String, Int> {
  val currentDirSizes = mutableMapOf<String, Int>()
  val currentPath = ArrayDeque<String>()
  var calculatingSize = false

  for (line in lines) {
    if (calculatingSize) {
      when {
        line.startsWith("$") -> {}
        line.startsWith("dir") -> continue
        else -> {
          val size = line.split(" ").first().toInt()
          updateDirSizes(size, currentPath, currentDirSizes)
          continue
        }
      }
    }
    calculatingSize = processCommand(line, currentPath, currentDirSizes)
  }

  return currentDirSizes
}

private fun updateDirSizes(size: Int, currentPath: ArrayDeque<String>, currentDirSizes: MutableMap<String, Int>) =
  currentPath.forEach {
    currentDirSizes[it] = currentDirSizes[it]!! + size
  }

private fun processCommand(
  line: String,
  currentPath: ArrayDeque<String>,
  currentDirSizes: MutableMap<String, Int>,
) = when (line) {
  "$ ls" -> true
  "$ cd .." -> {
    currentPath.removeLast()
    false
  }

  else -> {
    var dirName = line.substringAfterLast(' ')
    if (currentDirSizes.containsKey(dirName)) {
      dirName += UUID.randomUUID()
    }
    currentDirSizes[dirName] = 0
    currentPath.addLast(dirName)
    false
  }
}

fun main() {
  fun part1(input: List<String>): Int {
    val dirSizes = calculateDirSizes(input)
    return dirSizes.values.filter { it <= 100000 }.sum()
  }

  fun part2(input: List<String>): Int {
    val dirSizes = calculateDirSizes(input)
    val totalAvailableSpace = 70000000
    val unusedSpaceNeeded = 30000000
    val totalUsedSpace = dirSizes["/"] ?: throw Exception("Could not calculate total used space")
    val totalFreeSpace = totalAvailableSpace - totalUsedSpace
    val missingFreeSpace = unusedSpaceNeeded - totalFreeSpace
    return dirSizes.values.filter { it >= missingFreeSpace }.min()
  }

  val input = readInput("Day07")
  part1(input).println()
  part2(input).println()
}

