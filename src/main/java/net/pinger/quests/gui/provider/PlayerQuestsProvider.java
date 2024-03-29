package net.pinger.quests.gui.provider;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.pagination.GuiPagination;
import io.pnger.gui.provider.GuiProvider;
import io.pnger.gui.slot.GuiIteratorType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.item.ItemBuilder;
import net.pinger.quests.manager.PlayerQuestManager;
import net.pinger.quests.manager.QuestManager;
import net.pinger.quests.player.QuestPlayer;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestProgress;
import net.pinger.quests.quest.QuestType;
import net.pinger.quests.quest.data.QuestData;
import net.pinger.quests.reward.QuestReward;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerQuestsProvider implements GuiProvider {
    private final PlayerQuestsPlugin plugin;
    private final PlayerQuestManager playerQuestManager;
    private final QuestManager questManager;

    public PlayerQuestsProvider(PlayerQuestsPlugin plugin) {
        this.plugin = plugin;
        this.playerQuestManager = plugin.getPlayerQuestManager();
        this.questManager = plugin.getQuestManager();
    }

    @Override
    public void update(Player p, GuiContents contents) {
        final QuestPlayer player = this.playerQuestManager.getPlayer(p.getUniqueId());
        final Collection<Quest> quests = this.questManager.getPlayerQuests();
        final GuiPagination pagination = contents.getPagination();
        final GuiItem[] items = new GuiItem[quests.size()];

        int i = 0;
        for (final Quest quest : quests) {
            final QuestProgress progress = player.getProgress(quest);
            items[i++] = GuiItem.of(this.getItemStack(progress, quest), e -> {
                if (e.isLeftClick()) {
                    if (progress.isActive()) {
                        return;
                    }

                    if (player.getActiveQuest() != null) {
                        this.plugin.getConfiguration().send(p, "quest-must-disable-active");
                        return;
                    }

                    if (progress.isComplete()) {
                        this.plugin.getConfiguration().send(p, "quest-completed");
                        return;
                    }

                    progress.setActive(true);
                    this.plugin.getConfiguration().send(p, "quest-started", quest.getName());
                    return;
                }

                if (!e.isRightClick() || !e.isShiftClick()) {
                    return;
                }

                if (!progress.isActive()) {
                    return;
                }

                progress.setActive(false);
                this.plugin.getConfiguration().send(p, "quest-disabled", quest.getName());
            });
        }

        pagination.setItems(27, items);
        pagination.addToIterator(contents.newIterator(GuiIteratorType.HORIZONTAL, 0, 0));

        InventoryManager.addReturnButton(5, 4, contents);
        InventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getItemStack(QuestProgress progress, Quest quest) {
        final ItemBuilder builder = new ItemBuilder(quest.getMaterial());
        builder.name("&b&l" + quest.getName());

        if (progress.isActive()) {
            builder.enchant(Enchantment.DURABILITY, 0).flag();
        }

        final List<String> desc = new ArrayList<>();
        desc.add("&bLeft - Click &7to start this quest");
        desc.add("&bShift - Right Click &7to cancel this quest");

        final QuestType type = quest.getQuestType();
        desc.add("");
        desc.add("&b❙ Type");
        desc.add("&f" + type.getName());

        desc.add("");
        desc.add("&b❙ Description");
        if (quest.getDescription().isEmpty()) {
            desc.add("&fNot set");
        } else {
            desc.addAll(quest.getDescription());
        }

        desc.add("");

        desc.add("&b❙ Rewards");
        if (!quest.getRewards().isEmpty()) {
            final List<String> mapped = quest.getRewards()
                .stream()
                .filter(QuestReward::isValid)
                .map(reward -> "&f" + reward.getDisplayName())
                .collect(Collectors.toList());

            if (mapped.isEmpty()) {
                desc.add("&fNo rewards");
            } else {
                desc.addAll(mapped);
            }
        } else {
            desc.add("&fNo rewards");
        }

        desc.add("");

        final QuestData data = quest.getQuestData();
        desc.add("&b❙ Quest Data");
        desc.addAll(data.getDescription());
        desc.add("");

        desc.add("&b❙ Progress");
        desc.add("&fGoal: &b" + quest.getGoal());
        desc.add("&fActive: &b" + (progress.isActive() ? "Yes" : "No"));
        desc.add("&fProgress: &b" + (progress.isComplete() ? "Completed" : this.getProgress(progress, quest.getGoal())));

        builder.lore(desc);
        return builder.build();
    }

    private String getProgress(QuestProgress progress, int goal) {
        return "&b" + progress.getProgress() + " &f/&b " + goal;
    }
}
