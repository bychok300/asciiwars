package ru.bychek.asciiwars.fragments.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import ru.bychek.asciiwars.R
import ru.bychek.asciiwars.game.configs.GameConfig
import ru.bychek.asciiwars.game.configs.PropNames

class PlayWithBotSettings : Fragment() {

    private val TAG = "PlayWithBotSettings"

    companion object {
        fun newInstance(): PlayWithBotSettings = PlayWithBotSettings()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.play_with_bot_settings_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val shootSpeedRsTextView: TextView = view!!.findViewById(R.id.speedSeekRs)
        shootSpeedRsTextView.text = GameConfig.shootSpeed.toString()

        val shootSpeedControl: SeekBar = view!!.findViewById(R.id.shootSpeedSeekBar)
        val shootSpeedInitialState = shootSpeedRsTextView.text.toString().toInt()
        shootSpeedControl.progress = shootSpeedInitialState
        shootSpeedControl.incrementProgressBy(10)
        shootSpeedControl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                val min = 100
                val max = 1000
                if (progress in min..max && progress >= shootSpeedInitialState) {
                    var rs = progress + 10
                    shootSpeedRsTextView.text = rs.toString()
                } else if (progress in min..max && progress <= shootSpeedInitialState ){
                    var rs = progress - 10
                    shootSpeedRsTextView.text = rs.toString()
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
//                println()
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
//                println()
            }

        })

        val applySettingsBtn: Button = view!!.findViewById(R.id.applySettings)

        applySettingsBtn.setOnClickListener {
            Log.d(TAG, "settings applied")
            GameConfig.save(PropNames.SHOOT_SPEED.propName, shootSpeedRsTextView.text.toString())
        }
    }
}