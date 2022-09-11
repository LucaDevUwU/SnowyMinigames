package com.snowy.snowyminigames;

import com.snowy.snowyminigames.manager.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SnowyMinigame extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);

    }
}
