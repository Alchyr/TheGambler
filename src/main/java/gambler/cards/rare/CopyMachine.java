package gambler.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.cards.BaseCard;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class CopyMachine extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "CopyMachine",
            3,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 2;

    private static final int MAGIC = 3;

    public CopyMachine() {
        super(cardInfo, false);

        setCostUpgrade(UPG_COST);
        setMagic(MAGIC);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsAction(AbstractDungeon.player.masterDeck.group, this.magicNumber, cardStrings.EXTENDED_DESCRIPTION[0] + this.magicNumber + cardStrings.EXTENDED_DESCRIPTION[1], true, (c)->true,
                (cards)->{
                    for (AbstractCard c : cards) {
                        addToTop(new MakeTempCardInDrawPileAction(c, 1, true, true));
                    }
                }));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CopyMachine();
    }
}