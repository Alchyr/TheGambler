package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.StrengthEffect;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S3 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S3",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 2;

    private static final int BLOCK = 4;
    private static final int UPG_BLOCK = 2;

    private static final int VALUE = 3;
    private static final Suit SUIT = Suit.SPADE;

    public S3() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE, UPG_DAMAGE);
        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        damageSingle(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new StrengthEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new S3();
    }
}