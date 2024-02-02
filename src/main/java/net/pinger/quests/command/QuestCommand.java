package net.pinger.quests.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.quests.PlayerQuestsPlugin;
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

}
