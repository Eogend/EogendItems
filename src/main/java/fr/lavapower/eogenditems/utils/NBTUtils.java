package fr.lavapower.eogenditems.utils;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NBTUtils {
    public static Map<String, Object> getNBTConfigToMap(ConfigurationSection section) {
        Map<String, Object> result = new HashMap<>();
        for(String key: section.getKeys(false)) {
            Object value = section.get(key);
            if(value instanceof ConfigurationSection)
                result.put(key, getNBTConfigToMap((ConfigurationSection) value));
            else if(value instanceof Integer || value instanceof Boolean)
                result.put(key, value);
            else
                result.put(key, String.valueOf(value));
        }
        return result;
    }

    private static String getNBTVersion(Object obj) {
        if(obj instanceof Map)
            return convertWithStream((Map<String, ?>) obj);
        if(obj instanceof Boolean)
            return (boolean)obj ? "1b" : "0b";
        return String.valueOf(obj);
    }
    private static String convertWithStream(Map<String, ?> map) {
        return map.keySet().stream()
                .map(key -> key + ":" + getNBTVersion(map.get(key)))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    public static void ApplyNBT(ItemStack stack, Map<String, Object> nbt) {
        NBT.modify(stack, nbtStack -> {
            nbtStack.mergeCompound(NBT.parseNBT(convertWithStream(nbt)));
        });
    }
}
