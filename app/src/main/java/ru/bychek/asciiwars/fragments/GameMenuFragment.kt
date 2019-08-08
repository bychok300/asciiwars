package ru.bychek.asciiwars.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ru.bychek.asciiwars.R
import ru.bychek.asciiwars.fragments.utils.FragmentHelper

class GameMenuFragment : Fragment() {

    companion object {
        fun newInstance(): GameMenuFragment = GameMenuFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.game_menu_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val withBotBtn: Button = view!!.findViewById(R.id.play_with_bot)

        withBotBtn.setOnClickListener {
            GameFragment.withBotMode = true
            FragmentHelper().performFragmentReplace(context, GameFragment.newInstance(), R.id.container)
        }

        val withFriend: Button = view!!.findViewById(R.id.play_with_friend)
        withFriend.setOnClickListener {
            GameWithFriendFragment.withFriendMode = true
            FragmentHelper().performFragmentReplace(context, GameWithFriendFragment.newInstance(), R.id.container)
        }

    }
}