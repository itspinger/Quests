package net.pinger.quests.storage.implementation.sql;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.gson.GsonProvider;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.player.QuestPlayer;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestProgress;
import net.pinger.quests.quest.QuestType;
import net.pinger.quests.quest.data.QuestData;
import net.pinger.quests.reward.QuestReward;
import net.pinger.quests.storage.Storage;
import net.pinger.quests.storage.implementation.StorageImplementation;

public class SqlStorage implements StorageImplementation {
    private static final Type LIST_STRING_TYPE = new TypeToken<List<String>>(){}.getType();

    private static final String LOAD_PLAYER = "SELECT * FROM pquests_players WHERE player_uuid = ?;";
    private static final String LOAD_PLAYER_QUEST = "SELECT * FROM pquests_players_quests_progress WHERE player_uuid = ?;";
    private static final String SAVE_PLAYER = "INSERT IGNORE INTO pquests_players VALUES (?, ?);";
    private static final String SAVE_PLAYER_QUESTS = "INSERT INTO pquests_players_quests_progress(quest_id, player_uuid, active, "
                                                     + "progress, completed) VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_PLAYER_QUESTS = "UPDATE pquests_players_quests_progress SET progress = ?, completed = ?, active = "
                                                       + "? WHERE quest_id = ? AND player_uuid = ?";
    private static final String EXISTS_PLAYER_QUEST = "SELECT * FROM pquests_players_quests_progress WHERE quest_id = ? AND player_uuid = ?;";
    private static final String LOAD_QUEST_REWARDS = "SELECT * FROM pquests_rewards WHERE quest_id = ?;";
    private static final String SELECT_QUESTS = "SELECT * FROM pquests_quests;";
    private static final String DELETE_QUEST_REWARDS = "DELETE FROM pquests_rewards WHERE quest_id = ?;";
    private static final String DELETE_QUEST_PROGRESS = "DELETE FROM pquests_players_quests_progress WHERE quest_id = ?;";
    private static final String DELETE_QUEST = "DELETE FROM pquests_quests WHERE quest_id = ?;";
    private static final String DELETE_QUEST_REWARD = "DELETE FROM pquests_rewards WHERE reward_id = ?;";
    private static final String SAVE_QUEST = "INSERT INTO pquests_quests(name, type, icon, description, goal, data) VALUES (?, ?, ?, ?, "
                                             + "?, ?);";
    private static final String UPDATE_QUEST = "UPDATE pquests_quests SET icon = ?, description = ?, goal = ? WHERE quest_id = ?;";
    private static final String SAVE_REWARD = "INSERT INTO pquests_rewards(name, command, quest_id) VALUES (?, ?, ?);";
    private static final String UPDATE_REWARD = "UPDATE pquests_rewards SET name = ?, command = ? WHERE reward_id = ?;";

    private final PlayerQuestsPlugin plugin;
    private final Storage storage;

    public SqlStorage(PlayerQuestsPlugin plugin, Storage storage) {
        this.plugin = plugin;
        this.storage = storage;
    }

