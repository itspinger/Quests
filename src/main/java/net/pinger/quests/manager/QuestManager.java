package net.pinger.quests.manager;

import java.util.ArrayList;
import java.util.List;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.data.QuestDataType;

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

    public Quest getQuest(String name) {
        for (final Quest quest : this.getQuests()) {
            if (quest.getName().equalsIgnoreCase(name)) {
                return quest;
            }
        }

        return null;
    }

    public List<Quest> getQuests() {
        return this.quests;
    }
}
