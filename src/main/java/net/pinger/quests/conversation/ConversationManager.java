package net.pinger.quests.conversation;

import java.util.ArrayList;
import java.util.List;
import net.pinger.quests.PlayerQuestsPlugin;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class ConversationManager {

    private final List<Conversation> conversations = new ArrayList<>();
    private final ConversationFactory factory;

    public ConversationManager(PlayerQuestsPlugin questsPlugin) {
        this.factory = new ConversationFactory(questsPlugin);
    }

    /**
     * This method creates a conversation for a certain player.
     *
     * @param player the player
     * @param prompt the conversation prompt
     */

    public void createConversation(Player player, Prompt prompt) {
        this.createConversation(player, prompt, 15);
    }

    /**
     * This method creates a conversation for a certain player
     * with a specific timeout.
     *
     * @param player the player
     * @param prompt the prompt
     * @param timeout the timeout
     */

    public void createConversation(Player player, Prompt prompt, int timeout) {
        final Conversation conversation = this.factory.withFirstPrompt(prompt)
            .withLocalEcho(false)
            .withTimeout(timeout)
            .withEscapeSequence("Return")
            .buildConversation(player);

        // Automatically do this
        player.closeInventory();

        this.conversations.add(conversation);

        // Start it
        conversation.begin();
    }

    public void cancelAllConversations() {
        for (final Conversation conversation : this.conversations) {
            conversation.abandon();
        }
    }


}
