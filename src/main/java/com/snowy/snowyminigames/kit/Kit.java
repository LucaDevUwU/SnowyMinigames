package com.snowy.snowyminigames.kit;

import com.snowy.snowyminigames.SnowyMinigame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public abstract class Kit implements Listener {

    protected KitType type;
    protected UUID uuid;

    public Kit(SnowyMinigame minigame, KitType type, UUID uuid) {
        this.type = type;
        this.uuid = uuid;

        Bukkit.getPluginManager().registerEvents(this, minigame);
    }

    public UUID getUUID() { return uuid; }
    public KitType getType() { return type; }

    public abstract void onStart(Player player);

    public void remove() {
        HandlerList.unregisterAll(this);
    }
}
