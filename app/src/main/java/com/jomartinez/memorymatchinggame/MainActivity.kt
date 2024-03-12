package com.jomartinez.memorymatchinggame

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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
    private var boardSize: BoardSize = BoardSize.HARD

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)


        clMain = findViewById(R.id.main_constraint_layout)
        rvBoard = findViewById(R.id.recycler_view_board)
        tvNumberMoves = findViewById(R.id.text_view_number_moves)
        tvNumberPairs = findViewById(R.id.text_view_number_pairs)

        memoryGame = MemoryGame(boardSize)

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