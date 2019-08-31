package ru.bychek.asciiwars

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import ru.bychek.asciiwars.common.CommonUtils
import ru.bychek.asciiwars.fragments.GameMenuFragment
import ru.bychek.asciiwars.fragments.MenuFragment
import ru.bychek.asciiwars.fragments.utils.FragmentHelper


class MainActivity : AppCompatActivity() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

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
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareEnviroment()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        // set default selected menu item
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navView.selectedItemId = R.id.navigation_play

    }

    fun prepareEnviroment(){
        CommonUtils().copyResources(R.raw.with_bot_cfg)
    }
}
