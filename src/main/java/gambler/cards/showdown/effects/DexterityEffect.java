package gambler.cards.showdown.effects;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.DexterityPower;
import gambler.cards.showdown.ShowdownEffect;

public class DexterityEffect extends ShowdownEffect {
    private static final String ID = "Dexterity";
    private static final int PER = 2;

    private static final String[] TEXT = getText(ID);

    public DexterityEffect(int amount) {
        super(ID, 6, amount, PER);
    }

    @Override
    public void applyEffect(AbstractPlayer p, AbstractCard c) {
        if (active())
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, getFinalAmount()), getFinalAmount(), true));
    }

    @Override
    public String getDescription() {
        return TEXT[0] + getFinalAmount() + TEXT[1];
    }
}
