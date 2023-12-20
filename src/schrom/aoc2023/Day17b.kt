package schrom.aoc2023

import java.io.File
import kotlin.math.min

fun CityMap.possibleEdgesUltraCart(node: CartState): List<Edge> {
    val newEdges = mutableListOf<Edge>()
    when (node.direction) {
        CartDirection.U -> {
            if (isInCity(node.x, node.y - 1) && node.straightCount < 10) {
                newEdges.add(
                    Edge(node, CartState(node.x, node.y - 1, node.direction, node.straightCount + 1), getHeat(node.x, node.y - 1))
                )
            }
            if (isInCity(node.x - 1, node.y) && node.straightCount > 3) {
                newEdges.add(
                    Edge(node, CartState(node.x - 1, node.y, CartDirection.L), getHeat(node.x - 1, node.y))
                )
            }
            if (isInCity(node.x + 1, node.y) && node.straightCount > 3) {
                newEdges.add(
                    Edge(node, CartState(node.x + 1, node.y, CartDirection.R), getHeat(node.x + 1, node.y))
                )
            }
        }

        CartDirection.D -> {
            if (isInCity(node.x, node.y + 1) && node.straightCount < 10) {
                newEdges.add(
                    Edge(node, CartState(node.x, node.y + 1, node.direction, node.straightCount + 1), getHeat(node.x, node.y + 1))
                )
            }
            if (isInCity(node.x - 1, node.y) && node.straightCount > 3) {
                newEdges.add(
                    Edge(node, CartState(node.x - 1, node.y, CartDirection.L), getHeat(node.x - 1, node.y))
                )
            }
            if (isInCity(node.x + 1, node.y) && node.straightCount > 3) {
                newEdges.add(
                    Edge(node, CartState(node.x + 1, node.y, CartDirection.R), getHeat(node.x + 1, node.y))
                )
            }
        }

        CartDirection.L -> {
            if (isInCity(node.x - 1, node.y) && node.straightCount < 10) {
                newEdges.add(
                    Edge(node, CartState(node.x - 1, node.y, node.direction, node.straightCount + 1), getHeat(node.x - 1, node.y))
                )
            }
            if (isInCity(node.x, node.y - 1) && node.straightCount > 3) {
                newEdges.add(
                    Edge(node, CartState(node.x, node.y - 1, CartDirection.U), getHeat(node.x, node.y - 1))
                )
            }
            if (isInCity(node.x, node.y + 1) && node.straightCount > 3) {
                newEdges.add(
                    Edge(node, CartState(node.x, node.y + 1, CartDirection.D), getHeat(node.x, node.y + 1))
                )
            }
        }

        CartDirection.R -> {
            if (isInCity(node.x + 1, node.y) && node.straightCount < 10) {
                newEdges.add(
                    Edge(node, CartState(node.x + 1, node.y, node.direction, node.straightCount + 1), getHeat(node.x + 1, node.y))
                )
            }
            if (isInCity(node.x, node.y - 1) && node.straightCount > 3) {
                newEdges.add(
                    Edge(node, CartState(node.x, node.y - 1, CartDirection.U), getHeat(node.x, node.y - 1))
                )
            }
            if (isInCity(node.x, node.y + 1) && node.straightCount > 3) {
                newEdges.add(
                    Edge(node, CartState(node.x, node.y + 1, CartDirection.D), getHeat(node.x, node.y + 1))
                )
            }
        }
    }
    return newEdges
}

fun CityMap.isTargetUltraCart(c: CartState) = c.x == data.first().lastIndex && c.y == data.lastIndex && c.straightCount >= 4


fun main() {
    val data = File("data/day17.txt").readLines()
    val citymap = CityMap(data)
    val dist = min(
        dijkstra(CartState(0, 0, CartDirection.D), citymap::isTargetUltraCart, citymap::possibleEdgesUltraCart),
        dijkstra(CartState(0, 0, CartDirection.R), citymap::isTargetUltraCart, citymap::possibleEdgesUltraCart)
    )
    println(dist)
}
