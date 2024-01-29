package net.pinger.quests.reward;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class QuestReward {
    private int id = -1;

    private String displayName;
    private String command;

    public QuestReward() {}

    public QuestReward(int id, String displayName, String command) {
        this.id = id;
        this.displayName = displayName;
        this.command = command;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommand() {
        return this.command;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getId() {
        return this.id;
    }

    public boolean isValid() {
        return this.displayName != null && this.command != null;
    }

    public void execute(Player player) {
        if (this.command == null) {
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command.replace("{player}", player.getName()));
    }
}
