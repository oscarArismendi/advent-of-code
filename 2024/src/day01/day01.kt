package day01

import java.io.BufferedReader
import java.io.FileReader

/* fun printNumbers(vararg numbers: Int) {
    numbers.forEach { print("$it ") }
    print("\n")
} */

fun main() {
    val nums1 = ArrayList<Int>()
    val nums2 = ArrayList<Int>()

    val filePath = "./src/day01/input.txt" // path file

    val reader = BufferedReader(FileReader(filePath))
    var line: String?
    var i = 0
    while (reader.readLine().also { line = it } != null) {
        // println(line)

        var a = ""
        var b = ""

        var state = false
        for(c in line!!.toCharArray()) {
            if(c == ' '){
                state = true
            }

            if(state){
                if(c != ' '){
                    b += c.toString()
                }
                continue
            }
            a += c.toString()
        }
        // print("$a $b")
        // print("\n")
        nums1.add(a.toInt())
        nums2.add(b.toInt())
        i++
    }

    reader.close()

    nums1.sort()
    nums2.sort()

    var tot = 0
//    printNumbers(*nums1)
//    printNumbers(*nums2)
    for (j in nums1.indices) {
        var result = nums1[j] - nums2[j]
        if(result < 0){
            result *= -1
        }
        tot += result

    }
    print(tot)
}
