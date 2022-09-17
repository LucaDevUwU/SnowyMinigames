package com.snowy.snowyminigames.kit.type;

import com.snowy.snowyminigames.SnowyMinigame;
import com.snowy.snowyminigames.kit.Kit;
import com.snowy.snowyminigames.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class MinerKit extends Kit {
    public MinerKit(SnowyMinigame minigame, UUID uuid) {
        super(minigame, KitType.MINER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 3));
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        if (uuid.equals(e.getPlayer().getUniqueId())) {
            System.out.println(e.getPlayer().getName() + " the Miner just broke a block");
        }
    }
}
