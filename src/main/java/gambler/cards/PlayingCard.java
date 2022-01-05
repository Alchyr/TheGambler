package gambler.cards;

import basemod.ReflectionHacks;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import gambler.cards.showdown.ShowdownEffect;
import gambler.util.CardInfo;
import gambler.util.TextureLoader;

import java.util.Collections;
import java.util.List;

import static gambler.GamblerMod.makeCardPath;
import static gambler.GamblerMod.makeID;
import static gambler.character.Gambler.Enums.GAMBLER_CARD_COLOR;

//self note: Title looks kinda gross. Make replacement title banner texture thingy?

public abstract class PlayingCard extends BaseCard {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID("PlayingCard")).TEXT;
    protected static String cantAffordText = TEXT[3];
    public static final float CARD_SCALE = 1.333f;

    public static final Texture TEXT_AREA = TextureLoader.getTexture(makeCardPath("playing/TEXT_AREA.png"));
    public static final float TEXT_AREA_OFFSET = 250.0f * Settings.scale;

    public boolean publicHovered; //hovered is private and this just makes it easier rather than using reflection.

    public int value; //jokers are 0, then 1-13 for the rest
    public Suit suit;

    public enum Suit {
        SPADE,
        CLUB,
        HEART,
        DIAMOND,
        ANY //jokers
    }

    public final Texture bg;
    protected final Color renderColor;

    public PlayingCard(CardInfo cardInfo, boolean upgradesDescription, int value, Suit suit) {
        super(cardInfo, upgradesDescription, GAMBLER_CARD_COLOR);

        this.value = value;
        this.suit = suit;

        renderColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor");

        bg = TextureLoader.getTexture(makeCardPath("playing/" + getValueName() + getSuitName()));
    }

    private static final List<String> playingCardDescriptor = Collections.singletonList(TEXT[0]);

    @Override
    public List<String> getCardDescriptors() {
        return playingCardDescriptor;
    }

    private static final List<TooltipInfo> playingCardTooltip = Collections.singletonList(new TooltipInfo(TEXT[1], TEXT[2]));

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return playingCardTooltip;
    }

    public ShowdownEffect[] getShowdownEffects() {
        return new ShowdownEffect[] { getShowdownEffect() };
    }
    public ShowdownEffect getShowdownEffect() {
        return null;
    }

    @Override
    public void hover() {
        super.hover();
        this.publicHovered = true;
    }

    @Override
    public void unhover() {
        super.unhover();
        this.publicHovered = false;
    }

    @Override
    public int compareTo(AbstractCard o) {
        if (o instanceof PlayingCard) {
            int diff = Integer.compare(value, ((PlayingCard) o).value);
            if (diff == 0) {
                return compareSuit(suit, ((PlayingCard) o).suit);
            }
            else {
                return diff;
            }
        }
        else {
            return super.compareTo(o);
        }
    }

    public static int compareSuit(Suit a, Suit b) {
        return Integer.compare(suitValue(a), suitValue(b));
    }
    //arbitrary ordering for the sake of sorting
    private static int suitValue(Suit s) {
        switch (s) {
            case SPADE:
                return 0;
            case CLUB:
                return 1;
            case HEART:
                return 2;
            case DIAMOND:
                return 3;
            default:
                return -1;
        }
    }

    @SpireOverride
    public void renderCardBg(SpriteBatch sb, float x, float y) {
        renderHelper(sb, this.renderColor, bg, x, y, 0, 0, CARD_SCALE);
        renderHelper(sb, this.renderColor, TEXT_AREA, x, y, 0, -TEXT_AREA_OFFSET, 0.5f);
    }
    @SpireOverride
    public void renderPortrait(SpriteBatch sb) {

    }
    @SpireOverride
    public void renderAttackPortrait(SpriteBatch sb, float x, float y) {

    }
    @SpireOverride
    public void renderSkillPortrait(SpriteBatch sb, float x, float y) {

    }
    @SpireOverride
    public void renderPowerPortrait(SpriteBatch sb, float x, float y) {

    }
    private void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY, float xOffset, float yOffset, float scale) {
        sb.setColor(color);
        sb.draw(img, drawX - (float)img.getWidth() / 2.0F + xOffset, drawY - (float)img.getHeight() / 2.0F + yOffset, (float)img.getWidth() / 2.0F - xOffset, (float)img.getHeight() / 2.0F - yOffset, (float)img.getWidth(), (float)img.getHeight(), this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle, 0, 0, img.getWidth(), img.getHeight(), false, false);
    }

    @Override
    public TextureAtlas.AtlasRegion getCardBgAtlas() {
        return ImageMaster.CARD_POWER_BG_SILHOUETTE;
    }


    protected String getValueName() {
        return Integer.toString(value);
    }
    protected String getSuitName() {
        switch (suit) {
            case CLUB:
                return "C.png";
            case HEART:
                return "H.png";
            case DIAMOND:
                return "D.png";
            default:
                return "S.png";
        }
    }
}
