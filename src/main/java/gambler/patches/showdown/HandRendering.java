package gambler.patches.showdown;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import gambler.PokerHand;

public class HandRendering {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "render"
    )
    public static class NormalRendering
    {
        @SpirePostfixPatch
        public static void render(AbstractPlayer __instance, SpriteBatch sb)
        {
            PokerHand.render(sb);
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "renderHand"
    )
    public static class HoveredRendering
    {
        @SpirePostfixPatch
        public static void render(AbstractPlayer __instance, SpriteBatch sb)
        {
            PokerHand.renderHovered(sb);
        }
    }
}
