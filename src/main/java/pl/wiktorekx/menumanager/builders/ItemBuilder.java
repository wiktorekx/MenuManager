package pl.wiktorekx.menumanager.builders;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.yaml.snakeyaml.error.YAMLException;
import pl.wiktorekx.menumanager.objectives.Item;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {
    private ItemStack itemStack;
    private boolean hasThisPlayer;

    public ItemBuilder(ConfigurationSection section){
        if(section.contains("material") && section.isString("material")) {
            try {
                itemStack = new ItemStack(Material.getMaterial(section.getString("material").toUpperCase()));
            } catch (Exception ex) {
                throw new YAMLException("(" + section.getCurrentPath() + ") No Find Material \"" + section.getString("material").toUpperCase() + "\".");
            }
        } else if(section.contains("id") && section.isInt("id")){
            try {
            itemStack = new ItemStack(Material.getMaterial(section.getInt("id")));
            } catch (Exception ex) {
                throw new YAMLException("(" + section.getCurrentPath() + ") No Find Item Id \"" + section.getInt("id") + "\".");
            }
        } else if(section.contains("skull") && section.isString("skull")){
            itemStack = new ItemStack(Material.SKULL);
            setSkull(section.getString("skull"));
        }
        if(itemStack != null && this.getItemMeta() != null) {
            if (section.contains("amount") && section.isInt("amount")) {
                this.setAmount(section.getInt("amount"));
            }
            if (section.contains("data") && section.isInt("data")) {
                this.setDurability((short) section.getInt("data"));
            }
            if (section.contains("title") && section.isString("title")) {
                this.setDisplayName(section.getString("title"));
            }
            if (section.contains("lore") && section.isList("lore")) {
                this.setLore(section.getStringList("lore"));
            }
            if (section.contains("lore") && section.isList("lore")) {
                this.setLore(section.getStringList("lore"));
            }
            if (section.contains("enchants")) {
                for (Map<?, ?> enchants : section.getMapList("enchants")) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) enchants.entrySet().iterator().next();
                    String[] values = entry.getValue().replace(" ", "").split(":");
                    int level = 0;
                    boolean bool = false;
                    if (values.length > 1) {
                        bool = values[1].equalsIgnoreCase("true");
                    } else {
                        level = Integer.parseInt(values[0]);
                    }
                    Enchantment enchantment = Enchantment.getByName(entry.getKey());
                    if (enchantment != null) {
                        this.addEnchant(enchantment, level, bool);
                    }
                }
            }
            if (section.contains("itemFlags") && section.isList("itemFlags")) {
                for (String itemFlag : section.getStringList("itemFlags")) {
                    this.addItemFlag(ItemFlag.valueOf(itemFlag.toUpperCase()));
                }
            }
        }
    }

    /**
     * Return Item Builder with Item Stack
     * @param itemStack - Seated Item
     * @returm Item Builder
     */
    public ItemBuilder(ItemStack itemStack){
        Objects.requireNonNull(itemStack);
        this.itemStack = itemStack;
    }

    /**
     * Return Item Builder with seated Material
     * @param material - Item Type
     * @returm Item Builder
     */
    public ItemBuilder(Material material){
        Objects.requireNonNull(material);
        itemStack = new ItemStack(material);
    }

    /**
     * Return Item Builder with seated Material Name
     * @param material - Item Type Name
     * @returm Item Builder
     */
    public ItemBuilder(String material){
        this(Material.getMaterial(material));
    }

    /**
     * Return Item Builder with seated Material Id
     * @param material - Item Type Id
     * @returm Item Builder
     */
    public ItemBuilder(int material){
        this(Material.getMaterial(material));
    }

    /**
     * Return Item Builder with seated Item Amount
     * @param amount - Item Amount
     * @return Item Builder
     */
    public ItemBuilder setAmount(int amount){
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Return Item Builder with seated Item Durability
     * @param durability - Item Durability
     * @return Item Builder
     */
    public ItemBuilder setDurability(short durability){
        itemStack.setDurability(durability);
        return this;
    }

    /**
     * Return Item Builder with seated Item DisplayName
     * @param displayName - Item DisplayName
     * @return Item Builder
     */
    public ItemBuilder setDisplayName(String displayName){
        Objects.requireNonNull(displayName);
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Return Item Builder with seated Item Lore
     * @param lore - Item Lore
     * @return Item Builder
     */
    public ItemBuilder setLore(List<String> lore){
        Objects.requireNonNull(lore);
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setLore(lore);
        this.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Return Item Builder with seated Item Lore
     * @param lore - Item Lore
     * @return Item Builder
     */
    public ItemBuilder addLore(String... lore){
        Objects.requireNonNull(lore);
        List<String> itemLore = this.getLore();
        if(itemLore == null) itemLore = new ArrayList<>();
        itemLore.addAll(Arrays.asList(lore));
        this.setLore(itemLore);
        return this;
    }

    /**
     * Return Item Builder with added Item Enchant
     * @param enchantment - Enchant Enchantment
     * @param level - Enchant Level
     * @param ignoreLevelRestriction - Enchant IgnoreLevelRestriction
     * @return Item Builder
     */
    public ItemBuilder addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction){
        Objects.requireNonNull(enchantment);
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        this.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Return Item Builder with added Item Flag
     * @param itemFlag - Item Flag
     * @return Item Builder
     */
    public ItemBuilder addItemFlag(ItemFlag itemFlag){
        Objects.requireNonNull(itemFlag);
        this.addItemFlags(itemFlag);
        return this;
    }

    /**
     * Return Item Builder with added Item Flags
     * @param itemFlags - Item Flags
     * @return Item Builder
     */
    public ItemBuilder addItemFlags(ItemFlag... itemFlags){
        Objects.requireNonNull(itemFlags);
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addItemFlags(itemFlags);
        this.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Return Item Builder with seated Item Meta
     * @param itemMeta - Item Meta
     * @return Item Builder
     */
    public ItemBuilder setItemMeta(ItemMeta itemMeta){
        Objects.requireNonNull(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return this;
    }


    /**
     * Return Item Builder with seated Item Skull
     * @param skull - skull skin value
     * @return Item Builder
     */
    public ItemBuilder setSkull(String skull){
        Objects.requireNonNull(skull);
        itemStack.setType(Material.SKULL_ITEM);
        this.setDurability((short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        try{
            Field field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), UUID.randomUUID().toString().substring(0, 10));
            gameProfile.getProperties().put("textures", new Property("textures", skull));
            field.set(skullMeta, gameProfile);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        this.setItemMeta(skullMeta);
        return this;
    }

    /**
     * Return Item Builder with seated Item Skull
     * @param player - skull skin player
     * @return Item Builder
     */
    public ItemBuilder setSkull(OfflinePlayer player){
        Objects.requireNonNull(player);
        itemStack.setType(Material.SKULL_ITEM);
        this.setDurability((short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        skullMeta.setOwner(player.getName());
        this.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder setSkullThisPlayer(){
        hasThisPlayer = true;
        return this;
    }

    /**
     * Return boolean with has seated Item Flag
     * @param itemFlag - Item Flag
     * @return boolean
     */
    public boolean hasItemFlags(ItemFlag itemFlag){
        Objects.requireNonNull(itemFlag);
        return this.getItemMeta().hasItemFlag(itemFlag);
    }

    /**
     * Return Blinded Item Stack
     * @return ItemStack
     */
    public ItemStack build(){
        return itemStack;
    }


    /**
     * Return Item Material
     * @return Material
     */
    public Material getType(){
        return itemStack.getType();
    }

    /**
     * Return Item Amount
     * @return Amount
     */
    public int getAmount(){
        return itemStack.getAmount();
    }

    /**
     * Return Item Durability
     * @return Durability
     */
    public short getDurability(){
        return itemStack.getDurability();
    }

    /**
     * Return Item DisplayName
     * @return DisplayName
     */
    public String getDisplayName(){
        return this.getItemMeta().hasDisplayName() ? this.getItemMeta().getDisplayName() : null;
    }
    /**
     * Return Item Lore
     * @return Lore
     */
    public List<String> getLore(){
        return this.getItemMeta().hasLore() ? this.getItemMeta().getLore() : null;
    }

    /**
     * Return Item Enchants
     * @return Enchants
     */
    public Map<Enchantment, Integer> getEnchants(){
        return this.getItemMeta().hasEnchants() ? this.getItemMeta().getEnchants() : null;
    }

    /**
     * Return Item Flags
     * @return ItemFlags
     */
    public Collection<ItemFlag> getItemFlags(){
        return this.getItemMeta().getItemFlags();
    }

    /**
     * Return Item Meta
     * @return ItemMeta
     */
    public ItemMeta getItemMeta(){
        return itemStack.getItemMeta();
    }

    public ItemBuilder clone(){
        ItemBuilder clone = new ItemBuilder(itemStack.clone());
        if(isHasThisPlayer()) clone.setSkullThisPlayer();
        return clone;
    }

    public boolean isHasThisPlayer() {
        return hasThisPlayer;
    }
}
