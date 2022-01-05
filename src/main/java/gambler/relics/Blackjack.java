package gambler.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static gambler.GamblerMod.makeID;

public class Blackjack extends BaseRelic {
    public static final String ID = makeID("Blackjack");
    private static final String IMG = "Blackjack";
    private static final int COUNT = 21;

    public Blackjack()
    {
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.FLAT);

        this.counter = 0;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        ++this.counter;
        if (this.counter == COUNT) {
            this.counter = 0;
            this.flash();
            this.pulse = false;
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new GainEnergyAction(2));
        } else if (this.counter == COUNT - 1) {
            this.beginPulse();
            this.pulse = true;
        }

    }

    public void atBattleStart() {
        if (this.counter == COUNT - 1) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Blackjack();
    }
}