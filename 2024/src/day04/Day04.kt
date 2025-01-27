package day04

import java.io.File

fun isRightWord(y: Int, x: Int, lines: List<String>): Boolean {
    return lines[y][x+1] == 'M' && lines[y][x+2] == 'A' && lines[y][x+3] == 'S'
}

fun isLeftWord(y: Int, x: Int, lines: List<String>): Boolean {
    return lines[y][x-1] == 'M' && lines[y][x-2] == 'A' && lines[y][x-3] == 'S'
}

fun isUpWord(y: Int, x: Int, lines: List<String>): Boolean {
    return lines[y-1][x] == 'M' && lines[y-2][x] == 'A' && lines[y-3][x] == 'S'
}

fun isDownWord(y: Int, x: Int, lines: List<String>): Boolean {
    return lines[y+1][x] == 'M' && lines[y+2][x] == 'A' && lines[y+3][x] == 'S'
}

fun isDiagonalDownRightWord(y: Int, x: Int, lines: List<String>): Boolean {
    return lines[y+1][x+1] == 'M' && lines[y+2][x+2] == 'A' && lines[y+3][x+3] == 'S'
}

fun isDiagonalUpRightWord(y: Int, x: Int, lines: List<String>): Boolean {
    return lines[y-1][x+1] == 'M' && lines[y-2][x+2] == 'A' && lines[y-3][x+3] == 'S'
}

fun isDiagonalUpLeftWord(y: Int, x: Int, lines: List<String>): Boolean {
    return lines[y-1][x-1] == 'M' && lines[y-2][x-2] == 'A' && lines[y-3][x-3] == 'S'
}

fun isDiagonalDownLeftWord(y: Int, x: Int, lines: List<String>): Boolean {
    return lines[y+1][x-1] == 'M' && lines[y+2][x-2] == 'A' && lines[y+3][x-3] == 'S'
}

fun firstPart(lines: List<String>){
    println(lines)
    val elementsY = lines.size
    var cnt = 0
    lines.forEachIndexed {// [MMMSXXMASM, MSAMXMSMSA, AMXSXMAAMM, MSAMASMSMX, XMASAMXAMM, XXAMMXXAMA, SMSMSASXSS, SAXAMASAAA, MAMMMXMMMM, MXMXAXMASX]
        y, line -> line.forEachIndexed { x , c ->// format: MMMSXXMASM
                val elementsX = line.length
                if(c == 'X'){
                    if(x + 3 < elementsX){//right
                        if(isRightWord(y,x,lines)){
                            cnt++
                        }
                        if(y + 3 < elementsY){//diagonal-down-right
                            if(isDiagonalDownRightWord(y,x,lines)){
                                cnt++
                            }
                        }
                        if(y - 3 >= 0){//diagonal-up-right
                            if(isDiagonalUpRightWord(y,x,lines)){
                                cnt++
                            }
                        }

                    }
                    if(x - 3 >= 0){//left
                        if(isLeftWord(y,x,lines)){
                            cnt++
                        }

                        if(y + 3 < elementsY){//diagonal-down-left
                            if(isDiagonalDownLeftWord(y,x,lines)){
                                cnt++
                            }
                        }

                        if(y - 3 >= 0){//diagonal-up-left
                            if(isDiagonalUpLeftWord(y,x,lines)){
                                cnt++
                            }
                        }
                    }
                    if(y - 3 >= 0){//up
                        if(isUpWord(y,x,lines)){
                            cnt++
                        }
                    }
                    if(y + 3 < elementsY){//down
                        if(isDownWord(y,x,lines)){
                            cnt++
                        }
                    }
                }

            }
    }
    print(cnt)
}

fun secondPart(lines: List<String>){

}

fun main() {
    val fileName = "./src/day04/input.txt"
    val lines = File(fileName).readLines()
    val mode = readln().trim().toInt()
    if(mode == 1){
        firstPart(lines)
    }else{
        secondPart(lines)
    }
}