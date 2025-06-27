package me.zmaster.zgui.menu;

import me.zmaster.zgui.icon.IconHandler;
import me.zmaster.zgui.icon.IconMetadata;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public final class MenuMetadata {

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private final String inventoryName;
    private final SlotPattern slotPattern;
    private final Map<String, MethodHandle> iconMethods = new LinkedHashMap<>();
    private final Map<String, IconMetadata> iconMetas = new HashMap<>();

    @NotNull
    public String getInventoryName() {
        return inventoryName;
    }

    @NotNull
    public SlotPattern getSlotPattern() {
        return slotPattern;
    }

    @Nullable
    public IconMetadata getIconMeta(String key) {
        return iconMetas.get(key);
    }

    @NotNull
    public Map<String, IconMetadata> applyIcons(AbstractMenu menu) {
        Map<String, IconMetadata> notApplied = new HashMap<>(iconMetas);

        iconMethods.forEach((key, method) -> {
            IconMetadata meta = notApplied.remove(key);

            if (meta != null) try {
                method.invoke(menu, meta);

            } catch (Throwable e) {
                String menuName = menu.getClass().getSimpleName();
                throw new RuntimeException(String.format("An exception occurred while applying icon %s in %s", key, menuName), e);
            }
        });

        return notApplied;
    }

    public void applyAllIcons(AbstractMenu menu) {
        applyIcons(menu).values().forEach(m -> menu.putIcon(m.getSlot(), m::getDefaultItem));
    }

    public MenuMetadata(YamlConfiguration file, Class<? extends AbstractMenu> menuClass) {
        String inventoryName = file.getString("name");
        this.inventoryName = inventoryName == null ? "" : ChatColor.translateAlternateColorCodes('&', inventoryName);
        this.slotPattern = new SlotPattern(file.getStringList("slot_pattern"));

        initIconMethods(menuClass);
        initIconMetas(file);
    }

    private void initIconMethods(Class<?> menuClass) {
        Predicate<Method> filter = method -> method.isAnnotationPresent(IconHandler.class);
        Comparator<Method> comp = Comparator.comparingInt(method -> method.getAnnotation(IconHandler.class).priority());

        Arrays.stream(menuClass.getMethods())
                .filter(filter)
                .sorted(comp.reversed())
                .forEach(method -> {
                    IconHandler iconBuilder = method.getAnnotation(IconHandler.class);
                    try {
                        iconMethods.put(iconBuilder.value(), lookup.unreflect(method));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void initIconMetas(YamlConfiguration file) {
        ConfigurationSection slotsSection = file.getConfigurationSection("slots");
        if (slotsSection == null) {
            return;
        }

        for (String key : slotsSection.getKeys(false)) {
            IconMetadata iconMetadata = new IconMetadata(file, key);
            iconMetas.put(key, iconMetadata);
        }
    }

}
