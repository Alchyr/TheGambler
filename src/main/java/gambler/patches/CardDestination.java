package gambler.patches;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import gambler.PokerHand;
import gambler.cards.PlayingCard;
import javassist.CtBehavior;

import static gambler.GamblerMod.makeID;

public class CardDestination {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("PlayingCardUse"));

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class NoMovePlayingCards
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<?> noMove(UseCardAction __instance, AbstractCard ___targetCard, @ByRef float[] ___duration)
        {
            if (___targetCard instanceof PlayingCard)
            {
                if (PokerHand.cards.contains(___targetCard) || PokerHand.cards.size() < PokerHand.maxSize) //already added/not full
                {
                    ___duration[0] = ___duration[0] - Gdx.graphics.getDeltaTime();

                    AbstractDungeon.player.hand.removeCard(___targetCard);
                    AbstractDungeon.player.limbo.removeCard(___targetCard);

                    AbstractDungeon.player.cardInUse = null;
                    //___targetCard.exhaustOnUseOnce = false;
                    ___targetCard.dontTriggerOnUseCard = false;
                    AbstractDungeon.actionManager.addToBottom(new HandCheckAction());

                    return SpireReturn.Return(null);
                }
                else
                {
                    //No popup message, poker cards have their own independent effects so playing them is reasonable.

                    //popup message
                    //AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, uiStrings.TEXT[0], true));
                }
            }
            return SpireReturn.Continue();
        }


        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "purgeOnUse");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}