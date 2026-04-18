package me.zmaster.zgui.sign;

import me.zmaster.zgui.ZGui;
import me.zmaster.zgui.sign.meta.SignMenuMeta;
import me.zmaster.zgui.sign.util.SignPacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class SignMenu {

    public static void openSignInputMenu(SignMenuMeta meta, Player player, Consumer<List<String>> consumer) {
        Location loc = player.getLocation();
        Vector direction = loc.getDirection();
        direction.setY(0);
        direction.normalize();

        SignMenu menu = new SignMenu(meta, loc.clone().add(direction.multiply(-5)), true) {
            @Override
            protected void onClose(Player player, List<String> lines) {
                consumer.accept(lines);
            }
        };

        menu.open(player);
    }

    private final SignMenuMeta meta;
    private final Location signLocation;
    private final boolean fakeSign;

    public SignMenu(@NotNull SignMenuMeta meta, @NotNull Location signLocation, boolean sendFakeSign) {
        this.meta = Objects.requireNonNull(meta, "meta must not be null");
        this.signLocation = Objects.requireNonNull(signLocation, "meta must not be null");
        this.fakeSign = sendFakeSign;
    }

    public void open(Player player) {
        player.closeInventory();

        ZGui.get().getSignMenuManager().registerMenu(player.getUniqueId(), this);
        if (fakeSign) SignPacketUtil.openSignEditor(player, signLocation, meta.getLines(), meta.getSignType());
        SignPacketUtil.sendSignEditor(player, signLocation);
    }

    public @NotNull SignMenuMeta getMeta() {
        return meta;
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public boolean isFakeSign() {
        return fakeSign;
    }

    protected void onClose(Player player, List<String> lines) {

    }


}
