package gambler.cards.playing.hearts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.GamblerMod;
import gambler.actions.generic.ChangeGoldAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.GoldEffect;
import gambler.powers.JHPower;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class H11 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "H11",
            1,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 2;

    private static final int GOLD_COST = 10;

    private static final int VALUE = 11;
    private static final Suit SUIT = Suit.HEART;

    public H11() {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
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
        applySelf(new JHPower(p, this.magicNumber));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new GoldEffect(5);
    }

    @Override
    public AbstractCard makeCopy() {
        return new H11();
    }
}