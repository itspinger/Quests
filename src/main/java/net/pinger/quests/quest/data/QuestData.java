package net.pinger.quests.quest.data;

public abstract class QuestData {
    private final QuestDataType questDataType;
    private final int goal;

    protected QuestData(QuestDataType questDataType, int goal) {
        this.questDataType = questDataType;
        this.goal = goal;
    }

    public QuestDataType getQuestDataType() {
        return this.questDataType;
    }

    public int getGoal() {
        return this.goal;
    }
}
