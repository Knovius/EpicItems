package me.knovius.epicitems.commands;

import me.knovius.epicitems.EpicItems;
import me.knovius.epicitems.manager.EpicItem;
import me.knovius.epicitems.utils.CM;
import me.knovius.epicitems.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EpicItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("epicitems")) {

            if (!sender.hasPermission("epicitems.command.use")) {
                Message.NO_PERMISSION.sendMessage(sender);
                return true;
            }

            if (args.length == 0) {
                Message.HELP.sendHelpMessage(sender);
                return true;
            }
            if (args[0].length() < 0) {
                Message.HELP.sendHelpMessage(sender);
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                EpicItems.getInstance().getItemManager().reloadItemConfig();
                EpicItems.getInstance().reloadConfig();
                Message.RELOADED_CONFIG.sendMessage(sender);
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                Message.HELP.sendHelpMessage(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (!(sender instanceof Player)) {
                    Message.PLAYER_ONLY.sendMessage(sender);
                    return true;
                }
                Player player = (Player)sender;
                if (args.length < 2 || args[1].length() < 0) {
                    Message.HELP.sendHelpMessage(sender);
                    return true;
                }

                String id = args[1];
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack == null || itemStack.getType() == null || itemStack.getType() == Material.AIR) {
                    Message.ITEM_ERROR.sendMessage(player);
                    return true;
                }
                EpicItems.getInstance().getItemManager().addEpicItem(id, itemStack);
                sender.sendMessage(CM.color(EpicItems.getInstance().getConfig().getString("messages.item-added").replaceAll("%prefix%", EpicItems.getInstance().getConfig().getString("prefix"))));

                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {

                if (args.length < 2 || args[1].length() < 0) {
                    Message.HELP.sendHelpMessage(sender);
                    return true;
                }
                String id = args[1];
                if (!EpicItems.getInstance().getItemManager().getEpicItemMap().containsKey(id)) {
                    Message.EPICITEM_NON_EXISTENT.sendMessage(sender);
                    return true;
                }
                EpicItems.getInstance().getItemManager().removeEpicItem(id);
                sender.sendMessage(CM.color(EpicItems.getInstance().getConfig().getString("messages.item-removed").replaceAll("%prefix%", EpicItems.getInstance().getConfig().getString("prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length < 2 || args[1].length() < 0) {
                    Message.HELP.sendHelpMessage(sender);
                    return true;
                }
                String id = args[1];
                if (!EpicItems.getInstance().getItemManager().getEpicItemMap().containsKey(id)) {
                    Message.EPICITEM_NON_EXISTENT.sendMessage(sender);
                    return true;
                }
                EpicItem epicItem = EpicItems.getInstance().getItemManager().getEpicItem(id);
                if (args.length >= 3 && args[2].length() > 0) {

                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        Message.PLAYER_NULL.sendMessage(sender);
                        return true;
                    }
                    target.getInventory().addItem(epicItem.toItem());

                    String targetGiven = EpicItems.getInstance().getConfig().getString("messages.target-given-item").replaceAll("%prefix%", EpicItems.getInstance().getConfig().getString("prefix")).replaceAll("%epic_item%", id);
                    String given2 = EpicItems.getInstance().getConfig().getString("messages.sender-gave-item").replaceAll("%prefix%", EpicItems.getInstance().getConfig().getString("prefix")).replaceAll("%target%", target.getName()).replaceAll("%epic_item%", id);

                    target.sendMessage(CM.color(targetGiven));
                    sender.sendMessage(CM.color(given2));
                    return true;
                }
                if (!(sender instanceof Player)) {
                    Message.HELP.sendHelpMessage(sender);
                    return true;
                }
                Player player = (Player)sender;
                player.getInventory().addItem(epicItem.toItem());
                player.sendMessage(CM.color(EpicItems.getInstance().getConfig().getString("prefix") + " &eYou have been given &6" + id + "&e!"));
            }

            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(CM.color(EpicItems.getInstance().getConfig().getString("prefix") + " &7" + EpicItems.getInstance().getItemManager().getEpicItemMap().keySet().toString()));
            }


        }
        return false;
    }
}
