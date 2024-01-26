package net.pinger.quests.gui.provider;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.pagination.GuiPagination;
import io.pnger.gui.provider.GuiProvider;
import io.pnger.gui.slot.GuiIteratorType;
import io.pnger.gui.slot.GuiSlotIterator;
import java.util.Arrays;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.conversation.ConversationManager;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.item.ItemBuilder;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestType;
import org.bukkit.entity.Player;

public class QuestTypeProvider implements GuiProvider {
    private final PlayerQuestsPlugin playerQuestsPlugin;
    private final ConversationManager conversationManager;
    private final Quest quest;

    public QuestTypeProvider(PlayerQuestsPlugin playerQuestsPlugin, Quest quest) {
        this.playerQuestsPlugin = playerQuestsPlugin;
        this.conversationManager = playerQuestsPlugin.getConversationManager();
        this.quest = quest;
    }

    @Override
    public void initialize(Player player, GuiContents contents) {
        final GuiPagination pagination = contents.getPagination();

        final QuestType[] questTypes = QuestType.values();
        final GuiItem[] items = Arrays.stream(questTypes)
            .map(type -> GuiItem.of(this.getFromQuestType(type).build(), e -> {
                this.quest.setQuestType(type);
                this.conversationManager.createConversation(player, type.createPrompt(this.playerQuestsPlugin, this.quest));
            }))
            .toArray(GuiItem[]::new);

        final GuiSlotIterator it = contents.newIterator(GuiIteratorType.HORIZONTAL, 1, 1);
        it.blacklistColumn(0);
        it.blacklistColumn(8);
        it.blacklistRow(0);

        pagination.setItems(21, items);
        pagination.addToIterator(it);
    }

    @Override
    public void update(Player player, GuiContents contents) {
        InventoryManager.addReturnButton(4, 4, contents);
        InventoryManager.addPageButtons(4, contents);
    }

    private ItemBuilder getFromQuestType(QuestType type) {
        final ItemBuilder builder = new ItemBuilder(type.getMaterial());
        builder.name("&b&l" + type.getName());
        builder.lore("&7Click to set the quest type of this quest to this");
        return builder;
    }
}
