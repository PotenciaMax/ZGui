package me.zmaster.zgui.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a slot layout pattern for an inventory.
 * Converts a visual pattern (rows of characters) into an internal matrix
 * that maps characters to slot indexes for easy access.
 */
public class SlotPattern {

    private final char[] slotMatrix;
    private final InventoryType inventoryType;

    /**
     * Creates a slot pattern from a list of row strings.
     * Spaces are ignored, and all rows are concatenated into a single slot matrix.
     * The inventory type is automatically detected based on the pattern size.
     *
     * @param rows the rows representing the slot layout
     * @throws IllegalArgumentException if the slot count is not valid for a chest
     */
    public SlotPattern(@NotNull List<String> rows) {
        assert !rows.isEmpty();

        StringBuilder builder = new StringBuilder();
        for (String row : rows) {
            builder.append(row.replace(" ", ""));
        }

        String slots = builder.toString();
        this.slotMatrix = slots.toCharArray();

        if (slotMatrix.length == 5) {
            this.inventoryType = InventoryType.HOPPER;
        } else if (slotMatrix.length == 9 && rows.size() > 1) {
            this.inventoryType = InventoryType.CRAFTING;
        } else {
            if (slotMatrix.length % 9 != 0) {
                throw new IllegalArgumentException("Slots amount is not a multiple of 9");
            }
            this.inventoryType = InventoryType.CHEST;
        }
    }

    /**
     * Creates a Bukkit inventory instance based on the detected inventory type
     * and the slot pattern size.
     *
     * @param inventoryName the name displayed for the inventory
     * @return the created inventory
     */
    public Inventory createInventory(@NotNull String inventoryName) {
        if (inventoryType == InventoryType.CHEST) {
            return Bukkit.createInventory(null, slotMatrix.length, inventoryName);
        }
        return Bukkit.createInventory(null, inventoryType, inventoryName);
    }

    /**
     * Retrieves all slot indexes associated with one or more characters in the pattern.
     * For example, if the pattern contains 'A' at certain positions, calling with "A"
     * will return those slot indexes.
     *
     * @param chars the characters to search for
     * @return a list of slot indexes matching the characters
     */
    public List<Integer> getSlotsByChar(String chars) {
        LinkedList<Integer> slots = new LinkedList<>();

        if (chars == null || chars.isEmpty()) {
            return slots;
        }

        for (char caractere : chars.toCharArray()) {
            for (int slot = 0; slot < slotMatrix.length; slot++) {
                char charAtSlot = slotMatrix[slot];
                if (charAtSlot == caractere) {
                    slots.add(slot);
                }
            }
        }

        return slots;
    }
}
