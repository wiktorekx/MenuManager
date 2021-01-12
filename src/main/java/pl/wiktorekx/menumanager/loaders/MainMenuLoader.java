package pl.wiktorekx.menumanager.loaders;

import org.bukkit.configuration.ConfigurationSection;
import pl.wiktorekx.menumanager.config.Config;
import pl.wiktorekx.menumanager.objectives.Item;
import pl.wiktorekx.menumanager.objectives.Menu;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainMenuLoader implements pl.wiktorekx.menumanager.api.MenuLoader {
    private File menusFolder;
    private MenuLoader mainMenuLoader;

    public MainMenuLoader(File menusFolder) {
        this.menusFolder = menusFolder;
        this.mainMenuLoader = new MenuLoader(this);
    }

    public void loadMainMenu(String fileName) {
        mainMenuLoader.loadMenus(loadMenuConfiguration(fileName));
    }

    private ConfigurationSection loadMenuConfiguration(String fileName) {
        Config config = new Config(new File(menusFolder, fileName));
        config.loadConfig();
        return config;
    }

    MenuLoader includeMenu(String fileName){
        MenuLoader menuLoader = new MenuLoader(this);
        menuLoader.loadMenus(loadMenuConfiguration(fileName));
        return menuLoader;
    }

    public MenuLoader getMainMenuLoader() {
        return mainMenuLoader;
    }

    @Override
    public void loadMenuFile(String fileName) {
        Objects.requireNonNull(fileName);
        this.loadMainMenu(fileName);
    }

    @Override
    public boolean containsMenu(String menuName) {
        Objects.requireNonNull(menuName);
        return getMenus().containsKey(menuName);
    }

    @Override
    public boolean containsItem(String itemName) {
        Objects.requireNonNull(itemName);
        return getItems().containsKey(itemName);
    }

    @Override
    public Menu getMenu(String menuName) {
        Objects.requireNonNull(menuName);
        return getMenus().get(menuName);
    }

    @Override
    public Item getItem(String itemName) {
        Objects.requireNonNull(itemName);
        return getItems().get(itemName);
    }

    @Override
    public Map<String, Menu> getMenus() {
        return new HashMap<>(getMainMenuLoader().getMenus());
    }

    @Override
    public Map<String, Item> getItems() {
        return new HashMap<>(getMainMenuLoader().getItems());
    }
}
