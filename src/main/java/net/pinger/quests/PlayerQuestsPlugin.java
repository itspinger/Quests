package net.pinger.quests;

import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.listener.PlayerListener;
import net.pinger.quests.manager.PlayerQuestManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerQuestsPlugin extends JavaPlugin {
    private PlayerQuestManager playerQuestManager;
    private InventoryManager inventoryManager;
    private MessageConfiguration configuration;

    @Override
    public void onEnable() {
        this.configuration = new MessageConfiguration(this);
        this.inventoryManager = new InventoryManager(this);
        this.playerQuestManager = new PlayerQuestManager(this);

        final PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);
    }

    public MessageConfiguration getConfiguration() {
        return this.configuration;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public PlayerQuestManager getPlayerQuestManager() {
        return this.playerQuestManager;
    }
}
