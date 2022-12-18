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

    fun getRow(y: Int): Array<T> {
        return storage[y] as Array<T>
    }

    operator fun <T> get(point: Point2d): T? {
        return storage[point.y][point.x] as T?
    }

    operator fun <T> set(point: Point2d, value: T?) {
        storage[point.y][point.x] = value
    }
}