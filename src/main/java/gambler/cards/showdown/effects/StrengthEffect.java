package gambler.cards.showdown.effects;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.StrengthPower;
import gambler.cards.showdown.ShowdownEffect;

public class StrengthEffect extends ShowdownEffect {
    private static final String ID = "Strength";
    private static final int PER_STRENGTH = 2;

    private static final String[] TEXT = getText(ID);

    public StrengthEffect(int amount) {
        super(ID, 5, amount, PER_STRENGTH);
    }

    @Override
    public void applyEffect(AbstractPlayer p, AbstractCard c) {
        if (active())
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, getFinalAmount()), getFinalAmount(), true));
    }

    @Override
    public String getDescription() {
        return TEXT[0] + getFinalAmount() + TEXT[1];
    }
}
