package com.aoc.day12

import java.io.File

/**
 * Constants representing the number of squares used by each shape in different input types.
 * These are used for early validation of puzzle solutions.
 */
val SQUARE_USED_EASY_INPUT = listOf(7,7,7,7,7,7)
val SQUARE_USED_REAL_INPUT = listOf(7,6,7,7,5,7)

/**
 * Main entry point for the program.
 * Reads the input file, determines which part of the puzzle to solve based on user input,
 * and measures execution time.
 */
fun main(){
    val fileName = "src/main/kotlin/day12/input.txt"
    val lines = File(fileName).readLines().toMutableList()
    val mode = readln().trim().toInt() // Read user input to determine which part to run
    val startTime = System.currentTimeMillis()
    val ans = if(mode == 1){
        firstPart(lines)
    }else{
        secondPart(lines)
    }

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
    println("ans: $ans")
}

/**
 * Implementation for the second part of the puzzle.
 * Currently not implemented.
 */
fun secondPart(lines: MutableList<String>) {
    TODO("Not yet implemented")
}

/**
 * Constant representing the length of a key input line in the input file.
 */
private const val KEY_INPUT_LINE_LENGTH = 2

/**
 * Solves the first part of the puzzle.
 * 
 * This function processes the input lines to:
 * 1. Parse shape definitions
 * 2. Parse puzzle dimensions and required shape quantities
 * 3. Apply early optimization checks to skip impossible configurations
 * 4. Generate all possible placements for each shape
 * 5. Use Dancing Links algorithm to find exact cover solutions
 * 
 * @param lines The input lines from the puzzle file
 * @return The number of valid solutions found
 */
fun firstPart(lines: MutableList<String>): Int {
    val options = Options()
    var ans = 0
    var index = 0
    while(index < lines.size){
        val line = lines[index]
        // Parse shape definitions (key and shape pattern)
        if(line.length == KEY_INPUT_LINE_LENGTH){
            val key: Int = lines[index][0] - '0'
            val rawItem = mutableListOf(
                lines[index+1],
                lines[index+2],
                lines[index+3],
            )

            options.addItem(key , Item(rawItem))

            index += 4
            continue
        }
        if(line.isNotEmpty()){
            val (width,height,quantities) = line.parseDimensionsAndShapeQuantities()
            // Early optimization: Check if the puzzle is solvable based on area requirements
            println(line)
            var figuresIndex = 0
            // Calculate total area needed by all shapes
            val totalSquaresUsed = quantities.map{
                it * SQUARE_USED_REAL_INPUT[figuresIndex++]
            }.toList().sum()
            // If shapes require more area than available, skip this puzzle
            if(totalSquaresUsed > width * height){
                index++
                continue
            }
            val totalFigures = quantities.sum()
            // Another optimization: If the grid is large enough to fit all shapes without constraints
            val spaceNeededIfWeTheFiguresWereAFullSquare = totalFigures * 3
            if(width >= spaceNeededIfWeTheFiguresWereAFullSquare && height >= spaceNeededIfWeTheFiguresWereAFullSquare){
                ans++
                index++
                continue
            }
            // End of the early optimization checks

            val allPlacements = mutableListOf<Placement>()
            val shapeInstanceColumns = mutableListOf<String>()

            // Generate placements for each required shape
            for ((shapeIndex, count) in quantities.withIndex()) {
                if (count == 0) continue

                val item = options.getItem(shapeIndex) ?: continue

                // For each required copy of this shape
                for (instanceId in 0 until count) {
                    shapeInstanceColumns.add("shape_${shapeIndex}_${instanceId}")
                    // Generate all possible placements for this instance
                    val itemPlacements = generatePlacements(item, height, width, shapeIndex)

                    // Tag each placement with which instance it represents
                    for (basePlacement in itemPlacements) {
                        allPlacements.add(
                            Placement(
                                basePlacement.coveredCells,
                                shapeIndex,
                                instanceId  
                            )
                        )
                    }
                }
            }

            println("It'll enter the dancing link algorithm")
            val dlx = DancingLinks(allPlacements)
            dlx.buildMatrix(width, height, allPlacements, shapeInstanceColumns)
            if(dlx.solve()){ 
                ans++
            }
        }
        index++
    }
    return ans
}

