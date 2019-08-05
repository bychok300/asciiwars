package ru.bychek.asciiwars

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import ru.bychek.asciiwars.fragments.GameFragment
import ru.bychek.asciiwars.fragments.GameMenuFragment
import ru.bychek.asciiwars.fragments.MenuFragment
import ru.bychek.asciiwars.fragments.utils.FragmentHelper


class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragmentHelper = FragmentHelper()

        when (item.itemId) {
            R.id.navigation_menu -> {
                fragmentHelper.performFragmentReplace(this@MainActivity, MenuFragment.newInstance(), R.id.container )
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_play -> {
                fragmentHelper.performFragmentReplace(this@MainActivity, GameMenuFragment.newInstance(), R.id.container )
                return@OnNavigationItemSelectedListener true
            }
//            R.id.navigation_notifications -> {
//
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        // set default selected menu item
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }
}
