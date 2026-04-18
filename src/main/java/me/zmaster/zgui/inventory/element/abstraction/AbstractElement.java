package me.zmaster.zgui.inventory.element.abstraction;

import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class AbstractElement implements Element {

    private final ElementMeta meta;

    public AbstractElement(@NotNull ElementMeta meta) {
        this.meta = Objects.requireNonNull(meta, "meta must not be null");
    }

    @Override
    public ItemStack getItem() {
        return meta.getDefaultItem();
    }

    @Override
    public @NotNull List<Integer> getSlots() {
        return meta.getSlots();
    }
}
