package com.snowy.snowyminigames;

import com.snowy.snowyminigames.command.ArenaCommand;
import com.snowy.snowyminigames.listener.ConnectListener;
import com.snowy.snowyminigames.listener.GameListener;
import com.snowy.snowyminigames.manager.ArenaManager;
import com.snowy.snowyminigames.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SnowyMinigame extends JavaPlugin {

    private ArenaManager arenaManager;
    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);
        arenaManager = new ArenaManager(this);

        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(this), this);

        getCommand("arena").setExecutor(new ArenaCommand(this));
    }

    public ArenaManager getArenaManager() { return arenaManager; }
}
