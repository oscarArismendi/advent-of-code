package com.aoc.utils.geometry

data class Point2D(val x: Double, val y: Double){
    operator fun plus(v: Vector2D): Point2D =
        Point2D(this.x + v.x, this.y + v.y)

    operator fun minus(other: Point2D): Vector2D =
        Vector2D(this.x - other.x, this.y - other.y)

    fun toLongPair(): Pair<Long, Long> {
        return Pair(this.x.toLong(), this.y.toLong())
    }
}
