package net.pinger.quests.file.configuration;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfiguration extends ExternalConfigurationAdapter {

    public MessageConfiguration(JavaPlugin plugin) {
        super(plugin, "messages.yml");
    }

    public boolean has(String key) {
        return this.configuration.get(key) != null;
    }

    public String of(String key, boolean translate) {
        String value = this.configuration.getString(key);

        // Update value
        value = value == null ? "" : value;

        if (!translate) {
            return ChatColor.stripColor(value);
        }

        return ChatColor.translateAlternateColorCodes('&', value);
    }

    public String of(String key) {
        return this.of(key, true);
    }

    private String ofFormatted(String key, boolean translate, Object... objects) {
        return String.format(this.of(key, translate), objects);
    }

    public String ofFormatted(String key, Object... objects) {
        return this.ofFormatted(key, true, objects);
    }

}
