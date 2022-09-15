package com.snowy.snowyminigames.instance;

import com.snowy.snowyminigames.GameState;
import com.snowy.snowyminigames.SnowyMinigame;
import com.snowy.snowyminigames.manager.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    private SnowyMinigame minigame;
    private Arena arena;
    private int countdownSeconds;

    public Countdown(SnowyMinigame minigame, Arena arena) {
        this.minigame = minigame;
        this.arena = arena;
        this.countdownSeconds = ConfigManager.getCountdownSeconds();
    }

    public void start() {
        arena.setState(GameState.COUNTDOWN);
        runTaskTimer(minigame, 0, 20);
    }

    @Override
    public void run() {
        if (countdownSeconds == 0) {
            cancel();
            arena.start();
            return;
        }

        if (countdownSeconds <= 10 || countdownSeconds % 15 == 0) {
            arena.sendMessage(ChatColor.GREEN + "Game will start in " + countdownSeconds + " second" + (countdownSeconds == 1 ? "" : "s") + ".");
        }

        arena.sendTitle(ChatColor.GREEN.toString() + countdownSeconds + " second" + (countdownSeconds == 1 ? "" : "s"), ChatColor.GRAY + "until game starts");
        countdownSeconds--;
    }

}
