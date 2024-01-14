package net.pinger.quests;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.jonahseguin.drink.command.DrinkCommandService;
import com.jonahseguin.drink.parametric.DrinkProvider;
import io.pnger.gui.manager.GuiManager;
import net.pinger.quests.command.QuestAdminCommand;
import net.pinger.quests.conversation.ConversationManager;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.listener.PlayerListener;
import net.pinger.quests.manager.PlayerQuestManager;
import net.pinger.quests.manager.QuestManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerQuestsPlugin extends JavaPlugin {
    private QuestManager questManager;
    private PlayerQuestManager playerQuestManager;
    private InventoryManager inventoryManager;
    private MessageConfiguration configuration;
    private ConversationManager conversationManager;

    @Override
    public void onEnable() {
        this.questManager = new QuestManager();
        this.configuration = new MessageConfiguration(this);
        this.inventoryManager = new InventoryManager(this);
        this.playerQuestManager = new PlayerQuestManager(this);
        this.conversationManager = new ConversationManager(this);

        final CommandService service = Drink.get(this);
        service.register(new QuestAdminCommand(this), "questadmin");
        service.registerCommands();

        final PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);
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
}
