package net.pinger.quests.listener;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.manager.PlayerQuestManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
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

}
