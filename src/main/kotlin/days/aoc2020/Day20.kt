package days.aoc2020

import days.Day

class Day20: Day(2020, 20) {
    override fun partOne(): Any {
        val tiles = parseTiles(inputList)

        findRawNeighbors(tiles)

        val corners = tiles.filter { it.rawNeighbors.size == 2 }
        return corners.map { it.id.toLong() }.reduce { acc, l -> acc * l }
    }

    fun parseTiles(list: List<String>): List<Tile> {
        val tiles = mutableListOf<Tile>()
        var image = mutableListOf<MutableList<Char>>()
        var tileId = 0

        list.forEach { line ->
            when {
                line.contains(":") -> {
                    Regex("Tile (\\d+):").matchEntire(line)?.destructured?.let { (id) ->
                        tileId = id.toInt()
                    }
                }
                line.isBlank() -> {
                    tiles.add(Tile(tileId, image))
                    image = mutableListOf()
                }
                else -> {
                    image.add(line.toCharArray().toMutableList())
                }
            }
        }

        return tiles
    }

    override fun partTwo(): Any {
        val tiles = parseTiles(inputList)

        findRawNeighbors(tiles)

        // grab a corner
        val topLeft = tiles.first { it.rawNeighbors.size == 2 }

        // rotate it until it acts like the top left
        while (findTileInDirectionFrom(topLeft, Tile.Edge.RIGHT) == null || findTileInDirectionFrom(topLeft, Tile.Edge.BOTTOM) == null) {
            topLeft.rotateRight()
        }

        var current: Tile? = topLeft
        var leftmost: Tile = topLeft
        var above: Tile? = null
        var toLeft: Tile? = null

        // orient the remaining tiles from the upper left, left to right by rows
        while (current != null) {
            orient(current, toLeft, above)

            toLeft = current
            current = findTileInDirectionFrom(current, Tile.Edge.RIGHT)
            if (current == null) {
                above = leftmost
                toLeft = null
                current = findTileInDirectionFrom(leftmost, Tile.Edge.BOTTOM)
                if (current != null) {
                    leftmost = current
                }
            } else {
                if (above != null) {
                    above = findTileInDirectionFrom(above, Tile.Edge.RIGHT)
                }
            }
        }


        // now construct a giant tile from this corner
        val completeImage: MutableList<MutableList<Char>> = mutableListOf()
        current = topLeft
        var currentTileImages: MutableList<MutableList<Char>> = mutableListOf()
        var leftEdge: Tile? = current
        while (current != null) {
            current.imageWithoutBorders().forEachIndexed { index, line ->
                val destination = currentTileImages.elementAtOrElse(index) {
                    currentTileImages.add(index, mutableListOf())
                    currentTileImages[index]!!
                }
                destination.addAll(line)
            }

            if (current.neighbors.containsKey(Tile.Edge.RIGHT)) {
                current = current.neighbors[Tile.Edge.RIGHT]
            } else {
                current = leftEdge!!.neighbors[Tile.Edge.BOTTOM]
                leftEdge = current
                completeImage.addAll(currentTileImages)
                currentTileImages = mutableListOf()
            }
        }

        val finalTile = Tile(0, completeImage)

        return finalTile.countRoughWater()
    }

    private fun findRawNeighbors(tiles: List<Tile>) {
        tiles.forEachIndexed { index, tile ->
            val borders = tile.borders()
            if (index+1 in tiles.indices) {
                val neighbors = tiles.subList(index + 1, tiles.size).filter {
                    it.borders().intersect(borders).isNotEmpty()
                }
                tile.rawNeighbors.addAll(neighbors)
                neighbors.forEach { neighbor ->
                    neighbor.rawNeighbors.add(tile)
                }
            }
        }
    }

