package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import gambler.actions.generic.XCostAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.EnergyEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C12 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C12",
            -1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int BLOCK = 0;
    private static final int VALUE = 12;
    private static final Suit SUIT = Suit.CLUB;

    private static final int GOLD_FACTOR = 20;
    private static final int UPG_GOLD_FACTOR = 16;

    public C12() {
        super(cardInfo, true, VALUE, SUIT);

        setBlock(BLOCK);
    }

    @Override
    public void applyPowers() {
        int x = EnergyPanel.totalCount;
        x = applyXModifiers(x);

        this.block = this.baseBlock = (int) (AbstractDungeon.player.gold * (x / (float) (upgraded ? UPG_GOLD_FACTOR : GOLD_FACTOR)));

        super.applyPowers();

        this.rawDescription = upgraded ? cardStrings.EXTENDED_DESCRIPTION[1] : cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }
    public void justApply() {
        super.applyPowers();
        this.rawDescription = upgraded ? cardStrings.EXTENDED_DESCRIPTION[1] : cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, (amt, params)->{
            C12.this.block = C12.this.baseBlock = (int) (AbstractDungeon.player.gold * (amt / (float) (upgraded ? UPG_GOLD_FACTOR : GOLD_FACTOR)));
            C12.this.justApply();

            if (C12.this.block > 2)
                addToTop(new GainBlockAction(p, p, C12.this.block));
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
        return new C12();
    }
}