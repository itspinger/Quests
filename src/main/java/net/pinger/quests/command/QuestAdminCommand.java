package net.pinger.quests.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.gui.InventoryManager;
import org.bukkit.entity.Player;

public class QuestAdminCommand {
    private final PlayerQuestsPlugin playerQuestsPlugin;

    public QuestAdminCommand(PlayerQuestsPlugin playerQuestsPlugin) {
        this.playerQuestsPlugin = playerQuestsPlugin;
    }

    @Command(name = "questsadmin", desc = "The admin command for quests")
    @Require("quests.admin")
    public void openQuestMenu(@Sender Player sender) {
        final InventoryManager manager = this.playerQuestsPlugin.getInventoryManager();
        manager.getQuestsProvider().open(sender);
    }

}
