package net.pinger.quests.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.quest.Quest;
import org.bukkit.Bukkit;

public class QuestManager {
    private final Map<Integer, Quest> quests;
    private final List<Quest> unsavedQuests;
    private final PlayerQuestsPlugin plugin;

    public QuestManager(PlayerQuestsPlugin plugin) {
        this.plugin = plugin;
        this.quests = Collections.synchronizedMap(new HashMap<>());
        this.unsavedQuests = new ArrayList<>();

        this.loadQuestsTask();
    }

    public Quest getQuest(int id) {
        return this.quests.get(id);
    }

    public Quest getQuest(String name) {
        for (final Quest quest : this.unsavedQuests) {
            if (quest.getName().equalsIgnoreCase(name)) {
                return quest;
            }
        }

        return null;
    }

    public void addQuest(Quest quest) {
        this.unsavedQuests.add(quest);
    }

    public void removeQuest(Quest quest) {
        this.unsavedQuests.remove(quest);
    }

    public void loadQuests(Map<Integer, Quest> quests) {
        this.quests.clear();
        this.quests.putAll(quests);
    }

    private void loadQuestsTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            this.plugin.getStorage().loadAllQuests().join();
        }, 20L, 20L * 60L * 10);
    }
}