    private fun orient(tile: Tile, tileToLeft: Tile?, tileAbove: Tile?) {
        if (tileToLeft != null) {
            tile.neighbors[Tile.Edge.LEFT] = tileToLeft
            tileToLeft.neighbors[Tile.Edge.RIGHT] = tile
            val border = tileToLeft.normalBorders()[Tile.Edge.RIGHT.ordinal]

            if (!tile.reverseBorders().contains(border)) {
                tile.flipVertical()
            }
            if (!tile.reverseBorders().contains(border)) {
                tile.flipHorizontal()
            }

            while(tile.reverseBorders()[Tile.Edge.LEFT.ordinal] != border) {
                tile.rotateRight()
            }
        }
        else if (tileAbove != null) {
            tile.neighbors[Tile.Edge.TOP] = tileAbove
            tileAbove.neighbors[Tile.Edge.BOTTOM] = tile

            val border = tileAbove.normalBorders()[Tile.Edge.BOTTOM.ordinal]
            if (!tile.reverseBorders().contains(border)) {
                tile.flipVertical()
            }
            if (!tile.reverseBorders().contains(border)) {
                tile.flipHorizontal()
            }

            while(tile.reverseBorders()[Tile.Edge.TOP.ordinal] != border) {
                tile.rotateRight()
            }
        }
    }

    private fun findTileInDirectionFrom(tile: Tile, direction: Tile.Edge): Tile? {
        val tileBorders = tile.borders()
        return tile.rawNeighbors.firstOrNull { neighbor ->
            val neighborBorders = neighbor.borders()
            neighborBorders.contains(tileBorders[direction.ordinal * 2])
        }
    }

}

class Tile(val id: Int, val image: MutableList<MutableList<Char>>) {
    val rawNeighbors = mutableListOf<Tile>()
    val neighbors = mutableMapOf<Edge,Tile>()

    fun borders(): List<Int> {
        val result = mutableListOf<Int>()
        val (topRTL,topLTR) = charsToRTLAndLTR(image[0])
        result.addAll(listOf(topLTR, topRTL))

        val (rightRTL,rightLTR) = charsToRTLAndLTR(image.map { it[image.lastIndex] })
        result.addAll(listOf(rightLTR, rightRTL))

        val (bottomRTL, bottomLTR) = charsToRTLAndLTR(image[image.lastIndex])
        result.addAll(listOf(bottomRTL, bottomLTR))

        val (leftRTL, leftLTR) = charsToRTLAndLTR(image.map { it[0] })
        result.addAll(listOf(leftRTL, leftLTR))
        return result
    }

    fun normalBorders(): List<Int> {
        val result = mutableListOf<Int>()
        val (_, topLTR) = charsToRTLAndLTR(image[0])
        result.add(topLTR)

        val (_,rightLTR) = charsToRTLAndLTR(image.map { it[image.lastIndex] })
        result.add(rightLTR)

        val (bottomRTL, _) = charsToRTLAndLTR(image[image.lastIndex])
        result.add(bottomRTL)

        val (leftRTL, _) = charsToRTLAndLTR(image.map { it[0] })
        result.add(leftRTL)
        return result
    }

    fun reverseBorders(): List<Int> {
        val result = mutableListOf<Int>()
        val (topRTL,_) = charsToRTLAndLTR(image[0])
        result.add(topRTL)

        val (rightRTL,_) = charsToRTLAndLTR(image.map { it[image.lastIndex] })
        result.add(rightRTL)

        val (_,bottomLTR) = charsToRTLAndLTR(image[image.lastIndex])
        result.add(bottomLTR)

        val (_,leftLTR) = charsToRTLAndLTR(image.map { it[0] })
        result.add(leftLTR)
        return result
    }

    val imageWithoutBorders = {
        image.drop(1).dropLast(1).map { line ->
            line.drop(1).dropLast(1)
        }
    }

    // turn "..#.#....#" into [161,562]
    private fun charsToRTLAndLTR(chars: List<Char>): List<Int> {
        var first = 0
        var second = 0
        chars.forEachIndexed { index, c ->
            if (c == '#') {
                first += 1 shl index
                second += 1 shl (chars.lastIndex - index)
            }
        }
        return listOf(first, second)
    }

