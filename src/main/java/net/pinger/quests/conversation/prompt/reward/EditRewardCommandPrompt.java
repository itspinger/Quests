package net.pinger.quests.conversation.prompt.reward;

import java.util.logging.Level;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.reward.QuestReward;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

public class EditRewardCommandPrompt extends ValidatingPrompt {
    private final PlayerQuestsPlugin plugin;
    private final MessageConfiguration configuration;
    private final Quest quest;
    private final QuestReward reward;

    public EditRewardCommandPrompt(PlayerQuestsPlugin plugin, Quest quest, QuestReward reward) {
        this.plugin = plugin;
        this.quest = quest;
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
        final Player player = (Player) conversationContext.getForWhom();

        this.reward.setCommand(input);
        if (this.quest.getId() == -1 || !this.reward.isValid()) {
            return this.cancelPrompt(player);
        }

        try {
            this.plugin.getStorage().saveReward(this.quest, this.reward).get();
        } catch (Exception e) {
            this.configuration.send(player, "quest-reward-save-fail");
            this.plugin.getLogger().log(Level.SEVERE, "Failed to save reward ", e);
            return this.cancelPrompt(player);
        }

        this.configuration.send(player, "quest-reward-save-success");
        return this.cancelPrompt(player);
    }

    private Prompt cancelPrompt(Player player) {
        this.plugin.getInventoryManager().getEditRewardProvider(this.quest, this.reward).open(player);
        return null;
    }

}
