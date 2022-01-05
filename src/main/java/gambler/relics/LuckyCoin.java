package gambler.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import gambler.cards.showdown.Showdown;

import static gambler.GamblerMod.makeID;

public class LuckyCoin extends BaseRelic {
    public static final String ID = makeID("LuckyCoin");
    private static final String IMG = "LuckyCoin";

    public LuckyCoin()
    {
        super(ID, IMG, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (c.cardID.equals(Showdown.ID) && (c.costForTurn == 0 || c.freeToPlay())) {
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LuckyCoin();
    }
}