import utils.readInputLines

/** [https://adventofcode.com/2021/day/7] */
class Files : AdventOfCodeTask {
    sealed class Node {
        lateinit var parent: Directory
        abstract val totalSize: Int
    }

    data class File(val size: Int) : Node() {
        override val totalSize: Int = size
    }

    data class Directory(val name: String, val children: MutableSet<Node> = mutableSetOf()) : Node() {
        override val totalSize: Int
            get() = children.sumOf(Node::totalSize)
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
                val child = if (meta == "dir") Directory(name) else File(meta.toInt())
                current.children.add(child.apply { parent = current })
            }
        }

        val predicate = { size: Int ->
            if (part2) size >= 30000000 - (70000000 - root.totalSize) else size <= 100000
        }

        fun traverse(current: Directory = root, selected: MutableSet<Directory> = mutableSetOf()): Set<Directory> {
            if (predicate(current.totalSize)) {
                selected.add(current)
            }
            current.children.filterIsInstance<Directory>().forEach { traverse(it, selected) }
            return selected
        }

        return traverse().map(Node::totalSize).let { if (part2) it.min() else it.sum() }
    }
}

fun main() {
    print(Files().run(part2 = true))
}
