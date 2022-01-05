package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.VulnerableEffect;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C6 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C6",
            2,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int BLOCK = 12;
    private static final int UPG_BLOCK = 4;

    private static final int VALUE = 6;
    private static final Suit SUIT = Suit.CLUB;

    public C6() {
        super(cardInfo, false, VALUE, SUIT);

        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect[] getShowdownEffects() {
        return new ShowdownEffect[] {
                new WeakEffect(1),
                new VulnerableEffect(1)
        };
    }

    @Override
    public AbstractCard makeCopy() {
        return new C6();
    }
}