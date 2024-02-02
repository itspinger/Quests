package net.pinger.quests.conversation.prompt;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
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
        final List<String> lines = Arrays.stream(input.split(";"))
            .map(line -> ChatColor.translateAlternateColorCodes('&', line))
            .collect(Collectors.toList());

        this.quest.setDescription(lines);
        if (this.quest.getId() == -1) {
            return this.cancelPrompt(player);
        }

        try {
            this.plugin.getStorage().saveQuest(this.quest).get();
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to save quest", e);
            this.configuration.send(player, "quest-save-fail", this.quest.getName());
        }

        this.configuration.send(player, "quest-save-success", this.quest.getName());
        return this.cancelPrompt(player);
    }

    private Prompt cancelPrompt(Player player) {
        this.plugin.getInventoryManager().getEditQuestProvider(this.quest).open(player);
        return null;
    }
}
