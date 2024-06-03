import kotlin.math.pow

fun String.fromSNAFU(): Long {
  val highestPower = length - 1
  return this.foldIndexed(0L) { index, acc, c ->
    val currentPower = highestPower - index
    val value = when (c) {
      '-' -> -1
      '=' -> -2
      else -> c.digitToInt().toLong()
    }
    (acc + value * 5.0.pow(currentPower)).toLong()
  }
}

fun Long.toSNAFU(): String {
  var number = this
  val remainders = mutableListOf<Long>()
  while (number > 0) {
    remainders.add(number % 5)
    number /= 5
  }
  var correction = 0
  val reversedSnafu = mutableListOf<Long>()
  remainders.forEach {
    val currentDigit = it + correction
    correction = 0
    if (currentDigit in longArrayOf(0, 1, 2)) {
      reversedSnafu.add(currentDigit)
    } else {
      reversedSnafu.add(currentDigit - 5)
      correction = 1
    }
  }
  if (correction != 0) reversedSnafu.add(correction.toLong())
  return reversedSnafu.map {
    when (it) {
      -1L -> '-'
      -2L -> '='
      else -> it.toString()
    }
  }.joinToString("").reversed()
}

fun main() {
  val snafus = readInput("Day25")
  snafus.sumOf { it.fromSNAFU() }.toSNAFU().println()
}
