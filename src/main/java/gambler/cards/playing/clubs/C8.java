package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.DoublePoisonAction;
import com.megacrit.cardcrawl.actions.unique.TriplePoisonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.DexterityEffect;
import gambler.powers.Ante;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C8 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C8",
            1,
            CardType.SKILL,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int VALUE = 8;
    private static final Suit SUIT = Suit.CLUB;

    public C8() {
        super(cardInfo, true, VALUE, SUIT);

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            AbstractPower pow = m.getPower(Ante.POWER_ID);

            if (pow != null) {
                if (!this.upgraded) {
                    applySingle(m, new Ante(m, p, pow.amount));
                } else {
                    applySingle(m, new Ante(m, p, pow.amount * 2));
                }
            }
        }

        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new DexterityEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new C8();
    }
}