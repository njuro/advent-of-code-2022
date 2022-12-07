import utils.readInputLines

/** [https://adventofcode.com/2021/day/7] */
class Files : AdventOfCodeTask {
    sealed class Node(open val name: String) {
        lateinit var parent: Directory

        abstract fun getTotalSize(): Int
    }

    data class File(override val name: String, val size: Int) : Node(name) {
        override fun getTotalSize(): Int = size
    }

    data class Directory(override val name: String, val children: MutableSet<Node> = mutableSetOf()) : Node(name) {
        override fun getTotalSize(): Int = children.sumOf(Node::getTotalSize)
    }

    override fun run(part2: Boolean): Any {
        val root = Directory("/").apply { parent = this }
        var current = root
        readInputLines("7.txt").drop(1).map { it.split(" ") }.forEach { tokens ->
            if (tokens[0] == "$") {
                if (tokens[1] == "cd") {
                    current = if (tokens[2] == "..") {
                        current.parent
                    } else {
                        current.children.filterIsInstance<Directory>().first { it.name == tokens[2] }
                    }
                }
            } else {
                val (meta, name) = tokens
                val child = if (meta == "dir") Directory(name) else File(name, meta.toInt())
                current.children.add(child.apply { parent = current })
            }
        }

        val predicate = { size: Int ->
            if (part2) size >= 30000000 - (70000000 - root.getTotalSize()) else size <= 100000
        }

        fun traverse(current: Directory = root, selected: MutableSet<Directory> = mutableSetOf()): Set<Directory> {
            if (predicate(current.getTotalSize())) {
                selected.add(current)
            }
            current.children.filterIsInstance<Directory>().forEach { traverse(it, selected) }
            return selected
        }

        return traverse().map(Node::getTotalSize).let { if (part2) it.min() else it.sum() }
    }
}

fun main() {
    print(Files().run(part2 = true))
}
