package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.actions.common.BetterDiscardPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.GamblerMod;
import gambler.actions.generic.ChangeGoldAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class D2 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D2",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int COST = 4;
    private static final int UPG_COST = -2;

    private static final int VALUE = 2;
    private static final Suit SUIT = Suit.DIAMOND;

    public D2() {
        super(cardInfo, false, VALUE, SUIT);

        setGoldCost(COST, UPG_COST);
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else {
            if (!GamblerMod.canAfford(this.goldCost)) {
                canUse = false;
                this.cantUseMessage = cantAffordText;
            }

            return canUse;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChangeGoldAction(-this.goldCost, this.hb));
        addToBot(new BetterDiscardPileToHandAction(1));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new WeakEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new D2();
    }
}