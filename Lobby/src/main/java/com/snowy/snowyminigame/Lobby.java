package com.snowy.snowyminigame;

import com.snowy.snowyminigame.manager.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lobby extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);
    }
}
