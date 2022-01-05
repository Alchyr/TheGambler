package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.optionCards.BecomeAlmighty;
import com.megacrit.cardcrawl.cards.optionCards.FameAndFortune;
import com.megacrit.cardcrawl.cards.optionCards.LiveForever;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.GamblerMod;
import gambler.actions.generic.ChangeGoldAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.choice.Health;
import gambler.cards.choice.Power;
import gambler.cards.choice.Safety;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.util.CardInfo;

import java.util.ArrayList;
import java.util.Iterator;

import static gambler.GamblerMod.makeID;

public class D1 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D1",
            0,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int GOLD_COST = 15;

    private static final int STRENGTH = 2;
    private static final int UPG_STRENGTH = 1;

    private static final int DEX = 1;
    private static final int UPG_DEX = 1;

    private static final int HEAL = 7;
    private static final int UPG_HEAL = 4;

    private static final int VALUE = 1;
    private static final Suit SUIT = Suit.DIAMOND;

    public D1() {
        super(cardInfo, false, VALUE, SUIT);

        setGoldCost(GOLD_COST);

        setDamage(STRENGTH, UPG_STRENGTH);
        setBlock(DEX, UPG_DEX);
        setMagic(HEAL, UPG_HEAL);

        this.tags.add(CardTags.HEALING);
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else {
            if (!GamblerMod.canAfford(this.goldCost)) {
                canUse = false;
                this.cantUseMessage = cantAffordText;
            }

            return canUse;
        }
    }

    public void applyPowers() {
    }

    public void calculateCardDamage(AbstractMonster mo) {
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChangeGoldAction(-this.goldCost, this.hb));

        ArrayList<AbstractCard> choices = new ArrayList<>();

        choices.add(new Power(this.isDamageModified ? this.damage : this.baseDamage));
        choices.add(new Safety(this.isBlockModified ? this.block : this.baseBlock));
        choices.add(new Health(this.isMagicNumberModified ? this.magicNumber : this.baseMagicNumber));

        this.addToBot(new ChooseOneAction(choices));

        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return null;
    }

    @Override
    public AbstractCard makeCopy() {
        return new D1();
    }
}