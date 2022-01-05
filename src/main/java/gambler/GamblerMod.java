package gambler;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomReward;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import gambler.cards.BaseCard;
import gambler.cards.PlayingCard;
import gambler.character.Gambler;
import gambler.relics.BaseRelic;
import gambler.rewards.DieReward;
import gambler.util.KeywordWithProper;
import gambler.util.TextureLoader;
import gambler.variables.GoldCost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static gambler.character.Gambler.Enums.GAMBLER;
import static gambler.character.Gambler.Enums.GAMBLER_CARD_COLOR;

@SpireInitializer
public class GamblerMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        PreStartGameSubscriber,
        PostBattleSubscriber,
        OnStartBattleSubscriber
{

    public static final String modID = "gambler";

    //In-game mod info.
    private static final String MODNAME = "MODNAME";
    private static final String AUTHOR = "Alchyr";
    private static final String DESCRIPTION = "in-game description";

    public static final Logger logger = LogManager.getLogger("Gambler");

    //Character Color
    public static final Color GAMBLER_COLOR = CardHelper.getColor(48.0f, 196.0f, 70.0f);

    //Card Assets
    private static final String ATTACK_BACK = makeCardPath("assets/bg_attack.png");
    private static final String SKILL_BACK = makeCardPath("assets/bg_skill.png");
    private static final String POWER_BACK = makeCardPath("assets/bg_power.png");

    private static final String ATTACK_BACK_PORTRAIT = makeCardPath("assets/bg_attack_p.png");
    private static final String SKILL_BACK_PORTRAIT = makeCardPath("assets/bg_skill_p.png");
    private static final String POWER_BACK_PORTRAIT = makeCardPath("assets/bg_power_p.png");

    private static final String ENERGY_ORB = makeCardPath("assets/energy_orb.png");
    private static final String ENERGY_ORB_PORTRAIT = makeCardPath("assets/energy_orb_p.png");
    private static final String CARD_SMALL_ORB = makeCardPath("assets/small_orb.png");

    //Character assets
    private static final String CHARACTER_BUTTON = makeCharacterPath("button.png");
    private static final String CHARACTER_PORTRAIT = makeCharacterPath("portrait.png");

    //Badge
    public static final String BADGE_IMAGE = makeImagePath("badge.png");

    // Atlas and JSON files for the Animations
    //public static final String SKELETON_ATLAS = makeCharacterPath("alice.atlas");

    //public static final String STAND_JSON = makeCharacterPath("alice_stand.json");
    //public static final String ATTACK_JSON = makeCharacterPath("alice_attack.json");

    //makePaths
    public static String makePath(String resourcePath) {
        return modID + "/" + resourcePath;
    }
    public static String makeImagePath(String resourcePath) {
        return modID + "/images/" + resourcePath;
    }
    public static String makeRelicPath(String resourcePath) {
        return modID + "/images/relics/" + resourcePath;
    }
    public static String makePowerPath(String resourcePath) {
        return modID + "/images/powers/" + resourcePath;
    }
    public static String makeLargePowerPath(String resourcePath) {
        return modID + "/images/powers/big/" + resourcePath;
    }
    public static String makeCardPath(String resourcePath) {
        return modID + "/images/cards/" + resourcePath;
    }
    public static String    makeCharacterPath(String resourcePath) {
        return modID + "/images/character/" + resourcePath;
    }
    public static String makeEffectPath(String resourcePath) {
        return modID + "/images/effects/" + resourcePath;
    }
    public static String makeOrbPath(String resourcePath) {
        return modID + "/images/orb/" + resourcePath;
    }
    public static String makeLocalizationPath(String resourcePath) {
        return modID + "/localization/" + resourcePath;
    }

    public static String makeID(String id)
    {
        return modID + ":" + id;
    }


    public static boolean canAfford(int cost) {
        return AbstractDungeon.player.gold >= cost;
    }


    @SuppressWarnings("unused")
    public static void initialize() {
        new GamblerMod();
    }

    public GamblerMod()
    {
        BaseMod.subscribe(this);

        logger.info("Creating The Gambler's card color.");
        BaseMod.addColor(GAMBLER_CARD_COLOR, GAMBLER_COLOR, GAMBLER_COLOR, GAMBLER_COLOR,
                GAMBLER_COLOR, GAMBLER_COLOR, GAMBLER_COLOR, GAMBLER_COLOR,
                ATTACK_BACK, SKILL_BACK, POWER_BACK, ENERGY_ORB,
                ATTACK_BACK_PORTRAIT, SKILL_BACK_PORTRAIT, POWER_BACK_PORTRAIT,
                ENERGY_ORB_PORTRAIT, CARD_SMALL_ORB);

        logger.info("Color created!");
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new Gambler(),
                CHARACTER_BUTTON, CHARACTER_PORTRAIT, GAMBLER);
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.registerCustomReward(DieReward.RewardType.DIE_REWARD,
                (rewardSave) -> new DieReward(rewardSave.amount),
                (customReward) -> {
                    if (customReward instanceof DieReward) {
                        return new RewardSave(customReward.type.toString(), null, ((DieReward) customReward).reward, 0);
                    }
                    return null;
                });

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, null);

        logger.info("Done loading badge Image and mod options");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        //BaseMod.addRelicToCustomPool(new OccultBall(), SUMIREKO_CARD_COLOR);
        new AutoAdd(modID)
                .packageFilter(BaseRelic.class)
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelicToCustomPool(relic, GAMBLER_CARD_COLOR);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });

        logger.info("Done adding relics!");
    }

    @Override
    public void receiveEditCards() {
        //add variables
        BaseMod.addDynamicVariable(new GoldCost());

        //add cards
        new AutoAdd(modID)
                .packageFilter(BaseCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditStrings()
    {
        String lang = "eng";

        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocalizationPath(lang + "/RelicStrings.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizationPath(lang + "/CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocalizationPath(lang + "/CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizationPath(lang + "/PowerStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocalizationPath(lang + "/UIStrings.json"));

        lang = getLangString();
        if (lang.equals("eng")) return;

        try
        {
            BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocalizationPath(lang + "/RelicStrings.json"));
            BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizationPath(lang + "/CardStrings.json"));
            BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocalizationPath(lang + "/CharacterStrings.json"));
            BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizationPath(lang + "/PowerStrings.json"));
            BaseMod.loadCustomStringsFile(UIStrings.class, makeLocalizationPath(lang + "/UIStrings.json"));
        }
        catch (Exception e)
        {
            logger.error("Failed to load other language strings. ");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void receiveEditKeywords()
    {
        String lang = getLangString();

        try
        {
            Gson gson = new Gson();
            String json = Gdx.files.internal(makeLocalizationPath(lang + "/Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            KeywordWithProper[] keywords = gson.fromJson(json, KeywordWithProper[].class);

            if (keywords != null) {
                for (KeywordWithProper keyword : keywords) {
                    /*switch (keyword.ID)
                    {
                        case "occult":
                            //may need editing to function properly for other languages
                            KeywordWithProper.occult = modID + ":" + keyword.NAMES[0] + " NL ";
                            break;
                        case "fragile":
                            KeywordWithProper.fragile = " NL " + modID + ":" + keyword.NAMES[0];
                            break;
                    }*/
                    BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
        catch (Exception e)
        {
            Gson gson = new Gson();
            String json = Gdx.files.internal(makeLocalizationPath("eng/Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            KeywordWithProper[] keywords = gson.fromJson(json, KeywordWithProper[].class);

            if (keywords != null) {
                for (KeywordWithProper keyword : keywords) {
                    /*switch (keyword.ID)
                    {
                        case "occult":
                            //may need editing to function properly for other languages
                            KeywordWithProper.occult = modID + ":" + keyword.NAMES[0] + " NL ";
                            break;
                        case "fragile":
                            KeywordWithProper.fragile = " NL " + modID + ":" + keyword.NAMES[0];
                            break;
                    }*/
                    BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
    }

    private String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }



    private boolean hasPlayingCards(AbstractPlayer p)
    {
        return p instanceof Gambler || p.masterDeck.group.stream().anyMatch((c)->c instanceof PlayingCard);
    }


    //Stuff that actually happens during gameplay
    @Override
    public void receivePreStartGame() {
        PokerHand.reset();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        PokerHand.reset();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        PokerHand.setPositionForCombat(); //Sets positions based on where the player is.
        if (hasPlayingCards(AbstractDungeon.player))
        {
            PokerHand.enabled = true;
        }
    }
}