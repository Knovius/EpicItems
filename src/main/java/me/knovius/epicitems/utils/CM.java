package me.knovius.epicitems.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CM {
    public static String color(String s) {

        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static ArrayList<String> colorLore(String... lore) {

        ArrayList<String> coloredLore = new ArrayList<>();

        for (int x = 0; x < lore.length; x++) {
            coloredLore.set(x, color(Arrays.asList(lore).get(x)));
        }

        return coloredLore;
    }

    // for (int x=0; x < message.size(); x++) {
    //            sender.sendMessage(CM.color(message.get(x).replace("%prefix%", CM.color(EpicItems.getInstance().getConfig().getString("prefix")))));
    //        }

    public static ArrayList<String> colorLore(List<String> lore) {
        ArrayList<String> coloredString = new ArrayList<>();

        for (int x = 0; x < lore.size(); x++) {
            coloredString.add(ChatColor.translateAlternateColorCodes('&', lore.get(x)));
        }

        return coloredString;
    }
}
