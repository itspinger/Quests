package net.pinger.quests.quest;

import java.util.function.BiFunction;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.conversation.prompt.block.CreateBlockQuestPrompt;
import net.pinger.quests.conversation.prompt.entity.CreateEntityKillQuestPrompt;
import net.pinger.quests.conversation.prompt.entity.CreatePlayerKillQuestPrompt;
import net.pinger.quests.conversation.prompt.world.CreatePlayerWorldQuestPrompt;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.handler.BreakQuest;
import net.pinger.quests.quest.handler.EntityKillQuest;
import net.pinger.quests.quest.handler.PlaceQuest;
import net.pinger.quests.quest.handler.PlayerKillQuest;
import net.pinger.quests.quest.handler.QuestHandler;
import net.pinger.quests.quest.handler.WorldQuest;
import org.bukkit.conversations.Prompt;

public enum QuestType {

    BLOCK_BREAK(
        "Block Break Quest",
        XMaterial.DIAMOND_PICKAXE,
        CreateBlockQuestPrompt::new,
        BreakQuest::new,
        "&fBreak certain blocks in order to",
        "&fprogress on the quest"
    ),
    BLOCK_PLACE(
        "Block Place Quest",
        XMaterial.IRON_BLOCK,
        CreateBlockQuestPrompt::new,
        PlaceQuest::new,
        "&fPlace certain blocks in order to",
        "&fprogress on the quest"
    ),
    PLAYER_KILL(
        "Player Kill Quest",
        XMaterial.DIAMOND_SWORD,
        CreatePlayerKillQuestPrompt::new,
        PlayerKillQuest::new,
        "&fKill players in order to progress",
        "&fon the quest"
    ),
    ENTITY_KILL(
        "Entity Kill Quest",
        XMaterial.IRON_SWORD,
        CreateEntityKillQuestPrompt::new,
        EntityKillQuest::new,
        "&fKill mobs in order to progress",
        "&fon the quest"
    ),
    GO_TO_WORLD(
        "Go to World Quest",
        XMaterial.ENDER_EYE,
        CreatePlayerWorldQuestPrompt::new,
        WorldQuest::new,
        "&fChange the world in order to",
        "&fprogress on the quest"
    );

    private final String name;
    private final XMaterial material;
    private final BiFunction<PlayerQuestsPlugin, Quest, Prompt> promptFunction;
    private final BiFunction<PlayerQuestsPlugin, Quest, QuestHandler> questHandlerFunction;
    private final String[] description;

    QuestType(
        String name,
        XMaterial material,
        BiFunction<PlayerQuestsPlugin, Quest, Prompt> promptFunction,
        BiFunction<PlayerQuestsPlugin, Quest, QuestHandler> questHandlerFunction,
        String... description
    ) {
        this.name = name;
        this.material = material;
        this.promptFunction = promptFunction;
        this.questHandlerFunction = questHandlerFunction;
        this.description = description;
    }

    public static QuestType of(String name) {
        for (final QuestType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return null;
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
