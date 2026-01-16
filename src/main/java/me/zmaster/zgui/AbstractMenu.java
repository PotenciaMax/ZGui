package me.zmaster.zgui;

import me.zmaster.zgui.element.Element;
import me.zmaster.zgui.element.Icon;
import me.zmaster.zgui.meta.ElementMeta;
import me.zmaster.zgui.meta.MenuMeta;
import me.zmaster.zgui.meta.data.ItemData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Base implementation of the Menu interface representing a GUI menu.
 * <p>
 * Handles inventory management, icon placement, and navigation between menus.
 * Provides default implementations for opening, closing, and handling clicks in the menu.
 */
public abstract class AbstractMenu implements Menu {

    final Map<Integer, Element> elements = new HashMap<>();
    final Inventory inventory;
    private final MenuMeta metadata;
    private final Menu previousMenu;

    /**
     * Constructor that creates the menu inventory based on metadata.
     *
     * @param metadata     the metadata describing menu configuration
     * @param previousMenu the previous menu, can be null
     */
    public AbstractMenu(@NotNull MenuMeta metadata, @NotNull String inventoryName, @Nullable Menu previousMenu) {
        this.metadata = metadata;
        this.inventory = metadata.createInventory(inventoryName);
        this.previousMenu = previousMenu;

        applyStaticIcons();

        applyElement("close", meta -> Icon.from(meta.getDefaultItem(), click -> click.getWhoClicked().closeInventory()));
        applyElement("previous", this::previousIcon);
    }

    public AbstractMenu(@NotNull MenuMeta metadata, @Nullable Menu previousMenu) {
        this(metadata, metadata.getInventoryName(), previousMenu);
    }

    public MenuMeta getMetadata() {
        return metadata;
    }

    public @Nullable Menu getPreviousMenu() {
        return previousMenu;
    }

    @Override
    public Slot getSlot(int index) {
        return new Slot(this, index);
    }

    @Override
    public void open(HumanEntity player) {
        ZGui.get().registerMenu(this);
        player.openInventory(inventory);
    }

    @Override
    public void openPrevious(HumanEntity player) {
        if (previousMenu != null) previousMenu.open(player);
    }

    /**
     * Called when the inventory receives a click event.
     * Handles the icon click actions if the clicked slot has an icon.
     *
     * @param event the inventory click event
     */
    protected void onClick(InventoryClickEvent event) {
        if (inventory.equals(event.getClickedInventory())) {
            Element element = elements.get(event.getSlot());
            if (element != null) {
                element.onClick(event);
            }
        }
    }

    /**
     * Called when the inventory is opened.
     * Override to add custom behavior on menu open.
     *
     * @param event the inventory open event
     */
    protected void onOpen(InventoryOpenEvent event) {
    }

    /**
     * Called when the inventory is closed.
     * Override to add custom behavior on menu close.
     *
     * @param event the inventory close event
     */
    protected void onClose(InventoryCloseEvent event) {
    }

    protected void applyElement(String key, Function<ElementMeta, Element> factory) {
        applyElement(key, factory, true);
    }

    protected void applyElement(String key, Function<ElementMeta, Element> factory, boolean render) {
        ElementMeta meta = metadata.getElementMeta(key);
        if (meta == null) return;

        Element element = factory.apply(meta);
        if (element == null) return;

        for (int index : meta.getSlots()) {
            getSlot(index).setElement(element, render);
        }
    }

    protected void applyStaticIcons() {
        for (ElementMeta meta : getMetadata().getElementMetas().values()) {
            boolean staticIcon = meta.getData("static", Boolean.class).orElse(false);

            if (staticIcon) for (int index : meta.getSlots()) {
                getSlot(index).setItem(meta.getDefaultItem());
            }
        }
    }

    private Icon previousIcon(ElementMeta iconMeta) {
        return Icon.from(iconMeta.getItem(previousMenu == null ? "not_previous" : ItemData.DEFAULT_STATE), click ->
                openPrevious(click.getWhoClicked()));
    }
}
