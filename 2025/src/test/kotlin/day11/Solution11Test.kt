package day11

import com.aoc.day11.State
import com.aoc.day11.countOutPaths
import com.aoc.day11.countOutPathsThatContainDacAndFft
import com.aoc.day11.firstPart
import com.aoc.day11.secondPart
import com.aoc.day11.toAdjacencyList
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private const val EASY_INPUT_ANSWER = 5

private const val EASY_INPUT_PART_2_ANSWER = 2L

private const val REAL_PART_2_ANSWER = 294053029111296L

private const val REAL_PART_1_ANSWER = 796

class Solution11Test {

    private val inputEasyFile = File("src/main/kotlin/day11/inputEasy.txt")
    private val inputEasyPart2File = File("src/main/kotlin/day11/inputEasyPart2.txt")
    private val realInputFile = File("src/main/kotlin/day11/input.txt")

    private val inputEasyLines = inputEasyFile.readLines().toMutableList()
    private val inputEasyPart2Lines = inputEasyPart2File.readLines().toMutableList()
    private val realInputLines = realInputFile.readLines().toMutableList()
    
    @Test
    fun `We are able to convert the easy input in an adjacency list`(){
        // given
        // when
        val result = inputEasyLines.toAdjacencyList()
        // then
        val expectedResult = mapOf(
            "aaa" to listOf("you","hhh"),
            "you" to listOf("bbb","ccc"),
            "bbb" to listOf("ddd","eee"),
            "ccc" to listOf("ddd","eee","fff"),
            "ddd" to listOf("ggg"),
            "eee" to listOf("out"),
            "fff" to listOf("out"),
            "ggg" to listOf("out"),
            "hhh" to listOf("ccc","fff","iii"),
            "iii" to listOf("out")
        )

        assertEquals(expectedResult, result)
    }
    
    @Test
    fun `We get the correct answer for the easy input`(){
        // given
        // when
        val adjacencyList = inputEasyLines.toAdjacencyList()
        val result = countOutPaths(adjacencyList)
        // then
        assertEquals(EASY_INPUT_ANSWER, result)
    }
    
    @Test
    fun `We get the correct answer for the easy input in part 2`(){
        // given
        val start = State("svr", false, false)
        // when
        val adjacencyList = inputEasyPart2Lines.toAdjacencyList()
        val result = countOutPathsThatContainDacAndFft(start,adjacencyList,mutableMapOf())
        // then
        assertEquals(EASY_INPUT_PART_2_ANSWER, result)
    }

    @Test
    fun `We get the correct answer for the real input in part 1`(){
        // given
        // when
        val result = firstPart(realInputLines)
        // then
        assertEquals(REAL_PART_1_ANSWER, result)
    }
    
    @Test
    fun `We get the correct answer for the real input in part 2`(){
        // given
        // when
        val result = secondPart(realInputLines)
        // then
        assertEquals(REAL_PART_2_ANSWER, result)
    }    
}