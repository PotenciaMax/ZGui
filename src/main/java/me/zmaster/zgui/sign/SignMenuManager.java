package me.zmaster.zgui.sign;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SignMenuManager {

    private final Map<UUID, SignMenu> signMenus = new ConcurrentHashMap<>();

    public SignMenuManager() {
        PacketEvents.getAPI().getEventManager().registerListener(new SignPacketListener(this), PacketListenerPriority.LOW);
    }

    public Collection<SignMenu> getRegisteredMenus() {
        return signMenus.values();
    }

    public void registerMenu(UUID id, SignMenu menu) {
        signMenus.put(id, menu);
    }

    public void unregisterMenu(UUID id) {
        signMenus.remove(id);
    }

    public @Nullable SignMenu getOpenMenu(@NotNull UUID player) {
        Objects.requireNonNull(player, "player must not be null");
        return signMenus.get(player);
    }

}
