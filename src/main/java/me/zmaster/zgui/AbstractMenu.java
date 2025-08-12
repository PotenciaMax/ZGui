package me.zmaster.zgui;

import me.zmaster.zgui.icon.Icon;
import me.zmaster.zgui.icon.IconHandler;
import me.zmaster.zgui.icon.IconMetadata;
import me.zmaster.zgui.menu.Menu;
import me.zmaster.zgui.menu.MenuMetadata;
import me.zmaster.zgui.menu.SlotPattern;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of the Menu interface representing a GUI menu.
 * <p>
 * Handles inventory management, icon placement, and navigation between menus.
 * Provides default implementations for opening, closing, and handling clicks in the menu.
 */
public abstract class AbstractMenu implements Menu {

    private final Map<Integer, Icon> icons = new HashMap<>();
    private final Inventory inventory;
    private final Menu previousMenu;

    public Map<Integer, Icon> getIcons() {
        return icons;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void open(HumanEntity player) {
        ZGui.get().registerMenu(inventory, this);
        player.openInventory(inventory);
    }

    @Override
    public void openPrevious(HumanEntity player) {
        if (previousMenu == null) {
            player.closeInventory();
            return;
        }

        previousMenu.open(player);
    }

    /**
     * Adds or replaces an icon in multiple slots.
     *
     * @param slots           the list of slots to update
     * @param icon            the icon to place (null to remove)
     */
    public void putIcon(@NotNull List<Integer> slots, @Nullable Icon icon) {
        putIcon(slots, icon, true);
    }

    /**
     * Adds or replaces an icon in multiple slots, optionally updating the inventory immediately.
     *
     * @param slots            the list of slots to update
     * @param icon             the icon to place (null to remove)
     * @param updateInventory  whether to update the Bukkit inventory immediately
     */
    public void putIcon(@NotNull List<Integer> slots, @Nullable Icon icon, boolean updateInventory) {
        for (int slot : slots) {
            putIcon(slot, icon, updateInventory);
        }
    }

    /**
     * Adds or replaces an icon in a specific slot, optionally updating the inventory immediately.
     *
     * @param slot             the slot index to update
     * @param icon             the icon to place (null to remove)
     * @param updateInventory  whether to update the Bukkit inventory immediately
     */
    public void putIcon(int slot, @Nullable Icon icon, boolean updateInventory) {
        if (icon == null) {
            icons.remove(slot);
            if (updateInventory) {
                inventory.setItem(slot, null);
            }
            return;
        }

        icons.put(slot, icon);
        if (updateInventory) {
            inventory.setItem(slot, icon.getItem());
        }
    }

    /**
     * Called when the inventory receives a click event.
     * Handles the icon click actions if the clicked slot has an icon.
     *
     * @param event the inventory click event
     */
    protected void onClick(InventoryClickEvent event) {
        if (inventory.equals(event.getClickedInventory())) {
            Icon icon = icons.get(event.getSlot());
            if (icon != null) {
                icon.clickAction(event);
            }
        }
    }

    /**
     * Called when the inventory is opened.
     * Override to add custom behavior on menu open.
     *
     * @param event the inventory open event
     */
    protected void onOpen(InventoryOpenEvent event) {}

    /**
     * Called when the inventory is closed.
     * Override to add custom behavior on menu close.
     *
     * @param event the inventory close event
     */
    protected void onClose(InventoryCloseEvent event) {}

    @IconHandler("close")
    public Icon closeIcon(IconMetadata metadata) {
        return new Icon() {
            @Override
            public ItemStack getItem() {
                return metadata.getDefaultItem();
            }

            @Override
            public void clickAction(InventoryClickEvent event) {
                event.getWhoClicked().closeInventory();
            }
        };
    }

    @IconHandler("previous")
    public Icon previousIcon(IconMetadata metadata) {
        return new Icon() {
            @Override
            public ItemStack getItem() {
                if (previousMenu == null) {
                    return metadata.getItems().get("close");
                }
                return metadata.getDefaultItem();
            }

            @Override
            public void clickAction(InventoryClickEvent event) {
                openPrevious(event.getWhoClicked());
            }
        };
    }

    /**
     * Constructor that creates the menu inventory based on metadata.
     *
     * @param metadata     the metadata describing menu configuration
     * @param previousMenu the previous menu, can be null
     */
    public AbstractMenu(@NotNull MenuMetadata metadata, @Nullable Menu previousMenu) {
        this.inventory = metadata.getSlotPattern().createInventory(metadata.getInventoryName());
        this.previousMenu = previousMenu;
    }

    /**
     * Constructor that creates the menu inventory based on slot pattern and name.
     *
     * @param slotPattern   the slot pattern to use for inventory layout
     * @param inventoryName the title of the inventory
     * @param previousMenu  the previous menu, can be null
     */
    public AbstractMenu(@NotNull SlotPattern slotPattern, @NotNull String inventoryName, @Nullable Menu previousMenu) {
        this.inventory = slotPattern.createInventory(inventoryName);
        this.previousMenu = previousMenu;
    }

}
