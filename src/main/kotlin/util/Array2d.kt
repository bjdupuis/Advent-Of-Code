package util


class Array2d<T>(val width: Int, val height: Int, private val default: T?) {
    constructor (width: Int, height: Int, initializer: (Int,Int) -> T?) : this(width, height, null) {
        for (x in 0 until width) {
            for (y in 0 until height) {
                storage[y][x] = initializer.invoke(x, y)
            }
        }
    }

    private val storage = Array<Array<Any?>>(height) {
        Array(width) { default }
    }

    fun getRow(y: Int): Array<T> =
        storage[y] as Array<T>


     fun getColumn(x: Int): Array<T> {
         return Array(storage.size) {storage[it][x]} as Array<T>
     }

    operator fun <T> get(point: Point2d): T? {
        return storage[point.y][point.x] as T?
    }

    operator fun <T> set(point: Point2d, value: T?) {
        storage[point.y][point.x] = value
    }
}

class CharArray2d(val width: Int, val height: Int, private val default: Char): Cloneable {

    constructor(stringList: List<String>) : this(stringList.maxBy { it.length }.length, stringList.size, ' ') {
        stringList.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                storage[y][x] = c
            }
        }
    }

    public override fun clone(): Any {
        val clone = CharArray2d(width, height, default)
        storage.forEachIndexed { index, chars ->
            clone.storage[index] = CharArray(width) { x -> chars[x] }
        }

        return clone
    }

    private val storage = Array(height) {
        CharArray(width) { default }
    }

    fun getRow(y: Int): CharArray =
        storage[y]


    fun getColumn(x: Int): CharArray {
        return CharArray(storage.size) { storage[it][x] }
    }

    operator fun get(point: Point2d): Char {
        return storage[point.y][point.x]
    }

    operator fun set(point: Point2d, value: Char) {
        storage[point.y][point.x] = value
    }

    fun findFirst(value: Char): Point2d? {
        iterator().forEach {
            if (this[it] == value) {
                return it
            }
        }
        return null
    }

    fun find(value: Char): Sequence<Point2d> = sequence {
        iterator().forEach {
            if (this@CharArray2d[it] == value) {
                yield(it)
            }
        }
    }

    fun iterator(): Iterator<Point2d> = object : Iterator<Point2d> {
        var currentRow = 0
        var currentColumn = 0
        override fun hasNext() =
            currentRow < height && currentColumn < width

        override fun next(): Point2d {
            val point = Point2d(currentColumn, currentRow)
            if (currentColumn == width - 1) {
                currentColumn = 0
                currentRow++
            } else {
                currentColumn++
            }
            return point
        }

    }

    val rowIndices: IntRange = (0 until height)
    val columnIndices: IntRange = (0 until width)

    val maxColumnIndex = width - 1
    val maxRowIndex = height - 1

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharArray2d

        if (width != other.width) return false
        if (height != other.height) return false
        return storage.contentDeepEquals(other.storage)
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + storage.contentDeepHashCode()
        return result
    }

    fun print() {
        for (y in rowIndices) {
            val row = getRow(y)
            for (x in row.indices) {
                print(row.get(x))
            }
            println()
        }
    }


}