package pl.wiktorekx.menumanager.unties;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.wiktorekx.menumanager.MenuManager;
import pl.wiktorekx.menumanager.builders.ItemBuilder;
import pl.wiktorekx.menumanager.objectives.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Unties {
    private static List<Replace> replaces = new ArrayList<>();

    public static ItemBuilder replaceItem(Player player, Item item){
        ItemBuilder builder = item.getItemBuilder().clone();
        if(builder.isHasThisPlayer()){
            builder.setSkull(player);
        }
        if(builder.getDisplayName() != null) {
            builder.setDisplayName(ChatColor.RESET + Unties.replace(player, builder.getDisplayName()));
        }
        if(builder.getLore() != null) {
            List<String> lore = new ArrayList<>();
            for(String line : builder.getLore()) {
                lore.add(ChatColor.RESET + Unties.replace(player, line));
            }
            builder.setLore(lore);
        }
        return builder;
    }

    public static boolean hasVisible(Player player, String text){
        text = VisiblePlaceholder.getInstance().replace(player, text);
        text = replace(player, text);
        return !text.equalsIgnoreCase("false");
    }

    public static String replace(Player player, String text){
        text = color(text);
        for(Replace replace : replaces){
            text = replace.replace(player, text);
        }
        if(MenuManager.getInstance().isHasPapi()){
            if(player != null) {
                text = PlaceholderAPI.setPlaceholders(player, text);
            } else {
                if(Bukkit.getOfflinePlayers().length > 0) {
                    text = PlaceholderAPI.setPlaceholders(Arrays.stream(Bukkit.getOfflinePlayers()).iterator().next(), text);
                }
            }
        }
        return text;
    }

    public static void registerReplace(Replace replace){
        if(!replaces.contains(replace)) {
            replaces.add(replace);
        }
    }

    public static void unregisterReplace(Replace replace){
        replaces.remove(replace);
    }

    public static String color(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