    fun flipVertical() {
        val newImage = image.reversed()
        image.clear()
        image.addAll(newImage)
    }

    fun flipHorizontal() {
        val newImage = image.map { it.reversed().toMutableList() }
        image.clear()
        image.addAll(newImage)
    }

    fun rotateRight() {
        val newImage = image.flatMap { it.withIndex() }.groupBy( {it.index}, {it.value} ).map { it.value.reversed().toMutableList() }
        image.clear()
        image.addAll(newImage)
    }

    //.#.#...#.###...#.##.O#..
    //#.O.##.OO#.#.OO.##.OOO##
    //..#O.#O#.O##O..O.#O##.##
    fun countRoughWater(): Int {
        var step = 0
        do {
            if (imageHasSeaMonsters()) {
                replaceSeaMonsters()
                return image.map { it.count { it == '#' } }.sum()
            }

            when(step) {
                in 0..3, in 5..8, in 10..13 -> {
                    rotateRight()
                }
                4 -> {
                    flipHorizontal()
                }
                9 -> {
                    flipVertical()
                }
                else -> return 0
            }
            step++
        } while(true)
        return 0
    }

    fun imageHasSeaMonsters(): Boolean {
        for (y in image.indices) {
            for (x in image[0].indices) {
                if (isSeaMonster(y, x)) {
                    return true
                }
            }
        }
        return false
    }

    fun replaceSeaMonsters() {
        for (y in image.indices) {
            for (x in image[0].indices) {
                if (isSeaMonster(y, x)) {
                    replaceSeaMonster(y, x)
                }
            }
        }
    }

    fun replaceSeaMonster(lineNumber: Int, columnNumber: Int) {
        image[lineNumber-1][columnNumber] = 'O'
        image[lineNumber][columnNumber+1] = 'O'
        image[lineNumber][columnNumber+4] = 'O'
        image[lineNumber-1][columnNumber+5] = 'O'
        image[lineNumber-1][columnNumber+6] = 'O'
        image[lineNumber][columnNumber+7] = 'O'
        image[lineNumber][columnNumber+10] = 'O'
        image[lineNumber-1][columnNumber+11] = 'O'
        image[lineNumber-1][columnNumber+12] = 'O'
        image[lineNumber][columnNumber+13] = 'O'
        image[lineNumber][columnNumber+16] = 'O'
        image[lineNumber-1][columnNumber+17] = 'O'
        image[lineNumber-1][columnNumber+18] = 'O'
        image[lineNumber-1][columnNumber+19] = 'O'
        image[lineNumber-2][columnNumber+18] = 'O'
    }

    fun isSeaMonster(lineNumber: Int, columnNumber: Int): Boolean {
        if (lineNumber < 2 || columnNumber > image[0].size - 20)
            return false

        return image[lineNumber-1][columnNumber] == '#' &&
                image[lineNumber][columnNumber+1] == '#' &&
                image[lineNumber][columnNumber+4] == '#' &&
                image[lineNumber-1][columnNumber+5] == '#' &&
                image[lineNumber-1][columnNumber+6] == '#' &&
                image[lineNumber][columnNumber+7] == '#' &&
                image[lineNumber][columnNumber+10] == '#' &&
                image[lineNumber-1][columnNumber+11] == '#' &&
                image[lineNumber-1][columnNumber+12] == '#' &&
                image[lineNumber][columnNumber+13] == '#' &&
                image[lineNumber][columnNumber+16] == '#' &&
                image[lineNumber-1][columnNumber+17] == '#' &&
                image[lineNumber-1][columnNumber+18] == '#' &&
                image[lineNumber-1][columnNumber+19] == '#' &&
                image[lineNumber-2][columnNumber+18] == '#'
    }

    fun print() {
        println("Tile $id:")
        image.forEach { println(it.joinToString("")) }
        println()
    }

    enum class Edge {
        TOP, RIGHT, BOTTOM, LEFT;
    }

}