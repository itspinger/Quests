package net.pinger.quests.gui.provider;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.provider.GuiProvider;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.item.ItemBuilder;
import net.pinger.quests.item.XMaterial;
import org.bukkit.entity.Player;

public class QuestsProvider implements GuiProvider {
    private final PlayerQuestsPlugin playerQuestsPlugin;

    public QuestsProvider(PlayerQuestsPlugin playerQuestsPlugin) {
        this.playerQuestsPlugin = playerQuestsPlugin;
    }

    @Override
    public void update(Player player, GuiContents contents) {

        contents.setItem(5, 1, GuiItem.of(
            new ItemBuilder(XMaterial.COMPASS)
                .name("&b&lCreate new Quest")
                .build()
        ));
    }
}
