package net.pinger.quests.quest.data;

import java.util.List;
import java.util.UUID;

public class PlayerData extends QuestData {
    private final UUID id;

    public PlayerData(UUID id) {
        super(QuestDataType.PLAYER_DATA);

        this.id = id;
    }

    @Override
    public List<String> getDescription() {
        return List.of(
            "&fPlayer: &b".concat(this.id == null ? "Any" : this.id.toString())
        );
    }

    public UUID getId() {
        return this.id;
    }
}
