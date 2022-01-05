package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S2 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S2",
            0,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int BLOCK = 2;
    private static final int UPG_BLOCK = 2;

    private static final int VALUE = 2;
    private static final Suit SUIT = Suit.SPADE;

    public S2() {
        super(cardInfo, false, VALUE, SUIT);

        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        drawCards(1);
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new WeakEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new S2();
    }
}