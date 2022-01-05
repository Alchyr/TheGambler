package gambler.cards.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.cards.BaseCard;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class Defend extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Defend",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.BASIC);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 3;


    public Defend() {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);

        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Defend();
    }
}