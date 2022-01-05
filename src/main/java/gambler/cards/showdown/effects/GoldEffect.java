package gambler.cards.showdown.effects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import gambler.actions.generic.ChangeGoldAction;
import gambler.cards.showdown.ShowdownEffect;

public class GoldEffect extends ShowdownEffect {
    private static final String ID = "Gold";

    private static final String[] TEXT = getText(ID);

    public GoldEffect(int amount) {
        super(ID, 50, amount, 1);
    }

    @Override
    public void applyEffect(AbstractPlayer p, AbstractCard c) {
        addToBot(new ChangeGoldAction(getFinalAmount(), c.hb));
    }

    @Override
    public String getDescription() {
        return TEXT[0] + getFinalAmount() + TEXT[1];
    }
}