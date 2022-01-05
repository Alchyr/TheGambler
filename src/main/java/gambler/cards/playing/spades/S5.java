package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.StrengthEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S5 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S5",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 0;

    private static final int BLOCK = 5;
    private static final int VALUE = 5;
    private static final Suit SUIT = Suit.SPADE;

    public S5() {
        super(cardInfo, false, VALUE, SUIT);

        setCostUpgrade(UPG_COST);
        setBlock(BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new StrengthEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new S5();
    }
}