package pl.wiktorekx.menumanager.unties;

import org.bukkit.entity.Player;
import pl.wiktorekx.menumanager.api.Visible;

import java.util.ArrayList;
import java.util.List;

public class VisiblePlaceholder extends Placeholder {
    private static VisiblePlaceholder instance = new VisiblePlaceholder();
    private List<Visible> visibles = new ArrayList<>();

    private VisiblePlaceholder() {}

    @Override
    public String getIdentifier() {
        return "visible";
    }

    @Override
    public String placeholderRequest(Player player, String text) {
        for(Visible visible : visibles){
            if(visible.getName().equals(text)){
                return String.valueOf(visible.hasVisible(player));
            }
        }
        return "true";
    }

    public void registerVisible(Visible visible){
        if(!visibles.contains(visible)) {
            visibles.add(visible);
        }
    }

    public void unregisterVisible(Visible visible){
        visibles.remove(visible);
    }

    public static VisiblePlaceholder getInstance() {
        return instance;
    }
}
