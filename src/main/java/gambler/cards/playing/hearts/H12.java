package gambler.cards.playing.hearts;

import basemod.Pair;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import gambler.GamblerMod;
import gambler.actions.generic.ChangeGoldAction;
import gambler.actions.generic.XCostAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.playing.clubs.C12;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.EnergyEffect;
import gambler.util.CardInfo;

import java.util.ArrayList;
import java.util.List;

import static gambler.GamblerMod.makeID;

public class H12 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "H12",
            -1,
            CardType.ATTACK,
            CardTarget.ALL_ENEMY,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 0;
    private static final int VALUE = 12;

    private static final Suit SUIT = Suit.HEART;

    private static final int GOLD_COST = 20;

    private static final int GOLD_FACTOR = 15;
    private static final int UPG_GOLD_FACTOR = 12;

    public H12() {
        super(cardInfo, true, VALUE, SUIT);

        setDamage(DAMAGE);
        setGoldCost(GOLD_COST);

        this.isMultiDamage = true;
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

    @Override
    public void applyPowers() {
        int x = EnergyPanel.totalCount;
        x = applyXModifiers(x);

        this.damage = this.baseDamage = (int) (AbstractDungeon.player.gold * (x / (float) (upgraded ? UPG_GOLD_FACTOR : GOLD_FACTOR)));

        super.applyPowers();

        this.rawDescription = upgraded ? cardStrings.EXTENDED_DESCRIPTION[1] : cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChangeGoldAction(-this.goldCost, this.hb));

        addToBot(new XCostAction(this, (amt, params)->{
            H12.this.damage = H12.this.baseDamage = (int) (AbstractDungeon.player.gold * (amt / (float) (upgraded ? UPG_GOLD_FACTOR : GOLD_FACTOR)));
            H12.this.calculateCardDamage(m);

            if (H12.this.damage > 0) {
                addToTop(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));

                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (!mo.isDeadOrEscaped()) {
                        AbstractDungeon.effectsQueue.add(new GoldenSlashEffect(mo.hb.cX, mo.hb.cY, false));
                    }
                }
            }
            return true;
        }));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public void onMoveToDiscard() {
        super.onMoveToDiscard();
        this.rawDescription = upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new EnergyEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new H12();
    }
}