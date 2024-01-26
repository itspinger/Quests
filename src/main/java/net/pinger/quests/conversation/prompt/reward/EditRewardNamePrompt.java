package net.pinger.quests.conversation.prompt.reward;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.reward.QuestReward;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class EditRewardNamePrompt extends StringPrompt {
    private final PlayerQuestsPlugin plugin;
    private final MessageConfiguration configuration;
    private final QuestReward reward;

    public EditRewardNamePrompt(PlayerQuestsPlugin plugin, QuestReward reward) {
        this.plugin = plugin;
        this.configuration = this.plugin.getConfiguration();
        this.reward = reward;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-reward-name");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {
        if (input == null || input.isEmpty()) {
            return this;
        }

        this.reward.setDisplayName(input);

        // Open inventory

        return Prompt.END_OF_CONVERSATION;
    }
}
