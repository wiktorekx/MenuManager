package pl.wiktorekx.menumanager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.wiktorekx.menumanager.api.Action;
import pl.wiktorekx.menumanager.api.MenuManagerAPI;
import pl.wiktorekx.menumanager.listeners.InventoryClickListener;
import pl.wiktorekx.menumanager.menu.MenuOpener;
import pl.wiktorekx.menumanager.unties.DependManager;
import pl.wiktorekx.menumanager.unties.Unties;

import java.util.StringJoiner;

public final class MenuManager extends JavaPlugin {
    private static MenuManager instance;
    private boolean hasPapi;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        hasPapi = DependManager.dependPlugin("PlaceholderAPI", new DependManager.TruePluginDependCallback()) != null;
        MenuOpener menuOpener = new MenuOpener();
        menuOpener.runTaskTimer(this, 1, 1);
        Bukkit.getPluginManager().registerEvents(menuOpener, this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        defaultActions();
    }

    private void defaultActions(){
        MenuManagerAPI.registerAction(new Action() {
            @Override
            public String getName() {
                return "SendMessage";
            }

            @Override
            public void call(Player player, String text) {
                if(text != null) {
                    player.sendMessage(Unties.replace(player, text));
                }
            }
        });
        MenuManagerAPI.registerAction(new Action() {
            @Override
            public String getName() {
                return "CloseInventory";
            }

            @Override
            public void call(Player player, String text) {
                player.closeInventory();
            }
        });
        MenuManagerAPI.registerAction(new Action() {
            @Override
            public String getName() {
                return "SendCommand";
            }

            @Override
            public void call(Player player, String text) {
                if(text != null){
                    String[] strings = text.split(":");
                    String value;
                    if(strings.length > 1){
                        if(strings.length > 2){
                            StringJoiner stringJoiner = new StringJoiner(":");
                            for(int i = 1; i < strings.length; i++){
                                stringJoiner.add(strings[i]);
                            }
                            value = stringJoiner.toString();
                        } else {
                            value = strings[1];
                        }
                        String key = strings[0];
                        if(key.equalsIgnoreCase("console")){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
                        } else if(key.equalsIgnoreCase("player")){
                            Bukkit.dispatchCommand(player, value);
                        } else if(key.equalsIgnoreCase("op")){
                            boolean hasOp = player.isOp();
                            player.setOp(true);
                            Bukkit.dispatchCommand(player, value);
                            if(!hasOp) {
                                player.setOp(false);
                            }
                        }
                    }
                }
            }
        });
        MenuManagerAPI.registerAction(new Action() {
            @Override
            public String getName() {
                return "ConnectToServer";
            }

            @Override
            public void call(Player player, String text) {
                try {
                    if(text != null) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(text);
                        player.sendPluginMessage(MenuManager.getInstance(), "BungeeCord", out.toByteArray());
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    public static MenuManager getInstance() {
        return instance;
    }

    public boolean isHasPapi() {
        return hasPapi;
    }
}
