import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;
import java.util.Random;
import java.util.ArrayList;

public class Backend {
    private final String KEY = "0o1x/479ISUu4d37SBwmrA==GdMyXgTqq34QlvW2";
    public String createMultiline(String input, int lineLength) {
        int currentLineLength = lineLength;
        int[] insertLocals = new int[input.length()];
        int lastIndex = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                if (i > currentLineLength) {
                    insertLocals[lastIndex] = 1;
                    currentLineLength = lastIndex + currentLineLength;
                }
                lastIndex = i;
            }
        }
        String newString = "";
        for (int i = 0; i < input.length(); i++) {
            if (insertLocals[i] == 1) {
                newString += "\n";
            } else {
                newString += input.charAt(i);
            }
        }
        return newString;
    }

    public String makeString() {
        String astr = "";
        for (int i = 0; i < 6; i++) {
            astr += "word ";
        }
        return astr;
    }


    public String getWord(String[] array) {
        return array[(int) (Math.random() * array.length)];
    }

    public String parseSentenceBlueprint(String sentence) {
        String[] split = sentence.split("_");
        String formatted = "";
        for (String part: split) {
            switch (part) {
                case ",": formatted = formatted.substring(0, formatted.length() - 1) + ", "; break;
                case "SUBADJ": formatted += getWord(Constants.subjectsAdjectives) + " "; break;
                case "SUBJECT": formatted += getWord(Constants.subjects) + " "; break;
                case "MOVE": formatted += getWord(Constants.moveVerbs) + " "; break;
                case "ADVERB": formatted += getWord(Constants.adverbs) + " "; break;
                case "PLACEADJ": formatted += getWord(Constants.placeAdjectives) + " "; break;
                case "THINK": formatted += getWord(Constants.thinkVerbs) + " "; break;
                case "PLACE": formatted += getWord(Constants.places) + " "; break;
                case "FIGHT": formatted += getWord(Constants.fights) + " "; break;
                case "OBJECT": formatted += getWord(Constants.objects) + " "; break;
                case "OBJECTADJ": formatted += getWord(Constants.objectAdjectives) + " "; break;
                case "SPEAK": formatted += getWord(Constants.speaks) + " "; break;
                default: formatted += part + " "; break;
            }        
        }
        return " " + formatted.substring(0, 1).toUpperCase() + formatted.substring(1, formatted.length() - 1).toLowerCase() + ".";
    }

    public String createParagraph(int numSentences, Cap cap, double capPercentage) {
        Random rand = new Random();
        String paragraph = "";
        for (int i = 0; i < numSentences; i++) {
            int index = rand.nextInt(Constants.sentenceList.length);
            paragraph += filterCaps(parseSentenceBlueprint(Constants.sentenceList[index]), cap, capPercentage);
        }
        return paragraph.substring(1, paragraph.length());
    }

    public String createJumbleString(ArrayList<String> options, Cap cap, int numChar, double capPercentage) {
        String jumbleString = "";
        int lastSpace = 0;
        for (int i = 0; i < numChar; i++, lastSpace++) {
            jumbleString += options.get((int) (Math.random() * options.size()));
            if (lastSpace > 2) {
                int num = (int) (Math.random() * 5);
                jumbleString += (num == 2 ? " " : "");
                lastSpace = num == 2 ? 0 : lastSpace;
            }
        }
        jumbleString = filterCaps(jumbleString, cap, capPercentage);
        while (jumbleString.charAt(jumbleString.length() - 1) == ' ') {
            jumbleString = jumbleString.substring(0, jumbleString.length() - 1);
        }
        return jumbleString;
    }

    public static String filterCaps(String astr, Cap cap, double capPercentage) {
        switch (cap) {
            case ALL: return astr.toUpperCase();
            case NO: return astr.toLowerCase();
            case FIRST_LETTER:
                String temp = "";
                boolean space = false;
                for (int i = 0; i < astr.length(); i++) {
                    if (space) {
                        temp += astr.substring(i, i + 1).toUpperCase();
                    } else {
                        temp += astr.substring(i, i + 1).toLowerCase();
                    }
                    space = astr.charAt(i) == ' ' ? true : false;
                }
                return temp;
            case FIRST_WORD: 
                temp = "";
                space = false;
                boolean period = false;
                temp += (String.valueOf(astr.charAt(0))).toUpperCase();
                temp += (String.valueOf(astr.charAt(1))).toUpperCase();
                for (int i = 2; i < astr.length(); i++) {
                    temp += String.valueOf(astr.charAt(i)).toLowerCase();
                }
                return temp;
            case CUSTOM:
                temp = "";
                for (int i = 0; i < astr.length(); i++) {
                    temp += Math.random() * 100 > capPercentage ? String.valueOf(astr.charAt(i)).toLowerCase() : 
                        String.valueOf(astr.charAt(i)).toUpperCase();                
                }
                return temp;
            default: return " ";
        }
    }

    public static int countItem(ArrayList<String> alist, String s) {
        int count = 0;
        for (String astr : alist) {
            count = count + (astr.equals(s) ? 1 : 0);
        }
        return count;
    }

    public static ArrayList<String> multiplyArrayList(ArrayList<String> alist, int factor) {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < factor - 1; i++) {
            for (String astr: alist) {
                temp.add(astr);
            }
        }
        return temp;
    }
}