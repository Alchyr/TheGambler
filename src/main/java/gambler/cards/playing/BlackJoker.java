package gambler.cards.playing;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.PlayingCard;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class BlackJoker extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "BlackJoker",
            2,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.BASIC);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int BLOCK = 13;
    private static final int UPG_BLOCK = 4;

    private static final int VALUE = 0;
    private static final Suit SUIT = Suit.ANY;

    public BlackJoker() {
        super(cardInfo, false, VALUE, SUIT);

        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    protected String getSuitName() {
        return "B.png";
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackJoker();
    }
}