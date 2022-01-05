package gambler.actions.playingcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.StrangeSpoon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import gambler.PokerHand;
import gambler.cards.PlayingCard;
import gambler.powers.interfaces.PostShowdownPower;

public class ClearPokerHandAction extends AbstractGameAction {
    public ClearPokerHandAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        for (PlayingCard c : PokerHand.cards) {
            if (c.purgeOnUse) {
                c.target_y += 30.0F * Settings.scale;
                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                c.exhaustOnUseOnce = false;
                c.dontTriggerOnUseCard = false;
                continue;
            }

            if (c.type == AbstractCard.CardType.POWER) {
                AbstractDungeon.player.limbo.empower(c);
                c.exhaustOnUseOnce = false;
                c.dontTriggerOnUseCard = false;
                continue;
            }

            boolean spoonProc = false;
            if ((c.exhaust || c.exhaustOnUseOnce) && AbstractDungeon.player.hasRelic(StrangeSpoon.ID)) {
                spoonProc = AbstractDungeon.cardRandomRng.randomBoolean();
            }

            if ((c.exhaust || c.exhaustOnUseOnce) && !spoonProc) {
                AbstractDungeon.player.limbo.moveToExhaustPile(c);
                CardCrawlGame.dungeon.checkForPactAchievement();
            } else {
                if (spoonProc) {
                    AbstractDungeon.player.getRelic(StrangeSpoon.ID).flash();
                }

                if (c.shuffleBackIntoDrawPile) {
                    AbstractDungeon.player.limbo.moveToDeck(c, true);
                } else if (c.returnToHand) {
                    AbstractDungeon.player.limbo.moveToHand(c);
                    AbstractDungeon.player.onCardDrawOrDiscard();
                } else {
                    AbstractDungeon.player.limbo.moveToDiscardPile(c);
                }
            }

            c.exhaustOnUseOnce = false;
            c.dontTriggerOnUseCard = false;
        }

        PokerHand.cards.clear();

        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof PostShowdownPower) {
                ((PostShowdownPower) p).postShowdown();
            }
        }

        this.isDone = true;
    }
}
