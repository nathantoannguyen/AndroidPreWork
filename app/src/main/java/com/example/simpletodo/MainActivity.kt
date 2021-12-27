package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    // var can be reassigned
    // val can not be reassigned

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove item from list
                listOfTasks.removeAt(position)
                // Notify adapter that data set has changed
                adapter.notifyDataSetChanged()
                // update list
                saveItems()
            }
        }

        // findViewById<Button>(R.id.button).setOnClickListener {
            // Code executed when user clicks on button }

        // Populate list of data when app is open
        loadItems()

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up button and input field so user can entire task and add it to list

        var inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get a reference to the button
        // and set an onclicklistener
        findViewById<Button>(R.id.button).setOnClickListener{
            // Grab text user inputted in @id/addTaskField
            val userInputtedTask = findViewById<EditText>(R.id.addTaskField).text.toString()

            // Add string to list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            // Notify data adapter that data has been updated
            // Adds data to end of list
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // Reset text field
            inputTextField.setText("")

            // Save items into file
            saveItems()
        }
    }

    // Save user input data locally
    // Save data through writing and reading from files

    // Get file we need
    fun getDataFile(): File {

        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }
    // Load items by reading every line in file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    // Save items by writing them into data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }
}