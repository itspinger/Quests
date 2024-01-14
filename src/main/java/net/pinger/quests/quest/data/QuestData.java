package net.pinger.quests.quest.data;

import java.util.List;

public abstract class QuestData {
    private final QuestDataType questDataType;

    protected QuestData(QuestDataType questDataType) {
        this.questDataType = questDataType;
    }

    public abstract List<String> getDescription();

    public QuestDataType getQuestDataType() {
        return this.questDataType;
    }
}
