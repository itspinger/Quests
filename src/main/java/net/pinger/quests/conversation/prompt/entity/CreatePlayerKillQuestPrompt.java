package net.pinger.quests.conversation.prompt.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class CreatePlayerKillQuestPrompt extends ValidatingPrompt {
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return null;
    }

    @Override
    protected boolean isInputValid(ConversationContext conversationContext, String input) {
        final Matcher matcher = UUID_PATTERN.matcher(input);
        return matcher.matches();
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
        return null;
    }
}
