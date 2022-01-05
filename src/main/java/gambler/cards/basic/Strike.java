package gambler.cards.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.cards.BaseCard;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class Strike extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Strike",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.BASIC);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;


    public Strike() {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);

        tags.add(CardTags.STRIKE);
        tags.add(CardTags.STARTER_STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Strike();
    }
}