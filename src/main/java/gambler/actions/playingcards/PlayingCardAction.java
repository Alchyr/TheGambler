package gambler.actions.playingcards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import gambler.PokerHand;
import gambler.cards.PlayingCard;

public class PlayingCardAction extends AbstractGameAction {
    private PlayingCard card;

    public PlayingCardAction(PlayingCard c)
    {
        this.source = null;
        this.card = c;
    }

    @Override
    public void update() {
        if (PokerHand.cards.contains(card)) {
            this.isDone = true;
            return;
        }
        if (PokerHand.addCard(card)) {
            card.flash(Color.BLUE.cpy());
            card.resetAttributes();
            //card.beginGlowing();
            AbstractDungeon.player.limbo.removeCard(card); //ensure it doesn't stay in limbo if it comes from limbo
        }
        else {
            card.flash(Color.RED.cpy());
            AbstractDungeon.player.limbo.removeCard(card);
            /*if (card.exhaust || card.exhaustOnUseOnce) {
                AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
                AbstractDungeon.player.limbo.moveToExhaustPile(card);
            }
            else {
                AbstractDungeon.player.limbo.removeCard(card);
            }*/
        }
        this.isDone = true;
    }
}
