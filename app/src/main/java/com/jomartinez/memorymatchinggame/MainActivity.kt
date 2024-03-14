package com.jomartinez.memorymatchinggame

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jomartinez.memorymatchinggame.models.BoardSize
import com.jomartinez.memorymatchinggame.models.MemoryGame

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var clMain: ConstraintLayout
    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumberMoves: TextView
    private lateinit var tvNumberPairs: TextView

    private lateinit var memoryGame: MemoryGame
    private var boardSize: BoardSize = BoardSize.MEDIUM

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        clMain = findViewById(R.id.main_constraint_layout)
        rvBoard = findViewById(R.id.recycler_view_board)
        tvNumberMoves = findViewById(R.id.text_view_number_moves)
        tvNumberPairs = findViewById(R.id.text_view_number_pairs)

        setupBoard()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {
            R.id.mi_refresh -> {
                if( memoryGame.getNumberMoves() > 0 && !memoryGame.haveWonGame() ){
                    showAlertDialog(
                        "Quit your current game?",
                        null,
                        View.OnClickListener {
                            setupBoard()
                        }
                    )
                } else {
                    Log.i(TAG, "Refresh, on Options Item Selected")
                    setupBoard()
                }
                true
            }
            R.id.mi_resize -> {
//                Toast.makeText(this, "More", Toast.LENGTH_SHORT ).show()
                showResizeDialog()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun showResizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radio_group_size)
        when (boardSize) {
            BoardSize.EASY -> radioGroupSize.check(R.id.radio_button_easy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.radio_button_medium)
            BoardSize.HARD -> radioGroupSize.check(R.id.radio_button_hard)
        }
        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
            boardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.radio_button_easy -> BoardSize.EASY
                R.id.radio_button_medium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setupBoard()
        })
    }

    private fun showAlertDialog(
        title: String,
        view: View?,
        positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK"){_,_ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setupBoard() {
        memoryGame = MemoryGame(boardSize)

        when (boardSize) {
            BoardSize.EASY -> {
                tvNumberMoves.text = "Easy: 4 x 2"
                tvNumberPairs.text = "Pairs: 0/4"
            }
            BoardSize.MEDIUM -> {
                tvNumberMoves.text = "Medium: 6 x 3"
                tvNumberPairs.text = "Pairs: 0/9"
            }
            BoardSize.HARD -> {
                tvNumberMoves.text = "Hard: 6 x 4"
                tvNumberPairs.text = "Pairs: 0/12"
            }
        }

        adapter = MemoryBoardAdapter(
            this,
            boardSize,
            memoryGame.cards,
            object : MemoryBoardAdapter.CardClickListener{
                override fun onCardClick(position: Int) {
                    updateGameWithFlip(position)
                }
            }
        )
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.haveWonGame() ){
            Snackbar.make(clMain, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position) ){
            return
        }
        if (memoryGame.flipCard(position) ){
            tvNumberPairs.text = "Pairs: ${memoryGame.numberPairsFound} / ${boardSize.getNumberPairs()}"
            if (memoryGame.haveWonGame()) {
                Snackbar.make(clMain, "You won! Congratulations!", Snackbar.LENGTH_LONG).show()
            }
        }
        tvNumberMoves.text = "Moves: ${memoryGame.getNumberMoves()}"
        adapter.notifyDataSetChanged()
    }

}