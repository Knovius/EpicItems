package me.knovius.epicitems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.knovius.epicitems.EpicItems;
import me.knovius.epicitems.manager.EpicItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ConsumeListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();

        if (itemStack == null || itemStack.getType() == null || itemStack.getType() == Material.AIR) return;

        if (event.getAction().toString() == "RIGHT_CLICK_AIR" || event.getAction().toString() == "RIGHT_CLICK_BLOCK") {

            NBTItem nbtItem = new NBTItem(itemStack);
          if (nbtItem.hasTag("epicItem")) {
                EpicItem epicItem = EpicItems.getInstance().getItemManager().getEpicItem(nbtItem.getString("epicItem"));

                if (epicItem == null) return;

                if (epicItem.isCancelEvents()) {
                    event.setCancelled(true);
                }
                if (epicItem == null) return;

                if (event.getHand() == EquipmentSlot.OFF_HAND) {
                    EpicItems.getInstance().getItemManager().consumeOffHandItem(event.getPlayer(), epicItem, event.getItem());
                    return;
                }

                EpicItems.getInstance().getItemManager().consumeItem(event.getPlayer(), epicItem, event.getItem());
            }
        }
    }
}
