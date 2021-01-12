package pl.wiktorekx.menumanager.objectives;

import pl.wiktorekx.menumanager.builders.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String name;
    private ItemBuilder itemBuilder;
    private String visible;
    private List<String> actions;

    private Item(String name, ItemBuilder itemBuilder, String visible, List<String> actions) {
        this.name = name;
        this.itemBuilder = itemBuilder;
        this.visible = visible;
        this.actions = actions;
    }

    public static class Builder{
        private String name;
        private ItemBuilder itemBuilder;
        private String visible = "";
        private List<String> actions = new ArrayList<>();

        public Builder(String name, ItemBuilder itemBuilder) {
            this.name = name;
            this.itemBuilder = itemBuilder;
        }

        public Builder setVisible(String visible) {
            this.visible = visible;
            return this;
        }

        public Builder setActions(List<String> actions) {
            this.actions = actions;
            return this;
        }

        public Item build(){
            return new Item(name, itemBuilder, visible, actions);
        }
    }

    public String getName() {
        return name;
    }

    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    public String getVisible() {
        return visible;
    }

    public List<String> getActions() {
        return actions;
    }
}
