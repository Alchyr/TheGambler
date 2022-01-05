package gambler.cards.playing.hearts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.VulnerableEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class H4 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "H4",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 4;
    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 4;
    private static final Suit SUIT = Suit.HEART;

    public H4() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; ++i)
            damageSingle(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new VulnerableEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new H4();
    }
}