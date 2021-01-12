package pl.wiktorekx.menumanager.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import pl.wiktorekx.menumanager.objectives.Item;
import pl.wiktorekx.menumanager.objectives.Menu;
import pl.wiktorekx.menumanager.unties.Unties;

import java.util.*;

public class MenuOpener extends BukkitRunnable implements Listener {
    private static Map<Player, Map.Entry<Menu, Inventory>> openMenus = new HashMap<>();
    private long time;

    public static void openMenu(Player player, Menu menu){
        Inventory inventory = Bukkit.createInventory(null, menu.getRows() * 9, ChatColor.RESET + Unties.replace(player, menu.getTitle()));
        updateItems(player, menu, inventory);
        player.openInventory(inventory);
        openMenus.put(player, new AbstractMap.SimpleEntry<>(menu, inventory));
    }

    public static void updateItems(Player player){
        if(openMenus.containsKey(player)){
            Map.Entry<Menu, Inventory> entry = openMenus.get(player);
            updateItems(player, entry.getKey(), entry.getValue());
        }
    }

    public static void updateItems(Player player, Menu menu, Inventory inventory){
        if(inventory != null) {
            for (Integer slot : menu.getItems().keySet()) {
                Item item = menu.getItem(player, slot);
                if(item != null) {
                    inventory.setItem(slot, Unties.replaceItem(player, item).build());
                } else {
                    inventory.setItem(slot, null);
                }
            }
        }
    }

    public static Map<Player, Map.Entry<Menu, Inventory>> getOpenMenus() {
        return new HashMap<>(openMenus);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        openMenus.remove(event.getPlayer());
    }

    @Override
    public void run() {
        for(Map.Entry<Player, Map.Entry<Menu, Inventory>> entry : openMenus.entrySet()){
            int refresh = entry.getValue().getKey().getRefresh();
            if(refresh > 0 && time % refresh == 0) {
                updateItems(entry.getKey(), entry.getValue().getKey(), entry.getValue().getValue());
            }
        }
        time++;
    }
}
