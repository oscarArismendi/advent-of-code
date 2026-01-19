package com.aoc.day12

import java.io.File

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

fun secondPart(lines: MutableList<String>) {
    TODO("Not yet implemented")
}

private const val KEY_INPUT_LINE_LENGTH = 2

fun firstPart(lines: MutableList<String>): Int {
    val options = Options()
    var ans = 0
    var index = 0
    while(index < lines.size){
        val line = lines[index]
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
            println(line)
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

fun String.parseDimensionsAndShapeQuantities(): Triple<Int, Int, List<Int>> {
    val inputList = this.trim().split(":")
    val dimensions = inputList[0].split("x").map { it.toInt() }
    val quantities = inputList[1].trim().split(" ").map { it.toInt() }
    return Triple(dimensions[0], dimensions[1], quantities)
}

class Item constructor(
val usedCells: Set<Pair<Int, Int>>
) {
    constructor(rawInput: List<String>): this(
        parseRawInput(rawInput)
    )
    
    fun rotate(degrees: RotationDegrees):  Item {
        val rotatedCells = when(degrees){
            RotationDegrees.NINETY -> usedCells.map{ rotateCoordinate90Degrees(it) }.toSet()
            RotationDegrees.HUNDRED_EIGHTY -> usedCells.map{ rotateCoordinate180Degrees(it) }.toSet()
            RotationDegrees.TWO_HUNDRED_SEVENTY -> usedCells.map{ rotateCoordinate270Degrees(it) }.toSet()
        }.startShapeAtOrigin0()
        
        return Item(rotatedCells)
    }

    private fun rotateCoordinate270Degrees(pair: Pair<Int, Int>): Pair<Int, Int> = 
        Pair(ITEM_WIDTH - 1 - pair.second, pair.first)

    private fun rotateCoordinate180Degrees(pair: Pair<Int, Int>): Pair<Int, Int> =
        Pair(ITEM_WIDTH - 1 - pair.first, ITEM_WIDTH - 1 - pair.second)

    private fun rotateCoordinate90Degrees(pair: Pair<Int, Int>): Pair<Int, Int> =
        Pair(pair.second, ITEM_WIDTH - 1 - pair.first)

    enum class RotationDegrees {//We rotate clockwise
        NINETY,
        HUNDRED_EIGHTY,
        TWO_HUNDRED_SEVENTY
    }
    
    companion object {
        private const val MARKER_SYMBOL = '#'
        private const val ITEM_WIDTH = 3

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
    
    private fun Set<Pair<Int, Int>>.startShapeAtOrigin0():  Set<Pair<Int, Int>> {
        val minRow = this.minOf { it.first }
        val minCol = this.minOf { it.second }
        return this.map { (r,c) -> Pair(r - minRow, c - minCol) }.toSet()
    }
    
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

    fun flipHorizontal(): Item {
        val flipped = usedCells.map { (r, c) ->
            Pair(r, ITEM_WIDTH - 1 - c)
        }.toSet().startShapeAtOrigin0()

        return Item(flipped)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false
        return usedCells == other.usedCells
    }

    override fun hashCode(): Int {
        return usedCells.hashCode()
    }
}

class Options(){
    private val itemsList: MutableMap<Int, Item> = mutableMapOf()

    fun addItem(key: Int, item: Item) {
        itemsList[key] = item
    }

    fun getItem(key: Int): Item? = itemsList[key]
}

data class Placement(
    val coveredCells: Set<Pair<Int, Int>>,
    val shapeId: Int, // Which shape type (0-5)
    val instanceId: Int         // Which copy (0, 1, 2, etc.)
    
){
    fun getShapeColumnName(): String{
        return "shape_${shapeId}_${instanceId}"
    }
}

fun generatePlacements(
    item: Item,
    regionHeight: Int,
    regionWidth: Int,
    shapeIndex: Int
): List<Placement> {

    val placements = mutableListOf<Placement>()

    for (orientation in item.allOrientations()) {

        val maxRow = orientation.usedCells.maxOf { it.first }
        val maxCol = orientation.usedCells.maxOf { it.second }
        for (r in 0 .. regionHeight - maxRow - 1) { // e.g {0,1,2} and height = 3 would result in 3 - 2 - 1
            for (c in 0 .. regionWidth - maxCol - 1) {

                val translatedCells = orientation.usedCells.map { (x, y) ->
                    Pair(x + r, y + c)
                }.toSet()

                placements.add(Placement(
                    translatedCells,
                    shapeIndex,
                    0 // placeholder, will be set properly in the firstPare when we know how many instances we need
                    )
                )
            }
        }
    }
    return placements
}

open class DancingNode(
    var left: DancingNode? = null,
    var right: DancingNode? = null,
    var up: DancingNode? = null,
    var down: DancingNode? = null,
    var column: ColumnNode? = null,
    val placementIndex: Int = -1  // Which placement this row represents, in other words, is the index in the allPlacements list
)

class ColumnNode(
    val name: String,
    val isPrimary: Boolean = true,  // Primary columns MUST be covered
    var size: Int = 0
) : DancingNode()

class DancingLinks(
    private val placements: List<Placement>,  // Store placements to retrieve solution
) {
    /*
    * the header named "header" is a special dummy column that serves as the entry point to the circular column list.
        header ↔ shape_4_0 ↔ shape_4_1 ↔ cell_0_0 ↔ cell_0_1 ↔ ... ↔ header (circular)
    * */
    private val header = ColumnNode("header", false)
    
    private val solution = mutableListOf<Int>()  // Indices of chosen placements
    init {
        header.left = header
        header.right = header
        header.up = header
        header.down = header
    }

    // Build the entire matrix structure
    fun buildMatrix(
        regionWidth: Int,
        regionHeight: Int,
        allPlacements: List<Placement>,
        shapeInstanceColumns: List<String>  // e.g., ["shape4_0", "shape4_1"]
    ) {
        val columns = mutableMapOf<String, ColumnNode>()

        // Create PRIMARY columns for shape instances
        for (shapeName in shapeInstanceColumns) {
            val col = ColumnNode(shapeName, isPrimary = true)
            columns[shapeName] = col
            appendColumnHeader(col)
        }

        // Create SECONDARY columns for grid cells
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

            // Add node for shape instance column
            val shapeColName = placement.getShapeColumnName()
            val shapeCol = columns[shapeColName] // shapeCol is the ColumnNode object that represents the entire shape_X_X column
            if (shapeCol == null) {
                println("ERROR: Column not found: $shapeColName")
                continue
            }
            val shapeNode = DancingNode(column = shapeCol, placementIndex = placementIdx)
            rowNodes.add(shapeNode)
            appendNodeToColumn(shapeNode, shapeCol)
            
            // Add nodes for each covered cell
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
            for (i in rowNodes.indices) {
                val prevIndex = (i - 1 + rowNodes.size) % rowNodes.size
                val nextIndex = (i + 1) % rowNodes.size
                rowNodes[i].left = rowNodes[prevIndex]
                rowNodes[i].right = rowNodes[nextIndex]
            }
        }
    }

    private fun appendColumnHeader(col: ColumnNode) {
        // Initialize vertical pointers (column points to itself when empty)
        col.up = col
        col.down = col
        
        col.left = header.left
        col.right = header
        header.left?.right = col
        header.left = col
    }

    private fun appendNodeToColumn(node: DancingNode, col: ColumnNode) {
        if (col.up == null) {
            println("ERROR: col.up is null for column ${col.name}")
            col.up = col  // Fix it
        }
        if (col.down == null) {
            println("ERROR: col.down is null for column ${col.name}")
            col.down = col  // Fix it
        }
        node.up = col.up
        node.down = col
        col.up?.down = node
        col.up = node
        col.size++
    }

    // Cover a column (remove it and all conflicting rows)
    private fun cover(col: ColumnNode) {
        // Remove the column header from the header list
        col.right?.left = col.left
        col.left?.right = col.right

        // Remove all rows that use this column
        var row = col.down
        while (row != null && row != col) {
            var node = row.right
            while (node != null && node != row) {
                node.down?.up = node.up
                node.up?.down = node.down
                node.column?.size = node.column?.size?.minus(1) ?: 0
                node = node.right
            }
            row = row.down
        }
    }

    // Uncover a column (restore it exactly as before)
    private fun uncover(col: ColumnNode) {
        // Restore all rows (in reverse order)
        var row = col.up
        while (row != null && row != col) {
            var node = row.left
            while (node != null && node != row) {
                node.column?.size = node.column?.size?.plus(1) ?: 0
                node.down?.up = node
                node.up?.down = node
                node = node.left
            }
            row = row.up
        }

        // Restore column header
        col.right?.left = col
        col.left?.right = col
    }

    // Algorithm X - the main solving algorithm
    fun solve(depth: Int = 0): Boolean {
        if (depth > 1000) {
            println("ERROR: Recursion depth exceeded 1000!")
            return false
        }

        if (depth % 100 == 0 && depth > 0) {
            println("Depth: $depth")
            if(depth == 200) return false
        }

        // Check if all PRIMARY columns are covered
        var col = header.right
        while (col != header && col is ColumnNode && !col.isPrimary) {
            col = col.right
        }

        if (col == header) {
            return true  // Success! All primary columns covered
        }

        // Check if any primary column has size 0 (impossible to satisfy)
        var checkCol = header.right
        while (checkCol != header && checkCol is ColumnNode) {
            if (checkCol.isPrimary && checkCol.size == 0) {
                return false  // Impossible!
            }
            checkCol = checkCol.right
        }


        // Choose the primary column with minimum size (optimization)
        val chosenCol = choosePrimaryColumn() ?: return false
        cover(chosenCol)

        // Try each row in this column
        var row = chosenCol.down
        while (row != chosenCol) {
            solution.add(row?.placementIndex ?: -1)

            // Cover all other columns in this row
            var node = row?.right
            while (node != row) {
                node?.column?.let { cover(it) }
                node = node?.right
            }

            // Recurse
            if (solve(depth + 1)) {
                return true
            }

            // Backtrack - uncover columns
            solution.removeAt(solution.size - 1)
            node = row?.left
            while (node != row) {
                node?.column?.let { uncover(it) }
                node = node?.left
            }

            row = row?.down
        }

        uncover(chosenCol)
        return false
    }

    private fun choosePrimaryColumn(): ColumnNode? {
        var minSize = Int.MAX_VALUE
        var chosen: ColumnNode? = null

        var col = header.right
        while (col != header && col is ColumnNode) {
            if (col.isPrimary) {
                if (col.size == 0) return null  // No options = fail immediately
                if (col.size == 1) return col   // Only 1 choice = must take it
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