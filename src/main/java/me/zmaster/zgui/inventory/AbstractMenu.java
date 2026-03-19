package me.zmaster.zgui.inventory;

import me.zmaster.zgui.Menu;
import me.zmaster.zgui.ZGui;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.element.Icon;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import me.zmaster.zgui.inventory.meta.MenuMeta;
import me.zmaster.zgui.inventory.meta.data.ItemData;
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

public abstract class AbstractMenu implements Menu {

    final Map<Integer, Element> elements = new HashMap<>();
    final Inventory inventory;
    private final Menu previousMenu;

    public AbstractMenu(@NotNull MenuMeta<?> menuMeta, @NotNull String inventoryName, @Nullable Menu previousMenu) {
        this.inventory = menuMeta.createInventory(inventoryName);
        this.previousMenu = previousMenu;

        applyElement(menuMeta,"close", meta -> Icon.from(meta.getDefaultItem(), click -> click.getWhoClicked().closeInventory()));
        applyElement(menuMeta,"previous", this::previousIcon);

        for (ElementMeta meta : menuMeta.getElementMetas().values()) {
            if (meta.isAutoApply()) {
                setElement(meta.getSlots(), Icon.from(meta.getDefaultItem()));
            }
        }
    }

    public AbstractMenu(@NotNull MenuMeta<?> menuMeta, @Nullable Menu previousMenu) {
        this(menuMeta, menuMeta.getInventoryName(), previousMenu);
    }

    @Override
    public void open(@NotNull HumanEntity player) {
        Objects.requireNonNull(player, "player must not be null");

        ZGui.get().getInventoryMenuManager().registerMenu(this);
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

    public Slot getSlot(int index) {
        return new Slot(this, index);
    }

    protected void onClick(InventoryClickEvent event) {
        if (inventory.equals(event.getClickedInventory())) {
            Element element = elements.get(event.getSlot());
            if (element != null) {
                element.onClick(event);
            }
        }
    }

    protected void onOpen(InventoryOpenEvent event) {
    }

    protected void onClose(InventoryCloseEvent event) {
    }

    protected @Nullable <E extends ElementMeta, T extends Element> T applyElement(MenuMeta<E> menuMeta, String key, Function<E, T> factory, boolean render) {
        E meta = menuMeta.getElementMeta(key);
        if (meta == null) return null;

        T element = factory.apply(meta);
        if (element == null) return null;

        setElement(meta.getSlots(), element, render);
        return element;
    }

    protected @Nullable <E extends ElementMeta, T extends Element> T applyElement(MenuMeta<E> menuMeta, String key, Function<E, T> factory) {
        return applyElement(menuMeta, key, factory, true);
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
