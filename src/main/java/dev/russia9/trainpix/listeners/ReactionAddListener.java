package dev.russia9.trainpix.listeners;

import org.javacord.api.event.message.reaction.ReactionAddEvent;

/**
 * Reaction addition listener
 *
 * @author Russia9
 * @since v0.0.1
 */
public class ReactionAddListener implements org.javacord.api.listener.message.reaction.ReactionAddListener {
    private String clientID;

    public ReactionAddListener(String clientID) {
        this.clientID = clientID;
    }

    @Override
    public void onReactionAdd(ReactionAddEvent reactionAddEvent) {

    }
}
