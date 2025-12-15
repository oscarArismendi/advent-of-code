package com.aoc.utils.geometry

data class Polygon2D(val vertices: List<Point2D>) {
    val edges: List<Line2D> = vertices.indices.map { current ->
        val next = (current + 1) % vertices.size // size % size = 0
        Line2D(vertices[current], vertices[next])
    }
}