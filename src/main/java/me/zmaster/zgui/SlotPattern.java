package me.zmaster.zgui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.TreeSet;

public class SlotPattern {

    private final char[] slotMatrix;

    public Inventory createInventory(String inventoryName) {
        return Bukkit.createInventory(null, slotMatrix.length, inventoryName);
    }

    public TreeSet<Integer> getSlotsByChar(char... caracteres) {
        TreeSet<Integer> slots = new TreeSet<>();
        for (int slot = 0; slot < slotMatrix.length; slot++) {
            char charAtSlot = slotMatrix[slot];

            for (char caractere : caracteres) {
                if (charAtSlot == caractere) {
                    slots.add(slot);
                    break;
                }
            }
        }
        return slots;
    }

    public SlotPattern(List<String> rows) {
        assert ! rows.isEmpty();

        StringBuilder builder = new StringBuilder();
        for (String row : rows) {
            builder.append(row.replace(" ", ""));
        }

        String slots = builder.toString();
        this.slotMatrix = slots.toCharArray();
    }

}
