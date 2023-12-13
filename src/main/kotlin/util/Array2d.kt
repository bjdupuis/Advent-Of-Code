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

    constructor(stringList: List<String>) : this(stringList.first().length, stringList.size, ' ') {
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
}