/**
 * Extension function to parse a string containing puzzle dimensions and shape quantities.
 * Format expected: "WxH: Q1 Q2 Q3..." where W=width, H=height, Q=quantity of each shape type
 * 
 * @return Triple containing width, height, and a list of shape quantities
 */
fun String.parseDimensionsAndShapeQuantities(): Triple<Int, Int, List<Int>> {
    val inputList = this.trim().split(":")
    val dimensions = inputList[0].split("x").map { it.toInt() }
    val quantities = inputList[1].trim().split(" ").map { it.toInt() }
    return Triple(dimensions[0], dimensions[1], quantities)
}

/**
 * Represents a puzzle shape (tetromino-like piece) that can be placed on the grid.
 * Each shape is defined by a set of cells it occupies, and can be rotated and flipped.
 * 
 * @property usedCells Set of coordinate pairs representing the cells occupied by this shape
 */
class Item constructor(
val usedCells: Set<Pair<Int, Int>>
) {
    /**
     * Secondary constructor that parses a raw string representation of a shape.
     * 
     * @param rawInput List of strings representing the shape, where '#' marks occupied cells
     */
    constructor(rawInput: List<String>): this(
        parseRawInput(rawInput)
    )

    /**
     * Rotates the shape by the specified degrees clockwise.
     * 
     * @param degrees The rotation angle (90, 180, or 270 degrees)
     * @return A new Item with the rotated shape
     */
    fun rotate(degrees: RotationDegrees): Item {
        val rotatedCells = when(degrees){
            RotationDegrees.NINETY -> usedCells.map{ rotateCoordinate90Degrees(it) }.toSet()
            RotationDegrees.HUNDRED_EIGHTY -> usedCells.map{ rotateCoordinate180Degrees(it) }.toSet()
            RotationDegrees.TWO_HUNDRED_SEVENTY -> usedCells.map{ rotateCoordinate270Degrees(it) }.toSet()
        }.startShapeAtOrigin0()

        return Item(rotatedCells)
    }

    /**
     * Rotates a coordinate 270 degrees clockwise around the origin.
     */
    private fun rotateCoordinate270Degrees(pair: Pair<Int, Int>): Pair<Int, Int> = 
        Pair(ITEM_WIDTH - 1 - pair.second, pair.first)

    /**
     * Rotates a coordinate 180 degrees around the origin.
     */
    private fun rotateCoordinate180Degrees(pair: Pair<Int, Int>): Pair<Int, Int> =
        Pair(ITEM_WIDTH - 1 - pair.first, ITEM_WIDTH - 1 - pair.second)

    /**
     * Rotates a coordinate 90 degrees clockwise around the origin.
     */
    private fun rotateCoordinate90Degrees(pair: Pair<Int, Int>): Pair<Int, Int> =
        Pair(pair.second, ITEM_WIDTH - 1 - pair.first)

    /**
     * Enumeration of possible rotation angles (clockwise).
     */
    enum class RotationDegrees {
        NINETY,
        HUNDRED_EIGHTY,
        TWO_HUNDRED_SEVENTY
    }

    companion object {
        private const val MARKER_SYMBOL = '#'
        private const val ITEM_WIDTH = 3

        /**
         * Parses a raw string representation of a shape into a set of coordinates.
         * 
         * @param rawInput List of strings where '#' represents an occupied cell
         * @return Set of coordinate pairs for occupied cells
         */
        private fun parseRawInput(rawInput: List<String>): Set<Pair<Int, Int>> {
            val coordinates = mutableSetOf<Pair<Int, Int>>()
            for (r in rawInput.indices) {
                for (c in rawInput[r].indices) {
                    if (rawInput[r][c] == MARKER_SYMBOL) {
                        coordinates.add(Pair(r, c))
                    }
                }
            }
            return coordinates
        }
    }

    /**
     * Extension function that normalizes a set of coordinates to start at (0,0).
     * This ensures that shapes with the same pattern but different positions are considered equal.
     * 
     * @return A new set of coordinates normalized to start at the origin
     */
    private fun Set<Pair<Int, Int>>.startShapeAtOrigin0(): Set<Pair<Int, Int>> {
        val minRow = this.minOf { it.first }
        val minCol = this.minOf { it.second }
        return this.map { (r,c) -> Pair(r - minRow, c - minCol) }.toSet()
    }

    /**
     * Generates all possible orientations of this shape (rotations and reflections).
     * This includes the original shape, its 90/180/270 degree rotations,
     * and the same rotations of the horizontally flipped shape.
     * 
     * @return Set of all unique orientations of this shape
     */
    fun allOrientations(): Set<Item>{
        val itemFlipped = this.flipHorizontal()
        val orientations = mutableSetOf(
            this,
            this.rotate(RotationDegrees.NINETY),
            this.rotate(RotationDegrees.HUNDRED_EIGHTY),
            this.rotate(RotationDegrees.TWO_HUNDRED_SEVENTY),
            itemFlipped,
            itemFlipped.rotate(RotationDegrees.NINETY),
            itemFlipped.rotate(RotationDegrees.HUNDRED_EIGHTY),
            itemFlipped.rotate(RotationDegrees.TWO_HUNDRED_SEVENTY)
        )
        return orientations.toSet()
    }

    /**
     * Creates a horizontally flipped version of this shape.
     * 
     * @return A new Item with the shape flipped horizontally
     */
    fun flipHorizontal(): Item {
        val flipped = usedCells.map { (r, c) ->
            Pair(r, ITEM_WIDTH - 1 - c)
        }.toSet().startShapeAtOrigin0()

        return Item(flipped)
    }

    /**
     * Compares this Item with another object for equality.
     * Two Items are equal if they have the same set of used cells.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false
        return usedCells == other.usedCells
    }

    /**
     * Generates a hash code for this Item based on its used cells.
     */
    override fun hashCode(): Int {
        return usedCells.hashCode()
    }
}

