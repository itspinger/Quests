package net.pinger.quests.storage.implementation;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.pinger.quests.player.QuestPlayer;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.reward.QuestReward;

public interface StorageImplementation {

    QuestPlayer loadPlayer(UUID uniqueId) throws SQLException;

    void savePlayer(QuestPlayer player) throws SQLException;

    void loadAllQuests() throws SQLException;

    void saveQuest(Quest quest) throws SQLException;

    void saveReward(Quest quest, QuestReward reward) throws SQLException;

    void deleteQuest(Quest quest) throws SQLException;

    void deleteReward(QuestReward reward) throws SQLException;

}
