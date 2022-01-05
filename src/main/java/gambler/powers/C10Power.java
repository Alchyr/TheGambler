package gambler.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static gambler.GamblerMod.makeID;

public class C10Power extends BasePower implements BetterOnApplyPowerPower {
    public static final String NAME = "C10Power";
    public static final String POWER_ID = makeID(NAME);
    public static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = false;

    public C10Power(final AbstractCreature owner, int amount)
    {
        super(NAME, TYPE, TURN_BASED, owner, owner, amount);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public boolean betterOnApplyPower(AbstractPower p, AbstractCreature source, AbstractCreature target) {
        if (p.ID.equals(Ante.POWER_ID)) {
            this.flash();
            p.amount += this.amount;
        }
        return true;
    }
    @Override
    public int betterOnApplyPowerStacks(AbstractPower p, AbstractCreature target, AbstractCreature source, int stackAmount) {
        return p.ID.equals(Ante.POWER_ID) ? stackAmount + this.amount : stackAmount;
    }
}