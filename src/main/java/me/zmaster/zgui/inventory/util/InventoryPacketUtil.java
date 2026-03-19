package me.zmaster.zgui.inventory.util;

import com.cryptomorin.xseries.reflection.XReflection;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.lang.invoke.MethodHandle;

public final class InventoryPacketUtil {

    private static final MethodHandle GET_HANDLE;

    static {
        try {
            GET_HANDLE = XReflection.of(Player.class).method("getHandle").reflect();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendInventoryName(Player player, Inventory inventory, String name) {

    }

}
