package gambler.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import gambler.actions.playingcards.HandChangeAction;

import static gambler.GamblerMod.makeID;

public class D10Power extends BasePower {
    public static final String NAME = "D10Power";
    public static final String POWER_ID = makeID(NAME);
    public static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = false;

    public D10Power(final AbstractCreature owner, int amount)
    {
        super(NAME, TYPE, TURN_BASED, owner, owner, amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        addToBot(new HandChangeAction(this.amount, false));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + (amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
    }
}