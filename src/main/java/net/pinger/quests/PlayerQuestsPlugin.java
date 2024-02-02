package net.pinger.quests;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.tchristofferson.configupdater.ConfigUpdater;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import net.pinger.quests.command.QuestAdminCommand;
import net.pinger.quests.command.QuestCommand;
import net.pinger.quests.conversation.ConversationManager;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.listener.PlayerListener;
import net.pinger.quests.manager.PlayerQuestManager;
import net.pinger.quests.manager.QuestManager;
import net.pinger.quests.storage.Storage;
import net.pinger.quests.storage.credentials.StorageCredentials;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerQuestsPlugin extends JavaPlugin {
    private QuestManager questManager;
    private PlayerQuestManager playerQuestManager;
    private InventoryManager inventoryManager;
    private MessageConfiguration configuration;
    private ConversationManager conversationManager;
    private Storage storage;

    @Override
    public void onEnable() {
        this.addDefaultConfig();
        this.configuration = new MessageConfiguration(this);

        if (!this.loadStorage()) {
            return;
        }

        this.questManager = new QuestManager(this);
        this.inventoryManager = new InventoryManager(this);
        this.playerQuestManager = new PlayerQuestManager(this);
        this.conversationManager = new ConversationManager(this);

        final CommandService service = Drink.get(this);
        service.register(new QuestAdminCommand(this), "questadmin", "questsadmin");
        service.register(new QuestCommand(this), "quest", "quests");
        service.registerCommands();

        final PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        if (this.storage == null) {
            return;
        }

        this.storage.shutdown();
    }

    public void addDefaultConfig() {
        this.saveDefaultConfig();

        final File config = new File(this.getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(this, "config.yml", config, new ArrayList<>());
        } catch (Exception e) {
            this.getLogger().log(Level.SEVERE, "Failed to update the config: " + e.getMessage());
        }

        this.getLogger().info("Successfully loaded the config.yml");
        this.reloadConfig();
    }

    private boolean loadStorage() {
        if (!this.getConfig().getBoolean("database.enabled")) {
            this.getLogger().info("DISABLING THIS PLUGIN AS IT REQUIRES MYSQL TO WORK!");
            this.getLogger().info("DISABLING THIS PLUGIN AS IT REQUIRES MYSQL TO WORK!");
            this.getLogger().info("DISABLING THIS PLUGIN AS IT REQUIRES MYSQL TO WORK!");
            this.getPluginLoader().disablePlugin(this);
            return false;
        }

        final ConfigurationSection section = this.getConfig().getConfigurationSection("database");
        if (section == null) {
            this.getLogger().log(Level.SEVERE, "Failed to create a database");
            this.getPluginLoader().disablePlugin(this);
            return false;
        }

        // Create the database credentials
        final StorageCredentials credentials = new StorageCredentials(
                section.getString("host"),
                section.getString("username"),
                section.getString("password")
        );

        // Create the database
        try {
            this.storage = new Storage(this, credentials);
        } catch (Exception e) {
            this.getLogger().log(Level.SEVERE, "Failed to create a database", e);
            this.getPluginLoader().disablePlugin(this);
            return false;
        }

        return true;
    }

    public MessageConfiguration getConfiguration() {
        return this.configuration;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public ConversationManager getConversationManager() {
        return this.conversationManager;
    }

    public PlayerQuestManager getPlayerQuestManager() {
        return this.playerQuestManager;
    }

    public QuestManager getQuestManager() {
        return this.questManager;
    }

    public Storage getStorage() {
        return this.storage;
    }
}
