package schrom.aoc2023

import java.io.File

class AplentyPart(v: String) {
    val values = mutableMapOf<Char, Int>()

    init {
        v.split(",").forEach { s ->
            values[s[0]] = s.substring(2).toInt()
        }
    }
}

class AplentyCondition(condition: String) {
    val expression: String
    val target: String

    init {
        if (condition.contains(":")) {
            expression = condition.split(":")[0]
            target = condition.split(":")[1]
        } else {
            expression = ""
            target = condition
        }
    }
}

class AplentyWorkflow(val id: String) {
    val conditions = mutableListOf<AplentyCondition>()

    fun applyTo(part: AplentyPart): String {
        conditions.forEach {
            if (it.expression.isEmpty()) {
                return it.target
            } else {
                val partValue = part.values[it.expression[0]]!!
                val compValue = it.expression.substring(2).toInt()
                when (it.expression[1]) {
                    '<' -> if (partValue < compValue) {
                        return it.target
                    }

                    '>' -> if (partValue > compValue) {
                        return it.target
                    }
                }
            }
        }
        throw IllegalStateException("no condition matched")
    }
}

class AplentyWorkflowEngine {
    val accepted = mutableListOf<AplentyPart>()
    val workflows = mutableMapOf<String, AplentyWorkflow>()

    fun runEngine(parts: List<AplentyPart>) {
        parts.forEach {
            var wf = workflows["in"]
            do {
                when (val target = wf!!.applyTo(it)) {
                    "A" -> {
                        accepted.add(it)
                        wf = null
                    }

                    "R" -> {
                        wf = null
                    }

                    else -> {
                        wf = workflows[target]
                    }
                }
            } while (wf != null)
        }
    }
}

fun main() {
    val parts = mutableListOf<AplentyPart>()
    val engine = AplentyWorkflowEngine()
    val data = File("data/day19.txt").readLines()
    data.forEach {
        if (it.startsWith("{")) {
            val part = AplentyPart(it.substringAfter("{").substringBefore("}"))
            parts.add(part)
        } else if (it.isNotEmpty()) {
            val workflow = AplentyWorkflow(it.substringBefore("{"))
            it.substringAfter("{").substringBefore("}").split(",").forEach { c ->
                workflow.conditions.add(AplentyCondition(c))
            }
            engine.workflows[workflow.id] = workflow
        }
    }

    engine.runEngine(parts)
    val result = engine.accepted.sumOf {
        it.values.values.sum()
    }
    println(result)
}

