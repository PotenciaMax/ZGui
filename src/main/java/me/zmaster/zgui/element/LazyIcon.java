package me.zmaster.zgui.element;

import me.zmaster.zgui.Slot;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

public abstract class LazyIcon implements Element {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(LazyIcon.class);

    private ItemStack currentItem;

    public abstract ItemStack getPlaceholderItem();

    public abstract CompletableFuture<ItemStack> loadItem();

    @Override
    public void render(Slot slot) {
        if (currentItem == null) {
            currentItem = getPlaceholderItem();

            loadItem().thenAcceptAsync(loadedItem -> {
                currentItem = loadedItem;
                if (slot.getElement() == this) slot.setItem(currentItem);

            }, runnable -> Bukkit.getScheduler().runTask(plugin, runnable));
        }

        slot.setItem(currentItem);
    }
}
