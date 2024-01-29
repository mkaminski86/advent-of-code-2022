private interface IMonkey {
  val name: String
  fun yell(): Long
}

private class NumberMonkey(override val name: String, val number: Long): IMonkey {
  override fun yell() = number
}

private class OperationMonkey(override val name: String, val leftSideName: String, val rightSideName: String, val operator: Char) : IMonkey {
  lateinit var leftSideMonkey: IMonkey
  lateinit var rightSideMonkey: IMonkey
  override fun yell() = leftSideMonkey.yell() operator rightSideMonkey.yell()

  private infix fun Long.operator(right: Long): Long = when(operator) {
    '+' -> this + right
    '-' -> this - right
    '*' -> this * right
    else -> this / right
  }
}

fun main() {
  val monkeysByName = readInput("Day21").map {
    val monkeyName = it.substringBefore(':')
    return@map if (it.length == 17) {
      OperationMonkey(monkeyName, it.substring(6, 10), it.substringAfterLast(' '), it[11])
    } else {
      NumberMonkey(monkeyName, it.substringAfterLast(' ').toLong())
    }
  }.associateBy { it.name }

  monkeysByName.values.filterIsInstance<OperationMonkey>().forEach {
    it.leftSideMonkey = monkeysByName.getValue(it.leftSideName)
    it.rightSideMonkey =  monkeysByName.getValue(it.rightSideName)
  }
  val root = monkeysByName.getValue("root")

  fun part1() = root.yell()



  part1().println()
}


