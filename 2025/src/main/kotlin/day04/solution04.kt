package com.aoc.day04

import java.io.File

/**
 * Main function that reads input file, processes it based on user selection (mode 1 or 2),
 * and measures execution time.
 */
fun main(){


    val fileName = "src/main/kotlin/day04/input.txt"
    val lines = File(fileName).readLines().toMutableList()
    val mode = readln().trim().toInt() // Read user input to determine which part to run
    val startTime = System.currentTimeMillis()
    if(mode == 1){
        firstPart(lines)
    }else{
        secondPart(lines)
    }

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}

// Threshold value that determines when paper can be taken out
private const val PAPER_THRESHOLD = 4

/**
 * Solves the first part of the problem - counts papers that can be taken out
 * based on heat levels without modifying the grid.
 */
fun firstPart(lines: MutableList<String>): Int{

    val rows = lines.size
    val columns = lines[0].length
    val heatMap = createAHeatMap(rows, columns) // Create heat map with padding
    var ans = 0

    initializeHeatMap(rows, columns, lines, heatMap) // Calculate initial heat values

    ans = getNumberOfPaperTakenOut(rows, columns, heatMap, lines, ans)

    // Print the heat map for visualization
    for(row in heatMap){
        println(row)
    }
    println(ans)
    return ans
}

/**
 * Counts and optionally removes papers that can be taken out based on heat levels.
 * A paper can be taken out if its heat level is below PAPER_THRESHOLD.
 * 
 * @param isRemoveHeatEnabled If true, removes papers and updates heat map accordingly
 * @return Number of papers that were taken out
 */
private fun getNumberOfPaperTakenOut(
    rows: Int,
    columns: Int,
    heatMap: MutableList<MutableList<Int>>,
    lines: MutableList<String>,
    ans: Int,
    isRemoveHeatEnabled: Boolean = false
): Int {
    var ans1 = ans
    val positionsOfPaperToRemove = mutableListOf<Pair<Int, Int>>()

    // Check each position in the grid
    for (i in 0 until rows) {
        for (j in 0 until columns) {
            // +1 offset because heatMap has padding around edges
            if (heatMap[i + 1][j + 1] < PAPER_THRESHOLD && lines[i][j] == '@') {
                ans1++
                if (isRemoveHeatEnabled) {
                    positionsOfPaperToRemove.add(Pair(i, j))
                    // Replace paper with empty space in the grid
                    val originalLine = StringBuilder(lines[i])
                    originalLine.setCharAt(j, '.')
                    lines[i] = originalLine.toString()
                }
            }
        }
    }

    // Update heat map for all removed papers
    for(position in positionsOfPaperToRemove){
        removeHeatAround(heatMap, position.first, position.second)
    }
    return ans1
}

/**
 * Decreases heat level by 1 in all 8 surrounding cells when a paper is removed.
 * 
 * @param row Row index in the original grid
 * @param column Column index in the original grid
 * @return Updated heat map
 */
private fun removeHeatAround(heatMap: MutableList<MutableList<Int>>,
                             row: Int, column: Int): MutableList<MutableList<Int>>{
    val realRow = row + 1     // Adjust for padding
    val realColumn = column + 1
    for(i in realRow-1..realRow+1){
        for(j in realColumn-1..realColumn+1){
            if(i == realRow && j == realColumn) continue  // Skip the center cell
            heatMap[i][j]--   // Decrease heat in surrounding cells
        }
    }
    return heatMap
}

/**
 * Initializes the heat map by calculating heat values for each cell
 * based on the presence of papers in the grid.
 */
private fun initializeHeatMap(
    rows: Int,
    columns: Int,
    lines: List<String>,
    heatMap: MutableList<MutableList<Int>>
) {
    for (i in 0 until rows) {
        for (j in 0 until columns) {
            // Add heat around any non-empty cell
            if (lines[i][j] != '.') addHeatAround(heatMap, i, j)
        }
    }
}

/**
 * Solves the second part of the problem - iteratively removes papers and updates
 * heat levels until no more papers can be taken out.
 */
fun secondPart(lines: MutableList<String>): Long{
    val rows = lines.size
    val columns = lines[0].length
    val heatMap = createAHeatMap(rows, columns)
    var ans = 0L

    initializeHeatMap(rows, columns, lines, heatMap)

    // Iteratively remove papers until no more can be taken out
    while(true){
        val paperTakenOut = getNumberOfPaperTakenOut(
            rows, columns,
            heatMap, lines,
            0, true) // Enable paper removal
        if(paperTakenOut == 0) break // Stop when no more papers can be taken out
        ans += paperTakenOut
    }
    println(ans)
    return ans
}

/**
 * Creates a heat map with padding around the edges to simplify boundary handling.
 * 
 * @param n Number of rows in the original grid
 * @param m Number of columns in the original grid
 * @return A 2D grid with padding (size (m+2) x (n+2)) initialized with zeros
 */
fun createAHeatMap(n: Int, m: Int): MutableList<MutableList<Int>>{
    // Add padding rows and columns for borders to avoid edge checking
    return  MutableList(m+2){
        MutableList(n+2){0}
    }
}

/**
 * Increases heat level by 1 in all 8 surrounding cells of a paper.
 * 
 * @param row Row index in the original grid
 * @param column Column index in the original grid
 * @return Updated heat map
 */
fun addHeatAround(heatMap: MutableList<MutableList<Int>>,
                  row: Int, column: Int): MutableList<MutableList<Int>>{
    val realRow = row + 1     // Adjust for padding
    val realColumn = column + 1
    for(i in realRow-1..realRow+1){
        for(j in realColumn-1..realColumn+1){
            if(i == realRow && j == realColumn) continue  // Skip the center cell
            heatMap[i][j]++   // Increase heat in surrounding cells
        }
    }
    return heatMap
}
