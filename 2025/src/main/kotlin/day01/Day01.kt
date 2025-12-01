package com.aoc.day01

import java.io.File

fun wrapDial(dial: Int): Int{
    if(dial >= 200 || dial <= -100) throw IllegalArgumentException("Out of range dial: $dial")
    if(dial < 0) return dial + 100
    if(dial > 99) return dial - 100
    return dial
}

fun moveDial(dir:Char, currentDial: Int, steps: Int): Int{
    val effectiveSteps = steps % 100
    if(dir == 'L'){
        return wrapDial(currentDial - effectiveSteps)
    }
    return wrapDial(currentDial + effectiveSteps)

}

fun parseInstruction(line: String): Pair<Char, Int> {
    val dir = line[0]
    val value = line.substring(1).toInt()
    return dir to value
}

fun firstPart(lines: List<String>){
    var dial = 50
    var ans = 0
    for(line in lines) {
        val (dir, value) = parseInstruction(line)
        dial = moveDial(dir,dial, value)
        println("result: $dial")
        if(dial == 0) ans++
    }
    println(ans)
}

fun secondPart(lines: List<String>){
    var dial = 50
    var ans = 0
    for(line in lines) {
        val (dir, value) = parseInstruction(line)
        repeat(value ){
            dial = moveDial(dir,dial,1)
            if(dial == 0){
                ans++
            }
        }
        print("final dial position: $dial | ")
        println("ans: $ans")
    }
    println(ans)
}


fun main() {
    val fileName = "src/main/kotlin/day01/input.txt"
    val lines = File(fileName).readLines()
    val mode = readln().trim().toInt()
    if(mode == 1){
        firstPart(lines)
    }else{
        secondPart(lines)
    }
}
