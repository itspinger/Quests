package net.pinger.quests.gson.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.pinger.quests.quest.data.QuestData;
import net.pinger.quests.quest.data.QuestDataType;

public class QuestDataAdapter implements JsonDeserializer<QuestData>, JsonSerializer<QuestData> {

    @Override
    public QuestData deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject object = element.getAsJsonObject();
        final String questDataType = object.get("questDataType").getAsString();
        final QuestDataType dataType = QuestDataType.of(questDataType);
        if (dataType == null) {
            return null;
        }

        return context.deserialize(object, dataType.getClassifier());
    }

    @Override
    public JsonElement serialize(QuestData questData, Type type, JsonSerializationContext context) {
        return context.serialize(questData, questData.getQuestDataType().getClassifier());
    }
}
