package fr.lavapower.eogenditems.manager;

import fr.lavapower.eogenditems.EogendItems;
import fr.lavapower.eogenditems.data.CustomItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemManager
{
    private final Map<String, CustomItem> itemsMap;
    private final EogendItems plugin;

    public ItemManager(EogendItems plugin, FileConfiguration configuration) {
        this.plugin = plugin;
        itemsMap = new HashMap<>();
        reload(configuration);
    }

    /**
     * Reload ItemManager
     *
     * @param configuration Reloaded configuration
     */
    public void reload(FileConfiguration configuration) {
        itemsMap.clear();
        for(String key: configuration.getKeys(false))
            itemsMap.put(key, new CustomItem(plugin, configuration.getConfigurationSection(key)));
    }

    /**
     * Get Type of Custom Item by Name
     *
     * @param name Name of custom Item
     * @return Type of custom Item
     */
    public String getTypeByName(String name) {
        CustomItem item = itemsMap.get(name);
        if(item == null)
            return null;
        return item.getType();
    }

    /**
     * Get Custom Item by Name
     *
     * @param name Name of custom item
     * @return Custom Item
     */
    public CustomItem getItem(String name) {
        return itemsMap.get(name);
    }

    /**
     * Return if custom item exists
     *
     * @param name Name of custom item
     * @return if custom item exists
     */
    public boolean exists(String name) {
        return itemsMap.containsKey(name);
    }

    /**
     * Getting ItemStack by Name and Quantity
     *
     * @param name Name of custom item
     * @param quantity Quantity of itemstack
     * @return ItemStack
     */
    public ItemStack getItemStack(String name, int quantity) {
        CustomItem item = itemsMap.get(name);
        if(item == null)
            return null;
        ItemStack stack = item.getItemStack(quantity);
        UpdateLore(stack);
        return stack;
    }

    /**
     * Return all custom item's names
     *
     * @return Names of custom items
     */
    public Set<String> getNames() {
        return itemsMap.keySet();
    }

    /**
     * Update Lore for itemstack
     *
     * @param stack Stack which be updated
     */
    public void UpdateLore(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        String name = meta.getPersistentDataContainer().get(plugin.getNameKey(), PersistentDataType.STRING);
        if(name == null)
            return;

        CustomItem item = itemsMap.get(name);
        int durability = meta.getPersistentDataContainer().get(plugin.getDurabilityKey(), PersistentDataType.INTEGER);
        int maxDurability = meta.getPersistentDataContainer().get(plugin.getMaxDurabilityKey(), PersistentDataType.INTEGER);
        List<String> lore = new ArrayList<>(item.getLore());
        for(int i = 0; i < lore.size(); i++)
        {
            lore.set(i, lore.get(i).replace("%durability%", String.valueOf(durability)));
            lore.set(i, lore.get(i).replace("%uses%", maxDurability == -1 ? String.valueOf(durability) : String.valueOf(maxDurability - durability)));
            lore.set(i, lore.get(i).replace("%maxDurability%", String.valueOf(maxDurability)));
        }
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }
}
