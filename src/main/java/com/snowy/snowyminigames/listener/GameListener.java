package com.snowy.snowyminigames.listener;

import com.snowy.snowyminigames.GameState;
import com.snowy.snowyminigames.SnowyMinigame;
import com.snowy.snowyminigames.instance.Arena;
import com.snowy.snowyminigames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GameListener implements Listener {

    private SnowyMinigame minigame;
    public GameListener (SnowyMinigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

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
