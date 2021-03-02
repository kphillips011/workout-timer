package com.example.workout_timer

class Routine(private var elements: MutableList<RoutineElement>, private var name: String) {
    val identifier: String = this.toString()
    fun getName() = name
    fun getElements() = elements
    fun renameRoutine(newName: String) { name = newName }
    fun addElement(element: RoutineElement): Boolean { return elements.add(element) }
    fun removeElement(element: RoutineElement): Boolean { return elements.remove(element) }
}

class RoutineElement(private var name: String, private var duration: Int) {
    fun getName() = name
    fun getDuration() = duration
    fun rename(newName: String) { name = newName }
    fun editDuration(newDuration: Int) { duration = newDuration }
}