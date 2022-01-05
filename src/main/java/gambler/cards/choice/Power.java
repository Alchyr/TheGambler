package gambler.cards.choice;

import basemod.AutoAdd;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

@AutoAdd.Ignore
public class Power extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Power",
            -2,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.SPECIAL);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int VALUE = 13;
    private static final Suit SUIT = Suit.DIAMOND;

    public Power(int amt) {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(amt);
    }

    public void onChoseThisOption() {
        AbstractPlayer p = AbstractDungeon.player;
        this.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        this.addToTop(new VFXAction(p, new InflameEffect(p), 0.6F));
        this.addToTop(new VFXAction(new BorderLongFlashEffect(Color.FIREBRICK, true)));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new BorderLongFlashEffect(Color.FIREBRICK, true)));
        this.addToBot(new VFXAction(p, new InflameEffect(p), 0.6F));
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
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