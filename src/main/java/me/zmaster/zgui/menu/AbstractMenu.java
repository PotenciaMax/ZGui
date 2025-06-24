package me.zmaster.zgui.menu;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.zmaster.zgui.ZGui;
import me.zmaster.zgui.icon.Icon;
import me.zmaster.zgui.icon.IconHandler;
import me.zmaster.zgui.icon.IconMetadata;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMenu implements Menu {

    private final Map<Integer, Icon> icons = new HashMap<>();
    private final SlotPattern slotPattern;
    private final Inventory inventory;
    private final Menu previousMenu;

    public Map<Integer, Icon> getIcons() {
        return icons;
    }

    public SlotPattern getSlotPattern() {
        return slotPattern;
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

    public void putIcon(char caractere, @NotNull Icon icon) {
        putIcon(caractere, icon, true);
    }

    public void putIcon(char caractere, @NotNull Icon icon, boolean update) {
        for (int slot : slotPattern.getSlotsByChar(caractere)) {
            icons.put(slot, icon);
            if (update) {
                inventory.setItem(slot, icon.getItem());
            }
        }
    }

    public void onClick(InventoryClickEvent event) {
        if (inventory.equals(event.getClickedInventory())) {
            Icon icon = icons.get(event.getSlot());
            if (icon != null) {
                icon.clickAction(event);
            }
        }
    }

    public void onOpen(InventoryOpenEvent event) {}

    public void onClose(InventoryCloseEvent event) {}

    @IconHandler("close")
    public void closeIcon(IconMetadata metadata) {
        putIcon(metadata.getSlot(), new Icon() {
            @Override
            public ItemStack getItem() {
                return metadata.getItem();
            }

            @Override
            public void clickAction(InventoryClickEvent event) {
                event.getWhoClicked().closeInventory();
            }
        });
    }

    @IconHandler("previous")
    public void previousIcon(IconMetadata metadata) {
        putIcon(metadata.getSlot(), new Icon() {
            @Override
            public ItemStack getItem() {
                if (previousMenu == null) {
                    return metadata.getItem("close");
                }
                return metadata.getItem();
            }

            @Override
            public void clickAction(InventoryClickEvent event) {
                openPrevious(event.getWhoClicked());
            }
        });
    }

    public AbstractMenu(MenuMetadata metadata, @Nullable Menu previousMenu) {
        this.slotPattern = metadata.getSlotPattern();
        this.inventory = slotPattern.createInventory(metadata.getInventoryName());
        this.previousMenu = previousMenu;
    }

}
