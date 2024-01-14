package net.pinger.quests.conversation.prompt;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestType;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class SetQuestGoalPrompt extends NumericPrompt {
    private final PlayerQuestsPlugin playerQuestsPlugin;
    private final MessageConfiguration configuration;
    private final Quest quest;

    public SetQuestGoalPrompt(PlayerQuestsPlugin playerQuestsPlugin, Quest quest) {
        this.playerQuestsPlugin = playerQuestsPlugin;
        this.configuration = playerQuestsPlugin.getConfiguration();
        this.quest = quest;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-progress-goal");
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
        final int goal = number.intValue();
        if (goal <= 0) {
            return this;
        }

        final QuestType type = this.quest.getQuestType();
        this.quest.setGoal(goal);
        return type.createPrompt(this.playerQuestsPlugin, this.quest);
    }
}
