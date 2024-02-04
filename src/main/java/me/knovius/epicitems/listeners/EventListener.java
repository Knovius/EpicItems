package me.knovius.epicitems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.knovius.epicitems.EpicItems;
import me.knovius.epicitems.manager.EpicItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();

        if (itemStack == null || itemStack.getType() == null || itemStack.getType() == Material.AIR) return;

        NBTItem nbtItem = new NBTItem(itemStack);
        EpicItem epicItem = EpicItems.getInstance().getItemManager().getEpicItem(nbtItem.getString("epicItem"));
        if (nbtItem.hasTag("epicItem") && epicItem.isCancelEvents()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {

        for (ItemStack itemStack: event.getInventory().getStorageContents()) {

            if (itemStack == null || itemStack.getType() == null || itemStack.getType() == Material.AIR) continue;

            NBTItem nbtItem = new NBTItem(itemStack);
            if (!nbtItem.hasNBTData() || !nbtItem.hasTag("epicItem")) return;
            EpicItem epicItem = EpicItems.getInstance().getItemManager().getEpicItem(nbtItem.getString("epicItem"));
            //Working with an item now
            if (epicItem.isCancelEvents()) {
                event.getInventory().setResult(null);

            }
        }
    }

}
