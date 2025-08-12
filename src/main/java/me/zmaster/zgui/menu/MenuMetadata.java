package me.zmaster.zgui.menu;

import me.zmaster.zgui.AbstractMenu;
import me.zmaster.zgui.icon.Icon;
import me.zmaster.zgui.icon.IconHandler;
import me.zmaster.zgui.icon.IconMetadata;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * Represents the metadata of a menu loaded from a configuration file.
 * <p>
 * This class stores information such as:
 * - Menu title (inventoryName)
 * - Slot layout pattern (slotPattern)
 * - Icon metadata and their associated handler methods
 * <p>
 * It is responsible for linking the configuration file and the
 * menu's Java class, applying icon handlers, and providing access
 * to icon definitions.
 */
public final class MenuMetadata {

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private final String inventoryName;
    private final SlotPattern slotPattern;
    private final Map<String, MethodHandle> iconMethods = new LinkedHashMap<>();
    private final Map<String, IconMetadata> iconMetas = new HashMap<>();

    /**
     * Creates a new MenuMetadata from a configuration file and a menu class.
     * <p>
     * This will:
     * - Load the inventory name and slot pattern from the config.
     * - Scan the menu class for methods annotated with {@link IconHandler}.
     * - Load the icon metadata defined in the configuration.
     *
     * @param file       The configuration file containing the menu's settings.
     * @param menuClass  The menu class containing @IconHandler methods.
     */
    public MenuMetadata(FileConfiguration file, Class<? extends AbstractMenu> menuClass) {
        String inventoryName = file.getString("name");
        this.inventoryName = inventoryName == null ? "" : ChatColor.translateAlternateColorCodes('&', inventoryName);
        this.slotPattern = new SlotPattern(file.getStringList("slot_pattern"));

        initIconMethods(menuClass);
        initIconMetas(file);
    }

    /**
     * @return The formatted inventory title of the menu.
     */
    @NotNull
    public String getInventoryName() {
        return inventoryName;
    }

    /**
     * @return The slot pattern defining the menu's layout.
     */
    @NotNull
    public SlotPattern getSlotPattern() {
        return slotPattern;
    }

    /**
     * Retrieves the {@link IconMetadata} for a specific icon key.
     *
     * @param key The icon's key as defined in the menu configuration.
     * @return The icon metadata, or {@code null} if not found.
     */
    @Nullable
    public IconMetadata getIconMeta(String key) {
        return iconMetas.get(key);
    }

    /**
     * Applies all icons with matching {@link IconHandler} methods to the given menu.
     * <p>
     * Any icons without a handler method are returned in the result map,
     * allowing them to be processed separately (e.g., as static icons).
     *
     * @param menu The menu instance where icons will be applied.
     * @return A map of icons that were not applied (no handler method found).
     */
    @NotNull
    public Map<String, IconMetadata> applyIcons(AbstractMenu menu) {
        Map<String, IconMetadata> notApplied = new HashMap<>(iconMetas);

        iconMethods.forEach((key, method) -> {
            IconMetadata meta = notApplied.remove(key);

            if (meta != null) try {
                Icon icon = (Icon) method.invoke(menu, meta);
                if (icon != null) {
                    menu.putIcon(meta.getSlots(), icon);
                }

            } catch (Throwable e) {
                String menuName = menu.getClass().getSimpleName();
                throw new RuntimeException(String.format("An exception occurred while applying icon %s in %s", key, menuName), e);
            }
        });

        return notApplied;
    }

    /**
     * Applies all icons to the menu, treating those without handlers as static.
     * <p>
     * This is equivalent to {@link #applyIcons(AbstractMenu)}
     * followed by placing the default item for any remaining icons.
     *
     * @param menu The menu instance to populate with icons.
     */
    public void applyAllIcons(AbstractMenu menu) {
        applyIcons(menu).values().forEach(m -> menu.putIcon(m.getSlots(), m::getDefaultItem));
    }

    private void initIconMethods(Class<?> menuClass) {
        Predicate<Method> filter = method -> method.isAnnotationPresent(IconHandler.class);
        Comparator<Method> comp = Comparator.comparingInt(method -> method.getAnnotation(IconHandler.class).priority().ordinal());

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

    private void initIconMetas(FileConfiguration file) {
        ConfigurationSection slotsSection = file.getConfigurationSection("slots");
        if (slotsSection == null) {
            return;
        }

        for (String key : slotsSection.getKeys(false)) {
            IconMetadata iconMetadata = new IconMetadata(file, key, slotPattern);
            iconMetas.put(key, iconMetadata);
        }
    }

}
