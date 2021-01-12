package pl.wiktorekx.menumanager.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import pl.wiktorekx.menumanager.menu.MenuOpener;
import pl.wiktorekx.menumanager.objectives.Item;
import pl.wiktorekx.menumanager.objectives.Menu;
import pl.wiktorekx.menumanager.unties.ActionManager;

import java.util.Map;

public class InventoryClickListener implements Listener {
    @EventHandler
    private void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getView().getPlayer();
        if (MenuOpener.getOpenMenus().containsKey(player)) {
            Map.Entry<Menu, Inventory> entry = MenuOpener.getOpenMenus().get(player);
            Menu menu = entry.getKey();
            Inventory inventory = entry.getValue();
            if (event.getClickedInventory() != null && event.getClickedInventory().equals(inventory)) {
                event.setCancelled(true);
                if(menu.getItems().containsKey(event.getSlot())){
                    Item item = menu.getItem(player, event.getSlot());
                    if(item != null) {
                        MenuOpener.updateItems(player, menu, inventory);
                        ActionManager.callItemAction(player, item);
                    }
                }
            }
        }
    }

    @EventHandler
    private void onDrag(InventoryDragEvent event){
        Player player = (Player) event.getView().getPlayer();
        if(MenuOpener.getOpenMenus().containsKey(player)){
            if(event.getInventory().equals(MenuOpener.getOpenMenus().get(player).getValue())){
                event.setCancelled(true);
            }
        }
    }
}
