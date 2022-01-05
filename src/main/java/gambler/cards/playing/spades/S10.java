package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.DexterityEffect;
import gambler.powers.S10Power;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S10 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S10",
            2,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 1;

    private static final int VALUE = 10;
    private static final Suit SUIT = Suit.SPADE;

    public S10() {
        super(cardInfo, false, VALUE, SUIT);

        setCostUpgrade(UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySelf(new S10Power(p, 1));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new DexterityEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new S10();
    }
}