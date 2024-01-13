package net.pinger.quests.quest.data;

import java.util.UUID;

public class PlayerData extends QuestData {
    private final UUID id;

    public PlayerData(int goal, UUID id) {
        super(QuestDataType.PLAYER_DATA, goal);

        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
