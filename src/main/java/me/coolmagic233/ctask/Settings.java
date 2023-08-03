package me.coolmagic233.ctask;

import cn.nukkit.utils.Config;

public class Settings {
    public static String prefix;
    public static boolean enable_prefix;
    private Config config;

    public Settings(Config config) {
        this.config = config;
        prefix = config.getString("prefix.prefix" );
        enable_prefix = config.getBoolean("prefix.enable");

    }
}
