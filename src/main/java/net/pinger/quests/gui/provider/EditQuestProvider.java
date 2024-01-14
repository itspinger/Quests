package net.pinger.quests.gui.provider;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.provider.GuiProvider;
import java.util.ArrayList;
import java.util.List;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.conversation.ConversationManager;
import net.pinger.quests.conversation.prompt.ChangeQuestIconPrompt;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.item.ItemBuilder;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.Quest;
import org.bukkit.entity.Player;

public class EditQuestProvider implements GuiProvider {
    private final PlayerQuestsPlugin playerQuestsPlugin;
    private final ConversationManager conversationManager;
    private final Quest quest;

    public EditQuestProvider(PlayerQuestsPlugin playerQuestsPlugin, Quest quest) {
        this.playerQuestsPlugin = playerQuestsPlugin;
        this.conversationManager = playerQuestsPlugin.getConversationManager();
        this.quest = quest;
    }

    @Override
    public void initialize(Player player, GuiContents contents) {
        if (this.quest.isComplete()) {
            // Add save option to save to MySQL if the id is -1
            // This means that now other players will be able to see it as well
            contents.setItem(5, 1, GuiItem.of(
                new ItemBuilder(XMaterial.TRIPWIRE_HOOK)
                    .name("&b&lSave Quest")
                    .lore("&7Click here to save the quest to the database")
                    .build()
            ));
        }

        contents.setItem(1, 4, GuiItem.of(
            new ItemBuilder(this.quest.getMaterial())
                .name("&b&lChange Icon")
                .lore("&7Click to change icon of the quest")
                .build(),
            e -> {
                this.conversationManager.createConversation(player, new ChangeQuestIconPrompt(this.playerQuestsPlugin, this.quest));
            }
        ));

        contents.setItem(2, 3, GuiItem.of(
            this.getQuestType().build(),
            e -> {
                this.playerQuestsPlugin.getInventoryManager().getQuestTypeProvider(this.quest).open(player);
            }
        ));

        InventoryManager.addReturnButton(5, 4, contents);
    }

    private ItemBuilder getQuestType() {
        final ItemBuilder builder = new ItemBuilder(XMaterial.DIAMOND_SWORD);
        builder.name("&b&lQuest Type");

        if (this.quest.getQuestData() == null) {
            return builder.lore("&7Click here to setup the quest type");
        }

        final List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("&b❙ Quest Type");
        lore.add(this.quest.getQuestType().getName());
        lore.add("");
        lore.add("&b❙ Quest Data");
        lore.addAll(this.quest.getQuestData().getDescription());
        lore.add("&fGoal: &b" + this.quest.getGoal());

        return builder.lore(lore);
    }
}
