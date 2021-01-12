package pl.wiktorekx.menumanager.unties;

import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Placeholder implements Replace {
    @Override
    public String replace(Player player, String text) {
        Pattern pattern = Pattern.compile("%(.*)_(.*)%");
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()){
            String replaceValue = matcher.group();
            if(matcher.group(1).equals(getIdentifier())){
                replaceValue = placeholderRequest(player, matcher.group(2));
            }
            text = text.replace(matcher.group(), replaceValue);
        }
        return text;
    }

    public abstract String getIdentifier();

    public abstract String placeholderRequest(Player player, String text);
}
