package net.pinger.quests.quest.data;

import org.bukkit.Material;

public class BlockData extends QuestData {
    private final Material material;
    private final int data;

    public BlockData(int goal, Material material, int data) {
        super(QuestDataType.BLOCK_DATA, goal);

        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getData() {
        return this.data;
    }
}
