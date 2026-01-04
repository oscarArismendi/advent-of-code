package day11

import com.aoc.day11.countOutPaths
import com.aoc.day11.toAdjacencyList
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private const val EASY_INPUT_ANSWER = 5

class Solution11Test {

    private val inputEasyFile = File("src/main/kotlin/day11/inputEasy.txt")

    private val inputEasyLines = inputEasyFile.readLines().toMutableList()
    
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
}