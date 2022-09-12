package com.snowy.snowyminigames;

import com.snowy.snowyminigames.manager.ArenaManager;
import com.snowy.snowyminigames.manager.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SnowyMinigame extends JavaPlugin {

    private ArenaManager arenaManager;
    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);

        arenaManager = new ArenaManager(this);
    }

    public ArenaManager getArenaManager() { return arenaManager; }
}
