package me.knovius.epicitems.commands;

import me.knovius.epicitems.EpicItems;
import me.knovius.epicitems.manager.EpicItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EpicItemTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commands = new ArrayList<>();

        if (args.length == 1) {
            commands.add("help");
            commands.add("list");
            commands.add("add");
            commands.add("remove");
            commands.add("reload");
            commands.add("give");

            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        else if (args.length == 2) {
            if (args[0].equals("give")) {
                for (String string: EpicItems.getInstance().getItemManager().getEpicItemMap().keySet()) {
                    EpicItems.getInstance().getLogger().info(string);
                    commands.add(string);
                }
            }
             if (args[0].equals("remove")) {
                 for (String string: EpicItems.getInstance().getItemManager().getEpicItemMap().keySet()) {
                     EpicItems.getInstance().getLogger().info(string);
                     commands.add(string);
                 }
             }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        else if (args.length == 3){
            return null;
        }
        Collections.sort(completions);
        return completions;
    }
}
