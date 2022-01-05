package gambler.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import gambler.rewards.DieReward;

import static gambler.GamblerMod.makeID;

public class FairDie extends BaseRelic {
    public static final String ID = makeID("Die");
    private static final String IMG = "Die";

    public FairDie()
    {
        super(ID, IMG, RelicTier.STARTER, LandingSound.SOLID);

        this.tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[2]));
    }

    @Override
    public void onVictory() {
        AbstractDungeon.getCurrRoom().rewards.add(0, new DieReward(false));

        this.counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FairDie();
    }
}