package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S4 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S4",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 3;

    private static final int VALUE = 4;
    private static final Suit SUIT = Suit.SPADE;

    public S4() {
        super(cardInfo, false, VALUE, SUIT);

        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int amt = Math.max(0, EnergyPanel.getCurrentEnergy());

                if (amt > 0) {
                    addToTop(new DrawCardAction(amt));
                }
                this.isDone = true;
            }
        });
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new WeakEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new S4();
    }
}