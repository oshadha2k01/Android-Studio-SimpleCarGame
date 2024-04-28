package com.example.cargame

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView


class GameActivity : AppCompatActivity(), CarGameTask {
    private lateinit var rootLayout : LinearLayout
    private lateinit var startBtn :Button
    private lateinit var mGameView : GameView
    private lateinit var score:TextView
    private lateinit var highScoreText: TextView
    private var highestScore : Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        sharedPreferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        highestScore = sharedPreferences.getInt("high_Score", 0)


        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        highScoreText = findViewById(R.id.highscore)
        mGameView = GameView(this, this)

        startBtn.setOnClickListener {
            startCarGame()
        }
        updateCarHighScoreText()
    }

    private fun startCarGame() {
        mGameView = GameView(this, this)
        mGameView.setBackgroundResource(R.drawable.road)
        rootLayout.addView(mGameView)
        startBtn.visibility = View.GONE
        score.visibility = View.GONE

        val previousCarScore = sharedPreferences.getInt("current_score", 0)
        score.text = "Score: $previousCarScore"

        updateCarHighScoreText()
    }

    private fun updateCarHighScoreText() {
        highScoreText.text = "High Score: $highestScore"

    }

    override fun closeGame(mScore: Int) {

        if(mScore > highestScore) {
            highestScore = mScore
            sharedPreferences.edit().putInt("high_score", highestScore).apply()
            updateCarHighScoreText()
        }

        sharedPreferences.edit().putInt("current_score", mScore).apply()
        score.text = "Score : $mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        startCarGame()
    }
}
