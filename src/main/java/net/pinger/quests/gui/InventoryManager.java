package net.pinger.quests.gui;

import io.pnger.gui.GuiBuilder;
import io.pnger.gui.GuiInventory;
import io.pnger.gui.manager.GuiManager;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.gui.provider.PlayerQuestsProvider;
import net.pinger.quests.gui.provider.QuestsProvider;

public class InventoryManager {
    private final PlayerQuestsPlugin playerQuestsPlugin;
    private final GuiManager manager;

    public InventoryManager(PlayerQuestsPlugin playerQuestsPlugin) {
        this.playerQuestsPlugin = playerQuestsPlugin;
        this.manager = new GuiManager(this.playerQuestsPlugin);
    }

    public GuiInventory getQuestsProvider() {
        return GuiBuilder.of()
            .manager(this.manager)
            .provider(new QuestsProvider(this.playerQuestsPlugin))
            .title("&8Available Quests")
            .build();
    }
}
