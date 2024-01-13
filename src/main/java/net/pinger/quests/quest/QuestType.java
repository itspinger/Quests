package net.pinger.quests.quest;

public enum QuestType {

    BLOCK_BREAK(
        "&7Break certain blocks in order to",
        "&7progress on the quest"
    ),
    BLOCK_PLACE(
        "&7Place certain blocks in order to",
        "&7progress on the quest"
    ),
    PLAYER_KILL(
        "&7Kill players in order to progress",
        "&7on the quest"
    ),
    MOB_KILL(
        "&7Kill mobs in order to progress",
        "&7on the quest"
    ),
    GO_TO_WORLD(
        "&7Change the world in order to",
        "&7progress on the quest"
    );

    private final String[] description;

    QuestType(String... description) {
        this.description = description;
    }

    public String[] getDescription() {
        return this.description;
    }
}
