package net.pinger.quests.file.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ExternalConfigurationAdapter {
    private final File file;

    protected YamlConfiguration configuration;

    public ExternalConfigurationAdapter(JavaPlugin plugin, String name)  {
        this(plugin, name, false);
    }

    public ExternalConfigurationAdapter(JavaPlugin plugin, String name, boolean load) {
        this.file = new File(plugin.getDataFolder(), name);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
        if (!load && this.file.length() > 0) {
            return;
        }

        final InputStream inputStream = plugin.getResource(name);
        if (inputStream == null) {
            throw new IllegalArgumentException("File resource cannot be found (" + name + ")");
        }

        try (final Reader reader = new InputStreamReader(inputStream)) {
            final YamlConfiguration keyed = YamlConfiguration.loadConfiguration(reader);

            this.configuration.options().copyDefaults(true);
            for (final Map.Entry<String, Object> entries : keyed.getValues(true).entrySet()) {
                this.configuration.addDefault(entries.getKey(), entries.getValue());
            }

            this.configuration.save(this.file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.INFO, "Failed to create a file with name " + name, e);
        }
    }

    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

}
