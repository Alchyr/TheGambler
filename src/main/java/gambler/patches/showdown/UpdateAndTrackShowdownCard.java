package gambler.patches.showdown;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gambler.PokerHand;
import javassist.CtBehavior;

public class UpdateAndTrackShowdownCard {
    public static float x = 0;
    public static float y = 0;

    @SpirePatch(
            clz = CardGroup.class,
            method = "update"
    )
    public static class Update
    {
        @SpirePostfixPatch
        public static void doTheUpdateThing(CardGroup __instance)
        {
            if (!AbstractDungeon.isScreenUp && PokerHand.showdown != null)
            {
                PokerHand.showdown.update();
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "applyPowers"
    )
    public static class ApplyPowers
    {
        @SpirePostfixPatch
        public static void apply(CardGroup __instance)
        {
            if (PokerHand.showdown != null)
                PokerHand.showdown.applyPowers();
        }
    }
    @SpirePatch(
            clz = CardGroup.class,
            method = "glowCheck"
    )
    public static class GlowCheck
    {
        @SpirePostfixPatch
        public static void apply(CardGroup __instance)
        {
            if (PokerHand.showdown != null)
            {
                glowCheck(PokerHand.showdown);
                PokerHand.showdown.triggerOnGlowCheck();
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "updateHoverLogic"
    )
    public static class UpdateHoverLogic
    {
        @SpirePostfixPatch
        public static void update(CardGroup __instance)
        {
            if (PokerHand.showdown != null)
                PokerHand.showdown.updateHoverLogic();
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "refreshHandLayout"
    )
    public static class RefreshLayout
    {
        /*@SpireInsertPatch(
                locator = HoverLocator.class
        )*/
        @SpirePostfixPatch
        public static void onRefreshLayout(CardGroup __instance)
        {
            if (PokerHand.showdown != null)
                setPosition(PokerHand.showdown);
        }

        private static class HoverLocator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredCard");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "canUseAnyCard"
    )
    public static class MaybeYouCan
    {
        @SpirePostfixPatch
        public static boolean maybe(boolean __result, CardGroup __instance)
        {
            return __result || (PokerHand.showdown != null && PokerHand.showdown.hasEnoughEnergy());
        }
    }


    public static void glowCheck(AbstractCard c)
    {
        if (c != null) {
            if (c.canUse(AbstractDungeon.player, null) && !AbstractDungeon.isScreenUp) {
                c.beginGlowing();
            } else {
                c.stopGlowing();
            }
        }
    }
    public static void setPosition(AbstractCard c)
    {
        c.targetDrawScale = 0.5f;
        c.targetAngle = 0;
        c.target_x = x;
        c.target_y = y;
    }
    public static void instantSetPosition(AbstractCard c)
    {
        c.targetDrawScale = 0.5f;
        c.targetAngle = 0;
        c.target_x = c.current_x = x;
        c.target_y = c.current_y = y;
        c.transparency = 0;
        c.targetTransparency = 1;
    }
}
