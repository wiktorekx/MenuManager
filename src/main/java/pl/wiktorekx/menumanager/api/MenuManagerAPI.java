package pl.wiktorekx.menumanager.api;

import org.bukkit.entity.Player;
import pl.wiktorekx.menumanager.loaders.MainMenuLoader;
import pl.wiktorekx.menumanager.menu.MenuOpener;
import pl.wiktorekx.menumanager.objectives.Menu;
import pl.wiktorekx.menumanager.unties.ActionManager;
import pl.wiktorekx.menumanager.unties.Placeholder;
import pl.wiktorekx.menumanager.unties.Unties;
import pl.wiktorekx.menumanager.unties.VisiblePlaceholder;

import java.io.File;
import java.util.Objects;

public class MenuManagerAPI {
    public static MenuLoader getMenuLoader(File dataFolder){
        Objects.requireNonNull(dataFolder);
        return new MainMenuLoader(dataFolder);
    }

    public static void openMenu(Player player, Menu menu){
        Objects.requireNonNull(player);
        Objects.requireNonNull(menu);
        MenuOpener.openMenu(player, menu);
    }

    public static void registerPlaceholder(Placeholder placeholder){
        Objects.requireNonNull(placeholder);
        if(!placeholder.getIdentifier().equalsIgnoreCase("visible")) {
            Unties.registerReplace(placeholder);
        }
    }

    public static void unregisterPlaceholder(Placeholder placeholder){
        Objects.requireNonNull(placeholder);
        if(!placeholder.getIdentifier().equalsIgnoreCase("visible")) {
            Unties.unregisterReplace(placeholder);
        }
    }

    public static void registerVisible(Visible visible){
        Objects.requireNonNull(visible);
        VisiblePlaceholder.getInstance().registerVisible(visible);
    }

    public static void unregisterVisible(Visible visible){
        Objects.requireNonNull(visible);
        VisiblePlaceholder.getInstance().unregisterVisible(visible);
    }

    public static void registerAction(Action action){
        Objects.requireNonNull(action);
        ActionManager.registerAction(action);
    }

    public static void unregisterAction(Action action){
        Objects.requireNonNull(action);
        ActionManager.unregisterAction(action);
    }
}
