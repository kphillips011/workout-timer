package com.example.workout_timer

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // ref: https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
        //      https://www.raywenderlich.com/1364094-android-fragments-tutorial-an-introduction-with-kotlin
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

        // adapter to adapt the routineList into our list on the main screen
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, routineList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, i, _ ->
            val selectedWorkout =  routineList[i]
            val selectedWorkoutElements = selectedWorkout.getElements()
        }

        listView.setOnItemLongClickListener(OnItemLongClickListener { _, view, i, _ ->
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Are you sure you want to delete this workout?")
            // if yes, deletes
            builder.setPositiveButton("OK") { _,_ ->
                adapter.remove(routineList[i])
                adapter.notifyDataSetChanged()
                Snackbar.make(view, "Workout removed", Snackbar.LENGTH_LONG).setAction(
                    "Action",
                    null
                ).show()
            }
            // if no, does nothing
            builder.setNegativeButton("CANCEL") { _,_ -> }
            builder.show()
            false
        })

        // 'add' button to add a new workout
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            // add stuff in here to input name of new routine to be added
            // ref: https://www.journaldev.com/309/android-alert-dialog-using-kotlin
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Please enter a name for your workout: ")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                routineList.add(Routine(mutableListOf<RoutineElement>(), editText.text.toString()))
                adapter.notifyDataSetChanged()
                Snackbar.make(view, "Added new workout", Snackbar.LENGTH_LONG).setAction(
                    "Action",
                    null
                ).show()
            }
            builder.setNegativeButton("CANCEL") { _,_ -> }
            builder.show()
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