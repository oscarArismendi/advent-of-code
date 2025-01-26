package day03

import java.io.File

fun multiply(operation: String): Long {
    val finalOperation = operation.removeRange(0,4).replaceFirst(")","")// Remove mul( and remove last character )
    var total = 0
    finalOperation.split(",").zipWithNext().forEach {(a,b)->total = (a.toInt()*b.toInt())}
    return total.toLong()
}

fun firstPart(lines: List<String>){
    val myRegex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")
    var total:Long = 0
    lines.forEach {
//        println("--------------")
//        println(it)
        val matches = myRegex.findAll(it)
        // println(matches.count())
        matches.forEach {match ->
            val operation = match.groupValues[0]
            println(match.groupValues[0])
            total += multiply(operation)
        }
    }
    println(total)
}

fun secondPart(lines: List<String>){
    val myRegex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)|do\\(\\)|don't\\(\\)")
    var total:Long = 0
    var status = true
    lines.forEach {
//        println("--------------")
//        println(it)
        val matches = myRegex.findAll(it)
        //println(matches.count())
        matches.forEach {match ->
            val operation = match.groupValues[0]
            //println(operation)
            if(operation == "do()"){
                status = true
            }else if(operation == "don't()"){
                status = false
            }else{
                if(status){
                    total += multiply(operation)
                }
            }


        }
    }
    println(total)

}

fun main() {
    val fileName = "./src/day03/input.txt"
    val lines = File(fileName).readLines()
    val mode = readln().trim().toInt()
    if(mode == 1){
        firstPart(lines)
    }else{
        secondPart(lines)
    }
}