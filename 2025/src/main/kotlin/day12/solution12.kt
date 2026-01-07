package com.aoc.day12

import java.io.File

fun main(){
    val fileName = "src/main/kotlin/day12/inputEasy.txt"
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

fun firstPart(lines: MutableList<String>) {
    val options = Options()
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

            index += 3
            continue
        }
        if(line.isNotEmpty()){
            // TODO() : Parse width, height and number of shapes
        }
        index++
    }
}

class Item private constructor(
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
}

class Options(){
    private val itemsList: MutableMap<Int, MutableSet<Item>> = mutableMapOf() 

    fun addItem(key: Int, item: Item) {
        // Find the existing set for this key or create a new HashSet if it doesn't exist
        itemsList.getOrPut(key) { hashSetOf() }.add(item)
    }

    fun getOptions(): Map<Int, Set<Item>> {
        return itemsList
    }
}
