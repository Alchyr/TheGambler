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
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.ShowdownEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

@AutoAdd.Ignore
public class Safety extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Safety",
            -2,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.SPECIAL);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int VALUE = 12;
    private static final Suit SUIT = Suit.DIAMOND;

    public Safety(int amt) {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(amt);
    }

    public void onChoseThisOption() {
        AbstractPlayer p = AbstractDungeon.player;
        this.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        this.addToTop(new VFXAction(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.FOREST, ShockWaveEffect.ShockWaveType.ADDITIVE)));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.FOREST, ShockWaveEffect.ShockWaveType.ADDITIVE)));
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
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