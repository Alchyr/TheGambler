package gambler.actions.playingcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import gambler.PokerHand;
import gambler.screen.PokerHandChangeScreen;

public class HandChangeAction extends AbstractGameAction {
    private final boolean forValue;

    public HandChangeAction(int amt, boolean forValue) {
        this.amount = amt;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.forValue = forValue;
    }

    @Override
    public void update() {
        this.isDone = true;
        if (this.amount <= 0 || PokerHand.cards.isEmpty()) {
            return;
        }

        PokerHandChangeScreen.screen.open(this.amount, forValue);
    }
}
