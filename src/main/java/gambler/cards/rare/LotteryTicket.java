package gambler.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import gambler.actions.generic.XCostAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.BaseCard;
import gambler.powers.AHPower;
import gambler.powers.LotteryTicketPower;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class LotteryTicket extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "LotteryTicket",
            -1,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int GOLD_FACTOR = 12;
    private static final int UPG_GOLD_FACTOR = 10;

    public LotteryTicket() {
        super(cardInfo, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, (amt, params)->{
            int buff = (int) (100 * (amt / (float) (params[0])));

            addToTop(new ApplyPowerAction(p, p, new LotteryTicketPower(p, buff), buff, true));
            return true;
        }, upgraded ? UPG_GOLD_FACTOR : GOLD_FACTOR));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LotteryTicket();
    }
}