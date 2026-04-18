package me.zmaster.zgui.inventory;

import me.zmaster.zgui.Menu;
import me.zmaster.zgui.ZGui;
import me.zmaster.zgui.inventory.context.ClickContext;
import me.zmaster.zgui.inventory.element.Clickable;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.element.abstraction.PreviousMenuElement;
import me.zmaster.zgui.inventory.element.view.ElementsView;
import me.zmaster.zgui.inventory.element.view.SimpleElementsView;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import me.zmaster.zgui.inventory.meta.MenuMeta;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractMenu implements Menu {

    final Inventory inventory;
    private final Map<Integer, Clickable> clicks = new HashMap<>();
    private final ElementsView<Element> elementsView = new SimpleElementsView<>(this);
    private final Menu previousMenu;
    private boolean initialized;

    public AbstractMenu(@NotNull MenuMeta<?> menuMeta, @NotNull String inventoryName, @Nullable Menu previousMenu) {
        this.inventory = menuMeta.createInventory(inventoryName);
        this.previousMenu = previousMenu;

        menuMeta.getElementMeta("close")
                .map(meta -> Element.from(meta, click -> click.getPlayer().closeInventory()))
                .ifPresent(elementsView::addElement);

        menuMeta.getElementMeta("previous")
                .map(meta -> new PreviousMenuElement(meta, this))
                .ifPresent(elementsView::addElement);

        menuMeta.getElementMetas().values().stream()
                .filter(ElementMeta::isAutoApply)
                .forEach(meta -> {
                    ItemStack item = meta.getDefaultItem();
                    meta.getSlots().forEach(slot -> setItem(slot, item));
                });
    }

    public AbstractMenu(@NotNull MenuMeta<?> menuMeta, @Nullable Menu previousMenu) {
        this(menuMeta, menuMeta.getInventoryName(), previousMenu);
    }

    @Override
    public final void open(@NotNull Player player) {
        Objects.requireNonNull(player, "player must not be null");

        ZGui.get().getInventoryMenuManager().registerMenu(this);

        if (!initialized) {
            initialize();
            initialized = true;
        }

        if (!inventory.getViewers().contains(player)) player.openInventory(inventory);
    }

    public @NotNull ElementsView<Element> getElementsView() {
        return elementsView;
    }

    public @Nullable Menu getPreviousMenu() {
        return previousMenu;
    }

    public void setTitle(String title) {
        for (HumanEntity player : inventory.getViewers()) {
            player.getOpenInventory().setTitle(title);
        }
    }

    public void setItem(int slot, @Nullable ItemStack item) {
        inventory.setItem(slot, item);
    }

    public void setClick(int slot, @Nullable Clickable click) {
        if (click == null){
            clicks.remove(slot);
            return;
        }

        clicks.put(slot, click);
    }

    public void openPrevious(@NotNull Player player) {
        Objects.requireNonNull(player, "player must not be null");

        if (previousMenu != null) previousMenu.open(player);
    }

    public void close() {
        inventory.getViewers().forEach(HumanEntity::closeInventory);
    }

    protected void initialize() {}

    protected void onClick(ClickContext event) {
        Clickable click = clicks.get(event.getSlot());
        if (click != null) click.onClick(event);
    }

    protected void onBottomClick(ClickContext event) {}

    protected void onOpen(InventoryOpenEvent event) {}

    protected void onClose(InventoryCloseEvent event) {}

}
