package com.snowy.snowyminigames.manager;

import com.snowy.snowyminigames.Arena;
import com.snowy.snowyminigames.SnowyMinigame;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private List<Arena> arenas = new ArrayList<>();

    public ArenaManager(SnowyMinigame minigame) {
        FileConfiguration config = minigame.getConfig();

        for (String str : config.getConfigurationSection("arenas.").getKeys(false)) {
            arenas.add(new Arena());
        }
    }
}
