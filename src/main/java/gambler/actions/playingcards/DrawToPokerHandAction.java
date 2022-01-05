package gambler.actions.playingcards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import gambler.PokerHand;
import gambler.cards.PlayingCard;

import static gambler.GamblerMod.makeID;

public class DrawToPokerHandAction extends AbstractGameAction {
    public static final String[] TEXT;
    private final AbstractPlayer player;

    public DrawToPokerHandAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.drawPile.isEmpty()) {
                this.isDone = true;
            }
            else if (PokerHand.cards.size() >= PokerHand.maxSize) {
                AbstractDungeon.effectList.add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, TEXT[1], true));
                this.isDone = true;
            }
            else {
                CardGroup validCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                    if (c instanceof PlayingCard)
                        validCards.addToTop(c);
                }

                if (validCards.isEmpty()) {
                    this.isDone = true;
                    return;
                }

                if (validCards.size() == 1) {
                    PlayingCard c = (PlayingCard) validCards.getBottomCard();

                    if (PokerHand.addCard(c)) {
                        c.unhover();
                        this.player.drawPile.removeCard(c);
                        c.flash(Color.BLUE.cpy());
                        PokerHand.setSmallScale(c);
                        //c.beginGlowing();
                    }
                    else {
                        c.flash(Color.RED.cpy());
                        this.player.drawPile.moveToDiscardPile(c);
                    }

                    this.isDone = true;
                } else {
                    validCards.sortAlphabetically(true);
                    validCards.sortByRarityPlusStatusCardType(false);
                    AbstractDungeon.gridSelectScreen.open(validCards, 1, TEXT[0], false);

                    this.tickDuration();
                }
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    if (c instanceof PlayingCard && PokerHand.addCard((PlayingCard) c)) {
                        this.player.drawPile.removeCard(c);
                        c.unhover();
                        c.flash(Color.BLUE.cpy());
                        PokerHand.setSmallScale((PlayingCard) c);
                        //c.beginGlowing();
                    }
                    else {
                        c.flash(Color.RED.cpy());
                        this.player.drawPile.moveToDiscardPile(c);
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString(makeID("DrawToPokerHand")).TEXT;
    }
}