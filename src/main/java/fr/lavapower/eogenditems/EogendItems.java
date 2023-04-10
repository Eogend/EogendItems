package fr.lavapower.eogenditems;

import fr.lavapower.eogenditems.command.GiveItem;
import fr.lavapower.eogenditems.command.ReloadItems;
import fr.lavapower.eogenditems.listener.ParticleMakerListener;
import fr.lavapower.eogenditems.manager.ItemManager;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class EogendItems extends JavaPlugin {
    private ItemManager itemManager;
    private NamespacedKey nameKey;
    private NamespacedKey durabilityKey;
    private NamespacedKey maxDurabilityKey;

    @Override
    public void onEnable() {
        // Save config
        saveDefaultConfig();

        // Register Commands
        registerCommand("giveitem", new GiveItem(this));
        registerCommand("reloaditems", new ReloadItems(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new ParticleMakerListener(this), this);

        // Create Manager
        itemManager = new ItemManager(this, getConfig());

        // Create NBT Keys
        nameKey = new NamespacedKey(this, "name");
        durabilityKey = new NamespacedKey(this, "durability");
        maxDurabilityKey = new NamespacedKey(this, "maxDurability");
    }

    public void reload() {
        reloadConfig();
        itemManager.reload(getConfig());
    }

    private <T extends TabCompleter & CommandExecutor> void registerCommand(String name, T commandInstance)
    {
        PluginCommand command = getCommand(name);
        command.setExecutor(commandInstance);
        command.setTabCompleter(commandInstance);
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public NamespacedKey getNameKey()
    {
        return nameKey;
    }

    public NamespacedKey getDurabilityKey()
    {
        return durabilityKey;
    }

    public NamespacedKey getMaxDurabilityKey()
    {
        return maxDurabilityKey;
    }

}