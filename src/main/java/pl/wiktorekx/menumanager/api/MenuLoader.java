package pl.wiktorekx.menumanager.api;

import pl.wiktorekx.menumanager.objectives.Item;
import pl.wiktorekx.menumanager.objectives.Menu;

import java.util.Map;

public interface MenuLoader {
    void loadMenuFile(String fileName);

    boolean containsMenu(String menuName);

    boolean containsItem(String itemName);

    Menu getMenu(String menuName);

    Item getItem(String itemName);

    Map<String, Menu> getMenus();

    Map<String, Item> getItems();
}
