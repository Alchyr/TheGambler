package gambler.cards.playing;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.PlayingCard;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class RedJoker extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "RedJoker",
            2,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.BASIC);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 13;
    private static final int UPG_DAMAGE = 4;

    private static final int VALUE = 0;
    private static final Suit SUIT = Suit.ANY;

    public RedJoker() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    protected String getSuitName() {
        return "R.png";
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.FIRE);
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public AbstractCard makeCopy() {
        return new RedJoker();
    }
}