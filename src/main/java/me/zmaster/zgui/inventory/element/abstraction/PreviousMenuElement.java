package me.zmaster.zgui.inventory.element.abstraction;

import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.context.ClickContext;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import me.zmaster.zgui.inventory.meta.data.ItemData;
import org.bukkit.inventory.ItemStack;

public class PreviousMenuElement extends AbstractElement {

    private final ElementMeta meta;
    private final AbstractMenu menu;

    public PreviousMenuElement(ElementMeta meta, AbstractMenu menu) {
        super(meta);
        this.meta = meta;
        this.menu = menu;
    }

    @Override
    public ItemStack getItem() {
        return meta.getItem(menu.getPreviousMenu() == null ? "not_previous" : ItemData.DEFAULT_STATE);
    }

    @Override
    public void onClick(ClickContext context) {
        menu.openPrevious(context.getPlayer());
    }
}
