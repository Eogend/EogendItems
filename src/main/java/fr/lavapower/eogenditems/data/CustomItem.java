package fr.lavapower.eogenditems.data;

import fr.lavapower.eogenditems.EogendItems;
import fr.lavapower.eogenditems.utils.NBTUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItem
{
    private final String name;
    private final String material;
    private final String type;
    private final boolean showEnchants;
    private final boolean durabilityEnabled;
    private final int durability;
    private final String displayName;
    private final List<String> lore;
    private final Map<String, Integer> enchantments;
    private final Map<String, Object> nbt;

    private final ParticleData particleData;

    private final EogendItems plugin;

    public CustomItem(EogendItems plugin, ConfigurationSection section) {
        this.plugin = plugin;
        name = section.getName();
        material = section.getString("material");
        type = section.getString("type");
        showEnchants = section.getBoolean("showEnchants");
        durabilityEnabled = section.getBoolean("durabilityEnabled");
        durability = section.getInt("durability");
        displayName = ChatColor.translateAlternateColorCodes('&', section.getString("displayName"));
        lore = section.getList("lore").stream().map(x -> ChatColor.translateAlternateColorCodes('&', (String)x)).toList();
        enchantments = new HashMap<>();
        if(section.getConfigurationSection("enchantments") != null)
            for(String key: section.getConfigurationSection("enchantments").getKeys(false))
                enchantments.put(key, section.getConfigurationSection("enchantments").getInt(key));

        if(section.getConfigurationSection("nbt") != null)
            nbt = NBTUtils.getNBTConfigToMap(section.getConfigurationSection("nbt"));
        else
            nbt = new HashMap<>();

        if(type.equals("particle_maker"))
            particleData = new ParticleData(section.getConfigurationSection("particle-data"));
        else
            particleData = null;
    }

    /**
     * Create and return ItemStack for custom item
     *
     * @param quantity Quantity in ItemStack
     * @return result ItemStack
     */
    public ItemStack getItemStack(int quantity) {
        ItemStack result = new ItemStack(Material.getMaterial(material));
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.getPersistentDataContainer().set(plugin.getNameKey(), PersistentDataType.STRING, name);
        meta.getPersistentDataContainer().set(plugin.getMaxDurabilityKey(), PersistentDataType.INTEGER, isDurabilityEnabled() ? durability : -1);
        meta.getPersistentDataContainer().set(plugin.getDurabilityKey(), PersistentDataType.INTEGER, isDurabilityEnabled() ? durability : 0);
        for(Map.Entry<String, Integer> entry: enchantments.entrySet())
            meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(entry.getKey())), entry.getValue(), true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        if(!isShowEnchants())
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        result.setItemMeta(meta);
        result.setAmount(quantity);
        NBTUtils.ApplyNBT(result, nbt);
        return result;
    }

    public String getName() {
        return name;
    }

    public String getMaterial()
    {
        return material;
    }

    public String getType()
    {
        return type;
    }

    public boolean isShowEnchants()
    {
        return showEnchants;
    }

    public boolean isDurabilityEnabled()
    {
        return durabilityEnabled;
    }

    public int getDurability()
    {
        return durability;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public List<String> getLore()
    {
        return lore;
    }

    public Map<String, Integer> getEnchantments()
    {
        return enchantments;
    }

    public ParticleData getParticleData() {
        return particleData;
    }
}
