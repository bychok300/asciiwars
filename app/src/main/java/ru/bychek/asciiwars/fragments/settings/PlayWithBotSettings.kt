package ru.bychek.asciiwars.fragments.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.bychek.asciiwars.R
import ru.bychek.asciiwars.fragments.MenuFragment

class PlayWithBotSettings : Fragment() {
    companion object {
        fun newInstance(): PlayWithBotSettings = PlayWithBotSettings()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.play_with_bot_settings_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val text: TextView = view!!.findViewById(R.id.textView)
    }
}