package gambler.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static gambler.GamblerMod.makeID;

public class LotteryTicketPower extends BasePower {
    public static final String NAME = "LotteryTicketPower";
    public static final String POWER_ID = makeID(NAME);
    public static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = false;

    public LotteryTicketPower(final AbstractCreature owner, int amount)
    {
        super(NAME, TYPE, TURN_BASED, owner, owner, amount);

        if (this.amount > 100)
            this.amount = 100;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);

        if (this.amount > 100)
            this.amount = 100;
    }

    @Override
    public void onVictory() {
        if (AbstractDungeon.cardRandomRng.randomBoolean(amount / 100.0f)) {
            float tier = AbstractDungeon.cardRandomRng.random();

            AbstractRelic.RelicTier reward;
            if (tier > 0.8f) {
                reward = AbstractRelic.RelicTier.RARE;
            }
            else if (tier > 0.45f) {
                reward = AbstractRelic.RelicTier.UNCOMMON;
            }
            else {
                reward = AbstractRelic.RelicTier.COMMON;
            }
            AbstractDungeon.getCurrRoom().addRelicToRewards(reward);
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}