package ru.bychek.asciiwars.game.configs

import ru.bychek.asciiwars.MainActivity
import ru.bychek.asciiwars.common.CommonUtils


object GameConfig {

    val shootSpeed: Long = CommonUtils().getProp(PropNames.SHOOT_SPEED.propName, MainActivity.applicationContext()).toLong()

    fun save(propName: String, value: String) {
        CommonUtils().overrideProp(propName, value, MainActivity.applicationContext())
    }

}