package net.pinger.quests.conversation.prompt;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.quest.Quest;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class SetQuestDescriptionPrompt extends StringPrompt {
    private final PlayerQuestsPlugin plugin;
    private final MessageConfiguration configuration;
    private final Quest quest;

    public SetQuestDescriptionPrompt(PlayerQuestsPlugin plugin, Quest quest) {
        this.plugin = plugin;
        this.configuration = this.plugin.getConfiguration();
        this.quest = quest;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-description-set");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {
        if (input == null || input.isEmpty()) {
            return this;
        }

        final Player player = (Player) conversationContext.getForWhom();
        final String[] description = input.split(";");
        final List<String> lines = Arrays.stream(description)
            .map(line -> ChatColor.translateAlternateColorCodes('&', line))
            .collect(Collectors.toList());

        this.quest.setDescription(lines);
        this.plugin.getInventoryManager().getEditQuestProvider(this.quest).open(player);

        return Prompt.END_OF_CONVERSATION;
    }
}
