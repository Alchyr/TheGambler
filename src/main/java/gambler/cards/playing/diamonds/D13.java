package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import gambler.PokerHand;
import gambler.actions.generic.FatalDamageAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class D13 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D13",
            3,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 26;
    private static final int UPG_DAMAGE = 8;

    private static final int VALUE = 13;
    private static final Suit SUIT = Suit.DIAMOND;

    public D13() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE, UPG_DAMAGE);
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new VFXAction(new GoldenSlashEffect(m.hb.cX, m.hb.cY, true)));
            addToBot(new FatalDamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, (mo)->{
                if (PokerHand.showdown != null) {
                    if (PokerHand.showdown.baseDamage > 0) {
                        int amt = PokerHand.showdown.damage;

                        if (amt > 0) {
                            AbstractDungeon.player.gainGold(amt);

                            for(int i = 0; i < amt; ++i) {// 39
                                AbstractDungeon.effectList.add(new GainPennyEffect(p, m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY, true));
                            }
                        }
                    }
                }
            }));
        }
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return null;
    }

    @Override
    public AbstractCard makeCopy() {
        return new D13();
    }
}