package day02

import java.io.File
import kotlin.math.abs


fun isDifferenceMoreThan3OrLessThan1(a: Int): Boolean {
    return (a < 1) || (a > 3)
}

fun isAscendant(last: Int, new: Int): Int {
    return if(last > new) 1
    else -1
}

fun isReportSafe(lineArray: ArrayList<String>): Boolean {
    var lastValue = -1
    var direction = 0 // ascendant or descendant
    for(number in lineArray) {
        val newNumber = number.toInt()
        if(lastValue == -1){
            lastValue = newNumber
            continue
        }
        val diff = abs(newNumber - lastValue)
        if(isDifferenceMoreThan3OrLessThan1(diff)){// verify that the difference is in the right values
            return false
        }
        val actualDirection = isAscendant(lastValue,newNumber)
        if((direction != actualDirection) && (direction != 0)){// verify that the direction have been maintained through the report
            return false
        }else{
            lastValue = newNumber
            direction = actualDirection
        }

    }
    return true
}
fun firstPart(lines: List<String>){
    var safeTotal = 0
    for(line in lines) {
        val listLineArray = line.split(" ")
        val arrayListLine: ArrayList<String> = ArrayList<String>(listLineArray)

        if(isReportSafe(arrayListLine)){
            safeTotal++
        }
    }
    println(safeTotal)
}

fun secondPart(lines: List<String>){
    var safeTotal = 0
    for(line in lines) {
        val listLineArray = line.split(" ")
        var arrayListLine: ArrayList<String> = ArrayList<String>(listLineArray)
        if(isReportSafe(arrayListLine)){
            safeTotal++
        }else{

            for(i in listLineArray.indices){
                arrayListLine = ArrayList<String>(listLineArray)
                arrayListLine.removeAt(i)
                // println("cycle: $i")
                // println("line Array: $listLineArray")
                // println("Holder Array: $arrayListLine")
                if(isReportSafe(arrayListLine)){
                    //println("answer is safe")
                    safeTotal++
                    break
                }
                //println("answer is unsafe")
            }
        }
    }
    println(safeTotal)
}
fun main() {
    val fileName = "./src/day02/input.txt"
    val lines = File(fileName).readLines()
    val mode = readln().trim().toInt()
    if(mode == 1){
        firstPart(lines)
    }else{
        secondPart(lines)
    }
}