/**
 * Container class for storing and retrieving shape items by their key.
 * Acts as a registry of available shape types for the puzzle.
 */
class Options(){
    private val itemsList: MutableMap<Int, Item> = mutableMapOf()

    /**
     * Adds a shape item to the options registry.
     * 
     * @param key The identifier for this shape type
     * @param item The shape item to store
     */
    fun addItem(key: Int, item: Item) {
        itemsList[key] = item
    }

    /**
     * Retrieves a shape item by its key.
     * 
     * @param key The identifier of the shape to retrieve
     * @return The Item if found, null otherwise
     */
    fun getItem(key: Int): Item? = itemsList[key]
}

/**
 * Represents a specific placement of a shape on the grid.
 * Contains information about which cells are covered, which shape type it is,
 * and which instance of that shape type it represents.
 * 
 * @property coveredCells The set of grid coordinates covered by this placement
 * @property shapeId The type of shape (0-5)
 * @property instanceId Which copy of this shape type (0, 1, 2, etc.)
 */
data class Placement(
    val coveredCells: Set<Pair<Int, Int>>,
    val shapeId: Int,
    val instanceId: Int
){
    /**
     * Generates a unique column name for this shape instance in the Dancing Links matrix.
     * 
     * @return A string in the format "shape_{shapeId}_{instanceId}"
     */
    fun getShapeColumnName(): String{
        return "shape_${shapeId}_${instanceId}"
    }
}

/**
 * Generates all possible placements of a shape within the given region dimensions.
 * For each orientation of the shape (rotations and reflections), this function
 * calculates all valid positions where the shape can be placed on the grid.
 * 
 * @param item The shape to place
 * @param regionHeight The height of the grid
 * @param regionWidth The width of the grid
 * @param shapeIndex The type identifier of the shape
 * @return List of all possible placements for this shape
 */
