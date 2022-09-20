package com.snowy.snowyminigames.manager;

import com.snowy.snowyminigames.SnowyMinigame;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LangManager {
    private static YamlConfiguration lang;

    public static void setupLangFile(SnowyMinigame minigame) {
        File file = new File(minigame.getDataFolder(), "lang.yml");
        if (!file.exists()) {
            minigame.saveResource("lang.yml", false);
        }
        lang = YamlConfiguration.loadConfiguration(file);
    }

    public static String getLeaveArena() { return  lang.getString("leave-arena"); }
}
