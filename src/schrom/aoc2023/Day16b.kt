package schrom.aoc2023

import java.io.File

// may need more than default stack size - "java -Xss1G" did fine for me

fun main() {
    val data = File("data/day16.txt").readLines()

    val grid = Grid(data.size, data.first().length)
    data.forEachIndexed { row, r ->
        r.forEachIndexed { col, c ->
            grid[row, col] = when (c) {
                '.' -> GridTile.EMPTY
                '-' -> GridTile.SPLIT_1
                '|' -> GridTile.SPLIT_2
                '/' -> GridTile.MIRROR_1
                '\\' -> GridTile.MIRROR_2
                else -> throw IllegalArgumentException()
            }
        }
    }

    val energyLevels = mutableListOf<Int>()

    (0 .. data.first().lastIndex).forEach { col ->
        Beam(0, col, BeamDirection.D).moveIn(grid)
        energyLevels.add(grid.countEnergized())
        grid.resetEnergized()
        visited.clear()

        Beam(data.lastIndex, col, BeamDirection.U).moveIn(grid)
        energyLevels.add(grid.countEnergized())
        grid.resetEnergized()
        visited.clear()
    }

    (0 .. data.lastIndex).forEach { row ->
        Beam(row, 0, BeamDirection.R).moveIn(grid)
        energyLevels.add(grid.countEnergized())
        grid.resetEnergized()
        visited.clear()

        Beam(row, data.first().lastIndex, BeamDirection.L).moveIn(grid)
        energyLevels.add(grid.countEnergized())
        grid.resetEnergized()
        visited.clear()
    }

    println(energyLevels.max())
}
