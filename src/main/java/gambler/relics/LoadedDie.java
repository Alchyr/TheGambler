package gambler.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import gambler.rewards.DieReward;

import static gambler.GamblerMod.makeID;

public class LoadedDie extends BaseRelic {
    public static final String ID = makeID("LoadedDie");
    private static final String IMG = "LoadedDie";

    public LoadedDie()
    {
        super(ID, IMG, RelicTier.BOSS, LandingSound.SOLID);

        this.tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[2]));
    }

    public void atBattleStart() {
        if (this.counter == 2) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new DrawCardAction(AbstractDungeon.player, 3));
        }
    }

    public void beforeEnergyPrep() {
        if (this.counter == 1) {
            this.beginLongPulse();
            this.flash();
            ++AbstractDungeon.player.energy.energyMaster;
        }
    }

    @Override
    public void onVictory() {
        AbstractDungeon.getCurrRoom().rewards.add(0, new DieReward(true));

        if (this.counter == 1 && this.pulse) {
            --AbstractDungeon.player.energy.energyMaster;
            this.stopPulse();
        }

        this.counter = -1;
    }

    private boolean waitingForUpgrade = false;
    public void listenForUpgrade() {
        waitingForUpgrade = true;
    }

    @Override
    public void update() {
        super.update();

        if (waitingForUpgrade && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            waitingForUpgrade = false;
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            c.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(c);
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
        }
    }


    @Override
    public boolean canSpawn()
    {
        // Only spawn if player has the starter relic
        return AbstractDungeon.player.hasRelic(FairDie.ID);
    }

    @Override
    public void obtain() {
        // Replace the starter relic, or just give the relic if starter isn't found
        for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
            if (AbstractDungeon.player.relics.get(i).relicId.equals(FairDie.ID)) {
                instantObtain(AbstractDungeon.player, i, true);
                return;
            }
        }
        super.obtain();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LoadedDie();
    }
}