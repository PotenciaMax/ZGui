package me.zmaster.zgui.sign.meta;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignMenuMeta {

    private final Material signType;
    private final List<String> lines = new ArrayList<>();

    public SignMenuMeta(ConfigurationSection config) {
        this.signType = XMaterial.valueOf(config.getString("sign_type")).get();
        for (String line : config.getStringList("lines")) {
            lines.add(ChatColor.translateAlternateColorCodes('&', line));
        }
    }

    public Material getSignType() {
        return signType;
    }

    public List<String> getLines() {
        return lines;
    }
}
