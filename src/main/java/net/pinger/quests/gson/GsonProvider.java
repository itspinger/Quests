package net.pinger.quests.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.pinger.quests.gson.adapter.QuestDataAdapter;
import net.pinger.quests.quest.data.QuestData;

public class GsonProvider {
    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(QuestData.class, new QuestDataAdapter())
        .setPrettyPrinting()
        .create();

    public static Gson get() {
        return GSON;
    }
}
