private fun Int.visibleInRow(row: IntArray, currentPosition: Int): Boolean {
  if (currentPosition == 0 || currentPosition == row.size - 1) {
    return true
  }
  val leftVisible = row.slice(0 until currentPosition).none { el -> el >= this }
  val rightVisible = row.slice((currentPosition + 1) until row.size).none { el -> el >= this }
  return leftVisible || rightVisible
}

fun getColumn(matrix: Array<IntArray>, col: Int) = IntArray(matrix.size) { matrix[it][col] }

fun getScenicScore(currentTree: Int, x: Int, y: Int, forest: Array<IntArray>): Int {
  val row = forest[x]

  val column = getColumn(forest, y)
  val leftViewDistance = getNumberOfVisibleTrees(row.slice(0 until y).reversed(), currentTree)
  val rightViewDistance = getNumberOfVisibleTrees(row.slice((y + 1) until row.size), currentTree)

  val topViewDistance = getNumberOfVisibleTrees(column.slice(0 until x).reversed(), currentTree)
  val bottomViewDistance = getNumberOfVisibleTrees(column.slice((x + 1) until row.size), currentTree)
  return leftViewDistance * rightViewDistance * topViewDistance * bottomViewDistance
}

fun getNumberOfVisibleTrees(row: List<Int>, currentTree: Int): Int {
  var num = 0
  for (tree in row) {
    if (tree < currentTree) {
      num++
    } else {
      num++
      break
    }
  }
  return num
}

fun main() {
  fun part1(input: List<String>): Int {
    val forest = input.map { it.map(Char::digitToInt).toIntArray() }.toTypedArray()
    val size = forest.size
    var numberOfVisibleTrees = 0
    var x = 0
    while (x < size) {
      var y = 0
      while (y < size) {
        val currentTree = forest[x][y]
        if (currentTree.visibleInRow(forest[x], y) || currentTree.visibleInRow(getColumn(forest, y), x)) {
          numberOfVisibleTrees++
        }
        y++
      }
      x++
    }
    return numberOfVisibleTrees
  }


  fun part2(input: List<String>): Int {
    val forest = input.map { it.map(Char::digitToInt).toIntArray() }.toTypedArray()
    val size = forest.size
    var x = 0
    val scenicScores = mutableListOf<Int>()
    while (x < size) {
      var y = 0
      while (y < size) {
        val currentTree = forest[x][y]
        scenicScores += getScenicScore(currentTree, x, y, forest)
        y++
      }
      x++
    }
    return scenicScores.max()
  }

  val input = readInput("Day08")
  part1(input).println()
  part2(input).println()
}

