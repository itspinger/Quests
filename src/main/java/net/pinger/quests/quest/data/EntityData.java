package net.pinger.quests.quest.data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.entity.EntityType;

public class EntityData extends QuestData {
    private final EntityType entityType;

    public EntityData(EntityType entityType) {
        super(QuestDataType.MOB_DATA);

        this.entityType = entityType;
    }

    @Override
    public List<String> getDescription() {
        return List.of(
            "&fMob: &b".concat(this.getPrettyName())
        );
    }

    private String getPrettyName() {
        if (this.entityType == null) {
            return "Any";
        }

        return Arrays.stream(this.entityType.name().split("_"))
            .map(t -> t.charAt(0) + t.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));
    }

    public EntityType getEntityType() {
        return this.entityType;
    }
}
