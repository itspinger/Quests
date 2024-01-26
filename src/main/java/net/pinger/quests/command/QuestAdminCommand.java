package net.pinger.quests.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.gui.InventoryManager;
import org.bukkit.entity.Player;

public class QuestAdminCommand {
    private final PlayerQuestsPlugin plugin;

    public QuestAdminCommand(PlayerQuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(name = "", desc = "The admin command for quests")
    @Require("quests.admin")
    public void openQuestMenu(@Sender Player sender) {
        this.plugin.getInventoryManager().getQuestsProvider().open(sender);
    }

}
