package net.pinger.quests.gui;

import io.pnger.gui.GuiBuilder;
import io.pnger.gui.GuiInventory;
import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.manager.GuiManager;
import io.pnger.gui.pagination.GuiPagination;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.gui.provider.EditQuestProvider;
import net.pinger.quests.gui.provider.PlayerQuestsProvider;
import net.pinger.quests.gui.provider.QuestTypeProvider;
import net.pinger.quests.gui.provider.QuestsProvider;
import net.pinger.quests.item.ItemBuilder;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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

    public GuiInventory getQuestProvider(Quest quest) {
        return GuiBuilder.of()
            .manager(this.manager)
            .provider(new EditQuestProvider(this.playerQuestsPlugin, quest))
            .title("&8Edit Quest > " + quest.getName())
            .build();
    }

    public GuiInventory getQuestTypeProvider(Quest quest) {
        return GuiBuilder.of()
            .manager(this.manager)
            .provider(new QuestTypeProvider(this.playerQuestsPlugin, quest))
            .size(4, 9)
            .title(String.format("&8%s > Set Quest Type", quest.getName()))
            .build();
    }

    public static void addPageButtons(int row, GuiContents contents) {
        final GuiPagination p = contents.getPagination();

        // If it isn't last page we need next item
        if (!p.isLast()) {
            // Get the item
            final ItemStack next = new ItemBuilder(XMaterial.ARROW)
                .name("&b&lNext Page")
                .build();

            contents.setItem(row, 8, GuiItem.of(next, e -> {
                contents.getInventory().open((Player) e.getWhoClicked(), p.next().getPage());
            }));
        } else {
            contents.setItem(row, 8, GuiItem.of(new ItemStack(Material.AIR)));
        }

        // If it isn't first page we need back item
        if (!p.isFirst()) {
            final ItemStack previous = new ItemBuilder(Material.ARROW)
                .name("&b&lPrevious Page")
                .build();

            contents.setItem(row, 0, GuiItem.of(previous, e -> {
                contents.getInventory().open((Player) e.getWhoClicked(), p.previous().getPage());
            }));
        } else {
            contents.setItem(row, 0, GuiItem.of(new ItemStack(Material.AIR)));
        }
    }

    public static void addReturnButton(int row, int col, GuiContents contents) {
        final ItemStack stack = new ItemBuilder(XMaterial.OAK_SIGN)
            .name("&b&lBack")
            .lore("&7Click to go back")
            .build();

        // Creating the ClickableItem
        final GuiItem item = GuiItem.of(stack, e -> {
            if (e.isRightClick() || e.isShiftClick()) {
                return;
            }

            if (e.getClick() == ClickType.NUMBER_KEY) {
                return;
            }

            final GuiInventory inv = contents.getInventory().getParent().orElse(null);

            // If inventory doesn't have a parent close it
            // Otherwise open the parent
            if (inv == null) {
                e.getWhoClicked().closeInventory();
                return;
            }

            inv.open((Player) e.getWhoClicked());
        });

        // Adding it to the inventory
        contents.setItem(row, col, item);
    }
}
