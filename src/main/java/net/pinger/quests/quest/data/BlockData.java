package net.pinger.quests.quest.data;

import java.util.Arrays;
import java.util.List;
import net.pinger.quests.item.XMaterial;

public class BlockData extends QuestData {
    private final XMaterial material;

    public BlockData(XMaterial material) {
        super(QuestDataType.BLOCK_DATA);

        this.material = material;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
            "&fBlock: &b".concat(this.material == null ? "Any" : this.material.toString()),
            "&fBlock Data: &b".concat(this.material == null ? "Any" : String.valueOf(this.material.getData()))
        );
    }

    public XMaterial getMaterial() {
        return this.material;
    }
}
