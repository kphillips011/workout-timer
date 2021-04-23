package com.example.workout_timer

import android.graphics.drawable.Drawable

/*
 * Routine object holds the entire list of different workout Routines
 */
class Routine(var elements: MutableList<RoutineElement>, var name: String?) {
    private val identifier: String? = this.name
    fun renameRoutine(newName: String) { name = newName }
    fun addElement(element: RoutineElement): Boolean { return elements.add(element) }
    fun removeElement(element: RoutineElement): Boolean { return elements.remove(element) }
    override fun toString(): String = identifier.toString()
}

/*
 * RoutineElement object holds the entire list of poses/reps within a workout Routine
 */
class RoutineElement(var name: String?, var duration: Int?, var image: Drawable?) {
    // additional constructor to add drawable image, used for preset selection
    constructor(name: String?, duration: Int?) : this(name, duration, Drawable.createFromPath("drawable/ic_baseline_timer_24.xml"))
    private val identifier: String? = this.name
    fun rename(newName: String?) { name = newName }
    fun editDuration(newDuration: Int?) { duration = newDuration }
    override fun toString(): String = identifier.toString()
}