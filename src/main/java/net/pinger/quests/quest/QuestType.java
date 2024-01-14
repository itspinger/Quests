package net.pinger.quests.quest;

import java.util.function.BiFunction;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.conversation.prompt.block.CreateBlockQuestPrompt;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.handler.BreakQuest;
import net.pinger.quests.quest.handler.PlaceQuest;
import net.pinger.quests.quest.handler.QuestHandler;
import org.bukkit.conversations.Prompt;

public enum QuestType {

    BLOCK_BREAK(
        "Block Break Quest",
        XMaterial.DIAMOND_PICKAXE,
        CreateBlockQuestPrompt::new,
        BreakQuest::new,
        "&7Break certain blocks in order to",
        "&7progress on the quest"
    ),
    BLOCK_PLACE(
        "Block Place Quest",
        XMaterial.IRON_BLOCK,
        CreateBlockQuestPrompt::new,
        PlaceQuest::new,
        "&7Place certain blocks in order to",
        "&7progress on the quest"
    ),
    PLAYER_KILL(
        "Player Kill Quest",
        XMaterial.DIAMOND_SWORD,
        null,
        null,
        "&7Kill players in order to progress",
        "&7on the quest"
    ),
    MOB_KILL(
        "Mob Kill Quest",
        XMaterial.IRON_SWORD,
        null,
        null,
        "&7Kill mobs in order to progress",
        "&7on the quest"
    ),
    GO_TO_WORLD(
        "Go to World Quest",
        XMaterial.ENDER_EYE,
        null,
        null,
        "&7Change the world in order to",
        "&7progress on the quest"
    );

    private final String name;
    private final XMaterial material;
    private final BiFunction<PlayerQuestsPlugin, Quest, Prompt> promptFunction;
    private final BiFunction<PlayerQuestsPlugin, Quest, QuestHandler> questHandlerFunction;
    private final String[] description;

    QuestType(String name, XMaterial material, BiFunction<PlayerQuestsPlugin, Quest, Prompt> promptFunction, BiFunction<PlayerQuestsPlugin, Quest, QuestHandler> questHandlerFunction, String... description) {
        this.name = name;
        this.material = material;
        this.promptFunction = promptFunction;
        this.questHandlerFunction = questHandlerFunction;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public XMaterial getMaterial() {
        return this.material;
    }

    public String[] getDescription() {
        return this.description;
    }

    public Prompt createPrompt(PlayerQuestsPlugin plugin, Quest quest) {
        return this.promptFunction.apply(plugin, quest);
    }

    public QuestHandler createHandler(PlayerQuestsPlugin plugin, Quest quest) {
        return this.questHandlerFunction.apply(plugin, quest);
    }
}
