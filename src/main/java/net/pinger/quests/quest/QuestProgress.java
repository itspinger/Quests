package net.pinger.quests.quest;

import java.util.function.UnaryOperator;

public class QuestProgress {
    private int progress;
    private boolean complete;

    public QuestProgress(int progress, boolean complete) {
        this.progress = progress;
        this.complete = complete;
    }

    public QuestProgress() {
        this(0, false);
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void modifyProgress(UnaryOperator<Integer> modifier) {
        this.setProgress(modifier.apply(this.getProgress()));
    }

    public boolean isComplete() {
        return this.complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
