package gambler.patches.showdown;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.PokerHand;
import javassist.CtBehavior;

import static gambler.util.MathHelper.dist;

public class PlayShowdown {
    private static final float DROP_DIST = 200 * Settings.scale;

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "updateInput"
    )
    public static class ModifyDropZone
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void modify(AbstractPlayer __instance)
        {
            if (__instance.hoveredCard != null && __instance.hoveredCard.equals(PokerHand.showdown))
            {
                if (dist(InputHelper.mX, HoverShowdown.hoveredX) < DROP_DIST && dist(InputHelper.mY, HoverShowdown.hoveredY) < DROP_DIST)
                {
                    __instance.isHoveringDropZone = false;
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isDraggingCard");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard"
    )
    public static class RemoveCard
    {
        @SpirePostfixPatch
        public static void remove(AbstractPlayer __instance, AbstractCard c, AbstractMonster m, int e)
        {
            if (c.equals(PokerHand.showdown))
            {
                PokerHand.showdown = null;
                //Remove cards from poker hand
            }
        }
    }
}
