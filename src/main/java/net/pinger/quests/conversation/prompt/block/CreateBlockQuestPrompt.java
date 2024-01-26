package net.pinger.quests.conversation.prompt.block;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.data.BlockData;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class CreateBlockQuestPrompt extends StringPrompt {
    private final PlayerQuestsPlugin plugin;
    private final MessageConfiguration configuration;
    private final Quest quest;

    public CreateBlockQuestPrompt(PlayerQuestsPlugin plugin, Quest quest) {
        this.plugin = plugin;
        this.configuration = plugin.getConfiguration();
        this.quest = quest;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.configuration.of("quest-block-prompt");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {
        if (input == null || input.isEmpty()) {
            return this;
        }

        final Player player = (Player) conversationContext.getForWhom();

        // If it's any, then we set the material as null and end look for the goal
        final XMaterial material;
        if (input.equalsIgnoreCase("Any")) {
            material = null;
        } else {
            material = XMaterial.matchXMaterial(input).orElse(null);
            if (material == null) {
                this.configuration.send(player, "unknown-material");
                return this;
            }

            final Material mat = material.parseMaterial();
            if (mat == null) {
                this.configuration.send(player, "unknown-material");
                return this;
            }

            if (!mat.isSolid()) {
                this.configuration.send(player, "block-not-solid");
                return this;
            }
        }

        final BlockData data = new BlockData(material);
        this.quest.setQuestData(data);
        this.plugin.getInventoryManager().getEditQuestProvider(this.quest).open(player);

        return Prompt.END_OF_CONVERSATION;
    }
}
