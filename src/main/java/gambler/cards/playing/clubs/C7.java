package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.DexterityEffect;
import gambler.powers.Ante;
import gambler.util.CardInfo;

import java.util.Iterator;

import static gambler.GamblerMod.makeID;

public class C7 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C7",
            1,
            CardType.SKILL,
            CardTarget.ALL_ENEMY,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = 2;

    private static final int VALUE = 7;
    private static final Suit SUIT = Suit.CLUB;

    public C7() {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            applySingle(mo, new Ante(mo, p, this.magicNumber), true);
        }
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new DexterityEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new C7();
    }
}