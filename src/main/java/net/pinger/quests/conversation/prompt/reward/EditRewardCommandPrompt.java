package net.pinger.quests.conversation.prompt.reward;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.reward.QuestReward;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class EditRewardCommandPrompt extends ValidatingPrompt {
    private final PlayerQuestsPlugin plugin;
    private final MessageConfiguration configuration;
    private final QuestReward reward;

    public EditRewardCommandPrompt(PlayerQuestsPlugin plugin, QuestReward reward) {
        this.plugin = plugin;
        this.configuration = this.plugin.getConfiguration();
        this.reward = reward;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-reward-command");
    }

    @Override
    protected boolean isInputValid(ConversationContext conversationContext, String input) {
        return input.contains("{player}");
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String input) {
        this.reward.setCommand(input);

        // Open inventory
        return null;
    }

}
