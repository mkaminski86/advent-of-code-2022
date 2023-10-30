data class Point3d(val x: Int, val y: Int, val z: Int) {
  fun neighbours() = setOf(
    copy(x = x - 1),
    copy(x = x + 1),
    copy(y = y - 1),
    copy(y = y + 1),
    copy(z = z - 1),
    copy(z = z + 1)
  )
}

fun main() {
  val cubes =
    readInput("Day18").map { it.split(',').let { (x, y, z) -> Point3d(x.toInt(), y.toInt(), z.toInt()) } }

  fun part1() = cubes.sumOf { point -> 6 - point.neighbours().count { it in cubes } }


  fun part2(): Int = TODO()


  part1().println()
  part2().println()
}


