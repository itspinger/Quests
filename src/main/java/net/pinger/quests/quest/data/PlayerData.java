package net.pinger.quests.quest.data;

import java.util.List;

public class PlayerData extends QuestData {
    private final String playerName;

    public PlayerData(String playerName) {
        super(QuestDataType.PLAYER_DATA);

        this.playerName = playerName;
    }

    @Override
    public List<String> getDescription() {
        return List.of(
            "&fPlayer: &b".concat(this.playerName == null ? "Any" : this.playerName)
        );
    }

    public String getPlayerName() {
        return this.playerName;
    }
}
