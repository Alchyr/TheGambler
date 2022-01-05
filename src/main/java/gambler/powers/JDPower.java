package gambler.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import gambler.powers.interfaces.CostChangePower;

import static gambler.GamblerMod.makeID;

public class JDPower extends BasePower implements CostChangePower {
    public static final String NAME = "JDPower";
    public static final String POWER_ID = makeID(NAME);
    public static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = false;

    public JDPower(final AbstractCreature owner, int amount)
    {
        super(NAME, TYPE, TURN_BASED, owner, owner, amount);
    }

    private int getPercent() {
        return (int) (100 / Math.pow(2, amount));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + getPercent() + DESCRIPTIONS[1];
    }

    @Override
    public int modifyCost(int goldCost) {
        return (int) (goldCost / Math.pow(2, amount));
    }
}