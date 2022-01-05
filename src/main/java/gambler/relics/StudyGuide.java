package gambler.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Girya;
import com.megacrit.cardcrawl.relics.PeacePipe;
import com.megacrit.cardcrawl.relics.Shovel;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import gambler.ui.StudyOption;

import java.util.ArrayList;

import static gambler.GamblerMod.makeID;

public class StudyGuide extends BaseRelic {
    public static final String ID = makeID("StudyGuide");
    private static final String IMG = "StudyGuide";

    public StudyGuide()
    {
        super(ID, IMG, RelicTier.RARE, LandingSound.FLAT);

        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        if (this.counter != 0) {
            this.flash();
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, this.counter), this.counter));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public boolean canSpawn() {
        if (AbstractDungeon.floorNum >= 48 && !Settings.isEndless) {
            return false;
        } else {
            int campfireRelicCount = 0;

            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if ((r instanceof PeacePipe) || (r instanceof Shovel) || (r instanceof Girya)) {
                    ++campfireRelicCount;

                    if (campfireRelicCount >= 2)
                        return false;
                }
            }
            return true;
        }
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        options.add(new StudyOption(this.counter < 3));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new StudyGuide();
    }
}