fun generatePlacements(
    item: Item,
    regionHeight: Int,
    regionWidth: Int,
    shapeIndex: Int
): List<Placement> {

    val placements = mutableListOf<Placement>()

    // Try each possible orientation of the shape
    for (orientation in item.allOrientations()) {

        // Find the dimensions of this orientation
        val maxRow = orientation.usedCells.maxOf { it.first }
        val maxCol = orientation.usedCells.maxOf { it.second }

        // Try each possible position on the grid
        for (r in 0 .. regionHeight - maxRow - 1) { // Calculate valid row positions
            for (c in 0 .. regionWidth - maxCol - 1) { // Calculate valid column positions

                // Translate the shape to this position
                val translatedCells = orientation.usedCells.map { (x, y) ->
                    Pair(x + r, y + c)
                }.toSet()

                // Create a placement with this shape at this position
                placements.add(Placement(
                    translatedCells,
                    shapeIndex,
                    0 // placeholder, will be set properly in firstPart when we know how many instances we need
                    )
                )
            }
        }
    }
    return placements
}

/**
 * Base node class for the Dancing Links algorithm.
 * Each node is part of a doubly-linked list in both horizontal and vertical directions,
 * forming a sparse matrix representation.
 * 
 * @property left Reference to the node to the left in the row
 * @property right Reference to the node to the right in the row
 * @property up Reference to the node above in the column
 * @property down Reference to the node below in the column
 * @property column The column header this node belongs to
 * @property placementIndex Index of the placement this node represents (-1 for column headers)
 */
open class DancingNode(
    var left: DancingNode? = null,
    var right: DancingNode? = null,
    var up: DancingNode? = null,
    var down: DancingNode? = null,
    var column: ColumnNode? = null,
    val placementIndex: Int = -1  // Which placement this row represents, in other words, is the index in the allPlacements list
)

/**
 * Special node that serves as a column header in the Dancing Links matrix.
 * Each column represents either a shape instance that must be used (primary)
 * or a grid cell that may be covered (secondary).
 * 
 * @property name Identifier for this column (e.g., "shape_0_1" or "cell_2_3")
 * @property isPrimary Whether this column must be covered in the solution
 * @property size Number of nodes in this column (excluding the header)
 */
class ColumnNode(
    val name: String,
    val isPrimary: Boolean = true,  // Primary columns MUST be covered
    var size: Int = 0
) : DancingNode()

/**
 * Implementation of Donald Knuth's Algorithm X using Dancing Links (DLX).
 * This algorithm efficiently solves exact cover problems, which is what our
 * shape placement puzzle reduces to.
 * 
 * The matrix is structured as follows:
 * - Each row represents a possible placement of a shape
 * - Primary columns represent shape instances that must be used
 * - Secondary columns represent grid cells that may be covered
 * - A 1 in the matrix means that placement covers that cell or uses that shape
 * 
 * @property placements List of all possible shape placements
 */
