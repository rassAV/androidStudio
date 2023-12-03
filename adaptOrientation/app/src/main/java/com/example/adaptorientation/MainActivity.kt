package com.example.adaptorientation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast

class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var pictures: Array<Int>
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pictures = arrayOf(R.drawable.car1, R.drawable.car2, R.drawable.car3)

        adapter = ArrayAdapter.createFromResource(this, R.array.pictures, R.layout.item)
        val spinner = findViewById<Spinner>(R.id.pictures_list)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    fun onChangePictureClick(v: View) {
        val iv = findViewById<ImageView>(R.id.picture)
        position = (position + 1) % 3
        iv.setImageResource(pictures[position])
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Toast.makeText(this, "выбран элемент $pos", Toast.LENGTH_SHORT ).show()
        val iv = findViewById<ImageView>(R.id.picture)
        position = pos
        iv.setImageResource(pictures[position])
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "не выбран элемент", Toast.LENGTH_SHORT ).show()
    }
}