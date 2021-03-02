package com.example.workout_timer

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // ref: https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
        // create routineList, a Routine object
        listView = findViewById<ListView>(R.id.routine_list)
        var routineList = mutableListOf<Routine>()

        // add elements to routineList
        routineList.add(Routine(mutableListOf<RoutineElement>(), "yoga flow"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "core workout"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "get swol"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "intense butt workout"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "leg day"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "weights"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "mondays"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "easy peasy"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "lemon squeezy"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "need more workouts"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "abs. just sayin"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "swimming in the pool"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "walking up the stairs"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "core 2"))
        routineList.add(Routine(mutableListOf<RoutineElement>(), "impress the ladies and lads"))

        /*
         * routineList elements added into routineArray to then be adapted into the list shown
         * on screen
         */
        var routineArray = arrayOfNulls<String>(routineList.size)

        for (i in 0 until routineList.size) {
            val routine = routineList[i]
            routineArray[i] = routine.getName()
        }

        // adapter to adapt the routineArray into our list on the main screen
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, routineArray)
        listView.adapter = adapter

        // 'add' button
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            var routineElements = mutableListOf<RoutineElement>()
//            routineElements.add(RoutineElement(("pose 1"), 10))
//            routineList.add(Routine(routineElements, "yoga"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}