import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String[] sentenceList = new String[]{
        "THE_SUBADJ_SUBJECT_MOVE_TO_THE_ADVERB_PLACEADJ_PLACE",
        "THE_ADVERB_SUBADJ_SUBJECT_THINK_THE_PLACEADJ_PLACE",
        "THE_SUBJECT_MOVE_TO_THE_PLACE_AFTER_THE_SUBADJ_SUBJECT",
        "THE_ADVERB_SUBADJ_SUBJECT_FIGHT_THE_SUBJECT_IN_THE_PLACE",
        "THE_SUBJECT_MOVE_WHILE_THE_ADVERB_SUBADJ_SUBJECT_THINK_THE_PLACEADJ_PLACE",
        "THE_SUBJECT_THINK_THE_OBJECTADJ_OBJECT,_AND_A_SUBJECT_MOVE_TO_THE_ADVERB_PLACEADJ_PLACE",
        "THE_SUBJECT_FIGHT_WITH_THE_SUBADJ_SUBJECT_OVER_THE_OBJECTADJ_OBJECT",
        "THE_PLACE_WHERE_THE_ADVERB_SUBADJ_SUBJECT_THINK_THE_OBJECT_IS_PLACEADJ",
        "THE_ADVERB_SUBADJ_SUBJECT_FIGHT_A_SUBJECT_BECAUSE_THE_SUBJECT_MOVE_TO_THE_PLACEADJ_PLACE", 
        "THE_SUBADJ_SUBJECT_SPEAK_TO_THE_SUBADJ_SUBJECT_ABOUT_THE_ADVERB_OBJECTADJ_OBJECT",
        "THE_SUBADJ_SUBJECT_SPEAK_ABOUT_THE_OBJECTADJ_OBJECT_BUT_THE_SUBJECT_MOVE_TO_THE_PLACEADJ_PLACE",
        "THE_ADVERB_OBJECTADJ_OBJECT_IS_OBJECTADJ_,_AND_A_SUBJECT_THINK_IT",
        "THE_SUBJECT_MOVE_TO_THE_PLACEADJ_PLACE_WITH_THE_ADVERB_OBJECTADJ_OBJECT",
    };
    public static final String[] originalOptions = new String[]{"a","b","c","d","e","f","g","h","i","j","k",
        "l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    public static final String[] numsArray = new String[]{"1","2","3","4","5","6","7","8","9","0"};
    public static final ArrayList<String> numsList = new ArrayList<>(List.of(numsArray));

    public static final String[] puncArray = new String[]{",", ".", ":", ";", "?", "'", "\"", "!", "-"};
    public static final ArrayList<String> puncList = new ArrayList<>(List.of(puncArray));

    public static final String[] symArray = new String[]{"@","#","$","%","^","&","*","(",")", "`", "~", "_", "+", "=", "{", 
                                                            "}", "[", "]", "|", "\\", "/", "<", ">"};
    public static final ArrayList<String> symList = new ArrayList<>(List.of(symArray));

    public static final String[] arrowArray = new String[]{"↓", "→", "←", "↑"};
    public static final ArrayList<String> arrowList = new ArrayList<>(List.of(arrowArray));

    public static final String allKeys = new String("qwertyuiopasdfghjklzxcvbnm1234567890,.:;?\'\"!-/<>[]{}\\|`~@#$%^&*()_+=↓→←↑");

    public static final String[] subjects = new String[]{"boy", "cat", "dog", "penguin", "madman", "goblin", "predator", "zebra", "velociraptor", "walrus", "basketball player", "yeti monster", "megladon", "philosopher", "philantrophist", "soldier", "computer programmar", "gravedigger"};
    public static final String[] places = new String[]{"barn", "school", "observatory", "tower", "command center", "battlefield", "space station", "active volcano", "glacier"};
    public static final String[] moveVerbs = new String[]{"runs", "travels", "adventures", "navigates", "maneuvers", "cruises", "ziplines", "darts", "teleports", "time travels"};
    public static final String[] thinkVerbs = new String[]{"evaluates", "contemplates", "analyzes", "investigates", "inspects", "observes", "scrutinizes"};
    public static final String[] subjectsAdjectives = new String[]{"cowardly", "brave", "funny", "stupid", "unimaginative", "zesty", "built", "jacked", "lonely", "zombielike"};
    public static final String[] placeAdjectives = new String[]{"treacherous", "vibrant", "dreamlike", "bustling", "uninhabitable", "grotesque", "abominable"};
    public static final String[] adverbs = new String[]{"seriously", "horribly", "uncomfortably", "incredibly", "slightly", "outrageously"};
    public static final String[] fights = new String[]{"boomerangs", "knocks out", "slaps", "powerbombs", "moons", "pummels", "mogs", "haymakers", "assaults", "thrashes"};
    public static final String[] objects = new String[]{"rubix cube", "dumbell", "xylophone", "waterbed", "quesodilla", "vehicle", "lava lamp"};
    public static final String[] objectAdjectives = new String[]{"broken", "expensive", "unique", "amazing", "priceless", "unorthodox", "typical", "beloved"};
    public static final String[] speaks = new String[]{"speaks", "yells", "screams", "converses", "sings", "announces"};

    public static final ArrayList<String> fiftyLetters = Backend.multiplyArrayList(new ArrayList<String>(List.of(originalOptions)), 50);
    public static final ArrayList<String> hundredLetters = Backend.multiplyArrayList(new ArrayList<String>(List.of(originalOptions)), 100);
    public static final ArrayList<String> fiftyNumbers = Backend.multiplyArrayList(numsList, 50);
    public static final ArrayList<String> hundredNumbers = Backend.multiplyArrayList(numsList, 100);
    public static final ArrayList<String> fiftypunc = Backend.multiplyArrayList(puncList, 50);
    public static final ArrayList<String> hundredpunc = Backend.multiplyArrayList(puncList, 100);
    public static final ArrayList<String> fiftySymbols = Backend.multiplyArrayList(symList, 50);
    public static final ArrayList<String> hundredSymbols = Backend.multiplyArrayList(symList, 100);
    public static final ArrayList<String> fiftyArrows = Backend.multiplyArrayList(arrowList, 50);
    public static final ArrayList<String> hundredArrows = Backend.multiplyArrayList(arrowList, 100);
    public static final String[] buchanonColors = new String[]{"#17A6DF", "#25ED09", "#E56C0D", "#ffffff", "#ffffff", "#000000", "buchanon.jpg"};
    public static final String[] cbumColors = new String[]{"#606060", "#d4af37", "#c92617", "#000000", "#ffffff", "#000000", "cbum.jpg"};
    public static final String[] ronnieColors = new String[]{"#c92617", "#F1B510", "#248E5B", "#ffffff", "#ffffff", "#000000", "coleman.jpg"};
    public static final String[] arnoldColors = new String[]{"#d4af37", "#A4CB0E", "#B24CF7", "#000000", "#ffffff", "#000000", "arnold.jpg"};

}


