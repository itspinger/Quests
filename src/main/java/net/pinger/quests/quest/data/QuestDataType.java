package net.pinger.quests.quest.data;

public enum QuestDataType {

    BLOCK_DATA(BlockData.class),
    PLAYER_DATA(PlayerData.class);

    private final Class<? extends QuestData> classifier;

    QuestDataType(Class<? extends QuestData> classifier) {
        this.classifier = classifier;
    }

    public static QuestDataType of(String questDataType) {
        for (final QuestDataType type : values()) {
            if (type.name().equalsIgnoreCase(questDataType)) {
                return type;
            }
        }

        return null;
    }

    public Class<? extends QuestData> getClassifier() {
        return this.classifier;
    }
}