    @Override
    public QuestPlayer loadPlayer(UUID uniqueId) throws SQLException {
        try (final Connection connection = this.storage.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(LOAD_PLAYER)) {
                statement.setString(1, uniqueId.toString());
                try (final ResultSet set = statement.executeQuery()) {
                    if (!set.next()) {
                        return new QuestPlayer(uniqueId);
                    }
                }

                final Map<Quest, QuestProgress> map = this.selectPlayerQuests(connection, uniqueId);
                return new QuestPlayer(uniqueId, map);
            }
        }
    }

    private Map<Quest, QuestProgress> selectPlayerQuests(Connection c, UUID uniqueId) throws SQLException {
        final Map<Quest, QuestProgress> quests = new HashMap<>();
        try (final PreparedStatement statement = c.prepareStatement(LOAD_PLAYER_QUEST)) {
            statement.setString(1, uniqueId.toString());
            try (final ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    final Map.Entry<Quest, QuestProgress> quest = this.loadPlayerQuest(set);
                    if (quest == null) {
                        continue;
                    }

                    quests.put(quest.getKey(), quest.getValue());
                }
            }
        }

        return quests;
    }

    private Map.Entry<Quest, QuestProgress> loadPlayerQuest(ResultSet set) throws SQLException {
        final int questId = set.getInt("quest_id");
        final Quest quest = this.plugin.getQuestManager().getQuest(questId);
        if (quest == null) {
            return null;
        }

        final int progress = set.getInt("progress");
        final boolean completed = set.getBoolean("completed");
        final boolean active = set.getBoolean("active");

        final QuestProgress questProgress = new QuestProgress(progress, completed, active);
        return Maps.immutableEntry(quest, questProgress);
    }

    @Override
    public void savePlayer(QuestPlayer player) throws SQLException {
        try (final Connection connection = this.storage.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(SAVE_PLAYER)) {
                statement.setString(1, player.getId().toString());
                statement.setString(2, player.getName());
                statement.executeUpdate();
            }

            final Map<Quest, QuestProgress> progressMap = player.getQuestsMap();
            for (final Map.Entry<Quest, QuestProgress> entry : progressMap.entrySet()) {
                final Quest quest = entry.getKey();
                if (quest.getId() == -1 || this.plugin.getQuestManager().getQuest(quest.getId()) == null) {
                    continue;
                }

                final QuestProgress progress = entry.getValue();
                this.savePlayerQuest(connection, player, quest, progress);
            }
        }
    }

    private void savePlayerQuest(Connection c, QuestPlayer player, Quest quest, QuestProgress progress) throws SQLException {
        final boolean existsProgress = this.existsPlayerProgress(c, player, quest);
        if (existsProgress) {
            try (final PreparedStatement statement = c.prepareStatement(UPDATE_PLAYER_QUESTS)) {
                statement.setInt(1, progress.getProgress());
                statement.setBoolean(2, progress.isComplete());
                statement.setBoolean(3, progress.isActive());
                statement.setInt(4, quest.getId());
                statement.setString(5, player.getId().toString());
                statement.executeUpdate();
                return;
            }
        }

        try (final PreparedStatement statement = c.prepareStatement(SAVE_PLAYER_QUESTS)) {
            statement.setInt(1, quest.getId());
            statement.setString(2, player.getId().toString());
            statement.setBoolean(3, progress.isActive());
            statement.setInt(4, progress.getProgress());
            statement.setBoolean(5, progress.isComplete());
            statement.executeUpdate();
        }
    }

    private boolean existsPlayerProgress(Connection c, QuestPlayer player, Quest quest) throws SQLException {
        try (final PreparedStatement statement = c.prepareStatement(EXISTS_PLAYER_QUEST)) {
            statement.setInt(1, quest.getId());
            statement.setString(2, player.getId().toString());
            try (final ResultSet set = statement.executeQuery()) {
                return set.next();
            }
        }
    }

    @Override
    public void loadAllQuests() throws SQLException {
        final Map<Integer, Quest> quests = new HashMap<>();
        try (final Connection connection = this.storage.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(SELECT_QUESTS)) {
                try (final ResultSet set = statement.executeQuery()) {
                    while (set.next()) {
                        final Quest quest = this.loadQuestData(connection, set);
                        if (quest == null) {
                            continue;
                        }

                        quests.put(quest.getId(), quest);
                    }
                }
            }
        }

        this.plugin.getQuestManager().loadQuests(quests);
    }

    private Quest loadQuestData(Connection c, ResultSet set) throws SQLException {
        final int id = set.getInt("quest_id");
        if (id < 0) {
            return null;
        }

        final String name = set.getString("name");
        final String description = set.getString("description");
        final String material = set.getString("icon");
        final String type = set.getString("type");
        final int goal = set.getInt("goal");
        final String data = set.getString("data");

        // Try to convert certain data, if not possible return null
        final Quest quest = new Quest(id, name, goal);
        try {
            final List<String> desc = GsonProvider.get().fromJson(description, LIST_STRING_TYPE);
            final XMaterial icon = XMaterial.matchXMaterial(material).orElse(XMaterial.SUNFLOWER);
            final QuestType questType = QuestType.of(type);
            final QuestData questData = GsonProvider.get().fromJson(data, QuestData.class);

            quest.setDescription(desc);
            quest.setMaterial(icon);
            quest.setQuestType(questType);
            quest.setQuestData(questData);
        } catch (Exception e) {
            return null;
        }

        final List<QuestReward> rewards = this.loadRewards(c, id);
        quest.setRewards(rewards);

        return quest;
    }

    private List<QuestReward> loadRewards(Connection c, int questId) throws SQLException {
        final List<QuestReward> rewards = new ArrayList<>();
        try (final PreparedStatement statement = c.prepareStatement(LOAD_QUEST_REWARDS)) {
            statement.setInt(1, questId);
            try (final ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    rewards.add(
                        new QuestReward(
                            set.getInt("reward_id"),
                            set.getString("name"),
                            set.getString("command")
                        )
                    );
                }

                return rewards;
            }
        }
    }

    @Override
    public void saveQuest(Quest quest) throws SQLException {
        if (quest.getId() == -1) {
            try (final Connection connection = this.storage.getConnection()) {
                try (final PreparedStatement statement = connection.prepareStatement(SAVE_QUEST)) {
                    statement.setString(1, quest.getName());
                    statement.setString(2, quest.getQuestType().getName());
                    statement.setString(3, quest.getMaterial().name());
                    statement.setString(4, GsonProvider.get().toJson(quest.getDescription()));
                    statement.setInt(5, quest.getGoal());
                    statement.setString(6, GsonProvider.get().toJson(quest.getQuestData(), quest.getQuestData().getClass()));
                    statement.executeUpdate();

                    try (final ResultSet set = statement.executeQuery("SELECT LAST_INSERT_ID();")) {
                        if (set.next()) {
                            quest.setId(set.getInt(1));
                        }
                    }

                    for (final QuestReward reward : quest.getRewards()) {
                        this.saveReward(connection, quest, reward);
                    }
                }
            }
        } else {
            try (final Connection connection = this.storage.getConnection()) {
                try (final PreparedStatement statement = connection.prepareStatement(UPDATE_QUEST)) {
                    statement.setString(1, quest.getMaterial().name());
                    statement.setString(2, GsonProvider.get().toJson(quest.getDescription()));
                    statement.setInt(3, quest.getGoal());
                    statement.setInt(4, quest.getId());
                    statement.executeUpdate();
                }
            }
        }
    }

    @Override
    public void saveReward(Quest quest, QuestReward reward) throws SQLException {
        try (final Connection connection = this.storage.getConnection()) {
            this.saveReward(connection, quest, reward);
        }
    }

    private void saveReward(Connection connection, Quest quest, QuestReward reward) throws SQLException {
        if (reward.getId() == -1) {
            try (final PreparedStatement statement = connection.prepareStatement(SAVE_REWARD)) {
                statement.setString(1, reward.getDisplayName());
                statement.setString(2, reward.getCommand());
                statement.setInt(3, quest.getId());
                statement.executeUpdate();

                try (final ResultSet set = statement.executeQuery("SELECT LAST_INSERT_ID();")) {
                    if (set.next()) {
                        reward.setId(set.getInt(1));
                    }
                }
            }

            return;
        }

        try (final PreparedStatement statement = connection.prepareStatement(UPDATE_REWARD)) {
            statement.setString(1, reward.getDisplayName());
            statement.setString(2, reward.getCommand());
            statement.setInt(3, reward.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteQuest(Quest quest) throws SQLException {
        if (quest.getId() == -1) {
            return;
        }

        try (final Connection connection = this.storage.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(DELETE_QUEST_REWARDS)) {
                statement.setInt(1, quest.getId());
                statement.executeUpdate();
            }

            try (final PreparedStatement statement = connection.prepareStatement(DELETE_QUEST_PROGRESS)) {
                statement.setInt(1, quest.getId());
                statement.executeUpdate();
            }

            try (final PreparedStatement statement = connection.prepareStatement(DELETE_QUEST)) {
                statement.setInt(1, quest.getId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void deleteReward(QuestReward reward) throws SQLException {
        if (reward.getId() == -1) {
            return;
        }

        try (final Connection connection = this.storage.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(DELETE_QUEST_REWARD)) {
                statement.setInt(1, reward.getId());
                statement.executeUpdate();
            }
        }
    }

}
