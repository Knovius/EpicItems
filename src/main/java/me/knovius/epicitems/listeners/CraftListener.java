package me.knovius.epicitems.listeners;

import me.knovius.epicitems.EpicItems;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        for (ItemStack itemStack: event.getInventory().getMatrix()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;
            if (EpicItems.getInstance().getItemManager().isEpicItem(itemStack)) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                break;

            }
        }

    }
}
