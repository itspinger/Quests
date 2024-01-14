package net.pinger.quests.conversation.prompt;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.manager.QuestManager;
import net.pinger.quests.quest.Quest;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class CreateQuestPrompt extends StringPrompt {
    private final PlayerQuestsPlugin playerQuestsPlugin;
    private final QuestManager questManager;
    private final MessageConfiguration configuration;

    public CreateQuestPrompt(PlayerQuestsPlugin playerQuestsPlugin) {
        this.playerQuestsPlugin = playerQuestsPlugin;
        this.questManager = playerQuestsPlugin.getQuestManager();
        this.configuration = playerQuestsPlugin.getConfiguration();
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-create");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {
        if (input == null || input.isEmpty()) {
            return this;
        }

        final Player player = (Player) conversationContext.getForWhom();
        final Quest quest = this.questManager.getQuest(input);
        if (quest != null) {
            this.configuration.send(player, "quest-exists");
            return this;
        }

        final Quest newQuest = new Quest(input);
        this.questManager.addQuest(newQuest);
        this.playerQuestsPlugin.getInventoryManager().getQuestsProvider().open(player);

        return Prompt.END_OF_CONVERSATION;
    }
}
