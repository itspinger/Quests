package net.pinger.quests.quest;

import java.util.ArrayList;
import java.util.List;
import net.pinger.quests.quest.data.QuestData;
import net.pinger.quests.reward.QuestReward;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Quest {
    private final String name;
    private final List<QuestReward> rewards;

    private List<String> description;
    private QuestData questData;

    public Quest(String name) {
        this.name = name;
        this.description = new ArrayList<>();
        this.rewards = new ArrayList<>();
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public void addReward(QuestReward reward) {
        this.rewards.add(reward);
    }

    public void removeReward(QuestReward reward) {
        this.rewards.remove(reward);
    }

    public void reward(Player player) {
        for (final QuestReward reward : this.getRewards()) {
            reward.execute(player);
        }

        // TODO: Send message as well?
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    public String getName() {
        return this.name;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public List<QuestReward> getRewards() {
        return this.rewards;
    }
}
