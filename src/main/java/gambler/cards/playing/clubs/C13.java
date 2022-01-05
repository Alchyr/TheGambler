package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.DexterityEffect;
import gambler.powers.Ante;
import gambler.util.CardInfo;

import java.util.Iterator;

import static gambler.GamblerMod.makeID;

public class C13 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C13",
            1,
            CardType.SKILL,
            CardTarget.ALL_ENEMY,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 0;

    private static final int VALUE = 13;
    private static final Suit SUIT = Suit.CLUB;

    public C13() {
        super(cardInfo, false, VALUE, SUIT);

        setCostUpgrade(UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int e = 0;

        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped() && mo.hasPower(Ante.POWER_ID)) {
                e += 2;
            }
        }

        if (e > 0) {
            addToBot(new GainEnergyAction(e));
        }
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new DexterityEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new C13();
    }
}