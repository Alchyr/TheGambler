package gambler.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import gambler.powers.interfaces.GoldChangePower;

import static gambler.GamblerMod.makeID;

public class JHPower extends BasePower implements GoldChangePower {
    public static final String NAME = "JHPower";
    public static final String POWER_ID = makeID(NAME);
    public static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = true;

    public JHPower(final AbstractCreature owner, int amount)
    {
        super(NAME, TYPE, TURN_BASED, owner, owner, amount);
    }

    @Override
    public void onGoldChange(int amt) {
        this.flash();
        addToTop(new GainBlockAction(this.owner, this.owner, this.amount));
    }


    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}