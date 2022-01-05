package gambler.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import gambler.cards.PlayingCard;
import gambler.util.TextureLoader;

import static gambler.GamblerMod.makeImagePath;
import static gambler.cards.PlayingCard.TEXT_AREA_OFFSET;

public class SingleCardViewPlayingCards {
    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderCardBack"
    )
    public static class PlayingCardBack {
        @SpirePrefixPatch
        public static SpireReturn<?> playingCardBack(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof PlayingCard) {
                renderHelper(sb, ((PlayingCard) ___card).bg, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, 0, 0, PlayingCard.CARD_SCALE * 2.0f);
                renderHelper(sb, PlayingCard.TEXT_AREA, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, 0, -TEXT_AREA_OFFSET, 1.0f);

                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderPortrait"
    )
    public static class PlayingCardNoPortrait {
        @SpirePrefixPatch
        public static SpireReturn<?> no(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof PlayingCard) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderFrame"
    )
    public static class NoFrame {
        private static final TextureAtlas.AtlasRegion blank = new TextureAtlas.AtlasRegion(TextureLoader.getTexture(makeImagePath("blank.png")), 0, 0, 1, 1);

        @SpireInsertPatch(
                rloc = 91
        )
        public static void no(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card, @ByRef TextureAtlas.AtlasRegion[] ___tmpImg) {
            if (___card instanceof PlayingCard) {
                ___tmpImg[0] = blank;
            }
        }
    }

    private static void renderHelper(SpriteBatch sb, Texture img, float drawX, float drawY, float xOffset, float yOffset, float scale) {
        sb.draw(img, drawX - (float)img.getWidth() / 2.0F + xOffset, drawY - (float)img.getHeight() / 2.0F + yOffset, (float)img.getWidth() / 2.0F - xOffset, (float)img.getHeight() / 2.0F - yOffset, (float)img.getWidth(), (float)img.getHeight(), Settings.scale * scale, Settings.scale * scale, 0, 0, 0, img.getWidth(), img.getHeight(), false, false);
    }
}
