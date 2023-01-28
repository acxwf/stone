package com.github.acxwf.stone

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class StoneMain : JavaPlugin() {

    override fun onEnable() {
        logger.info("[STN] ..!")
        Bukkit.getPluginManager().registerEvents(StoneListenerEvent(), this)
    }

    override fun onDisable() {
        logger.info("[STN] ..?")
    }

}