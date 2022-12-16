package util


class Array2d<T>(val width: Int, val height: Int, private val default: T?) {
    private val storage = Array<Array<Any?>>(height) {
        Array(width) { default }
    }

    operator fun <T> get(point: Point2d): T? {
        return storage[point.y][point.x] as T?
    }

    operator fun <T> set(point: Point2d, value: T?) {
        storage[point.y][point.x] = value
    }
}