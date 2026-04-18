package me.zmaster.zgui.sign;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import me.zmaster.zgui.sign.util.ChatComponentUtil;
import me.zmaster.zgui.sign.util.SignPacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SignPacketListener implements PacketListener {

    private final Plugin plugin = JavaPlugin.getProvidingPlugin(SignPacketListener.class);
    private final SignMenuManager signMenuManager;

    public SignPacketListener(SignMenuManager signMenuManager) {
        this.signMenuManager = signMenuManager;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.UPDATE_SIGN) {
            User user = event.getUser();

            SignMenu signMenu = signMenuManager.getOpenMenu(user.getUUID());
            if (signMenu == null) return;

            WrapperPlayClientUpdateSign packet = new WrapperPlayClientUpdateSign(event);
            List<String> lines = Arrays.stream(packet.getTextLines())
                    .map(ChatComponentUtil::toPlainText)
                    .collect(Collectors.toList());

            Bukkit.getScheduler().runTask(plugin, () -> {
                Player player = Bukkit.getPlayer(user.getUUID());
                if (signMenu.isFakeSign()) SignPacketUtil.sendClearSign(player, signMenu.getSignLocation());

                signMenu.onClose(player, lines);
            });

            signMenuManager.unregisterMenu(user.getUUID());
        }
    }
}
