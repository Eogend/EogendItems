package fr.lavapower.eogenditems.listener;

import fr.lavapower.eogenditems.EogendItems;
import fr.lavapower.eogenditems.data.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleMakerListener implements Listener {
    private final EogendItems plugin;
    public ParticleMakerListener(EogendItems plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClickAir(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            ItemStack stack = player.getInventory().getItemInMainHand();
            ItemMeta meta = stack.getItemMeta();
            if (meta == null)
                return;
            String name = meta.getPersistentDataContainer().get(plugin.getNameKey(), PersistentDataType.STRING);
            if (name == null)
                return;
            CustomItem item = plugin.getItemManager().getItem(name);
            if (item.getType().equals("particle_maker")) {
                // Update Durability
                if(meta.getPersistentDataContainer().get(plugin.getMaxDurabilityKey(), PersistentDataType.INTEGER) == -1)
                    meta.getPersistentDataContainer().set(plugin.getDurabilityKey(), PersistentDataType.INTEGER, meta.getPersistentDataContainer().get(plugin.getDurabilityKey(), PersistentDataType.INTEGER) + 1);
                else {
                    int durability = meta.getPersistentDataContainer().get(plugin.getDurabilityKey(), PersistentDataType.INTEGER) - 1;
                    meta.getPersistentDataContainer().set(plugin.getDurabilityKey(), PersistentDataType.INTEGER, durability);
                    if(durability == 0)
                        player.getInventory().setItemInMainHand(null);
                }
                stack.setItemMeta(meta);
                plugin.getItemManager().UpdateLore(stack);

                final Location position = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(0.5));
                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        player.getWorld().spawnParticle(item.getParticleData().getType(), position.add(new Vector(0, 0.1, 0)), 1);
                        i++;
                        if(i == 20)
                            cancel();
                    }
                }.runTaskTimerAsynchronously(plugin, 0, 1L);
            }
        }
    }
}
