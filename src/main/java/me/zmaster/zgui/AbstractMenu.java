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

    public void putIcon(@NotNull List<Integer> slots, @Nullable Icon icon) {
        putIcon(slots, icon, true);
    }

    public void putIcon(@NotNull List<Integer> slots, @Nullable Icon icon, boolean updateInventory) {
        for (int slot : slots) {
            putIcon(slot, icon, updateInventory);
        }
    }

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

    protected void onClick(InventoryClickEvent event) {
        if (inventory.equals(event.getClickedInventory())) {
            Icon icon = icons.get(event.getSlot());
            if (icon != null) {
                icon.clickAction(event);
            }
        }
    }

    protected void onOpen(InventoryOpenEvent event) {}

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

    public AbstractMenu(@NotNull MenuMetadata metadata, @Nullable Menu previousMenu) {
        this.inventory = metadata.getSlotPattern().createInventory(metadata.getInventoryName());
        this.previousMenu = previousMenu;
    }

    public AbstractMenu(@NotNull SlotPattern slotPattern, @NotNull String inventoryName, @Nullable Menu previousMenu) {
        this.inventory = slotPattern.createInventory(inventoryName);
        this.previousMenu = previousMenu;
    }

}
