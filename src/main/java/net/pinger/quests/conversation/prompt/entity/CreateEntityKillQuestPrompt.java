package net.pinger.quests.conversation.prompt.entity;

import java.util.logging.Level;
import javax.annotation.Nonnull;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.data.EntityData;
import net.pinger.quests.quest.data.PlayerData;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CreateEntityKillQuestPrompt extends ValidatingPrompt {
    private final PlayerQuestsPlugin plugin;
    private final MessageConfiguration configuration;
    private final Quest quest;

    public CreateEntityKillQuestPrompt(PlayerQuestsPlugin plugin, Quest quest) {
        this.plugin = plugin;
        this.configuration = plugin.getConfiguration();
        this.quest = quest;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-entity-prompt");
    }

    @Override
    protected boolean isInputValid(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase("any")) {
            return true;
        }

        final String format = this.format(s);
        try {
            EntityType.valueOf(format);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String input) {
        final EntityType type = input.equalsIgnoreCase("any") ? null : EntityType.valueOf(this.format(input));
        final Player player = (Player) conversationContext.getForWhom();

        final EntityData data = new EntityData(type);
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

    private String format(@Nonnull String name) {
        final int len = name.length();
        final char[] chs = new char[len];
        int count = 0;
        boolean appendUnderline = false;

        for (int i = 0; i < len; i++) {
            final char ch = name.charAt(i);
            if (!appendUnderline && count != 0 && (ch == '-' || ch == ' ' || ch == '_') && chs[count] != '_')
                appendUnderline = true;
            else {
                // Old materials have numbers in them.
                if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {
                    if (appendUnderline) {
                        chs[count++] = '_';
                        appendUnderline = false;
                    }

                    chs[count++] = (char) (ch & 0x5f);
                }
            }
        }

        return new String(chs, 0, count);
    }

    private Prompt cancelPrompt(Player player) {
        this.plugin.getInventoryManager().getEditQuestProvider(this.quest).open(player);
        return null;
    }

}
