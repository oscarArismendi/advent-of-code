package com.aoc.utils.dataStructures

class UnionFind(val size: Int) {
    val parent: MutableList<Int> = (0 until size).map { it }.toMutableList() // parent[i] = i mean root node
    val groupSize: MutableList<Int> = MutableList(size){1} 
    var numberOfComponents: Int = size
// Not to useful now, but maybe in the future I can use it    
//    fun connected(node1: Int, node2: Int): Boolean{
//        return findParent(node1) == findParent(node2)
//    }
    
    fun findParent(currentNode: Int, initialNode: Int): Int{
        if(parent[currentNode] == currentNode){
            pathCompression(initialNode, currentNode)
            return currentNode
        }
        return findParent(parent[currentNode],initialNode) // call recursively until find root node
    }

    private fun pathCompression(initialNode: Int, parentNode: Int) {
        var pointer = initialNode
        while (pointer != parentNode) {
            val next = parent[pointer]
            parent[pointer] = parentNode
            pointer = next
        }
    }

    fun union(node1: Int, node2: Int) {
        val root1 = findParent(node1,node1)
        val root2 = findParent(node2,node2)
        if(root1 == root2) return // already connected
        
        //
        if(groupSize[root1] > groupSize[root2]){
            groupSize[root1] += groupSize[root2] // update size of the group
            groupSize[root2] = 0 // mean root2 is not root anymore
            parent[root2] = root1 // make root1 parent of root2
        }else{
            groupSize[root2] += groupSize[root1] // update size of the group
            groupSize[root1] = 0 // mean root1 is not root anymore
            parent[root1] = root2 // make root2 parent of root1
        }
        numberOfComponents--
    }
    
    // Custom functions
    fun multiplyTheThreeLargestGroupSizes(): Int{
        var first = 1 to -1 // pair of size and rootNode
        var second = 1 to -1
        var third = Pair(1,-1)
        for(i in 0 until size){
            val parent = findParent(i,i)
            if(parent == first.second || parent == second.second || parent == third.second)continue
            if(groupSize[parent] > first.first){
                // we move everything down to make space for the new max
                third = second
                second = first
                first = groupSize[parent] to parent                
            }else if(groupSize[parent] > second.first){
                third = second
                second = groupSize[parent] to parent
            }else if(groupSize[parent] > third.first){
                third = groupSize[parent] to parent
            }
        }
        return first.first * second.first * third.first
    }
}