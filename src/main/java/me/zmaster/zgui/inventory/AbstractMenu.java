package me.zmaster.zgui.inventory;

import me.zmaster.zgui.Menu;
import me.zmaster.zgui.ZGui;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import me.zmaster.zgui.inventory.meta.MenuMeta;
import me.zmaster.zgui.inventory.meta.data.ItemData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public abstract class AbstractMenu implements Menu {

    private final Map<Integer, Element> elements = new HashMap<>();
    private final Inventory inventory;
    private final Menu previousMenu;

    public AbstractMenu(@NotNull MenuMeta<?> menuMeta, @NotNull String inventoryName, @Nullable Menu previousMenu) {
        this.inventory = menuMeta.createInventory(inventoryName);
        this.previousMenu = previousMenu;

        applyElement(menuMeta,"close", meta -> Element.from(meta.getDefaultItem(), click -> click.getWhoClicked().closeInventory()));
        applyElement(menuMeta,"previous", this::previousIcon);

        for (ElementMeta meta : menuMeta.getElementMetas().values()) {
            if (meta.isAutoApply()) {
                setElement(meta.getSlots(), Element.from(meta.getDefaultItem()));
            }
        }
    }

    public AbstractMenu(@NotNull MenuMeta<?> menuMeta, @Nullable Menu previousMenu) {
        this(menuMeta, menuMeta.getInventoryName(), previousMenu);
    }

    @Override
    public void open(@NotNull HumanEntity player) {
        Objects.requireNonNull(player, "player must not be null");

        ZGui.get().getInventoryMenuManager().registerMenu(player.getUniqueId(), this);
        player.openInventory(inventory);
    }

    public @Nullable Menu getPreviousMenu() {
        return previousMenu;
    }

    public void openPrevious(@NotNull HumanEntity player) {
        Objects.requireNonNull(player, "player must not be null");

        if (previousMenu != null) previousMenu.open(player);
    }

    public void setTittle(String title) {
        for (HumanEntity player : inventory.getViewers()) {
            player.getOpenInventory().setTitle(title);
        }
    }

    protected void onClick(InventoryClickEvent event) {
        Element element = elements.get(event.getSlot());
        if (element != null) {
            element.onClick(event);
        }
    }

    protected void onBottomClick(InventoryClickEvent event) {

    }


    protected void onOpen(InventoryOpenEvent event) {
    }

    protected void onClose(InventoryCloseEvent event) {
    }

    public @Nullable <M extends ElementMeta, T extends Element> T applyElement(MenuMeta<M> menuMeta, String key, Function<M, T> factory) {
        return Element.applyElement(menuMeta, key, factory, this::setElement);
    }

    public void setElement(Iterable<Integer> slots, Element element, boolean render) {
        for (int slot : slots) elements.put(slot, element);

        if (render) {
            ItemStack item = element != null ? element.getItem() : null;
            for (int slot : slots) inventory.setItem(slot, item);
        }
    }

    public void setElement(Iterable<Integer> slots, Element element) {
        setElement(slots, element, true);
    }

    public void setElement(int slot, Element element, boolean render) {
        setElement(Collections.singletonList(slot), element, render);
    }

    public void setElement(int slot, Element element) {
        setElement(Collections.singletonList(slot), element, true);
    }

    private Element previousIcon(ElementMeta iconMeta) {
        return Element.from(iconMeta.getItem(previousMenu == null ? "not_previous" : ItemData.DEFAULT_STATE), click ->
                openPrevious(click.getWhoClicked()));
    }
}
