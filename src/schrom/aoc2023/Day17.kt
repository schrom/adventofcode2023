package schrom.aoc2023

import java.io.File
import java.util.*
import kotlin.math.min

data class CartState(val x: Int, val y: Int, val direction: CartDirection, val straightCount: Int = 1)

data class Edge(val from: CartState, val to: CartState, val heatloss: Int)

enum class CartDirection {
    U, D, L, R
}

class CityMap(internal val data: List<String>) {

    internal fun isInCity(x: Int, y: Int) = (0..data.lastIndex).contains(y) && (0..data.first().lastIndex).contains(x)

    internal fun getHeat(x: Int, y: Int) = data[y][x].digitToInt()

    fun possibleEdges(node: CartState): List<Edge> {
        val newEdges = mutableListOf<Edge>()
        when (node.direction) {
            CartDirection.U -> {
                if (isInCity(node.x, node.y - 1) && node.straightCount < 3) {
                    newEdges.add(
                        Edge(node, CartState(node.x, node.y - 1, node.direction,node.straightCount + 1), getHeat(node.x, node.y - 1))
                    )
                }
                if (isInCity(node.x - 1, node.y)) {
                    newEdges.add(
                        Edge(node, CartState(node.x - 1, node.y,  CartDirection.L), getHeat(node.x - 1, node.y))
                    )
                }
                if (isInCity(node.x + 1, node.y)) {
                    newEdges.add(
                        Edge(node, CartState(node.x + 1, node.y, CartDirection.R), getHeat(node.x + 1, node.y))
                    )
                }
            }

            CartDirection.D -> {
                if (isInCity(node.x, node.y + 1) && node.straightCount < 3) {
                    newEdges.add(
                        Edge(node, CartState(node.x, node.y + 1, node.direction, node.straightCount + 1 ), getHeat(node.x, node.y + 1))
                    )
                }
                if (isInCity(node.x - 1, node.y)) {
                    newEdges.add(
                        Edge(node, CartState(node.x - 1, node.y, CartDirection.L), getHeat(node.x - 1, node.y))
                    )
                }
                if (isInCity(node.x + 1, node.y)) {
                    newEdges.add(
                        Edge(node, CartState(node.x + 1, node.y, CartDirection.R), getHeat(node.x + 1, node.y))
                    )
                }
            }

            CartDirection.L -> {
                if (isInCity(node.x - 1, node.y) && node.straightCount < 3) {
                    newEdges.add(
                        Edge(node, CartState(node.x - 1, node.y, node.direction,node.straightCount + 1), getHeat(node.x - 1, node.y))
                    )
                }
                if (isInCity(node.x, node.y - 1)) {
                    newEdges.add(
                        Edge(node, CartState(node.x, node.y - 1, CartDirection.U), getHeat(node.x, node.y - 1))
                    )
                }
                if (isInCity(node.x, node.y + 1)) {
                    newEdges.add(
                        Edge(node, CartState(node.x, node.y + 1, CartDirection.D), getHeat(node.x, node.y + 1))
                    )
                }
            }

            CartDirection.R -> {
                if (isInCity(node.x + 1, node.y) && node.straightCount < 3) {
                    newEdges.add(
                        Edge(node, CartState(node.x + 1, node.y, node.direction,node.straightCount + 1), getHeat(node.x + 1, node.y))
                    )
                }
                if (isInCity(node.x, node.y - 1)) {
                    newEdges.add(
                        Edge(node, CartState(node.x, node.y - 1, CartDirection.U), getHeat(node.x, node.y - 1))
                    )
                }
                if (isInCity(node.x, node.y + 1)) {
                    newEdges.add(
                        Edge(node, CartState(node.x, node.y + 1, CartDirection.D), getHeat(node.x, node.y + 1))
                    )
                }
            }
        }
        return newEdges
    }

    fun isTarget(c:CartState) = c.x == data.first().lastIndex && c.y == data.lastIndex
}

fun dijkstra(source: CartState, isTarget: (c:CartState) -> Boolean, findPossibleEdges: (n: CartState) -> List<Edge> ): Int {

    val dist = mutableMapOf<CartState, Int>()
    val q = PriorityQueue<CartState>( compareBy { dist[it] ?: 0 } )
    q.add(source)
    dist[source] = 0

    while (q.isNotEmpty()) {
        val u = q.remove()
        if (isTarget(u)) {
            return dist[u]!!
        }
        findPossibleEdges(u)
            .forEach {
                val a = dist[u]!! + it.heatloss
                if (a < dist.getOrDefault(it.to, Int.MAX_VALUE)) {
                    dist[it.to] = a
                    q.add(it.to)
                }
            }
    }
    throw IllegalStateException("should have found target")
}


fun main() {
    val data = File("data/day17.txt").readLines()
    val citymap = CityMap(data)
    val dist = min(
        dijkstra(CartState(0, 0, CartDirection.D), citymap::isTarget, citymap::possibleEdges),
        dijkstra(CartState(0, 0, CartDirection.R), citymap::isTarget, citymap::possibleEdges)
    )
    println(dist)
}
