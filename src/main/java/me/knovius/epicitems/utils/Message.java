package me.knovius.epicitems.utils;

import me.knovius.epicitems.EpicItems;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public enum Message {
    HELP(EpicItems.getInstance().getConfig().getStringList("messages.help")),
    PLAYER_ONLY(EpicItems.getInstance().getConfig().getString("messages.player-only")),
    PLAYER_NULL(EpicItems.getInstance().getConfig().getString("messages.player-null")),
    EPICITEM_NON_EXISTENT(EpicItems.getInstance().getConfig().getString("messages.item-non-existent")),

    ITEM_ERROR(EpicItems.getInstance().getConfig().getString("messages.item-air-error")),
    RELOADED_CONFIG(EpicItems.getInstance().getConfig().getString("messages.config-reloaded")),
    NO_PERMISSION(EpicItems.getInstance().getConfig().getString("messages.no-permission"));

    private List<String> message;
    private String sMessage;

    Message(String string) {
        this.sMessage = string;
    }
    Message(List<String> message) {
        this.message = message;
    }
    public void sendMessage(CommandSender sender) {

        sender.sendMessage(CM.color(sMessage.replace("%prefix%", CM.color(EpicItems.getInstance().getConfig().getString("prefix")))));
    }
    public void sendMessage(Player player) {
        player.sendMessage(CM.color(sMessage.replace("%prefix%", CM.color(EpicItems.getInstance().getConfig().getString("prefix")))));
    }

    public void sendHelpMessage(CommandSender player) {
        for (int x=0; x < message.size(); x++) {
            player.sendMessage(CM.color(message.get(x).replace("%prefix%", CM.color(EpicItems.getInstance().getConfig().getString("prefix")))));
        }
    }
}
