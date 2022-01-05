package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.GoldEffect;
import gambler.powers.Ante;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C9 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C9",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 9;
    private static final int UPG_DAMAGE = 3;

    private static final int VALUE = 9;
    private static final Suit SUIT = Suit.CLUB;

    public C9() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE, UPG_DAMAGE);
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int originalBase = baseDamage;

        AbstractPower p = mo.getPower(Ante.POWER_ID);
        if (p != null) {
            baseDamage += p.amount;
        }

        super.calculateCardDamage(mo);

        baseDamage = originalBase;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new GoldEffect(5);
    }

    @Override
    public AbstractCard makeCopy() {
        return new C9();
    }
}