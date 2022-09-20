package com.snowy.snowyminigames.listener;

import com.snowy.snowyminigames.GameState;
import com.snowy.snowyminigames.SnowyMinigame;
import com.snowy.snowyminigames.instance.Arena;
import com.snowy.snowyminigames.kit.KitType;
import com.snowy.snowyminigames.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.EquipmentSlot;

public class GameListener implements Listener {

    private SnowyMinigame minigame;
    public GameListener (SnowyMinigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        Arena arena = minigame.getArenaManager().getArena(e.getWorld());
        if (arena != null) {
            arena.toggleCanJoin();
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {
        if (e.getHand().equals(EquipmentSlot.HAND) && e.hasBlock() && e.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN)) {
            Arena arena = minigame.getArenaManager().getArena(e.getClickedBlock().getLocation());
            if (arena != null) {
                Bukkit.dispatchCommand(e.getPlayer(), "arena join " + arena.getId());
            }
        }
    }
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getInventory() != null && e.getCurrentItem() != null && e.getView().getTitle().contains("Team Selection")) {
            Team team = Team.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

            Arena arena = minigame.getArenaManager().getArena(player);
            if (arena != null) {
                if (arena.getTeam(player) == team) {
                    player.sendMessage(ChatColor.RED + "You are already on this team");
                } else {
                    player.sendMessage(ChatColor.AQUA + "You are now on " + team.getDisplay() + ChatColor.AQUA + " team!");
                    arena.setTeam(player, team);
                }
            }
            player.closeInventory();
            e.setCancelled(true);
        }

        if (e.getView().getTitle().contains("Kit Selection") && e.getInventory() != null && e.getCurrentItem() != null) {
            e.setCancelled(true);
            KitType type = KitType.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

            Arena arena = minigame.getArenaManager().getArena(player);
            if (arena != null) {
                KitType activated = arena.getKitType(player);
                if (activated != null && activated == type) {
                    player.sendMessage(ChatColor.RED + "You already have this kit equipped");
                } else {
                    player.sendMessage(ChatColor.RED + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit!");
                    arena.setKit(player.getUniqueId(), type);
                }
            }

        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Arena arena = minigame.getArenaManager().getArena(e.getPlayer());
        if (arena != null && arena.getState().equals(GameState.LIVE)) {
            arena.getGame().addPoint(e.getPlayer());
        }

    }
}
