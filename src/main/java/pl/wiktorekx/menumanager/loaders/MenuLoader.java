package pl.wiktorekx.menumanager.loaders;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import pl.wiktorekx.menumanager.builders.ItemBuilder;
import pl.wiktorekx.menumanager.objectives.Item;
import pl.wiktorekx.menumanager.objectives.Menu;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MenuLoader {
    private Map<String, MenuLoader> includeMenus = new HashMap<>();
    private Map<String, Menu> menus = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private MainMenuLoader mainMenuLoader;

    public MenuLoader(MainMenuLoader mainMenuLoader) {
        this.mainMenuLoader = mainMenuLoader;
    }

    public void loadMenus(ConfigurationSection configuration){
        if(configuration.contains("include") && configuration.isList("include")){
            for(String include : configuration.getStringList("include")){;
                MenuLoader includeMenu = mainMenuLoader.includeMenu(include);
                includeMenus.put(include, includeMenu);
                items.putAll(includeMenu.getItems());
            }
        }
        if(configuration.contains("items") && configuration.isConfigurationSection("items")){
            ConfigurationSection section = configuration.getConfigurationSection("items");
            for(String key : section.getKeys(false)){
                if(section.isConfigurationSection(key)) {
                    loadItem(section.getConfigurationSection(key));
                }
            }
        }
        if(configuration.contains("menus") && configuration.isConfigurationSection("menus")){
            ConfigurationSection section = configuration.getConfigurationSection("menus");
            for(String key : section.getKeys(false)){
                if(section.isConfigurationSection(key)) {
                    loadMenu(section.getConfigurationSection(key));
                }
            }
        }
    }

    private void loadMenu(ConfigurationSection section){
        Menu menu = null;
        if(section.contains("include") && section.isString("include")){
            String[] include = section.getString("include").split("->");
            if(include.length > 1){
                if(includeMenus.containsKey(include[0])) {
                    MenuLoader includeMenuLoader = includeMenus.get(include[0]);
                    if(includeMenuLoader.getMenus().containsKey(include[1])){
                        menu = includeMenuLoader.getMenus().get(include[1]).clone();
                    }
                }
            }
        }
        if(menu == null) menu = new Menu(section.getName());
        if(section.contains("rows") && section.isInt("rows")){
            menu.setRows(section.getInt("rows"));
        }
        if(section.contains("title") && section.isString("title")){
            menu.setTitle(section.getString("title"));
        }
        if(section.contains("refresh") && section.isInt("refresh")){
            menu.setRefresh(section.getInt("refresh"));
        }
        if(section.contains("items")){
            for(Map<?, ?> item : section.getMapList("items")) {
                if(item.size() > 0) {
                    Map.Entry<?, ?> entry = item.entrySet().iterator().next();
                    String itemName = (String) entry.getValue();
                    if(items.containsKey(itemName)) {
                        menu.putItem((Integer) entry.getKey(), items.get(itemName));
                    }
                }
            }
        }
        menus.put(section.getName(), menu);
    }

    private void loadItem(ConfigurationSection section) {
        ItemBuilder itemBuilder = new ItemBuilder(section);
        if (section.contains("skullPlayer") && section.isString("skullPlayer")) {
            String skullPlayer = section.getString("skullPlayer");
            if(skullPlayer.equals("[THIS_PLAYER]")) {
                itemBuilder.setSkullThisPlayer();
            } else {
                itemBuilder.setSkull(Bukkit.getOfflinePlayer(skullPlayer));
            }
        }
        Item.Builder builder = new Item.Builder(section.getName(), itemBuilder);
        if (section.contains("visible") && section.isString("visible")) {
            builder.setVisible(section.getString("visible"));
        }
        if (section.contains("actions") && section.isList("actions")) {
            builder.setActions(section.getStringList("actions"));
        }
        items.put(section.getName(), builder.build());
    }

    public Map<String, MenuLoader> getIncludeMenus() {
        return new HashMap<>(includeMenus);
    }

    public Map<String, Menu> getMenus() {
        return new HashMap<>(menus);
    }

    public Map<String, Item> getItems() {
        return new HashMap<>(items);
    }
}
