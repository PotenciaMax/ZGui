package me.zmaster.zgui.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class SlotPattern {

    //private final InventoryType inventoryType;
    private final char[] slotMatrix;

    public Inventory createInventory(@NotNull String inventoryName) {
        return Bukkit.createInventory(null, slotMatrix.length, inventoryName);
    }

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

    public SlotPattern(@NotNull List<String> rows) {
        assert ! rows.isEmpty();

        StringBuilder builder = new StringBuilder();
        for (String row : rows) {
            builder.append(row.replace(" ", ""));
        }

        String slots = builder.toString();
        this.slotMatrix = slots.toCharArray();
    }
}
