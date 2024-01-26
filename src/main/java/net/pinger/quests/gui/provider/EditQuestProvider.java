package net.pinger.quests.gui.provider;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.provider.GuiProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

        contents.setItem(2, 4, GuiItem.of(
            this.getRewards().build(),
            e -> {
                this.playerQuestsPlugin.getInventoryManager().getQuestRewardsProvider(this.quest).open(player);
            }
        ));

        contents.setItem(2, 5, GuiItem.of(
            this.getDescription().build(),
            e -> {
                this.conversationManager.createConversation(player, new SetQuestDescriptionPrompt(this.playerQuestsPlugin, this.quest));
            }
        ));

        contents.setItem(3, 4, GuiItem.of(
            this.getGoal().build(),
            e -> {
                this.conversationManager.createConversation(player, new SetQuestGoalPrompt(this.playerQuestsPlugin, this.quest));
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
        lore.add("&f" + this.quest.getQuestType().getName());
        lore.add("");
        lore.add("&b❙ Quest Data");
        lore.addAll(this.quest.getQuestData().getDescription());
        lore.add("&fGoal: &b" + this.quest.getGoal());

        return builder.lore(lore);
    }

    private ItemBuilder getRewards() {
        final ItemBuilder builder = new ItemBuilder(XMaterial.PAPER);
        builder.name("&b&lQuest Rewards");

        final List<String> lore = new ArrayList<>();
        lore.add("&7Click here to edit the rewards");
        lore.add("");
        lore.add("&b❙ Rewards");
        if (this.quest.getRewards().isEmpty()) {
            lore.add("&fNone");
        } else {
            final Set<String> mapped = this.quest.getRewards()
                .stream()
                .map(reward -> "&f" + reward.getDisplayName())
                .collect(Collectors.toSet());

            lore.addAll(mapped);
        }

        return builder.lore(lore);
    }

    private ItemBuilder getDescription() {
        final ItemBuilder builder = new ItemBuilder(XMaterial.MAP);
        builder.name("&b&lQuest Description");

        final List<String> lore = new ArrayList<>();
        lore.add("&7Click to change the description of this quest");
        lore.add("");
        lore.add("&b❙ Description");
        if (this.quest.getDescription().isEmpty()) {
            lore.add("&fNone");
        } else {
            lore.addAll(this.quest.getDescription());
        }

        return builder.lore(lore);
    }

    private ItemBuilder getGoal() {
        return new ItemBuilder(XMaterial.DIAMOND)
            .name("&b&lQuest Goal")
            .lore(
                "&7Click to change the goal of this quest",
                "",
                "&b❙ Goal",
                "&f" + this.quest.getGoal()
            );
    }
}
