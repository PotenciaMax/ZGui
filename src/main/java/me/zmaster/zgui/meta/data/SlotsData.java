package me.zmaster.zgui.meta.data;

import me.zmaster.zgui.meta.SlotPattern;
import me.zmaster.zgui.meta.path.KeyPath;

import java.util.Collections;
import java.util.List;

public class SlotsData {

    private final List<Integer> slots;

    public SlotsData(KeyPath path, SlotPattern pattern) {
        this.slots = Collections.unmodifiableList(pattern.getSlotsByChar(path.asString()));
    }

    public List<Integer> getSlots() {
        return slots;
    }
}
