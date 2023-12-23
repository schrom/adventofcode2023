package schrom.aoc2023

import java.io.File
import java.util.*

enum class Pulse { LOW, HIGH }

abstract class Module(var id: String) {

    val input: Queue<Pair<Module, Pulse>> = LinkedList()
    val destinations = mutableListOf<Module>()
    var lowCount = 0
    var highCount = 0

    fun receivePulse(p: Pulse, from: Module) {
        if (p == Pulse.LOW) {
            lowCount++
        } else {
            highCount++
        }
        input.add(from to p)
    }

    fun callReceivers() {
        destinations.forEach { it.processInput() }
    }

    internal abstract fun processInput()
}

class Broadcaster(id: String) : Module(id) {

    fun pushButton() {
        receivePulse(Pulse.LOW, NoopModule("button"))
        processInput()
    }

    override fun processInput() {
        while (input.isNotEmpty()) {
            val (_, pulse) = input.remove()
            destinations.forEach {
                it.receivePulse(pulse, this)
            }
        }
        callReceivers()
    }
}

class FlipFlop(id: String) : Module(id) {
    enum class State { ON, OFF }

    private var state = State.OFF

    override fun processInput() {
        var didSend = false
        while (input.isNotEmpty()) {
            val (_, pulse) = input.remove()
            if (pulse == Pulse.LOW) {
                flipState()
                didSend = true
            }
        }
        if (didSend) {
            callReceivers()
        }
    }

    private fun flipState() {
        if (state == State.OFF) {
            state = State.ON
            destinations.forEach {
                it.receivePulse(Pulse.HIGH, this)
            }
        } else {
            state = State.OFF
            destinations.forEach {
                it.receivePulse(Pulse.LOW, this)
            }
        }
    }
}

class Conjunction(id: String) : Module(id) {

    val allSources = mutableListOf<Module>()

    private val memory = mutableMapOf<String, Pulse>()

    override fun processInput() {
        while (input.isNotEmpty()) {
            val (from, pulse) = input.remove()
            memory[from.id] = pulse
            if (allMemoryIsHigh()) {
                destinations.forEach { it.receivePulse(Pulse.LOW, this) }
            } else {
                destinations.forEach { it.receivePulse(Pulse.HIGH, this) }
            }
        }
        callReceivers()
    }

    private fun allMemoryIsHigh() =
        allSources.count { memory.getOrDefault(it.id, Pulse.LOW) == Pulse.HIGH } == allSources.size

}

class NoopModule(id: String) : Module(id) {
    override fun processInput() { }
}

fun main() {
    val data = File("data/day20.txt").readLines()

    val modules = data.associate {
        val typeAndId = it.substringBefore(" -> ")
        val module = if (typeAndId == "broadcaster") {
            Broadcaster(typeAndId)
        } else if (typeAndId.startsWith("%")) {
            FlipFlop(typeAndId.substring(1))
        } else {
            Conjunction(typeAndId.substring(1))
        }
        module.id to module
    }.toMutableMap()

    data.forEach {
        val typeAndId = it.substringBefore(" -> ")
        val id = if (typeAndId == "broadcaster") {
            typeAndId
        } else {
            typeAndId.substring(1)
        }
        val source = modules[id]!!
        val destinations = it.substringAfter(" -> ").split(", ")
        destinations
            .map { d ->
                modules.putIfAbsent(d, NoopModule(d))
                modules[d]!!
            }
            .forEach { m ->
                if (m is Conjunction) {
                    m.allSources.add(source)
                }
                source.destinations.add(m)
            }
    }

    repeat(1000) {
        (modules["broadcaster"] as Broadcaster).pushButton()
    }
    val numberOfPulses = modules.values.sumOf { it.lowCount } * modules.values.sumOf { it.highCount }
    println(numberOfPulses)
}
