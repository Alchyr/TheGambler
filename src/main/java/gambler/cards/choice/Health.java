package gambler.cards.choice;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.ShowdownEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

@AutoAdd.Ignore
public class Health extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Health",
            -2,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.SPECIAL);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int VALUE = 11;
    private static final Suit SUIT = Suit.DIAMOND;

    public Health(int amt) {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(amt);
    }

    public void onChoseThisOption() {
        AbstractPlayer p = AbstractDungeon.player;
        this.addToTop(new HealAction(p, p, this.magicNumber));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new HealAction(p, p, this.magicNumber));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return null;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Madness();
    }
}