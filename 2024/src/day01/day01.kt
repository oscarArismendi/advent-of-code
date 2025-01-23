package day01

import java.io.BufferedReader
import java.io.FileReader

/* fun printNumbers(vararg numbers: Int) {
    numbers.forEach { print("$it ") }
    print("\n")
} */

fun firstPart(){
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

fun secondPart(){
    val nums1 = ArrayList<Int>()
    val nums2 = ArrayList<Int>()
    val cntNums2 = LinkedHashMap<Int,Int>()
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
        print(b.toInt())
        print(" ")
            println(cntNums2[b.toInt()])
            if(cntNums2[b.toInt()] == null){
                cntNums2[b.toInt()] = 0
            }
            cntNums2[b.toInt()] = cntNums2[b.toInt()]!! + 1

        i++
    }

    reader.close()
    var tot = 0
    for (j in nums1.indices) {
        if(cntNums2[nums1[j]] == null){
            cntNums2[nums1[j]] = 0
        }
        val result = nums1[j] * cntNums2[nums1[j]]!!
        tot += result

    }
    print(tot)
}

fun main() {
    secondPart()
}
