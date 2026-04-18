package me.zmaster.zgui.inventory.context;

import me.zmaster.zgui.inventory.AbstractMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClickContext {

    private final AbstractMenu menu;
    private final InventoryClickEvent event;

    public ClickContext(AbstractMenu menu, InventoryClickEvent event) {
        this.menu = menu;
        this.event = event;
    }

    public AbstractMenu getMenu() {
        return menu;
    }

    public Player getPlayer() {
        return (Player) event.getWhoClicked();
    }

    public int getSlot() {
        return event.getSlot();
    }

    public ClickType getType() {
        return event.getClick();
    }

    public InventoryAction getAction() {
        return event.getAction();
    }

    public ItemStack getItem() {
        return event.getCurrentItem();
    }

    public void setItem(ItemStack item) {
        event.setCurrentItem(item);
    }

    public boolean isEventCancelled() {
        return event.isCancelled();
    }

    public void setEventCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }

}
