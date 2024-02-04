package me.knovius.epicitems.manager;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.knovius.epicitems.EpicItems;
import me.knovius.epicitems.utils.CM;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

@Getter
public class EpicItem {
    private EpicItems plugin;
    private String nbtValue;
    private Material material;
    private String name;
    private int customModelData;
    private int amount;
    private boolean enchanted;
    private boolean consume;
    private boolean cancelEvents;
    private List<String> lore;
    private List<String> commandsByConsole;
    private List<String> commandsByPlayer;

    private long delayByConsole;
    private long delayByPlayer;
    private List<String> delayedCommandsByPlayer;
    private List<String> delayedCommandsByConsole;

    private long cooldown;
    private HashMap<String, Long> cooldownMap;

    public EpicItem(EpicItems plugin, String id, YamlConfiguration config) {

        this.plugin = plugin;
        nbtValue = id;
        material = Material.getMaterial(config.getString(id + ".item.material"));
        name = config.getString(id + ".item.name");
        customModelData = config.getInt(id + ".item.customModelData");
        amount = config.getInt(id + ".item.amount");
        enchanted = config.getBoolean(id + ".item.enchanted");
        consume = config.getBoolean(id + ".consume");
        cancelEvents = config.getBoolean(id + ".cancelEvents");
        lore = config.getStringList(id + ".item.lore");
        commandsByConsole = config.getStringList(id + ".commands.byconsole");
        commandsByPlayer = config.getStringList(id + ".commands.byplayer");
        delayByConsole = config.getLong(id + ".commands.delayed.delayByConsole");
        delayByPlayer = config.getLong(id + ".commands.delayed.delayByPlayer");
        cooldown = config.getLong(id+".cooldown");
        delayedCommandsByPlayer = config.getStringList(id + ".commands.delayed.byPlayer");
        delayedCommandsByConsole = config.getStringList(id + ".commands.delayed.byConsole");

        cooldownMap = new HashMap<>();
    }

    public ItemStack toItem() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (name != null) {
            itemMeta.setDisplayName(CM.color(name));
        }
        if (lore != null) {
            itemMeta.setLore(CM.colorLore(lore));
        }
        if (customModelData != 999999999) {
            itemMeta.setCustomModelData(customModelData);
        }
        if (enchanted) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(amount);

        NBTItem nbtItem = new NBTItem(itemStack);

        nbtItem.setString("epicItem", nbtValue);

        return nbtItem.getItem();
    }

    public void runCommands(Player player) {
        for (String s: commandsByConsole) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("%player%", player.getName()));
        }
        for (String s: commandsByPlayer) {
            player.performCommand(s.replaceAll("%player%", player.getName()));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (String s: delayedCommandsByConsole) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("%player%", player.getName()));
                }
            }
        }.runTaskLater(plugin, delayByConsole*20);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (String s: delayedCommandsByPlayer) {
                    player.performCommand(s.replaceAll("%player%", player.getName()));
                }
            }
        }.runTaskLater(plugin, delayByPlayer*20);
    }

    public boolean isInCooldown(Player player) {
        if (cooldownMap.containsKey(player.getUniqueId().toString())) {
            long timeLeft = (cooldownMap.get(player.getUniqueId().toString()) - System.currentTimeMillis())/1000;
            if (timeLeft > 0) {
                return true;
            }

        }

        return false;
    }
}
