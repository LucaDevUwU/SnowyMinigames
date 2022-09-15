package com.snowy.snowyminigames.command;

import com.snowy.snowyminigames.GameState;
import com.snowy.snowyminigames.SnowyMinigame;
import com.snowy.snowyminigames.instance.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ArenaCommand implements CommandExecutor {

    private SnowyMinigame minigame;

    public ArenaCommand(SnowyMinigame minigame) {
        this.minigame = minigame;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                player.sendMessage(ChatColor.GREEN + "These are the available arenas:");
                for (Arena arena : minigame.getArenaManager().getArenas()) {
                    player.sendMessage(ChatColor.GREEN + "- " + arena.getId() + "(" + arena.getState().name() + ")");
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
                Arena arena = minigame.getArenaManager().getArena(player);
                if (arena != null) {
                    player.sendMessage(ChatColor.RED + "You left the arena.");
                    arena.removePlayer(player);
                } else {
                    player.sendMessage(ChatColor.RED + "You are not in an arena");
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
                if (minigame.getArenaManager().getArena(player) != null) {
                    player.sendMessage(ChatColor.RED + "You are already playing in an arena");
                    return false;
                }

                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "You specified an invalid arena ID");
                    return false;
                }

                if (id >= 0 && id < minigame.getArenaManager().getArenas().size()) {
                    Arena arena = minigame.getArenaManager().getArena(id);
                    if (arena.getState() == GameState.RECRUITING || arena.getState() == GameState.COUNTDOWN) {
                        player.sendMessage(ChatColor.GREEN + "You are now playing in Arena" + id + ".");
                        arena.addPlayer(player);
                    } else {
                        player.sendMessage(ChatColor.RED + "You can't join this arena yet");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You specified an invalid arena ID");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Invalid usage! These are the available commands:");
                player.sendMessage(ChatColor.RED + "- /arena list");
                player.sendMessage(ChatColor.RED + "- /arena join");
                player.sendMessage(ChatColor.RED + "- /arena leave <id>");

            }
        }
        return false;
    }
}