package com.jomartinez.memorymatchinggame

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jomartinez.memorymatchinggame.models.BoardSize
import com.jomartinez.memorymatchinggame.models.MemoryCard
import com.jomartinez.memorymatchinggame.models.MemoryGame
import com.jomartinez.memorymatchinggame.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumberMoves: TextView
    private lateinit var tvNumberPairs: TextView

    private lateinit var memoryGame: MemoryGame
    private var boardSize: BoardSize = BoardSize.HARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.haveWonGame() ){
            return
        }
        if (memoryGame.isCardFaceUp(position) ){
            return
        }
        memoryGame.flipCard(position)
        adapter.notifyDataSetChanged()
    }
}