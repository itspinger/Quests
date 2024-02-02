package net.pinger.quests.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.handler.QuestHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class QuestManager {
    private final Map<Integer, Quest> quests;
    private final Map<Integer, QuestHandler> handlers;
    private final List<Quest> unsavedQuests;
    private final PlayerQuestsPlugin plugin;

    public QuestManager(PlayerQuestsPlugin plugin) {
        this.plugin = plugin;
        this.quests = Collections.synchronizedMap(new HashMap<>());
        this.handlers = Collections.synchronizedMap(new HashMap<>());
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

        for (final Quest quest : this.quests.values()) {
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
        if (quest.getId() == -1) {
            this.unsavedQuests.remove(quest);
            return;
        }

        this.quests.remove(quest.getId());
        final QuestHandler handler = this.handlers.remove(quest.getId());
        if (handler == null) {
            return;
        }

        HandlerList.unregisterAll(handler);
    }

    public void loadQuests(Map<Integer, Quest> quests) {
        for (final Iterator<Entry<Integer, Quest>> it = this.quests.entrySet().iterator(); it.hasNext();) {
            final Entry<Integer, Quest> next = it.next();
            final QuestHandler handler = this.handlers.remove(next.getKey());
            if (handler == null) {
                return;
            }

            HandlerList.unregisterAll(handler);
            it.remove();
        }

        this.quests.putAll(quests);
        this.quests.forEach(($, quest) -> this.registerQuestHandler(quest));
    }

    public List<Quest> getQuests() {
        final List<Quest> quests = new ArrayList<>(this.quests.values());
        quests.addAll(this.unsavedQuests);
        return quests;
    }

    /**
     * This method returns a collection of quests that players can see.
     * <p>
     * This means that only the quests that have been configured correctly will be seen by the players, others won't.
     *
     * @return the player quests
     */

    public Collection<Quest> getPlayerQuests() {
        return this.quests.values();
    }

    public void storeQuest(Quest quest) {
        if (quest.getId() == -1) {
            return;
        }

        this.unsavedQuests.remove(quest);
        this.quests.put(quest.getId(), quest);
        this.registerQuestHandler(quest);
    }

    private void registerQuestHandler(Quest quest) {
        final QuestHandler handler = quest.getQuestType().createHandler(this.plugin, quest);
        this.handlers.put(quest.getId(), handler);
        this.plugin.getServer().getPluginManager().registerEvents(handler, this.plugin);
        System.out.println("Creating handler for quest name " + quest.getName());
        System.out.println(handler);
    }

    private void loadQuestsTask() {
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            try {
                this.plugin.getStorage().loadAllQuests().get();
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to fetch quests from database", e);
            }
        }, 20L, 20L * 60L * 10);
    }
}
