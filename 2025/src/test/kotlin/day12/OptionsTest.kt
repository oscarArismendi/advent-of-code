package day12

import com.aoc.day12.Item
import com.aoc.day12.Options
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class OptionsTest {
    val firstRawItem = mutableListOf(
        "###",
        "##.",
        "##.",
    )
    val secondRawItem = mutableListOf(
        "###",
        "##.",
        "##.",
    )
    
    @Test
    fun `We're able to store items in the options`() {
        // Given
        val options = Options()
        // When
        val firstItem = Item(firstRawItem)
        val secondItem = Item(secondRawItem)

        options.addItem(0,firstItem)
        options.addItem(0,secondItem)
        options.addItem(1,firstItem)
        // Then
        val expectResult = mutableMapOf(
            0 to mutableSetOf(secondItem, firstItem),
            1 to mutableSetOf(firstItem)
        )

        assertEquals(expectResult,options.getOptions())
    }

}