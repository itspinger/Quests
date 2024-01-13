package net.pinger.quests.quest;

import java.util.function.UnaryOperator;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.manager.PlayerQuestManager;
import net.pinger.quests.player.QuestPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class QuestHandler implements Listener {
    private final PlayerQuestsPlugin playerQuestsPlugin;
    private final PlayerQuestManager playerQuestManager;
    private final Quest quest;
    private final int goal;

    protected QuestHandler(PlayerQuestsPlugin playerQuestsPlugin, Quest quest, int goal) {
        this.playerQuestsPlugin = playerQuestsPlugin;
        this.playerQuestManager = playerQuestsPlugin.getPlayerQuestManager();
        this.quest = quest;
        this.goal = goal;
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
                if (progress.getProgress() >= this.goal) {
                    progress.setComplete(true);
                    progress.modifyProgress(($1) -> this.goal);
                }

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
