package gambler.patches.showdown;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gambler.PokerHand;
import javassist.CtBehavior;

public class ShowdownEnergyColor {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderEnergy"
    )
    public static class RenderAltColor
    {
        private static final Color RED = new Color(1.0f, 0.3f, 0.3f, 1.0f);

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = { "costColor" }
        )
        public static void modifyColor(AbstractCard __instance, SpriteBatch sb, @ByRef Color[] costColor)
        {
            if (AbstractDungeon.player != null)
            {
                if (__instance == PokerHand.showdown && !__instance.hasEnoughEnergy()) {
                    costColor[0] = RED;
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "transparency");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
