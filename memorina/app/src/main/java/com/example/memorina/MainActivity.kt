package com.example.memorina

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val GRID_SIZE = 4
    }

    private val cardsImages = listOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4,
        R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8
    )
    private var cardsIndexes = (1..8).flatMap { listOf(it, it) }.shuffled().toTypedArray()
    private var firstClick: ImageView? = null
    private var pairsFound: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resetBoard()
    }

    private fun resetBoard() {
        cardsIndexes = (1..8).flatMap { listOf(it, it) }.shuffled().toTypedArray()
        pairsFound = 0

        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1f

        for (idx in 1..(GRID_SIZE)) {
            layout.addView(LinearLayout(applicationContext).apply {
                orientation = LinearLayout.HORIZONTAL
                (1..GRID_SIZE).map {
                    addView(ImageView(applicationContext).apply {
                        setImageResource(R.drawable.card_back)
                        layoutParams = params
                        tag = "${cardsIndexes[(idx - 1) * GRID_SIZE + it - 1]}"
                        setOnClickListener { cardsListener(this) }
                    })
                }
            })
        }

        setContentView(layout)
    }

    private suspend fun setBackgroundWithDelay(v: ImageView) {
        v.setImageResource(cardsImages[v.tag.toString().filter { it.isDigit() }.toInt() - 1])
        delay(500)

        if (firstClick == null) {
            firstClick = v
        } else {
            if (firstClick!!.tag == v.tag) {
                firstClick!!.visibility = View.INVISIBLE
                v.visibility = View.INVISIBLE
                firstClick = null
                pairsFound++

                if (pairsFound == GRID_SIZE * GRID_SIZE / 2) {
                    Toast.makeText(this@MainActivity, "YOU WON!", Toast.LENGTH_SHORT).show()
                    resetBoard()
                }
            } else {
                listOf(firstClick, v).forEach {
                    it?.setImageResource(R.drawable.card_back)
                }
                firstClick = null
            }
        }
    }

    private val cardsListener: (ImageView) -> Unit = {
        lifecycleScope.launch(Dispatchers.Main) {
            setBackgroundWithDelay(it)
        }
    }
}