package gambler.cards.showdown.effects;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import gambler.cards.showdown.ShowdownEffect;

public class EnergyEffect extends ShowdownEffect {
    private static final String ID = "Energy";

    private static final String[] TEXT = getText(ID);

    public EnergyEffect(int amount) {
        super(ID, 40, amount, 1);
    }

    @Override
    public void applyEffect(AbstractPlayer p, AbstractCard c) {
        if (active())
            addToBot(new GainEnergyAction(getFinalAmount()));
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder(TEXT[0]);

        if (TEXT[1].equals("")) {
            for (int i = 0; i < getFinalAmount(); ++i)
                sb.append(TEXT[2]);
        }
        else {
            sb.append(TEXT[1]);
            sb.append(getFinalAmount());
        }

        sb.append(TEXT[3]);

        return sb.toString();
    }
}