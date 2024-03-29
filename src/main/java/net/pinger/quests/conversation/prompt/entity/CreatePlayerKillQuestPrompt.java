package net.pinger.quests.conversation.prompt.entity;

import java.util.logging.Level;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.data.PlayerData;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

public class CreatePlayerKillQuestPrompt extends ValidatingPrompt {
    private final PlayerQuestsPlugin plugin;
    private final MessageConfiguration configuration;
    private final Quest quest;

    public CreatePlayerKillQuestPrompt(PlayerQuestsPlugin plugin, Quest quest) {
        this.plugin = plugin;
        this.configuration = plugin.getConfiguration();
        this.quest = quest;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-player-prompt");
    }

    @Override
    protected boolean isInputValid(ConversationContext conversationContext, String input) {
        return input.length() <= 16;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String input) {
        String playerName = input;
        if (input.equalsIgnoreCase("any")) {
            playerName = null;
        }

        final Player player = (Player) conversationContext.getForWhom();
        final PlayerData data = new PlayerData(playerName);
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
