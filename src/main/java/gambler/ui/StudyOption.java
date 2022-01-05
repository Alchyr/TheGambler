package gambler.ui;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import gambler.effects.CampfireStudyEffect;
import gambler.util.TextureLoader;

import static gambler.GamblerMod.makeID;
import static gambler.GamblerMod.makeImagePath;

public class StudyOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Study"));
    public static final String[] TEXT = uiStrings.TEXT;

    public StudyOption(boolean active) {
        this.label = TEXT[0];
        this.usable = active;
        this.description = active ? TEXT[1] : TEXT[2];
        this.img = TextureLoader.getTexture(makeImagePath("ui/Study.png"));
    }

    @Override
    public void useOption() {
        if (this.usable) {
            AbstractDungeon.effectList.add(new CampfireStudyEffect());
        }
    }
}
