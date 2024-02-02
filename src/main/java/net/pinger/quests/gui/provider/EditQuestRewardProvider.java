package net.pinger.quests.gui.provider;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.provider.GuiProvider;
import java.util.Optional;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.conversation.ConversationManager;
import net.pinger.quests.conversation.prompt.reward.EditRewardCommandPrompt;
import net.pinger.quests.conversation.prompt.reward.EditRewardNamePrompt;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.item.ItemBuilder;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.reward.QuestReward;
import org.bukkit.entity.Player;

public class EditQuestRewardProvider implements GuiProvider {
    private final PlayerQuestsPlugin plugin;
    private final ConversationManager conversationManager;
    private final Quest quest;
    private final QuestReward reward;

    public EditQuestRewardProvider(PlayerQuestsPlugin plugin, Quest quest, QuestReward reward) {
        this.plugin = plugin;
        this.conversationManager = plugin.getConversationManager();
        this.quest = quest;
        this.reward = reward;
    }

    @Override
    public void initialize(Player player, GuiContents contents) {
        contents.setItem(1, 3, GuiItem.of(
            new ItemBuilder(XMaterial.PAPER)
                .name("&b&lSet Display Name")
                .lore(
                    "&7Click here to change the display name of this reward",
                    "",
                    "&b❙ Display Name",
                    "&f" + Optional.ofNullable(this.reward.getDisplayName()).orElse("Not set")
                )
                .build(),
            e -> {
                this.conversationManager.createConversation(player, new EditRewardNamePrompt(this.plugin, this.quest, this.reward));
            }
        ));

        contents.setItem(1, 5, GuiItem.of(
            new ItemBuilder(XMaterial.COMMAND_BLOCK)
                .name("&b&lSet Command")
                .lore(
                    "&7Click here to change the command that will be executed",
                    "&7when the player completes the quest",
                    "",
                    "&b❙ Command",
                    "&f" + Optional.ofNullable(this.reward.getCommand()).orElse("Not set")
                )
                .build(),
            e -> {
                this.conversationManager.createConversation(player, new EditRewardCommandPrompt(this.plugin, this.quest, this.reward));
            }
        ));

        InventoryManager.addReturnButton(3, 4, contents);
    }


}
