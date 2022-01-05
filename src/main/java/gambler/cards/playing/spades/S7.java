package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.DexterityEffect;
import gambler.util.CardInfo;

import java.util.Iterator;

import static gambler.GamblerMod.makeID;

public class S7 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S7",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int BLOCK = 3;
    private static final int UPG_BLOCK = 1;

    private static final int VALUE = 7;
    private static final Suit SUIT = Suit.SPADE;

    public S7() {
        super(cardInfo, false, VALUE, SUIT);

        setBlock(BLOCK, UPG_BLOCK);
    }

    public void applyPowers() {
        super.applyPowers();
        int count = 0;

        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == CardType.SKILL) {
                ++count;
            }
        }

        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0] + count;
        if (count == 1) {
            this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[1];
        } else {
            this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[2];
        }

        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int blockAmt = this.block;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                int count = 0;

                for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                    if (c.type == CardType.SKILL) {
                        ++count;
                    }
                }
                --count;

                for (int i = 0; i < count; ++i) {
                    addToTop(new GainBlockAction(p, p, blockAmt));
                }
            }
        });

        addToBot(new PlayingCardAction(this));
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new DexterityEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new S7();
    }
}