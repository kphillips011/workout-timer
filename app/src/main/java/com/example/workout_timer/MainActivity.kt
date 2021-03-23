package com.example.workout_timer

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var routineListView: ListView
    private lateinit var elementsListView: ListView
    private lateinit var viewSwitcher: ViewSwitcher
    private lateinit var elementDetailsView: View
    private var elementDetailsUp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val fab: View = findViewById(R.id.fab)

        // ref: https://tutorialwing.com/android-viewswitcher-using-kotlin-example/
        viewSwitcher = findViewById<ViewSwitcher>(R.id.viewSwitcher)
        val `in` = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        viewSwitcher.inAnimation = `in`
        val out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
        viewSwitcher.outAnimation = out

        // ref: https://stackoverflow.com/questions/19765938/show-and-hide-a-view-with-a-slide-up-down-animation
        elementDetailsView = findViewById<LinearLayout>(R.id.element_details)
        elementDetailsView.visibility = View.INVISIBLE
        var elementDetailsUp: Boolean = false

        // ref: https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
        //      https://www.raywenderlich.com/1364094-android-fragments-tutorial-an-introduction-with-kotlin
        // create routineList, a Routine object
        routineListView = findViewById<ListView>(R.id.routine_list)
        elementsListView = findViewById<ListView>(R.id.routine_elements)
        var routineList = mutableListOf<Routine>()

        // add elements to routineList, sample data
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

        routineList[0].addElement(RoutineElement("yoga 1", 0))
        routineList[0].addElement(RoutineElement("yoga 2", 5))
        routineList[4].addElement(RoutineElement("leg day 1", 10))

        // adapter to adapt the routineList into our list on the main screen
        val routineListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, routineList)
        routineListView.adapter = routineListAdapter

        routineListView.setOnItemClickListener { _, routineListView, i, _ ->
            val selectedWorkout = routineList[i]
            var elementsList = selectedWorkout.getElements()
            val elementsListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, elementsList)
            elementsListView.adapter = elementsListAdapter
            viewSwitcher.showNext()

            elementsListView.setOnItemClickListener { _, _, i, _ ->
                // TODO fill elementDetailsView with element details, image etc
                // slide view up
                if (!elementDetailsUp) {
                    elementDetailsView.visibility = View.VISIBLE
                    val animate = TranslateAnimation(
                            0F,  // fromXDelta
                            0F,  // toXDelta
                            elementDetailsView.height.toFloat(),  // fromYDelta
                            0F) // toYDelta
                    animate.duration = 500
                    animate.fillAfter = true
                    elementDetailsView.startAnimation(animate)
                }
            }

            elementsListView.onItemLongClickListener = OnItemLongClickListener { _, elementsListView, i, _ ->
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Are you sure you want to delete this element?")
                // if yes, deletes
                builder.setPositiveButton("OK") { _, _ ->
                    elementsListAdapter.remove(elementsList[i])
                    elementsListAdapter.notifyDataSetChanged()
                    Snackbar.make(elementsListView, "Element removed", Snackbar.LENGTH_LONG).setAction(
                            "Action",
                            null
                    ).show()
                }
                // if no, does nothing
                builder.setNegativeButton("CANCEL") { _, _ -> }
                builder.show()
                true
            }

            // 'add' button to add a new element
            fab.setOnClickListener() { view ->
                if (viewSwitcher.currentView.equals(elementsListView)) {
                    val elementBuilder = AlertDialog.Builder(this)
                    val elementInflater = layoutInflater
                    elementBuilder.setTitle("Please enter a name for your element: ")
                    val dialogLayout = elementInflater.inflate(R.layout.alert_dialog_with_edittext, null)
                    val editText2 = dialogLayout.findViewById<EditText>(R.id.editText)
                    elementBuilder.setView(dialogLayout)
                    elementBuilder.setPositiveButton("OK") { dialogInterface, i ->
                        elementsList.add(
                                RoutineElement(
                                        editText2.text.toString(),
                                        0
                                )
                        )
                        elementsListAdapter.notifyDataSetChanged()
                        Snackbar.make(elementsListView, "Added new element", Snackbar.LENGTH_LONG)
                            .setAction(
                                    "Action",
                                    null
                            ).show()
                    }
                    elementBuilder.setNegativeButton("CANCEL") { _, _ -> }
                    elementBuilder.show()
                }
            }
        }

        fun slideDownDetails(view: View) {
            val animate = TranslateAnimation(
                    0F,  // fromXDelta
                    0F,  // toXDelta
                    0F,  // fromYDelta
                    view.height.toFloat()) // toYDelta
            animate.duration = 500
            animate.fillAfter = true
            view.startAnimation(animate)
        }

        routineListView.onItemLongClickListener = OnItemLongClickListener { _, routineListView, i, _ ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure you want to delete this workout?")
            // if yes, deletes
            builder.setPositiveButton("OK") { _, _ ->
                routineListAdapter.remove(routineList[i])
                routineListAdapter.notifyDataSetChanged()
                Snackbar.make(routineListView, "Workout removed", Snackbar.LENGTH_LONG).setAction(
                        "Action",
                        null
                ).show()
            }
            // if no, does nothing
            builder.setNegativeButton("CANCEL") { _, _ -> }
            builder.show()
            true
        }

        // 'add' button to add a new workout
        fab.setOnClickListener { view ->
            if (viewSwitcher.currentView.equals(routineListView)) {
                // add stuff in here to input name of new routine to be added
                // ref: https://www.journaldev.com/309/android-alert-dialog-using-kotlin
                val workoutBuilder = AlertDialog.Builder(this)
                val workoutInflater = layoutInflater
                workoutBuilder.setTitle("Please enter a name for your workout: ")
                val dialogLayout = workoutInflater.inflate(R.layout.alert_dialog_with_edittext, null)
                val editText = dialogLayout.findViewById<EditText>(R.id.editText)
                workoutBuilder.setView(dialogLayout)
                workoutBuilder.setPositiveButton("OK") { dialogInterface, i ->
                    routineList.add(
                            Routine(
                                    mutableListOf<RoutineElement>(),
                                    editText.text.toString()
                            )
                    )
                    routineListAdapter.notifyDataSetChanged()
                    Snackbar.make(routineListView, "Added new workout", Snackbar.LENGTH_LONG)
                        .setAction(
                                "Action",
                                null
                        ).show()
                }
                workoutBuilder.setNegativeButton("CANCEL") { _, _ -> }
                workoutBuilder.show()
            }
        }
    }

    // slide the view from its current position to below itself
    private fun slideDownDetails(view: View) {
        val animate = TranslateAnimation(
                0F,  // fromXDelta
                0F,  // toXDelta
                0F,  // fromYDelta
                view.height.toFloat()) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    // if android back button is pressed while on the elements list, we want
    // to go back to the main screen where the routine list is displayed
    override fun onBackPressed() {
        if (viewSwitcher.currentView.equals(elementsListView)) {
            if (elementDetailsUp) {
                slideDownDetails(elementDetailsView)
                elementDetailsUp = false
            } else {viewSwitcher.showNext() }
        }
        else { this.finish() }
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