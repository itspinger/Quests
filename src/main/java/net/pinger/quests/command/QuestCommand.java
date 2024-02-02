package net.pinger.quests.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import java.util.Map.Entry;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.player.QuestPlayer;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestProgress;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuestCommand {

    private final PlayerQuestsPlugin plugin;

    public QuestCommand(PlayerQuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(name = "", desc = "The command for viewing quests")
    public void openQuestMenu(@Sender Player sender) {
        this.plugin.getInventoryManager().getPlayerQuestsProvider().open(sender);
    }

    @Command(name = "status", desc = "See the status of your quests")
    public void checkStatus(@Sender Player sender) {
        final QuestPlayer player = this.plugin.getPlayerQuestManager().getPlayer(sender.getUniqueId());
        if (player == null) {
            return;
        }

        final Entry<Quest, QuestProgress> entry = player.getActiveQuest();
        if (entry == null) {
            this.plugin.getConfiguration().send(sender, "quest-no-active");
            return;
        }

        final Quest quest = entry.getKey();
        final QuestProgress progress = entry.getValue();

        final String builder = "&b❙ Quest Status"
                               + "\n"
                               + "&fQuest: &b"
                               + quest.getName()
                               + "\n"
                               + "&fProgress: &b"
                               + (progress.isComplete() ? "Completed" : this.getProgress(progress, quest.getGoal()));

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', builder));
    }

    private String getProgress(QuestProgress progress, int goal) {
        return "&b" + progress.getProgress() + " &f/&b " + goal;
    }

}
