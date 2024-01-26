package net.pinger.quests.gui.provider;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.pagination.GuiPagination;
import io.pnger.gui.provider.GuiProvider;
import io.pnger.gui.slot.GuiIteratorType;
import java.util.ArrayList;
import java.util.List;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.conversation.ConversationManager;
import net.pinger.quests.conversation.prompt.CreateQuestPrompt;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.item.ItemBuilder;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.manager.QuestManager;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestType;
import net.pinger.quests.quest.data.QuestData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuestsProvider implements GuiProvider {
    private final PlayerQuestsPlugin playerQuestsPlugin;
    private final QuestManager questManager;
    private final ConversationManager conversationManager;

    public QuestsProvider(PlayerQuestsPlugin playerQuestsPlugin) {
        this.playerQuestsPlugin = playerQuestsPlugin;
        this.questManager = playerQuestsPlugin.getQuestManager();
        this.conversationManager = playerQuestsPlugin.getConversationManager();
    }

    @Override
    public void update(Player player, GuiContents contents) {
        final GuiPagination pagination = contents.getPagination();
        final List<Quest> quests = new ArrayList<>(this.questManager.getQuests());
        final GuiItem[] items = new GuiItem[quests.size()];

        int i = 0;
        for (final Quest quest : quests) {
            // Get the item from the group
            items[i++] = GuiItem.of(this.getItemStack(quest), e -> {
                this.playerQuestsPlugin.getInventoryManager().getEditQuestProvider(quest).open(player);
            });
        }

        pagination.setItems(27, items);
        pagination.addToIterator(contents.newIterator(GuiIteratorType.HORIZONTAL, 0, 0));

        contents.setItem(5, 1, GuiItem.of(
            new ItemBuilder(XMaterial.COMPASS)
                .name("&b&lCreate new Quest")
                .lore("&7Click to create a new quest")
                .build(),
            e -> {
                this.conversationManager.createConversation(player, new CreateQuestPrompt(this.playerQuestsPlugin));
            }
        ));

        InventoryManager.addReturnButton(5, 4, contents);
        InventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getItemStack(Quest quest) {
        final ItemBuilder builder = new ItemBuilder(quest.getMaterial());
        builder.name("&b&l" + quest.getName());

        final List<String> desc = new ArrayList<>();
        if (!quest.isComplete()) {
            desc.add("");
            desc.add("&c&lNOT COMPLETE");
        }

        desc.add("");
        desc.add("&b❙ Description");

        if (quest.getDescription().isEmpty()) {
            desc.add("&fNot set");
        } else {
            desc.addAll(quest.getDescription());
        }

        desc.add("");

        final QuestType type = quest.getQuestType();
        desc.add("&b❙ Type");
        desc.add(type == null ? "&fNot set" : "&f" + type.getName());
        desc.add("");

        final QuestData data = quest.getQuestData();
        desc.add("&b❙ Quest Data");
        if (data != null) {
            desc.addAll(data.getDescription());
        }

        desc.add("&fGoal: &b" + (quest.getGoal() == 0 ? "Not set" : String.valueOf(quest.getGoal())));

        builder.lore(desc);
        return builder.build();
    }
}
