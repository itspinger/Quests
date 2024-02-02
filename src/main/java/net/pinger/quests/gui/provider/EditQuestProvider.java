package net.pinger.quests.gui.provider;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.provider.GuiProvider;
import java.util.List;
import java.util.stream.Collectors;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.conversation.ConversationManager;
import net.pinger.quests.conversation.prompt.ChangeQuestIconPrompt;
import net.pinger.quests.conversation.prompt.SetQuestDescriptionPrompt;
import net.pinger.quests.conversation.prompt.SetQuestGoalPrompt;
import net.pinger.quests.gui.InventoryManager;
import net.pinger.quests.item.ItemBuilder;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.Quest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditQuestProvider implements GuiProvider {
    private final PlayerQuestsPlugin plugin;
    private final ConversationManager conversationManager;
    private final Quest quest;

    public EditQuestProvider(PlayerQuestsPlugin playerQuestsPlugin, Quest quest) {
        this.plugin = playerQuestsPlugin;
        this.conversationManager = playerQuestsPlugin.getConversationManager();
        this.quest = quest;
    }

    @Override
    public void initialize(Player player, GuiContents contents) {
        contents.setItem(1, 4, GuiItem.of(
            new ItemBuilder(this.quest.getMaterial())
                .name("&b&lChange Icon")
                .lore("&7Click to change icon of the quest")
                .build(),
            e -> {
                this.conversationManager.createConversation(player, new ChangeQuestIconPrompt(this.plugin, this.quest));
            }
        ));

        contents.setItem(2, 3, GuiItem.of(
            this.getQuestTypeItem(),
            e -> {
                if (this.quest.getQuestData() != null) {
                    return;
                }

                this.plugin.getInventoryManager().getQuestTypeProvider(this.quest).open(player);
            }
        ));

        contents.setItem(2, 4, GuiItem.of(
            this.getRewardsItem(),
            e -> {
                this.plugin.getInventoryManager().getQuestRewardsProvider(this.quest).open(player);
            }
        ));

        contents.setItem(2, 5, GuiItem.of(
            this.getDescriptionItem(),
            e -> {
                this.conversationManager.createConversation(player, new SetQuestDescriptionPrompt(this.plugin, this.quest));
            }
        ));

        contents.setItem(3, 4, GuiItem.of(
            this.getGoalItem(),
            e -> {
                this.conversationManager.createConversation(player, new SetQuestGoalPrompt(this.plugin, this.quest));
            }
        ));

        InventoryManager.addReturnButton(5, 4, contents);
    }

    private ItemStack getQuestTypeItem() {
        final ItemBuilder builder = new ItemBuilder(XMaterial.DIAMOND_SWORD);
        builder.name("&b&lQuest Type");

        if (this.quest.getQuestData() == null) {
            return builder.lore("&7Click here to setup the quest type").build();
        }

        builder.addLore(
            "",
            "&b❙ Quest Type",
            "&f" + this.quest.getQuestType().getName(),
            "",
            "&b❙ Quest Data"
        );

        builder.addLore(this.quest.getQuestData().getDescription());
        builder.addLore("&fGoal: &b" + this.quest.getGoal());
        return builder.build();
    }

    private ItemStack getRewardsItem() {
        final ItemBuilder builder = new ItemBuilder(XMaterial.PAPER);
        builder.name("&b&lQuest Rewards");

        builder.addLore(
            "&7Click here to edit the rewards",
            "",
            "&b❙ Rewards"
        );

        if (this.quest.getRewards().isEmpty()) {
            builder.addLore("&fNone");
        } else {
            final List<String> mapped = this.quest.getRewards()
                .stream()
                .map(reward -> "&f" + reward.getDisplayName())
                .collect(Collectors.toList());

            builder.addLore(mapped);
        }

        return builder.build();
    }

    private ItemStack getDescriptionItem() {
        final ItemBuilder builder = new ItemBuilder(XMaterial.MAP);
        builder.name("&b&lQuest Description");

        builder.addLore(
            "&7Click to change the description of this quest",
            "",
            "&b❙ Description"
        );

        if (this.quest.getDescription().isEmpty()) {
            builder.addLore("&fNone");
        } else {
            builder.addLore(this.quest.getDescription());
        }

        return builder.build();
    }

    private ItemStack getGoalItem() {
        return new ItemBuilder(XMaterial.DIAMOND)
            .name("&b&lQuest Goal")
            .lore(
                "&7Click to change the goal of this quest",
                "",
                "&b❙ Goal",
                "&f" + this.quest.getGoal()
            )
            .build();
    }
}
