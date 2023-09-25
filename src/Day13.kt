private sealed class Packet : Comparable<Packet> {
  companion object {
    fun of(line: String): Packet =
      of(line.split("""((?<=[\[\],])|(?=[\[\],]))""".toRegex()).filter { it.isNotBlank() }.filter { it != "," }
        .iterator())

    private fun of(tokens: Iterator<String>): Packet {
      val packets = mutableListOf<Packet>()
      while (tokens.hasNext()) {
        when (val symbol = tokens.next()) {
          "]" -> return ListPacket(packets)
          "[" -> packets.add(of(tokens))
          else -> packets.add(IntPacket(symbol.toInt()))
        }
      }
      return ListPacket(packets)
    }
  }
}


private class ListPacket(val subPackets: List<Packet>) : Packet() {

  override fun compareTo(other: Packet): Int = when (other) {
    is IntPacket -> compareTo(other.asList())
    is ListPacket -> subPackets.zip(other.subPackets).map { it.first.compareTo(it.second) }.firstOrNull { it != 0 }
      ?: subPackets.size.compareTo(other.subPackets.size)
  }
}

private class IntPacket(val value: Int) : Packet() {
  fun asList(): Packet = ListPacket(listOf(this))
  override fun compareTo(other: Packet): Int = when (other) {
    is IntPacket -> value.compareTo(other.value)
    is ListPacket -> asList().compareTo(other)
  }
}

fun main() {
  val packets = readInput("Day13").filter { it.isNotBlank() }.map { Packet.of(it) }

  fun part1() = packets.chunked(2).mapIndexed { index, packets ->
    if (packets.first() < packets.last()) index + 1 else 0
  }.sum()


  fun part2(): Int {
    val controlPacket1 = Packet.of("[[2]]")
    val controlPacket2 = Packet.of("[[6]]")
    val sortedPackets = (packets + controlPacket1 + controlPacket2).sorted()
    return (sortedPackets.indexOf(controlPacket1) + 1) * (sortedPackets.indexOf(controlPacket2) + 1)
  }



  part1().println()
  part2().println()
}

