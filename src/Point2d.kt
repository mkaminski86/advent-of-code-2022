data class Point2d(val x: Int = 0, val y: Int = 0) {
  fun getNonDiagonalNeighbours() = setOf(
    copy(x = x - 1),
    copy(x = x + 1),
    copy(y = y - 1),
    copy(y = y + 1)
  )
}
