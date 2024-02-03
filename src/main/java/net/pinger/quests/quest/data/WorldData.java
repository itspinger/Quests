package net.pinger.quests.quest.data;

import java.util.List;

public class WorldData extends QuestData {
    private final String worldName;

    public WorldData(String worldName) {
        super(QuestDataType.WORLD_DATA);

        this.worldName = worldName;
    }

    @Override
    public List<String> getDescription() {
        return List.of(
            "&fPlayer: &b".concat(this.worldName == null ? "Any" : this.worldName)
        );
    }

    public String getWorldName() {
        return this.worldName;
    }
}
