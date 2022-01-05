package gambler.cards.showdown.effects;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import gambler.cards.showdown.ShowdownEffect;

public class DrawEffect extends ShowdownEffect {
    private static final String ID = "Draw";

    private static final String[] TEXT = getText(ID);

    public DrawEffect(int amount) {
        super(ID, 20, amount, 1);
    }

    @Override
    public void applyEffect(AbstractPlayer p, AbstractCard c) {
        if (active())
            addToBot(new DrawCardAction(getFinalAmount()));
    }

    @Override
    public String getDescription() {
        if (getFinalAmount() == 1) {
            return TEXT[0] + getFinalAmount() + TEXT[1];
        }
        else {
            return TEXT[0] + getFinalAmount() + TEXT[2];
        }
    }
}