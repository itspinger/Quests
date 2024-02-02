package net.pinger.quests.quest.handler;

import java.util.function.UnaryOperator;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.manager.PlayerQuestManager;
import net.pinger.quests.player.QuestPlayer;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class QuestHandler implements Listener {
    private final PlayerQuestManager playerQuestManager;
    private final int goal;

    protected final Quest quest;

    protected QuestHandler(PlayerQuestsPlugin plugin, Quest quest) {
        this.playerQuestManager = plugin.getPlayerQuestManager();
        this.quest = quest;
        this.goal = quest.getGoal();
    }

    public void handle(Player player, UnaryOperator<Integer> modifier) {
        if (player == null) {
            return;
        }

        final QuestPlayer questPlayer = this.playerQuestManager.getPlayer(player.getUniqueId());
        this.handle(questPlayer, modifier);
    }

    public void handle(QuestPlayer player, UnaryOperator<Integer> modifier) {
        // If this quest isn't enabled for this player, return
        if (!player.isActiveQuest(this.quest)) {
            return;
        }

        player.modifyQuests((quests -> {
            quests.compute(this.quest, ($, progress) -> {
                if (progress == null) {
                    progress = new QuestProgress();
                }

                if (progress.isComplete()) {
                    return progress;
                }

                progress.modifyProgress(modifier);
                if (progress.getProgress() < this.goal) {
                    return progress;
                }

                progress.setComplete(true);
                progress.modifyProgress(($1) -> this.goal);

                final Player playerEntity = player.getPlayer();
                if (playerEntity == null) {
                    return progress;
                }

                this.quest.reward(playerEntity);
                return progress;
            });
        }));
    }
}
