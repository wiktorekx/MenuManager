package pl.wiktorekx.menumanager.objectives;

import org.bukkit.entity.Player;
import pl.wiktorekx.menumanager.unties.Unties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private String name ;
    private String title = "";
    private int refresh = 20;
    private int rows = 0;
    private Map<Integer, List<Item>> items = new HashMap<>();

    public Menu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getRefresh() {
        return refresh;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }

    public void setItems(Map<Integer, List<Item>> items) {
        this.items = items;
    }

    public Item getItem(Player player, int slot){
        if(getItems().containsKey(slot)){
            for(Item item : getItems().get(slot)){
                if(Unties.hasVisible(player, item.getVisible())) {
                    return item;
                }
            }
        }
        return null;
    }

    public int getRows() {
        return rows;
    }

    public Menu setName(String name) {
        this.name = name;
        return this;
    }

    public Menu setTitle(String title) {
        this.title = title;
        return this;
    }

    public Menu setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public void putItem(int slot, Item item){
        List<Item> items = getItems().get(slot);
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        getItems().put(slot, items);
    }
    public Map<Integer, List<Item>> getItems() {
        return items;
    }

    public Menu clone(){
        Menu menu = new Menu(name);
        menu.setTitle(title);
        menu.setRows(rows);
        menu.getItems().clear();
        menu.getItems().putAll(items);
        return menu;
    }
}
