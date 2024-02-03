package net.pinger.quests.listener;

import java.util.Map.Entry;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.manager.PlayerQuestManager;
import net.pinger.quests.player.QuestPlayer;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestProgress;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final PlayerQuestManager playerQuestManager;

    public PlayerListener(PlayerQuestsPlugin playerQuestsPlugin) {
        this.playerQuestManager = playerQuestsPlugin.getPlayerQuestManager();
    }

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event) {
        this.playerQuestManager.loadPlayer(event.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.playerQuestManager.savePlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final QuestPlayer questPlayer = this.playerQuestManager.getPlayer(player.getUniqueId());
        if (questPlayer == null) {
            return;
        }

        final Entry<Quest, QuestProgress> entry = questPlayer.getActiveQuest();
        if (entry == null) {
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

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', builder));
    }

    private String getProgress(QuestProgress progress, int goal) {
        return "&b" + progress.getProgress() + " &f/&b " + goal;
    }

}
