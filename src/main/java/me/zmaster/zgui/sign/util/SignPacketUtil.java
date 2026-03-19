package me.zmaster.zgui.sign.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenSignEditor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class SignPacketUtil {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(SignPacketUtil.class);

    public static void openSignEditor(Player player, Location location, List<String> lines, Material signType) {
        player.sendBlockChange(location, signType, (byte) 0);
        player.sendSignChange(location, lines.toArray(new String[4]));
    }

    public static void sendSignEditor(Player player, Location location) {
        Vector3i pos = new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        WrapperPlayServerOpenSignEditor packet = new WrapperPlayServerOpenSignEditor(pos, false);
        PacketEvents.getAPI().getPlayerManager().getUser(player).sendPacket(packet);
    }

    public static void sendClearSign(Player player, Location location) {
        Block block = location.getBlock();
        if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_13)) {
            player.sendBlockChange(location, block.getType(), block.getData());
        } else {
            player.sendBlockChange(location, block.getBlockData());
        }
    }

}
