## Thought process part 1
1. Calculate the area between points
2. Sort the list in descending order (Could be more efficient, but I feel it could be handy for part 2)
## Thought process part 2
1. Don't want to just brute force it by doing a matrix with all the valid green and red tiles and then pass through it to see if the area is valid
## Problems
1. Time complexity
## Solution
1. Ray casting

## Learnings
1. Need to make a course of graphs
2. To solve part two let's begin by learning ray-casting

## Ray Casting Algorithm Issues

### 1. Checking Only Vertices vs. Full Edges
One of the main reasons the algorithm was failing initially was that it only checked if vertices were inside the polygon, not the full edges. This caused problems because:
- Points that lie exactly on an edge of the polygon should be considered "inside" the polygon
- When checking if a rectangle is valid, all points along its edges must be inside the polygon, not just the corners
- In the Day 9 problem, rectangles that appeared valid based on their corners could actually have edges that passed outside the polygon

### 2. The `t < 1` vs `t <= 1` Issue
In the ray casting algorithm's line intersection check:
- The parameter `t` represents how far along the edge the intersection occurs (0 = start point, 1 = end point)
- Using `t <= 1` counts intersections at the end points of edges
- Using `t < 1` excludes intersections at the end points
- The correct implementation uses `t <= 1` because:
  - This ensures that intersections at the end points of edges are properly counted
  - When combined with the boundary check that's performed first, this correctly handles points on vertices and edges

### 3. Other Implementation Challenges
- Floating-point precision: Using an EPSILON value (1e-9) was necessary for reliable comparisons
- Boundary detection: A separate check for points on the boundary (vertices or edges) was needed before the ray casting algorithm
- Memory usage: When checking large rectangles, sampling points along edges rather than checking every point was necessary to avoid OutOfMemoryError
- Edge cases: Special handling was needed for rectangles that were actually lines (when x1 = x2 or y1 = y2)
