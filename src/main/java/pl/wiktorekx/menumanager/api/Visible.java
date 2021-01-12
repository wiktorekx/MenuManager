package pl.wiktorekx.menumanager.api;

import org.bukkit.entity.Player;

public interface Visible {
    String getName();

    boolean hasVisible(Player player);
}
