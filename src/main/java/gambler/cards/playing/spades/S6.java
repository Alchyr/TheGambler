package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.VulnerableEffect;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S6 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S6",
            2,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 6;
    private static final Suit SUIT = Suit.SPADE;

    public S6() {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        drawCards(this.magicNumber, new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard c : DrawCardAction.drawnCards) {
                    if (c.costForTurn > 0) {
                        c.setCostForTurn(c.costForTurn - 1);
                    }
                }
                this.isDone = true;
            }
        });
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect[] getShowdownEffects() {
        return new ShowdownEffect[] {
                new WeakEffect(1),
                new VulnerableEffect(1)
        };
    }

    @Override
    public AbstractCard makeCopy() {
        return new S6();
    }
}