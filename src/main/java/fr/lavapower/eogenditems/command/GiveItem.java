package fr.lavapower.eogenditems.command;

import fr.lavapower.eogenditems.EogendItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveItem implements CommandExecutor, TabCompleter {
    private final EogendItems plugin;
    public GiveItem(EogendItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player))
            sender.sendMessage("Cette commande n'est utilisable que par un joueur");
        else {
            if (args.length >= 2) {
                if (!plugin.getItemManager().exists(args[0]))
                    sender.sendMessage("Item inconnu !");
                else {
                    try {
                        int amount = Integer.parseInt(args[1]);
                        if (amount <= 0)
                            sender.sendMessage("Quantité invalide !");
                        else {
                            player.getInventory().addItem(plugin.getItemManager().getItemStack(args[0], amount));
                            sender.sendMessage("Item give !");
                        }

                    } catch (NumberFormatException ignored) {
                        sender.sendMessage("Quantité invalide !");
                    }
                }
            }
            else
                sender.sendMessage("Usage : /giveitem <item> <quantity>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1)
            return plugin.getItemManager().getNames().stream().filter(x -> args[0].isEmpty() || x.startsWith(args[0].toLowerCase())).toList();
        return null;
    }
}
