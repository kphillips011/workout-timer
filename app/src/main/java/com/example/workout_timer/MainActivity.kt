package com.example.workout_timer

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var routineListView: ListView
    private lateinit var elementsListView: ListView
    private lateinit var elementDetailsView: View
    private lateinit var detailsText: TextView
    private lateinit var detailsImage: ImageView
    private lateinit var routineList: MutableList<Routine>
    private lateinit var elementsList: MutableList<RoutineElement>
    private lateinit var routineListAdapter: ArrayAdapter<Routine>
    private lateinit var elementsListAdapter: ArrayAdapter<RoutineElement>
    private lateinit var viewSwitcher: ViewSwitcher
    private lateinit var fab: ExtendedFloatingActionButton
    private lateinit var presetFab: FloatingActionButton
    private lateinit var customFab: FloatingActionButton
    private lateinit var presetActionText: TextView
    private lateinit var customActionText: TextView
    private lateinit var playButton: MenuItem
    private var elementDetailsUp: Boolean = false
    private var allFabsVisible: Boolean = false
    private val pickImage = 0
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        fab = findViewById(R.id.fab)
        presetFab = findViewById(R.id.add_preset_fab)
        customFab = findViewById(R.id.add_custom_fab)
        presetActionText = findViewById(R.id.add_preset_action_text)
        customActionText = findViewById(R.id.add_custom_action_text)
        closeFab()

        // ref: https://tutorialwing.com/android-viewswitcher-using-kotlin-example/
        viewSwitcher = findViewById<ViewSwitcher>(R.id.viewSwitcher)
        val `in` = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        viewSwitcher.inAnimation = `in`
        val out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
        viewSwitcher.outAnimation = out

        // ref: https://stackoverflow.com/questions/19765938/show-and-hide-a-view-with-a-slide-up-down-animation
        elementDetailsView = findViewById<LinearLayout>(R.id.element_details)
        elementDetailsView.visibility = View.INVISIBLE
        detailsText = findViewById(R.id.details_text_view);
        detailsImage = findViewById(R.id.details_image_view)
        detailsText.visibility = View.GONE
        detailsImage.visibility = View.GONE

        // ref: https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
        //      https://www.raywenderlich.com/1364094-android-fragments-tutorial-an-introduction-with-kotlin
        // create routineList, a Routine object
        routineListView = findViewById<ListView>(R.id.routine_list)
        elementsListView = findViewById<ListView>(R.id.routine_elements)
        routineList = mutableListOf<Routine>()


        // add elements to routineList, sample data
        val defaultWorkoutsArray: TypedArray = resources.obtainTypedArray(R.array.default_workout_names)
        var defaultWorkoutNames: MutableList<String?> = mutableListOf<String?>()
        var defaultWorkout: TypedArray

        for (i in 0 until defaultWorkoutsArray.length()) {
            val res = defaultWorkoutsArray.getResourceId(i, -1)
            if (res < 0) { continue }
            defaultWorkout = resources.obtainTypedArray(res)
            defaultWorkoutNames.add(defaultWorkout.getString(0))
        }

        for (workout in defaultWorkoutNames) {
            routineList.add(
                Routine(
                    mutableListOf(RoutineElement("element", 10)),
                    workout as String
                )
            )
        }

        // adapter to adapt the routineList into our list on the main screen
        routineListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, routineList)
        routineListView.adapter = routineListAdapter

        routineListView.setOnItemClickListener { _, routineListView, i, _ ->
            playButton.isVisible = true
            if(allFabsVisible) { closeFab() }
            val selectedWorkout = routineList[i]
            // set toolbar title to be the routine's name
            (this as? AppCompatActivity)?.supportActionBar?.title = selectedWorkout.getName()
            elementsList = selectedWorkout.getElements()
            elementsListAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                elementsList
            )
            elementsListView.adapter = elementsListAdapter
            viewSwitcher.showNext()

            elementsListView.setOnItemClickListener { _, _, i, _ ->
                // TODO fill elementDetailsView with element details, image etc
                val selectedElement = elementsList[i]
                detailsText.text = selectedElement.getName() + "\nfor " + selectedElement.getDuration() + " seconds"
                detailsImage.setImageResource(R.drawable.downward_dog)

                // slide view up
                if (!elementDetailsUp) {
                    if (allFabsVisible) {
                        closeFab()
                    }
                    fab.visibility = View.GONE
                    detailsText.visibility = View.VISIBLE
                    detailsImage.visibility = View.VISIBLE
                    elementDetailsView.visibility = View.VISIBLE
                    slideUpDetails(elementDetailsView)
                    elementDetailsUp = true
                }
            }

            elementsListView.onItemLongClickListener = OnItemLongClickListener { _, elementsListView, i, _ ->
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Are you sure you want to delete this element?")
                // if yes, deletes
                builder.setPositiveButton("OK") { _, _ ->
                    elementsListAdapter.remove(elementsList[i])
                    elementsListAdapter.notifyDataSetChanged()
                    Snackbar.make(elementsListView, "Element removed", Snackbar.LENGTH_LONG)
                            .setAction("DISMISS") {}
                            .show()
                }
                // if no, does nothing
                builder.setNegativeButton("CANCEL") { _, _ -> }
                builder.show()
                true
            }
        }

        routineListView.onItemLongClickListener = OnItemLongClickListener { _, routineListView, i, _ ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure you want to delete this workout?")
            // if yes, deletes
            builder.setPositiveButton("OK") { _, _ ->
                routineListAdapter.remove(routineList[i])
                routineListAdapter.notifyDataSetChanged()
                Snackbar.make(routineListView, "Workout removed", Snackbar.LENGTH_LONG)
                        .setAction("DISMISS") {}
                        .show()
            }
            // if no, does nothing
            builder.setNegativeButton("CANCEL") { _, _ -> }
            builder.show()
            true
        }

        fab.setOnClickListener { view ->
            if (viewSwitcher.currentView.equals(routineListView)) {
                if (!allFabsVisible) {
                    expandFab("Add Preset Workout", "Add Custom Workout")
                } else { closeFab() }
            } else if (viewSwitcher.currentView.equals(elementsListView)) {
                if (!allFabsVisible) {
                    expandFab("Add Preset Element", "Add Custom Element")
                } else { closeFab() }
            }
        }

        // 'add' button to add a new workout or element

        customFab.setOnClickListener { view ->
            if (viewSwitcher.currentView.equals(routineListView)) {
                // add stuff in here to input name of new routine to be added
                // ref: https://www.journaldev.com/309/android-alert-dialog-using-kotlin
                val workoutBuilder = AlertDialog.Builder(this)
                val workoutInflater = layoutInflater
                workoutBuilder.setTitle("Please enter a name for your workout: ")
                val dialogLayout = workoutInflater.inflate(R.layout.alert_dialog_edittext, null)
                var editTextName = dialogLayout.findViewById<EditText>(R.id.editTextName)
                var editTextDuration = dialogLayout.findViewById<EditText>(R.id.editTextDuration)
                editTextDuration.visibility = View.GONE
                workoutBuilder.setView(dialogLayout)
                workoutBuilder.setPositiveButton("OK") { dialogInterface, i ->
                    routineList.add(
                        Routine(
                            mutableListOf<RoutineElement>(),
                            editTextName.text.toString()
                        )
                    )
                    routineListAdapter.notifyDataSetChanged()
                    Snackbar.make(routineListView, "Added new workout", Snackbar.LENGTH_LONG)
                            .setAction("DISMISS") {}
                            .show()
                }
                workoutBuilder.setNegativeButton("CANCEL") { _, _ -> }
                workoutBuilder.show()
            }
            else if (viewSwitcher.currentView.equals(elementsListView)) {
                val elementBuilder = AlertDialog.Builder(this)
                val elementInflater = layoutInflater
                elementBuilder.setTitle("Please enter a name and duration for your element: ")
                val dialogLayout = elementInflater.inflate(R.layout.alert_dialog_edittext, null)
                var editTextName = dialogLayout.findViewById<EditText>(R.id.editTextName)
                var editTextDuration = dialogLayout.findViewById<EditText>(R.id.editTextDuration)
                elementBuilder.setView(dialogLayout)
                elementBuilder.setPositiveButton("OK") { dialogInterface, i ->
                    elementsList.add(
                        RoutineElement(
                            editTextName.text.toString(),
                            editTextDuration.text.toString().toInt()
                        )
                    )
                    elementsListAdapter.notifyDataSetChanged()
                    Snackbar.make(elementsListView, "Added new element", Snackbar.LENGTH_LONG)
                            .setAction("DISMISS") {}
                            .show()
                }
                elementBuilder.setNegativeButton("CANCEL") { _, _ -> }
                elementBuilder.show()
            }
        }

        presetFab.setOnClickListener { view ->
            // TODO
            val workoutNamesArray: TypedArray = resources.obtainTypedArray(R.array.default_workout_names)
            var itemNames: MutableList<String?> = mutableListOf<String?>()
            var item: TypedArray

            for (i in 0 until workoutNamesArray.length()) {
                val res = workoutNamesArray.getResourceId(i, -1)
                if (res < 0) { continue }
                item = resources.obtainTypedArray(res)
                itemNames.add(item.getString(0))
            }

            val elementNamesArray: TypedArray = resources.obtainTypedArray(R.array.default_element_names)
            var elementNames: MutableList<String?> = mutableListOf<String?>()
            var itemElements: MutableList<RoutineElement> = mutableListOf<RoutineElement>()
            var arrayElement: TypedArray

            for (i in 0 until elementNamesArray.length()) {
                val elementRes = elementNamesArray.getResourceId(i, -1)
                if (elementRes < 0) { continue }
                arrayElement = resources.obtainTypedArray(elementRes)
                elementNames.add(arrayElement.getString(0))
            }

            if (viewSwitcher.currentView.equals(routineListView)) {
                val workoutBuilder = AlertDialog.Builder(this)
                val workoutInflater = layoutInflater
                workoutBuilder.setTitle("Please select a workout: ")
                val dialogLayout = workoutInflater.inflate(R.layout.alert_dialog_spinner, null)

                var spinner: Spinner = dialogLayout.findViewById(R.id.preset_spinner)
                val spinnerAdapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, itemNames
                ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter
                    }

                spinner.onItemSelectedListener = this
                workoutBuilder.setView(dialogLayout)
                // TODO add elements of preset
                workoutBuilder.setPositiveButton("OK") { dialogInterface, i ->
                    val workoutName = spinner.selectedItem
                    /*
                    var elementItem: TypedArray
                    for (e in 1 until workoutName.length()) {
                        val elementRes = item.getResourceId(e, -1)
                        if (elementRes < 0) { continue }
                        elementItem = resources.obtainTypedArray(elementRes)
                    }
                     */
                    routineList.add(Routine(itemElements, workoutName as String))
                    routineListAdapter.notifyDataSetChanged()
                    Snackbar.make(routineListView, "Added new workout", Snackbar.LENGTH_LONG)
                        .setAction("DISMISS") {}
                        .show()
                }
                workoutBuilder.setNegativeButton("CANCEL") { _, _ -> }
                workoutBuilder.show()
            }
            else if (viewSwitcher.currentView.equals(elementsListView)) {
                val elementBuilder = AlertDialog.Builder(this)
                val elementInflater = layoutInflater
                elementBuilder.setTitle("Please select a workout element: ")
                val dialogLayout = elementInflater.inflate(R.layout.alert_dialog_spinner, null)

                var spinner: Spinner = dialogLayout.findViewById(R.id.preset_spinner)
                val spinnerAdapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, elementNames
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }

                spinner.onItemSelectedListener = this
                elementBuilder.setView(dialogLayout)
                elementBuilder.setPositiveButton("OK") { dialogInterface, i ->
                    // TODO add to elements list with specific duration
                    val elementName = spinner.selectedItem
                    val elementDuration = 0
                    elementsList.add(RoutineElement(elementName as String, elementDuration))
                    elementsListAdapter.notifyDataSetChanged()
                    Snackbar.make(elementsListView, "Added new element", Snackbar.LENGTH_LONG)
                        .setAction("DISMISS") {}
                        .show()
                }
                elementBuilder.setNegativeButton("CANCEL") { _, _ -> }
                elementBuilder.show()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        parent?.getItemAtPosition(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    private fun expandFab(pt: String, ct: String) {
        presetActionText.text = pt
        customActionText.text = ct
        presetFab.show()
        customFab.show()
        presetActionText.visibility = View.VISIBLE
        customActionText.visibility = View.VISIBLE
        fab.extend()
        allFabsVisible = true
    }

    private fun closeFab() {
        presetFab.hide()
        customFab.hide()
        presetActionText.visibility = View.GONE
        customActionText.visibility = View.GONE
        fab.shrink()
        allFabsVisible = false
    }

    // slide the view from its current position to below itself
    private fun slideDownDetails(view: View) {
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            0F,  // fromYDelta
            view.height.toFloat()
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        elementDetailsView.visibility = View.INVISIBLE
        detailsImage.isClickable = false
    }

    private fun slideUpDetails(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            view.height.toFloat(),  // fromYDelta
            0F
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        detailsImage.isClickable = true
    }

    // if android back button is pressed while on the elements list, we want
    // to go back to the main screen where the routine list is displayed
    override fun onBackPressed() {
        if(allFabsVisible) { closeFab() }
        if (viewSwitcher.currentView.equals(elementsListView)) {
            if (elementDetailsUp) {
                slideDownDetails(elementDetailsView)
                elementDetailsUp = false
                fab.visibility = View.VISIBLE
            } else {
                viewSwitcher.showNext()
                // set title on toolbar back to workout timer
                (this as? AppCompatActivity)?.supportActionBar?.title = title
                invalidateOptionsMenu()
            }
        }
        else { this.finish() }
    }

    // ref: https://stackoverflow.com/questions/14433096/allow-the-user-to-insert-an-image-in-android-app
    //      https://www.tutorialspoint.com/how-to-pick-an-image-from-an-image-gallery-on-android-using-kotlin
    fun selectImage(view: View) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            detailsImage.setImageURI(imageUri)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        playButton = menu.findItem(R.id.play_button)
        playButton.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
            // TODO play a workout when play button selected. Need Timer.java completed first
        }
    }
}