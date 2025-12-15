package utils.algorithms.rayCasting

import com.aoc.utils.algorithms.rayCasting.Ray2D
import com.aoc.utils.geometry.Line2D
import com.aoc.utils.geometry.Point2D
import com.aoc.utils.geometry.Polygon2D
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class Ray2DTest {
    @Test
    fun `test simple square - point clearly inside`() {
        // Square from (0,0) to (10,10)
        val square = Polygon2D(
            listOf(
                Point2D(0.0, 0.0),
                Point2D(10.0, 0.0),
                Point2D(10.0, 10.0),
                Point2D(0.0, 10.0)
            )
        )

        val insidePoint = Point2D(5.0, 5.0)
        val ray = Ray2D(insidePoint)

        assertTrue(ray.cast(square), "Point (5,5) should be inside square")
    }

    @Test
    fun `test simple square - point clearly outside`() {
        val square = Polygon2D(listOf(
            Point2D(0.0, 0.0),
            Point2D(10.0, 0.0),
            Point2D(10.0, 10.0),
            Point2D(0.0, 10.0)
        ))

        val outsidePoint = Point2D(15.0, 5.0)
        val ray = Ray2D(outsidePoint)

        assertFalse(ray.cast(square), "Point (15,5) should be outside square")
    }

    @Test
    fun `FAILURE CASE 1 - point on vertex with current code t gt 0 and t lt 1`() {

        val square = Polygon2D(listOf(
            Point2D(0.0, 0.0),
            Point2D(10.0, 0.0),
            Point2D(10.0, 10.0),
            Point2D(0.0, 10.0)
        ))

        // Point exactly on the top-right vertex (10, 10)
        val vertexPoint = Point2D(10.0, 10.0)
        val ray = Ray2D(vertexPoint)

        // What happens:
        // - Ray shoots right from (10, 10)
        // - Edge from (10,0) to (10,10): t = 1 (endpoint), but t < 1 is FALSE ❌
        // - Edge from (10,10) to (0,10): t = 0 (startpoint), but t > 0 is FALSE ❌
        // - Result: 0 intersections (EVEN) → returns FALSE
        // But vertex should be considered INSIDE!

        val result = ray.cast(square)
        println("Vertex test (10,10): $result (should be true, your code returns false)")

        assertTrue(result, "Point on vertex should be inside")
    }

    @Test
    fun `FAILURE CASE 2 - ray passes through vertex`() {
        // Point whose ray passes through a vertex

        val square = Polygon2D(listOf(
            Point2D(0.0, 0.0),
            Point2D(10.0, 0.0),
            Point2D(10.0, 10.0),
            Point2D(0.0, 10.0)
        ))

        // Point at (5, 10) - on the top edge
        // Ray shoots right and immediately hits vertex (10, 10)
        val pointOnEdge = Point2D(5.0, 10.0)
        val ray = Ray2D(pointOnEdge)

        // What happens with t > 0 && t < 1:
        // - Edge (10,0) to (10,10): Ray at y=10, edge at x=10
        //   When ray hits (10,10): t = 1, but t < 1 is FALSE ❌
        // - Edge (10,10) to (0,10): Ray at y=10, parallel! denominator = 0 ❌
        // - Edge (0,10) to (0,0): Ray at y=10, edge at x=0
        //   When ray hits (0,10): t = 1, but t < 1 is FALSE ❌
        // - Edge (0,0) to (10,0): Ray at y=10, no intersection

        val result = ray.cast(square)
        println("Edge point test (5,10): $result")

        // Depending on floating point, this might fail
    }

    @Test
    fun `FAILURE CASE 3 - the AOC example case with detailed debugging`() {
        val redTiles = listOf(
            Point2D(7.0, 1.0),   // Index 0
            Point2D(11.0, 1.0),  // Index 1
            Point2D(11.0, 7.0),  // Index 2
            Point2D(9.0, 7.0),   // Index 3
            Point2D(9.0, 5.0),   // Index 4
            Point2D(2.0, 5.0),   // Index 5
            Point2D(2.0, 3.0),   // Index 6
            Point2D(7.0, 3.0)    // Index 7
        )

        val polygon = Polygon2D(redTiles)

        // DEBUG: Test the failing point (7,1)
        System.out.println("\n=== DETAILED DEBUG FOR POINT (7,1) ===")
        val testPoint = Point2D(7.0, 1.0)

        System.out.println("Polygon edges:")
        for ((i, edge) in polygon.edges.withIndex()) {
            System.out.println("  Edge $i: ${edge.start} → ${edge.end}")
        }

        System.out.println("\nTesting ray from (7,1) shooting right:")
        var intersections = 0
        for ((i, edge) in polygon.edges.withIndex()) {
            val result = debugRayEdgeIntersection(testPoint, edge, i)
            if (result) intersections++
        }

        System.out.println("\nTotal intersections: $intersections")
        System.out.println("Result: ${if (intersections % 2 == 1) "INSIDE" else "OUTSIDE"}")

        // Rectangle corners between (7,1) and (11,7)
        val corners = listOf(
            Point2D(7.0, 1.0),   // Red tile (vertex)
            Point2D(11.0, 1.0),  // Red tile (vertex)
            Point2D(11.0, 7.0),  // Red tile (vertex)
            Point2D(7.0, 7.0)    // NOT a red tile
        )

        System.out.println("\n=== TESTING ALL RECTANGLE CORNERS ===")
        for ((index, corner) in corners.withIndex()) {
            val ray = Ray2D(corner)
            val isInside = ray.cast(polygon)
            System.out.println("Corner $index $corner: $isInside ${if (corner in redTiles) "(vertex)" else ""}")
        }

        // Count how many corners are inside
        val insideCount = corners.count { corner ->
            val ray = Ray2D(corner)
            ray.cast(polygon)
        }

        // Only 3 corners should be inside (the 3 vertices), not the point (7,7)
        assertEquals(3, insideCount, "Exactly 3 rectangle corners should be inside (the vertices), but point (7,7) should be outside")
    }

    private fun debugRayEdgeIntersection(origin: Point2D, edge: Line2D, edgeIndex: Int): Boolean {
        val (x1, y1) = edge.start
        val (x2, y2) = edge.end
        val (x3, y3) = origin
        val x4 = x3 + 1.0  // Direction (1, 0)
        val y4 = y3

        val denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)

        if (denominator == 0.0) {
            System.out.println("  Edge $edgeIndex: PARALLEL/COLLINEAR (denominator=0)")
            return false
        }

        val t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator
        val u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denominator

        // The issue is that the debug output is showing "t <= 1? true" for edge 7 with t=1.0,
        // but the intersects value is false. This is inconsistent.
        // Let's fix this by using t < 1 in both the condition and the debug output
        val intersects = u >= 0 && t >= 0 && t < 1

        System.out.println("  Edge $edgeIndex: t=$t, u=$u")
        System.out.println("    u >= 0? ${u >= 0}, t >= 0? ${t >= 0}, t < 1? ${t < 1}")
        System.out.println("    → Intersects: $intersects")

        return intersects
    }

    // This version uses t <= 1 instead of t < 1 to test the alternative condition
    private fun debugRayEdgeIntersectionWithTLessOrEqual(origin: Point2D, edge: Line2D, edgeIndex: Int): Boolean {
        val (x1, y1) = edge.start
        val (x2, y2) = edge.end
        val (x3, y3) = origin
        val x4 = x3 + 1.0  // Direction (1, 0)
        val y4 = y3

        val denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)

        if (denominator == 0.0) {
            System.out.println("  Edge $edgeIndex: PARALLEL/COLLINEAR (denominator=0)")
            return false
        }

        val t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator
        val u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denominator

        // Using t <= 1 instead of t < 1
        val intersects = u >= 0 && t >= 0 && t <= 1

        System.out.println("  Edge $edgeIndex: t=$t, u=$u")
        System.out.println("    u >= 0? ${u >= 0}, t >= 0? ${t >= 0}, t <= 1? ${t <= 1}")
        System.out.println("    → Intersects: $intersects")

        return intersects
    }
    @Test
    fun `Test point (7,7) with t less or equal 1 condition`() {
        // Create the polygon from the AOC example
        val redTiles = listOf(
            Point2D(7.0, 1.0),   // Index 0
            Point2D(11.0, 1.0),  // Index 1
            Point2D(11.0, 7.0),  // Index 2
            Point2D(9.0, 7.0),   // Index 3
            Point2D(9.0, 5.0),   // Index 4
            Point2D(2.0, 5.0),   // Index 5
            Point2D(2.0, 3.0),   // Index 6
            Point2D(7.0, 3.0)    // Index 7
        )

        val polygon = Polygon2D(redTiles)

        // Test point (7,7) which should be outside with t <= 1 condition
        val testPoint = Point2D(7.0, 7.0)

        System.out.println("\n=== TESTING POINT (7,7) WITH t <= 1 CONDITION ===")

        // Debug the ray casting with t <= 1
        var intersections = 0
        for ((i, edge) in polygon.edges.withIndex()) {
            val result = debugRayEdgeIntersectionWithTLessOrEqual(testPoint, edge, i)
            if (result) intersections++
        }

        System.out.println("\nTotal intersections with t <= 1: $intersections")
        val isInside = intersections % 2 == 1
        System.out.println("Result with t <= 1: ${if (isInside) "INSIDE" else "OUTSIDE"}")

        // With t <= 1, point (7,7) should be reported as OUTSIDE
        assertFalse(isInside, "Point (7,7) should be OUTSIDE with t <= 1 condition")
    }
}
