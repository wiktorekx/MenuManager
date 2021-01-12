# MenuManager

```java
    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().isPluginEnabled("MenuManager")) {
            if (!getDataFolder().exists()) getDataFolder().mkdir();
            MenuLoader menuLoader = MenuManagerAPI.getMenuLoader(getDataFolder());
            menuLoader.loadMenuFile("Menu.yml");
            Menu menu = menuLoader.getMenu("exampleMenu");
            MenuOpener.openMenu(Bukkit.getPlayer("PlayerName"), menu);
        }
    }

```

## Menu.yml

```yml
menus:
  exampleMenu:
    title: "Title"
    rows: 5
    items:
      - 5: exampleItem
items:
  exampleItem:
    material: Stone
    title: "&eExample Item"
    lore:
      - "&eExample Lore"
      - "..."
```
