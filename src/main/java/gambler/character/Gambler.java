package gambler.character;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.Strawberry;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import gambler.cards.basic.Defend;
import gambler.cards.basic.Strike;
import gambler.cards.playing.BlackJoker;
import gambler.cards.playing.RedJoker;
import gambler.cards.playing.clubs.C2;
import gambler.cards.playing.diamonds.D2;
import gambler.cards.playing.hearts.H2;
import gambler.cards.playing.spades.S2;
import gambler.effects.DropGoldEffect;
import gambler.relics.FairDie;
import gambler.util.SpriteAnimation;
import gambler.util.TextureLoader;

import java.util.ArrayList;

import static gambler.GamblerMod.*;

public class Gambler extends CustomPlayer {
    //enums
    public static class Enums
    {
        @SpireEnum
        public static AbstractPlayer.PlayerClass GAMBLER;
        @SpireEnum(name = "GAMBLER_COLOR")
        public static AbstractCard.CardColor GAMBLER_CARD_COLOR;
        @SpireEnum(name = "GAMBLER_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    //stats
    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 70;
    public static final int MAX_HP = 70;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    //strings
    private static final String ID = makeID("Gambler");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    private static final String SHOULDER_1 = makeCharacterPath("shoulder.png");
    private static final String SHOULDER_2 = makeCharacterPath("shoulder2.png");
    private static final String CORPSE = makeCharacterPath("corpse.png");

    private static final String spriterFile = makeCharacterPath("spriter/Character.scml");

    //private static String ATLAS = makeCharacterPath("sumirenko.atlas");
    //private static String JSON = makeCharacterPath("sumirenko.json");

    private static final String orbVfx = makeOrbPath("vfx.png");

    public Gambler()
    {
        super(NAMES[0], Enums.GAMBLER, null, null, null, new SpriterAnimation(spriterFile));

        initializeClass(null,
                SHOULDER_1,
                SHOULDER_2,
                CORPSE,
                getLoadout(),
                20.0F, -20.0F, 200.0F, 250.0F,
                new EnergyManager(ENERGY_PER_TURN));

        //loadAnimation(ATLAS, JSON, 1.0f);

        //this.stateData.setMix("idle", "attackUp", 0.2f);
        //this.stateData.setMix("attackUp", "idle", 0.4f);

        //this.state.setAnimation(0, "idle", true);

        /*changeAnimation(stand, "stand");

        //load attack animation ahead of time
        loadAnimation(attack, "attack"); //if loop is false it be dumb*/

        dialogX = (drawX + 0.0F * Settings.scale); //location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale);
    }

    /*@Override
    protected void updateFastAttackAnimation() {
        if (animation.equals(attack))
        {
            if (animationMap.get(attack).trackEntry.isComplete())
            {
                changeAnimation(stand, "stand");
                this.animationTimer = 0.0f;
            }
        }
        else
        {
            changeAnimation(stand, "stand");
            this.animationTimer = 0.0f;
        }
    }*/


    /*@Override
    public void useFastAttackAnimation() {
        super.useFastAttackAnimation();
        changeAnimation(attack, "attack");
    }*/

    @Override
    public void playDeathAnimation() {
        super.playDeathAnimation();

        //AbstractDungeon.deathScreen
        for (int i = 0; i < 50; ++i) {
            AbstractDungeon.topLevelEffects.add(new DropGoldEffect(this.hb.cX + MathUtils.random(-5, 5), this.hb.cY + MathUtils.random(-5, 5)));
        }
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        /*retVal.add(C2.ID);
        retVal.add(C2.ID);
        retVal.add(S2.ID);
        retVal.add(S2.ID);
        retVal.add(H2.ID);
        retVal.add(H2.ID);
        retVal.add(D2.ID);
        retVal.add(D2.ID);*/
        retVal.add(RedJoker.ID);
        retVal.add(BlackJoker.ID);

        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(FairDie.ID);
        UnlockTracker.markRelicAsSeen(FairDie.ID);

        return retVal;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA(getCustomModeCharacterButtonSoundKey(), 1.25f);
        CardCrawlGame.sound.playA("ATTACK_MAGIC_SLOW_1", -0.2f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT,
                false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return MathUtils.randomBoolean() ? "ATTACK_MAGIC_FAST_1" : "ATTACK_MAGIC_FAST_2";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.GAMBLER_CARD_COLOR;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }
    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAMES[1];
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new BlackJoker();
    }

    @Override
    public Color getCardTrailColor() {
        return GAMBLER_COLOR.cpy();
    }

    @Override
    public Color getCardRenderColor() {
        return GAMBLER_COLOR.cpy();
    }

    @Override
    public Color getSlashAttackColor() {
        return GAMBLER_COLOR.cpy();
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_VERTICAL
        };
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Gambler();
    }


    private static boolean isDisposed(TextureAtlas atlas)
    {
        for (Texture t : atlas.getTextures())
        {
            if (t.getTextureObjectHandle() == 0)
                return true;
        }
        return false;
    }
}
