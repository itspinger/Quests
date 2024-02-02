package net.pinger.quests.gui.provider;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.pagination.GuiPagination;
import io.pnger.gui.provider.GuiProvider;
import io.pnger.gui.slot.GuiIteratorType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.file.configuration.MessageConfiguration;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.item.ItemBuilder;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.reward.QuestReward;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuestRewardsProvider implements GuiProvider {
    private final PlayerQuestsPlugin plugin;
    private final MessageConfiguration configuration;
    private final Quest quest;

    public QuestRewardsProvider(PlayerQuestsPlugin plugin, Quest quest) {
        this.plugin = plugin;
        this.configuration = plugin.getConfiguration();
        this.quest = quest;
    }

    @Override
    public void update(Player player, GuiContents contents) {
        final GuiPagination pagination = contents.getPagination();
        final List<QuestReward> quests = this.quest.getRewards();
        final GuiItem[] items = new GuiItem[quests.size()];

        int i = 0;
        for (final QuestReward reward : quests) {
            // Get the item from the group
            items[i++] = GuiItem.of(this.getRewardItem(reward), e -> {
                if (e.isLeftClick()) {
                    this.plugin.getInventoryManager().getEditRewardProvider(this.quest, reward).open(player);
                    return;
                }

                if (!e.isRightClick() || !e.isShiftClick()) {
                    return;
                }

                try {
                    this.plugin.getStorage().deleteReward(reward).get();
                } catch (Exception ex) {
                    this.plugin.getLogger().log(Level.SEVERE, "Failed to delete reward", ex);
                    this.configuration.send(player, "quest-reward-delete-fail");
                    return;
                }

                this.configuration.send(player, "quest-reward-delete-success", this.quest.getName());
                this.quest.removeReward(reward);
            });
        }

        pagination.setItems(27, items);
        pagination.addToIterator(contents.newIterator(GuiIteratorType.HORIZONTAL, 0, 0));

        contents.setItem(5, 1, GuiItem.of(
            new ItemBuilder(XMaterial.COMPASS)
                .name("&b&lCreate new Reward")
                .lore("&7Click to create a new reward")
                .build(),
            e -> {
                final QuestReward reward = new QuestReward();
                this.quest.addReward(reward);

                this.plugin.getInventoryManager().getEditRewardProvider(this.quest, reward).open(player);
            }
        ));

        InventoryManager.addReturnButton(5, 4, contents);
        InventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getRewardItem(QuestReward reward) {
        final ItemBuilder builder = new ItemBuilder(XMaterial.PAPER);
        final String name = reward.getDisplayName() != null ? reward.getDisplayName() : "Name not set";
        builder.name(name);
        builder.addLore(
            "&bLeft - Click &7to edit this reward",
            "&bShift - Right Click &7to delete this reward",
            "",
            "&b❙ Reward",
            "&fDisplay Name: &b" + name,
            "&fCommand: &b" + Optional.ofNullable(reward.getCommand()).orElse("Not set")
        );

        return builder.build();
    }
}
