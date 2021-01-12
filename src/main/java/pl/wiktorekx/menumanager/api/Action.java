package pl.wiktorekx.menumanager.api;

import org.bukkit.entity.Player;

public interface Action {
    String getName();

    void call(Player player, String text);
}
