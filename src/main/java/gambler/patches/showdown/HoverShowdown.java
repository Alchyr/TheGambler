package gambler.patches.showdown;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gambler.PokerHand;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "updateInput"
)
public class HoverShowdown {
    public static float hoveredX = 0, hoveredY = 0;

    @SpireInsertPatch(
            locator = FirstLocator.class
    )
    public static void GetHoverOtherHand(AbstractPlayer __instance)
    {
        if (__instance.hoveredCard == null)
        {
            if (PokerHand.showdown != null)
            {
                PokerHand.showdown.hb.update();
                if (PokerHand.showdown.hb.hovered)
                {
                    for (CardQueueItem q : AbstractDungeon.actionManager.cardQueue)
                    {
                        if (q.card == PokerHand.showdown)
                            return;
                    }
                    __instance.hoveredCard = PokerHand.showdown;
                }
            }
        }
    }

    @SpireInsertPatch(
            locator = AngleLocator.class
    )
    public static void reposition(AbstractPlayer __instance)
    {
        if (__instance.hoveredCard.equals(PokerHand.showdown))
        {
            __instance.hoveredCard.current_x = hoveredX;
            __instance.hoveredCard.target_x = hoveredX;
            __instance.hoveredCard.current_y = hoveredY;
            __instance.hoveredCard.target_y = hoveredY;
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "hoverCardPush"
    )
    public static class NoPush
    {
        @SpirePrefixPatch
        public static SpireReturn<?> no(CardGroup __instance, AbstractCard c)
        {
            if (c.equals(PokerHand.showdown))
            {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    private static class FirstLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            ArrayList<Matcher> requiredMatches = new ArrayList<>();
            requiredMatches.add(new Matcher.MethodCallMatcher(CardGroup.class, "getHoveredCard"));
            requiredMatches.add(new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredCard"));

            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredCard");

            return LineFinder.findInOrder(ctMethodToPatch, requiredMatches, finalMatcher);
        }
    }
    private static class AngleLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "setAngle");

            int[] result = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[] { result[0], result[1] };
        }
    }
}
