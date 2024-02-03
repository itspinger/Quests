package net.pinger.quests.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.data.QuestData;
import net.pinger.quests.reward.QuestReward;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Quest {
    private final String name;
    private final List<QuestReward> rewards;

    private int id;
    private XMaterial material;
    private List<String> description;
    private int goal;
    private QuestType questType;
    private QuestData questData;

    public Quest(int id, String name, int goal) {
        this.id = id;
        this.name = name;
        this.goal = goal;
        this.material = XMaterial.SUNFLOWER;
        this.description = new ArrayList<>();
        this.rewards = new ArrayList<>();
    }

    public Quest(String name) {
        this(-1, name, 0);
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

        player.sendMessage(ChatColor.GREEN + "You've successfully completed quest " + ChatColor.YELLOW + this.name + ChatColor.GREEN + " and received your rewards!");
    }

    @SuppressWarnings("unchecked")
    public <T extends QuestData> T getData() {
        return (T) this.questData;
    }

    /**
     * Return whether this quest is complete (meaning that it can be saved
     * and displayed to the players)
     *
     * @return whether this quest is complete
     */

    public boolean isComplete() {
        return this.getQuestType() != null && this.getQuestData() != null && this.goal > 0;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public void setDescription(List<String> description) {
        this.description = new ArrayList<>(description);
    }

    public XMaterial getMaterial() {
        return this.material;
    }

    public void setMaterial(XMaterial material) {
        this.material = material;
    }

    public List<QuestReward> getRewards() {
        return this.rewards;
    }

    public void setRewards(List<QuestReward> rewards) {
        this.rewards.addAll(rewards);
    }

    public QuestType getQuestType() {
        return this.questType;
    }

    public void setQuestType(QuestType questType) {
        this.questType = questType;
    }

    public QuestData getQuestData() {
        return this.questData;
    }

    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }

    public int getGoal() {
        return this.goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public int hashCode() {
        if (this.id != -1) {
            return Objects.hashCode(this.id);
        }

        return Objects.hashCode(this.name);
    }
}
