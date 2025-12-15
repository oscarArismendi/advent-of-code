package com.aoc.utils.algorithms.rayCasting

import com.aoc.utils.geometry.Line2D
import com.aoc.utils.geometry.Point2D
import com.aoc.utils.geometry.Polygon2D
import com.aoc.utils.geometry.Vector2D
import kotlin.math.abs

class Ray2D(val origin: Point2D, direction: Vector2D = Vector2D(1.0,0.0)) {
    // Documented on:
    // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line_segment
    // In our case because we only have one line, and we're projecting a point, U only has to be >= 0, 
    // And T has to be between 0 and 1 inclusive
    val direction: Vector2D = direction.normalized()

    fun cast(line: Line2D): Boolean{
        val x1 = line.start.x
        val y1 = line.start.y
        val x2 = line.end.x
        val y2 = line.end.y

        val x3 = origin.x
        val y3 = origin.y
        val (x4, y4) = origin + direction


        val denominator = (x1 - x2) * (y3 - y4) -
                (y1 - y2) * (x3 - x4)

        if (denominator == 0.0) return false // parallel

        val t = ((x1 - x3) * (y3 - y4) -
                (y1 - y3) * (x3 - x4)) / denominator

        val u = -((x1 - x2) * (y1 - y3) -
                (y1 - y2) * (x1 - x3)) / denominator 
        return u >= 0 && t >= 0 && t <= 1
    }

    fun cast(polygon: Polygon2D): Boolean{
        // First check if the point is on the boundary (vertex or edge)
        if (isPointOnBoundary(polygon)) {
            return true
        }

        // If not on boundary, use ray casting algorithm
        var intersections = 0
        for (edge in polygon.edges) {
            if (cast(edge)) {
                intersections++
            }
        }
        return intersections % 2 == 1 // true if the number of intersections is odd
    }

    // Helper method to check if a point is on the boundary of the polygon
    private fun isPointOnBoundary(polygon: Polygon2D): Boolean {
        // Check if point is on any vertex
        if (polygon.vertices.any { vertex -> 
            abs(vertex.x - origin.x) < EPSILON && abs(vertex.y - origin.y) < EPSILON 
        }) {
            return true
        }

        // Check if point is on any edge
        for (edge in polygon.edges) {
            if (isPointOnLineSegment(edge)) {
                return true
            }
        }

        return false
    }

    // Helper method to check if a point is on a line segment
    private fun isPointOnLineSegment(line: Line2D): Boolean {
        // Vector from line start to point
        val v1 = origin - line.start
        // Vector from line start to line end
        val v2 = line.end - line.start

        // Check if vectors are collinear
        val crossProduct = v1.x * v2.y - v1.y * v2.x
        if (abs(crossProduct) > EPSILON) {
            return false // Not collinear
        }

        // Check if point is within the line segment bounds
        val dotProduct = v1.x * v2.x + v1.y * v2.y
        if (dotProduct < 0) {
            return false // Point is before line start
        }

        val squaredLength = v2.x * v2.x + v2.y * v2.y
        if (dotProduct > squaredLength) {
            return false // Point is after line end
        }

        return true // Point is on the line segment
    }

    companion object {
        private const val EPSILON = 1e-9 // Small value for floating-point comparisons
    }
}
