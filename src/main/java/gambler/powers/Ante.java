package gambler.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import gambler.cards.showdown.Showdown;

import static gambler.GamblerMod.makeID;

public class Ante extends BasePower implements HealthBarRenderPower {
    public static final String NAME = "Ante";
    public static final String POWER_ID = makeID(NAME);
    public static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    public static final boolean TURN_BASED = false;

    private static final Color c = new Color(17.0f/255.0f, 128.0f/255.0f, 13.0f/255.0f, 1.0f);

    public Ante(final AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(NAME, TYPE, TURN_BASED, owner, source, amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.cardID.equals(Showdown.ID)) {
            this.flash();
            this.addToBot(new LoseHPAction(this.owner, source, this.amount));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public int getHealthBarAmount() {
        return amount;
    }

    @Override
    public Color getColor() {
        return c;
    }
}