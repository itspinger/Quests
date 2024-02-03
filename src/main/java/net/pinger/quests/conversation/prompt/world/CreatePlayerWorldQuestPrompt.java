package net.pinger.quests.conversation.prompt.world;

import java.util.logging.Level;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.data.BlockData;
import net.pinger.quests.quest.data.WorldData;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class CreatePlayerWorldQuestPrompt extends StringPrompt {
    private final PlayerQuestsPlugin plugin;
    private final MessageConfiguration configuration;
    private final Quest quest;

    public CreatePlayerWorldQuestPrompt(PlayerQuestsPlugin plugin, Quest quest) {
        this.plugin = plugin;
        this.configuration = plugin.getConfiguration();
        this.quest = quest;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-world-prompt");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {
        if (input == null || input.isEmpty()) {
            return this;
        }

        final Player player = (Player) conversationContext.getForWhom();
        final String world = input.equalsIgnoreCase("Any") ? null : input;

        final WorldData data = new WorldData(world);
        this.quest.setQuestData(data);
        if (!this.quest.isComplete()) {
            return this.cancelPrompt(player);
        }

        try {
            this.plugin.getStorage().saveQuest(this.quest).get();
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to save quest", e);
            this.configuration.send(player, "quest-save-fail", this.quest.getName());
        }

        // Add to the manager
        this.plugin.getQuestManager().storeQuest(this.quest);

        this.configuration.send(player, "quest-save-success", this.quest.getName());
        return this.cancelPrompt(player);
    }

    private Prompt cancelPrompt(Player player) {
        this.plugin.getInventoryManager().getEditQuestProvider(this.quest).open(player);
        return null;
    }
}
