class CycleControl(private var cycleNumber: Int = 0) {
  var accumulatedSignalStrength = 0
    private set

  fun increaseCycle(currentXRegisterValue: Int, monitor: Array<CharArray>) {
    cycleNumber++
    if (shouldMeasureSignalStrength()) {
      accumulatedSignalStrength += currentXRegisterValue * cycleNumber
    }
    drawPixel(currentXRegisterValue, monitor)
  }

  private fun drawPixel(currentXRegisterValue: Int, monitor: Array<CharArray>) {
    val drawnPosition = (cycleNumber - 1) % 40
    val spritePositions = (currentXRegisterValue - 1)..(currentXRegisterValue + 1)
    monitor[(cycleNumber - 1) / 40][drawnPosition] = if (spritePositions.contains(drawnPosition)) '#' else '.'
  }

  private fun shouldMeasureSignalStrength() = cycleNumber == 20 || (cycleNumber - 20) % 40 == 0
}

fun main() {
  val monitor = Array(6) { CharArray(40) }

  fun part1(input: List<String>): Int {
    var x = 1
    val cycleController = CycleControl()
    input.forEach {
      if (it == "noop") {
        cycleController.increaseCycle(x, monitor)
      } else {
        val numberToAdd = it.substringAfter(" ").toInt()
        cycleController.increaseCycle(x, monitor)
        cycleController.increaseCycle(x, monitor)
        x += numberToAdd
      }
    }
    return cycleController.accumulatedSignalStrength
  }


  fun part2() = monitor.forEach {
    it.forEach { c -> print(c) }
    println()
  }

  val input = readInput("Day10")
  part1(input).println()
  part2()
}

