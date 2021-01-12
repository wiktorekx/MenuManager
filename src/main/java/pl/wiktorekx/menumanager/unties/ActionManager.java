package pl.wiktorekx.menumanager.unties;

import org.bukkit.entity.Player;
import pl.wiktorekx.menumanager.api.Action;
import pl.wiktorekx.menumanager.objectives.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ActionManager {
    private static List<Action> actions = new ArrayList<>();

    public static void callItemAction(Player player, Item item){
        for(String actionLine : item.getActions()){
            String[] strings = actionLine.split(":");
            if(strings.length > 1){
                String value;
                if(strings.length > 2){
                    StringJoiner stringJoiner = new StringJoiner(":");
                    for(int i = 1; i < strings.length; i++){
                        stringJoiner.add(strings[i]);
                    }
                    value = stringJoiner.toString();
                } else {
                    value = strings[1];
                }
                callAction(player, strings[0], value);
            } else if(strings.length > 0){
                callAction(player, strings[0], null);
            }
        }
    }

    private static void callAction(Player player, String actionName, String text){
        for(Action action : actions){
            if(action.getName().equals(actionName)){
                action.call(player, text);
            }
        }
    }

    public static void registerAction(Action action){
        if(!actions.contains(action)){
            actions.add(action);
        }
    }

    public static void unregisterAction(Action action){
        actions.remove(action);
    }
}