class DancingLinks(
    private val placements: List<Placement>,  // Store placements to retrieve solution
) {
    /**
     * The header is a special dummy column that serves as the entry point to the circular column list.
     * The column structure forms a circular doubly-linked list:
     * header ↔ shape_4_0 ↔ shape_4_1 ↔ cell_0_0 ↔ cell_0_1 ↔ ... ↔ header (circular)
     */
    private val header = ColumnNode("header", false)

    /**
     * Stores the indices of placements chosen for the solution
     */
    private val solution = mutableListOf<Int>()  // Indices of chosen placements

    /**
     * Initialize the header node to point to itself, creating an empty circular list
     */
    init {
        header.left = header
        header.right = header
        header.up = header
        header.down = header
    }

    /**
     * Builds the entire Dancing Links matrix structure.
     * This creates the sparse matrix representation where:
     * - Columns represent constraints (shape instances and grid cells)
     * - Rows represent possible placements
     * - A node at the intersection means that placement satisfies that constraint
     * 
     * @param regionWidth Width of the puzzle grid
     * @param regionHeight Height of the puzzle grid
     * @param allPlacements List of all possible shape placements
     * @param shapeInstanceColumns List of column names for shape instances (e.g., ["shape_0_0", "shape_1_0"])
     */
    fun buildMatrix(
        regionWidth: Int,
        regionHeight: Int,
        allPlacements: List<Placement>,
        shapeInstanceColumns: List<String>
    ) {
        val columns = mutableMapOf<String, ColumnNode>()

        // Create PRIMARY columns for shape instances (must be covered exactly once)
        for (shapeName in shapeInstanceColumns) {
            val col = ColumnNode(shapeName, isPrimary = true)
            columns[shapeName] = col
            appendColumnHeader(col)
        }

        // Create SECONDARY columns for grid cells (must not be covered more than once)
        for (r in 0 until regionHeight) {
            for (c in 0 until regionWidth) {
                val cellName = "cell_${r}_${c}"
                val col = ColumnNode(cellName, isPrimary = false)
                columns[cellName] = col
                appendColumnHeader(col)
            }
        }

        // Create rows (one per placement)
        for ((placementIdx, placement) in allPlacements.withIndex()) {
            val rowNodes = mutableListOf<DancingNode>()

            // Add node for shape instance column (this placement uses this shape instance)
            val shapeColName = placement.getShapeColumnName()
            val shapeCol = columns[shapeColName]
            if (shapeCol == null) {
                println("ERROR: Column not found: $shapeColName")
                continue
            }
            val shapeNode = DancingNode(column = shapeCol, placementIndex = placementIdx)
            rowNodes.add(shapeNode)
            appendNodeToColumn(shapeNode, shapeCol)

            // Add nodes for each covered cell (this placement covers these cells)
            if (placement.coveredCells.isEmpty()) {
                println("WARNING: Placement $placementIdx has no covered cells!")
            }
            for ((r, c) in placement.coveredCells) {
                val cellName = "cell_${r}_${c}"
                val cellCol = columns[cellName]
                if (cellCol == null) {
                    println("ERROR: Cell column not found: $cellName")
                    continue
                }
                val cellNode = DancingNode(column = cellCol, placementIndex = placementIdx)
                rowNodes.add(cellNode)
                appendNodeToColumn(cellNode, cellCol)
            }

            // Link all nodes in this row horizontally (circular)
            if (rowNodes.size < 2) {
                println("WARNING: Row $placementIdx has only ${rowNodes.size} nodes")
            }
            // Create circular doubly-linked list for this row
            for (i in rowNodes.indices) {
                val prevIndex = (i - 1 + rowNodes.size) % rowNodes.size
                val nextIndex = (i + 1) % rowNodes.size
                rowNodes[i].left = rowNodes[prevIndex]
                rowNodes[i].right = rowNodes[nextIndex]
            }
        }
    }

    /**
     * Appends a column header to the circular linked list of columns.
     * 
     * @param col The column header to append
     */
    private fun appendColumnHeader(col: ColumnNode) {
        // Initialize vertical pointers (column points to itself when empty)
        col.up = col
        col.down = col

        // Insert into the horizontal circular linked list
        col.left = header.left
        col.right = header
        header.left?.right = col
        header.left = col
    }

    /**
     * Appends a node to a column, maintaining the vertical circular linked list.
     * 
     * @param node The node to append
     * @param col The column to append to
     */
    private fun appendNodeToColumn(node: DancingNode, col: ColumnNode) {
        // Error checking and recovery
        if (col.up == null) {
            println("ERROR: col.up is null for column ${col.name}")
            col.up = col  // Fix it
        }
        if (col.down == null) {
            println("ERROR: col.down is null for column ${col.name}")
            col.down = col  // Fix it
        }

        // Insert at the bottom of the column
        node.up = col.up
        node.down = col
        col.up?.down = node
        col.up = node
        col.size++
    }

    /**
     * Covers a column in the Dancing Links matrix.
     * This removes the column from the header list and all rows that have a 1 in this column.
     * This is a key operation in Knuth's Algorithm X.
     * 
     * @param col The column to cover
     */
    private fun cover(col: ColumnNode) {
        // Remove the column header from the header list
        col.right?.left = col.left
        col.left?.right = col.right

        // Remove all rows that use this column
        var row = col.down
        while (row != null && row != col) {
            var node = row.right
            while (node != null && node != row) {
                // Remove this node from its column
                node.down?.up = node.up
                node.up?.down = node.down
                node.column?.size = node.column?.size?.minus(1) ?: 0
                node = node.right
            }
            row = row.down
        }
    }

    /**
     * Uncovers a column in the Dancing Links matrix.
     * This is the exact inverse of the cover operation, restoring the column
     * and all affected rows to their original state.
     * 
     * @param col The column to uncover
     */
    private fun uncover(col: ColumnNode) {
        // Restore all rows (in reverse order to maintain consistency)
        var row = col.up
        while (row != null && row != col) {
            var node = row.left
            while (node != null && node != row) {
                // Restore this node to its column
                node.column?.size = node.column?.size?.plus(1) ?: 0
                node.down?.up = node
                node.up?.down = node
                node = node.left
            }
            row = row.up
        }

        // Restore column header to the header list
        col.right?.left = col
        col.left?.right = col
    }

    /**
     * Implements Donald Knuth's Algorithm X using Dancing Links.
     * This is a recursive backtracking algorithm that finds an exact cover solution.
     * 
     * @param depth Current recursion depth (for debugging and preventing infinite recursion)
     * @return true if a solution was found, false otherwise
     */
    fun solve(depth: Int = 0): Boolean {
        // Safety check to prevent stack overflow
        if (depth > 1000) {
            println("ERROR: Recursion depth exceeded 1000!")
            return false
        }

        // Debugging output for deep recursion
        if (depth % 100 == 0 && depth > 0) {
            println("Depth: $depth")
        }

        // Check if all PRIMARY columns are covered (solution found)
        var col = header.right
        while (col != header && col is ColumnNode && !col.isPrimary) {
            col = col.right
        }

        if (col == header) {
            return true  // Success! All primary columns covered
        }

        // Early failure detection: if any primary column has size 0, it's impossible to satisfy
        var checkCol = header.right
        while (checkCol != header && checkCol is ColumnNode) {
            if (checkCol.isPrimary && checkCol.size == 0) {
                return false  // Impossible to satisfy this constraint
            }
            checkCol = checkCol.right
        }

        // Choose the primary column with minimum size (optimization to reduce branching)
        val chosenCol = choosePrimaryColumn() ?: return false
        cover(chosenCol)

        // Try each row in this column
        var row = chosenCol.down
        while (row != chosenCol) {
            // Add this placement to our solution
            solution.add(row?.placementIndex ?: -1)

            // Cover all other columns in this row (they're now satisfied)
            var node = row?.right
            while (node != row) {
                node?.column?.let { cover(it) }
                node = node?.right
            }

            // Recursively try to solve the reduced problem
            if (solve(depth + 1)) {
                return true
            }

            // Backtrack - uncover columns and remove from solution
            solution.removeAt(solution.size - 1)
            node = row?.left
            while (node != row) {
                node?.column?.let { uncover(it) }
                node = node?.left
            }

            row = row?.down
        }

        // No solution found with this column, uncover it and return false
        uncover(chosenCol)
        return false
    }

    /**
     * Chooses the primary column with the fewest nodes.
     * This is an optimization that reduces the branching factor of the search.
     * 
     * @return The chosen column, or null if no valid column exists
     */
    private fun choosePrimaryColumn(): ColumnNode? {
        var minSize = Int.MAX_VALUE
        var chosen: ColumnNode? = null

        var col = header.right
        while (col != header && col is ColumnNode) {
            if (col.isPrimary) {
                if (col.size == 0) return null  // No options = fail immediately
                if (col.size == 1) return col   // Only 1 choice = must take it (optimization)
                if (col.size < minSize) {
                    minSize = col.size
                    chosen = col
                }
            }
            col = col.right
        }

        return chosen
    }
}
