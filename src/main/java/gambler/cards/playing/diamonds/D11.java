package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.GamblerMod;
import gambler.actions.generic.ChangeGoldAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.GoldEffect;
import gambler.powers.JDPower;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class D11 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D11",
            1,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 0;

    private static final int GOLD_COST = 10;

    private static final int VALUE = 11;
    private static final Suit SUIT = Suit.DIAMOND;

    public D11() {
        super(cardInfo, false, VALUE, SUIT);

        setCostUpgrade(UPG_COST);
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
        applySelf(new JDPower(p, 1));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new GoldEffect(5);
    }

    @Override
    public AbstractCard makeCopy() {
        return new D11();
    }
}