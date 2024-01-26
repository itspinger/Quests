package net.pinger.quests.conversation.prompt;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.Quest;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChangeQuestIconPrompt extends StringPrompt {
    private final PlayerQuestsPlugin playerQuestsPlugin;
    private final MessageConfiguration configuration;
    private final Quest quest;

    public ChangeQuestIconPrompt(PlayerQuestsPlugin playerQuestsPlugin, Quest quest) {
        this.playerQuestsPlugin = playerQuestsPlugin;
        this.configuration = playerQuestsPlugin.getConfiguration();
        this.quest = quest;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-change-icon");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {
        if (input == null || input.isEmpty()) {
            return this;
        }

        if (!this.isConfirmWord(input)) {
            return this;
        }

        final Player player = (Player) conversationContext.getForWhom();
        final ItemStack item = player.getItemInHand();
        if (item.getType() == Material.AIR) {
            this.configuration.send(player, "quest-icon-invalid");
            return this;
        }

        final XMaterial material = XMaterial.matchXMaterial(item);
        this.quest.setMaterial(material);
        this.playerQuestsPlugin.getInventoryManager().getEditQuestProvider(this.quest).open(player);

        return Prompt.END_OF_CONVERSATION;
    }

    private boolean isConfirmWord(String word) {
        return word.equalsIgnoreCase("Done") || word.equalsIgnoreCase("Confirm");
    }
}
