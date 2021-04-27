package com.example.workout_timer

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import kotlin.coroutines.coroutineContext

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
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(name: String?, duration: Int?, context: Context) : this(name, duration, context.getDrawable(R.drawable.downward_dog))
    private val identifier: String? = this.name
    fun rename(newName: String?) { name = newName }
    fun editDuration(newDuration: Int?) { duration = newDuration }
    override fun toString(): String = identifier.toString()
}