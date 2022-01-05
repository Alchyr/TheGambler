package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.GamblerMod;
import gambler.actions.generic.ChangeGoldAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.GoldEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C11 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C11",
            0,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int BLOCK = 20;
    private static final int UPG_BLOCK = 5;
    private static final int GOLD_COST = 15;

    private static final int VALUE = 11;
    private static final Suit SUIT = Suit.CLUB;

    public C11() {
        super(cardInfo, false, VALUE, SUIT);

        setBlock(BLOCK, UPG_BLOCK);
        setGoldCost(GOLD_COST);
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
        block();
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new GoldEffect(5);
    }

    @Override
    public AbstractCard makeCopy() {
        return new C11();
    }
}