package me.knovius.epicitems;

import lombok.Getter;
import me.knovius.epicitems.commands.EpicItemCommand;
import me.knovius.epicitems.commands.EpicItemTabCompleter;
import me.knovius.epicitems.listeners.ConsumeListener;
import me.knovius.epicitems.listeners.CraftListener;
import me.knovius.epicitems.listeners.EventListener;
import me.knovius.epicitems.manager.ItemManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EpicItems extends JavaPlugin {

    @Getter
    public static EpicItems instance;

    @Getter
    private ItemManager itemManager = new ItemManager(this);

    @Override
    public void onEnable() {
        this.instance = this;
        registerEvents();
        getCommand("epicitems").setExecutor(new EpicItemCommand());
        getCommand("epicitems").setTabCompleter(new EpicItemTabCompleter());

        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new ConsumeListener(), this);
        pm.registerEvents(new EventListener(), this);
        pm.registerEvents(new CraftListener(), this);
    }


}
