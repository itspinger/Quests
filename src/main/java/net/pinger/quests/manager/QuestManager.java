package net.pinger.quests.manager;

import java.util.ArrayList;
import java.util.List;
import net.pinger.quests.quest.Quest;

public class QuestManager {
    private final List<Quest> quests;

    public QuestManager() {
        this.quests = new ArrayList<>();
    }

    public void load() {
        // TODO:
    }

    public void save(Quest quest) {
        // TODO
    }

    public void addQuest(Quest quest) {
        this.quests.add(quest);
    }

    public void removeQuest(Quest quest) {
        this.quests.remove(quest);
    }

    public List<Quest> getQuests() {
        return this.quests;
    }
}
