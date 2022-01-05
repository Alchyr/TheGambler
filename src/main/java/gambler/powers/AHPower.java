package gambler.powers;

import basemod.interfaces.XCostModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static gambler.GamblerMod.makeID;

public class AHPower extends BasePower implements XCostModifier {
    public static final String NAME = "AHPower";
    public static final String POWER_ID = makeID(NAME);
    public static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = false;

    public AHPower(final AbstractCreature owner, int amount)
    {
        super(NAME, TYPE, TURN_BASED, owner, owner, amount);
    }


    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public int modifyX(AbstractCard c) {
        return this.amount;
    }
}