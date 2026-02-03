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
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        applyElement("close", meta -> Icon.from(meta.getDefaultItem(), click -> click.getWhoClicked().closeInventory()));
        applyElement("previous", this::previousIcon);

        for (ElementMeta meta : metadata.getElementMetas().values()) {
            if (meta.getData("static", Boolean.class).orElse(false)) {
                setElement(meta.getSlots(), Icon.from(meta.getDefaultItem()));
            }
        }
    }

    public AbstractMenu(@NotNull MenuMeta metadata, @Nullable Menu previousMenu) {
        this(metadata, metadata.getInventoryName(), previousMenu);
    }

    public @NotNull MenuMeta getMetadata() {
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
    public void open(@NotNull HumanEntity player) {
        Objects.requireNonNull(player, "player must not be null");

        ZGui.get().registerMenu(this);
        player.openInventory(inventory);
    }

    @Override
    public void openPrevious(@NotNull HumanEntity player) {
        Objects.requireNonNull(player, "player must not be null");

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

    protected @Nullable <E extends Element> E applyElement(String key, Function<ElementMeta, E> factory, boolean render) {
        ElementMeta meta = metadata.getElementMeta(key);
        if (meta == null) return null;

        E element = factory.apply(meta);
        if (element == null) return null;

        setElement(meta.getSlots(), element, render);
        return element;
    }

    protected @Nullable <E extends Element> E applyElement(String key, Function<ElementMeta, E> factory) {
        return applyElement(key, factory, true);
    }

    protected void setElement(List<Integer> slots, Element element, boolean render) {
        for (int slot : slots) getSlot(slot).setElement(element, render);
    }

    protected void setElement(List<Integer> slots, Element element) {
        setElement(slots, element, true);
    }

    private Icon previousIcon(ElementMeta iconMeta) {
        return Icon.from(iconMeta.getItem(previousMenu == null ? "not_previous" : ItemData.DEFAULT_STATE), click ->
                openPrevious(click.getWhoClicked()));
    }
}
