package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.VulnerableEffect;
import gambler.powers.Ante;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C5 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C5",
            2,
            CardType.ATTACK,
            CardTarget.ALL_ENEMY,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 2;

    private static final int MAGIC = 5;
    private static final int UPG_MAGIC = 2;

    private static final int VALUE = 5;
    private static final Suit SUIT = Suit.CLUB;

    public C5() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                if (target != null) {
                    C5.this.calculateCardDamage((AbstractMonster)target);
                    addToTop(new ApplyPowerAction(this.target, AbstractDungeon.player, new Ante(this.target, AbstractDungeon.player, C5.this.magicNumber), C5.this.magicNumber, true));
                    addToTop(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player, C5.this.damage, C5.this.damageTypeForTurn), AttackEffect.SMASH));
                }

                this.isDone = true;
            }
        });
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                if (target != null) {
                    C5.this.calculateCardDamage((AbstractMonster)target);
                    addToTop(new ApplyPowerAction(this.target, AbstractDungeon.player, new Ante(this.target, AbstractDungeon.player, C5.this.magicNumber), C5.this.magicNumber, true));
                    addToTop(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player, C5.this.damage, C5.this.damageTypeForTurn), AttackEffect.SMASH));
                }

                this.isDone = true;
            }
        });
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new VulnerableEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new C5();
    }
}