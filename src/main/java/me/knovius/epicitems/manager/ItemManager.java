package me.knovius.epicitems.manager;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.knovius.epicitems.EpicItems;
import me.knovius.epicitems.utils.CM;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {
    private EpicItems plugin;


    private String defaultName;
    private ArrayList<String> defaultLore;
    private ArrayList<String> defaultCommands;

    private File file;
    @Getter
    private YamlConfiguration epicItemConfig;

    @Getter
    private HashMap<String, EpicItem> epicItemMap;

    public ItemManager(EpicItems plugin) {
        this.plugin = plugin;
        this.epicItemMap = new HashMap<>();

        file = new File(plugin.getDataFolder(), "items.yml");
        if (!file.exists()) {
            plugin.saveResource("items.yml", false);
        }
        epicItemConfig = YamlConfiguration.loadConfiguration(file);

        defaultName = "edit this name in config!";
        defaultLore = new ArrayList<>();
        defaultLore.add("edit this in config!");
        defaultCommands = new ArrayList<>();
        defaultCommands.add("say please edit this in the config!");

        for (String path: epicItemConfig.getKeys(false)) {
            EpicItem epicItem = new EpicItem(plugin, path, epicItemConfig);
            epicItemMap.put(path, epicItem);
        }
    }

    public void consumeItem(Player player, EpicItem epicItem, ItemStack item) {
        if (!epicItem.isInCooldown(player)) {
            if (epicItem.isConsume()) {
                if (item.getAmount() > 1) {

                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                }
                else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
            }
            epicItem.runCommands(player);
            epicItem.getCooldownMap().put(player.getUniqueId().toString(), System.currentTimeMillis()+(epicItem.getCooldown()*1000) );
            return;
        }
        long timeLeft = (epicItem.getCooldownMap().get(player.getUniqueId().toString()) - System.currentTimeMillis()) /1000;

        String cooldownMessage = plugin.getConfig().getString("messages.cooldown").replaceAll("%prefix%", plugin.getConfig().getString("prefix")).replaceAll("%time_left%", String.valueOf(timeLeft));
        player.sendMessage(CM.color(cooldownMessage));
    }

    public void consumeOffHandItem(Player player, EpicItem epicItem, ItemStack item) {
        if (!epicItem.isInCooldown(player)) {
            if (epicItem.isConsume()) {
                if (item.getAmount() > 1) {

                    player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                }
                else {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                }
            }
            epicItem.runCommands(player);
            epicItem.getCooldownMap().put(player.getUniqueId().toString(), System.currentTimeMillis()+(epicItem.getCooldown()*1000) );
            return;
        }
        long timeLeft = (epicItem.getCooldownMap().get(player.getUniqueId().toString()) - System.currentTimeMillis()) /1000;

        String cooldownMessage = plugin.getConfig().getString("messages.cooldown").replaceAll("%prefix%", plugin.getConfig().getString("prefix")).replaceAll("%time_left%", String.valueOf(timeLeft));
        player.sendMessage(CM.color(cooldownMessage));
    }

    public EpicItem getEpicItem(String id) {
        return epicItemMap.get(id);
    }

    public void removeEpicItem(String id) {

        if (!epicItemMap.containsKey(id)) {
            plugin.getLogger().info("Error! Whilst attempting to a remove a non-existent epicItem!");
            return;
        }
        epicItemMap.remove(id);
        epicItemConfig.set(id, null);

        try {
            epicItemConfig.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        reloadItemConfig();
    }

    public void addEpicItem(String id, ItemStack itemStack) {
        reloadItemConfig();

        if (itemStack.getType() == Material.AIR) return;

        epicItemConfig.createSection(id);
        epicItemConfig.set(id + ".cooldown", 5);
        epicItemConfig.set(id + ".cancelEvents", true);
        epicItemConfig.set(id + ".consume", true);
        epicItemConfig.set(id + ".item.material", itemStack.getType().toString());
        epicItemConfig.set(id+".item.amount", itemStack.getAmount());

        if (itemStack.hasItemMeta()) {

            if (itemStack.getItemMeta().hasCustomModelData()) {
                epicItemConfig.set(id + ".item.customModelData", itemStack.getItemMeta().getCustomModelData());
            } else {
                epicItemConfig.set(id + ".item.customModelData", 999999999);
            }
            if (itemStack.getItemMeta().hasEnchants()) {
                epicItemConfig.set(id+".item.enchanted", true);
            }
            else {
                epicItemConfig.set(id+".item.enchanted", false);
            }
            if (itemStack.getItemMeta().hasDisplayName()) {
                epicItemConfig.set(id + ".item.name", itemStack.getItemMeta().getDisplayName());
            }
            else {
                epicItemConfig.set(id+".item.name", defaultName);
            }
            if (itemStack.getItemMeta().hasLore()) {
                epicItemConfig.set(id + ".item.lore", itemStack.getItemMeta().getLore());
            }
            else {
                epicItemConfig.set(id+".item.lore", defaultLore);

            }
        }
        else {
            epicItemConfig.set(id+".item.name", defaultName);
            epicItemConfig.set(id+".item.lore", defaultLore);
            epicItemConfig.set(id + ".item.customModelData", 999999999);
            epicItemConfig.set(id+".item.enchanted", false);
        }
        epicItemConfig.set(id+".commands.byconsole", defaultCommands.listIterator());
        epicItemConfig.set(id+".commands.byplayer", defaultCommands.listIterator());
        epicItemConfig.set(id+".commands.delayed.byConsole", defaultCommands.listIterator());
        epicItemConfig.set(id+".commands.delayed.delayByConsole", 5);
        epicItemConfig.set(id+".commands.delayed.byPlayer", defaultCommands.listIterator());
        epicItemConfig.set(id+".commands.delayed.delayByPlayer", 5);
        EpicItem epicItem = new EpicItem(plugin, id, epicItemConfig);
        epicItemMap.put(id, epicItem);

        try {
            epicItemConfig.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void reloadItemConfig() {
        epicItemConfig = YamlConfiguration.loadConfiguration(file);

        new BukkitRunnable() {
            public void run() {
                epicItemMap.clear();

                for (String id : epicItemConfig.getKeys(false)) {
                    EpicItem epicItem1 = new EpicItem(plugin, id, epicItemConfig);
                    epicItemMap.put(id, epicItem1);
                }
            }
        }.runTaskLater(plugin, 20);


    }

    public boolean isEpicItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasTag("epicItem")) {
            return true;
        }
        return false;
    }


}
