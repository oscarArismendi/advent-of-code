package com.aoc.utils.geometry

import kotlin.math.sqrt

data class Vector2D(val x: Double, val y: Double) {

    fun length(): Double =
        sqrt(x * x + y * y)

    fun normalized(): Vector2D {
        val len = length()
        require(len != 0.0) { "Cannot normalize zero vector" }
        return Vector2D(x / len, y / len)
    }

    operator fun plus(other: Vector2D) =
        Vector2D(x + other.x, y + other.y)

    operator fun minus(other: Vector2D) =
        Vector2D(x - other.x, y - other.y)

    operator fun times(scalar: Double) =
        Vector2D(x * scalar, y * scalar)
}