package gambler.cards.playing.hearts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import gambler.actions.generic.XCostAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.StrengthEffect;
import gambler.powers.Ante;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class H13 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "H13",
            -1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int VALUE = 13;
    private static final Suit SUIT = Suit.HEART;

    public H13() {
        super(cardInfo, true, VALUE, SUIT);
    }

    public void applyPowers()
    {
        super.applyPowers();
        this.rawDescription = upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = this.damage = 0;

        AbstractPower p = mo.getPower(Ante.POWER_ID);
        if (p != null) {
            this.baseDamage += p.amount;
        }
        super.calculateCardDamage(mo);

        this.rawDescription = upgraded ? cardStrings.EXTENDED_DESCRIPTION[1] : cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, (amt, params)-> {
            amt += params[1];
            if (damage > 0 && amt > 0 && m != null) {
                for (int i = 0; i < amt; ++i) {
                    addToTop(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
                }
            }

            return true;
        }, this.damage, upgraded ? 1 : 0));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new StrengthEffect(1/2);
    }

    @Override
    public AbstractCard makeCopy() {
        return new H13();
    }
}