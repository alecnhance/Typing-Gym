import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import java.util.Arrays;
import java.util.ArrayList;

public class Test extends Application {
    private Backend backend = new Backend();
    private StackPane[] textList = new StackPane[1000];
    private int cursor = 0;
    private boolean finished = false;
    private VBox totalWordBox;
    private double types = 0;
    private double wrongs = 0;
    private int greens = 0;
    private int reds = 0;
    private ArrayList<Integer> colorHolder = new ArrayList<>();
    private ArrayList<Integer> mistakes = new ArrayList<>();
    private Label wpmData;
    private Label trueAccData;
    private Label highScoreData;
    private int best = 0;
    private long start;
    private double time;
    private boolean started;
    private int lastSpace = -1;
    private VBox leftside;
    private ArrayList<String> options = Backend.multiplyArrayList(new ArrayList<String>(Arrays.asList(Constants.originalOptions)), 50);
    /*private ArrayList<String> letterOptions = Backend.multiplyArrayList(new ArrayList<String>(Arrays.asList(Constants.originalOptions)), 50);
    private ArrayList<String> numberOptions = new ArrayList<String>();
    private ArrayList<String> symbolOptions = new ArrayList<String>();
    private ArrayList<String> arrowOptions = new ArrayList<String>();
    private ArrayList<String> puncOptions = new ArrayList<String>();*/
    private Cap cap = Cap.NO;
    private boolean jumbleNums = false;
    private boolean jumbleSymbols = false;
    private boolean jumblePunc = false;
    private boolean jumbleArrow = false;
    private boolean advancedPage = false;
    private boolean usingBasic = true;
    private double capPercentage = 0;
    private boolean isJumble = true;
    private VBox[] boxes = new VBox[72];
    private int sentenceCount = 2;
    private int jumbleCharCount = 100;
    private Color typingTextColor = Color.rgb(0, 0, 0);
    private Color titleTextColor = Color.rgb(0, 179, 255);
    private Color typingBoxColor = Color.rgb(149, 157, 161);
    private Color correctTypeColor = Color.rgb(0, 179, 255);
    private Color fixedTypeColor = Color.rgb(220,250,250);
    private Color wrongTypeColor = Color.rgb(129,25,189);
    private Image backgroundImage = new Image("buchanon.jpg");

    public void start(Stage primaryStage) {
        StackPane mainPane = new StackPane();
        mainPane.getChildren().addAll(createBackgroundImage(), makeBP(primaryStage));
        Scene mainScene = new Scene(mainPane, 1000, 1000);
        mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(mainScene);
        primaryStage.show();
        setUpKeyEvent();
    }

    public ImageView createBackgroundImage() {
        ImageView iv1 = new ImageView();
        iv1.setImage(backgroundImage);
        iv1.setFitWidth(1500);
        iv1.setPreserveRatio(true);
        iv1.setSmooth(true);
        return iv1;
    }

    public void setUpKeyEvent() {
        textList[0].setOnKeyTyped(e -> {
            if (!started) {
                started = true;
                start = System.nanoTime();
            }
            if (finished) {
            } else if (e.getCharacter().equals("")) {
                if (cursor > 0) {
                    ((Text)textList[--cursor].getChildren().get(0))
                    .setFill(((Text)textList[cursor].getChildren().get(0)).getText().equals("␣") ? null : typingTextColor);
                    if (colorHolder.get(cursor) == 1) {
                        colorHolder.remove(colorHolder.size() - 1);
                        greens--;
                    } else {
                        colorHolder.remove(colorHolder.size() - 1);
                        reds--;
                    }
                }
            } else if(textList[cursor] == null) {
            } else if (e.getCharacter().equals(" ")) {
                types++;
                if (((Text)textList[cursor].getChildren().get(0)).getText().equals("␣")) {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? fixedTypeColor : null);
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(wrongTypeColor);
                    colorHolder.add(0);
                    mistakes.add(cursor);
                    wrongs++;
                    reds++;
                }
            } else if (e.getCharacter().equals(((Text)textList[cursor].getChildren().get(0)).getText())
                        && !e.getCharacter().equals("␣")) {
                types++;
                //textList[cursor++].setStyle(mistakes.contains(cursor) ? yellow : green);
                ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? fixedTypeColor : correctTypeColor);
                colorHolder.add(1);
                greens++;
            } else {
                types++;
                ((Text)textList[cursor++].getChildren().get(0)).setFill(wrongTypeColor);
                colorHolder.add(0);
                mistakes.add(cursor);
                wrongs++;
                reds++;
            }
            if (textList[cursor] == null) {
                if (reds == 0 && !finished) {
                    finished = true;
                    time = (System.nanoTime() - start) / ((Math.pow(10, 9)) * 78.0);
                    int tempWPM = calculateWPM(cursor, time);
                    wpmData.setText(Integer.toString(tempWPM));
                    trueAccData.setText(String.format("%d%%", Math.round((1 - wrongs / types) * 100)));
                    if (tempWPM > best) {
                        best = tempWPM;
                    }
                    highScoreData.setText(String.format("%d", best));
                    //((Text)textList[lastSpace].getChildren().get(0)).setFill(null);
                }
            }
        });
        textList[0].setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.UP)) {
                if (((Text)textList[cursor].getChildren().get(0)).getText().equals("↑")) {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? fixedTypeColor : correctTypeColor);
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(wrongTypeColor);
                    colorHolder.add(0);
                    mistakes.add(cursor);
                    wrongs++;
                    reds++;
                }
            } else if (e.getCode().equals(KeyCode.DOWN)) {
                if (((Text)textList[cursor].getChildren().get(0)).getText().equals("↓")) {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? fixedTypeColor : correctTypeColor);
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(wrongTypeColor);
                    colorHolder.add(0);
                    mistakes.add(cursor);
                    wrongs++;
                    reds++;
                }
            } else if (e.getCode().equals(KeyCode.RIGHT)) {
                if (((Text)textList[cursor].getChildren().get(0)).getText().equals("→")) {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? fixedTypeColor : correctTypeColor);
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(wrongTypeColor);
                    colorHolder.add(0);
                    mistakes.add(cursor);
                    wrongs++;
                    reds++;
                }
            } else if (e.getCode().equals(KeyCode.LEFT)) {
                if (((Text)textList[cursor].getChildren().get(0)).getText().equals("←")) {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? fixedTypeColor : correctTypeColor);
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(wrongTypeColor);
                    colorHolder.add(0);
                    mistakes.add(cursor);
                    wrongs++;
                    reds++;
                }
            }
        });
        textList[0].requestFocus();
    }

    

    public FlowPane makeWords(String astr) {
        astr = astr + " ";
        int i = 0;
        FlowPane flowpane = new FlowPane(1, 1);
        while (i < astr.length()) {
            HBox hbox = new HBox(1);
            while(astr.charAt(i) != ' ') {
                StackPane pane = new StackPane();
                Text letter = new Text(astr.substring(i,i+1));
                letter.setFont(Font.font("Times New Roman", 24));
                pane.getChildren().add(letter);
                textList[i] = pane;
                hbox.getChildren().add(textList[i++]);
            }
            if (i < astr.length() - 1) {
                Text text = new Text("␣");
                text.setFont(Font.font("Times New Roman", 24));
                text.setFill(null);
                StackPane pane1 = new StackPane();
                pane1.getChildren().add(text);
                textList[i] = pane1;
                hbox.getChildren().add(textList[i++]);
                //flowpane.getChildren().add(pane1);
            } else {
                i++;
            }
            flowpane.getChildren().add(hbox);
        }
        return flowpane;
    }

    public BorderPane makeBP(Stage primaryStage) {
        BorderPane border = new BorderPane();
        makeTotalWordBox();
        VBox centerScreen = new VBox(makeTopBar(), totalWordBox);
        centerScreen.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(centerScreen.getChildren().get(0), new Insets(0, 0, 10, 0));
        border.setCenter(centerScreen);
        Label text = new Label("Typing Gym");
        text.setFont(Font.font("Times New Roman", 35));
        text.setStyle("-fx-text-fill: cyan");
        border.setTop(text);
        BorderPane.setAlignment(text, Pos.TOP_CENTER);
        BorderPane.setAlignment(centerScreen, Pos.TOP_CENTER);
        BorderPane.setMargin(text, new Insets(25, 0, 0, 0));
        BorderPane.setMargin(centerScreen, new Insets(25, 0, 0, 0));
        border.setLeft(leftside(primaryStage));
        border.setRight(rightside());
        return border;
    }

    public void makeTotalWordBox() {
        totalWordBox = new VBox();
        FlowPane flowpane = makeWords(isJumble ? backend.createJumbleString(options, cap, jumbleCharCount, capPercentage) 
                                                : backend.createParagraph(sentenceCount, cap, capPercentage));
        flowpane.setMaxWidth(1078);
        flowpane.setMaxHeight(1);
        HBox hbox = new HBox();
        hbox.getChildren().add(makeRedo());
        hbox.setAlignment(Pos.CENTER);
        totalWordBox.getChildren().add(flowpane);
        totalWordBox.getChildren().add(hbox);
        totalWordBox.setStyle("-fx-background-color: D3D3D3");
        totalWordBox.setAlignment(Pos.CENTER);
        VBox.setMargin(flowpane, new Insets(10, 0, 15, 15));
        VBox.setMargin(hbox, new Insets(5, 0, 15, 0));
        totalWordBox.setMaxHeight(200);
        totalWordBox.setMaxWidth(2000);
    }

    public Button makeRedo() {
        Button redoButton = new Button("Redo");
        redoButton.setFont(Font.font("Times New Roman", 24));
        /*DropShadow shad = new DropShadow(2, Color.BLACK);
        redoButton.setStyle("-fx-border-color: black");
        redoButton.setEffect(shad);*/
        redoButton.setStyle("-fx-border-color: black; -fx-background-color: transparent");
        redoButton.setFocusTraversable(false);
        redoButton.setOnAction(e -> {
            /*for (int i = 0; i < cursor; i++) {
                textList[i] = null;
            }*/
            updateWords();
            resetData();
            setUpKeyEvent();
        });
        return redoButton;
    }

    public void resetData() {
        cursor = 0;
        finished = false;
        started = false;
        reds = 0;
        types = 0;
        wrongs = 0;
        greens = 0;
        colorHolder.clear();
        mistakes.clear();
        wpmData.setText("  ");
        trueAccData.setText("   ");
    }

    public FlowPane makeTopBar() {
        Label wpm = new Label("wpm:");
        wpmData = new Label("N/A");
        wpm.setId("wpm");
        Label trueAcc = new Label("true accuracy:");
        trueAccData = new Label("N/A");
        Label highScore = new Label("best:");
        highScoreData = new Label("N/A");
        FlowPane flowpane = new FlowPane(wpm, wpmData, trueAcc, trueAccData, highScore, highScoreData);
        wpm.setFont(Font.font("Times New Roman", 20));
        wpmData.setFont(Font.font("Times New Roman", 20));
        trueAcc.setFont(Font.font("Times New Roman", 20));
        trueAccData.setFont(Font.font("Times New Roman", 20));
        highScore.setFont(Font.font("Times New Roman", 20));
        highScoreData.setFont(Font.font("Times New Roman", 20));
        FlowPane.setMargin(wpm, new Insets(0, 5, 0, 0));
        FlowPane.setMargin(trueAcc, new Insets(0, 5, 0, 15));
        FlowPane.setMargin(highScore, new Insets(0, 5, 0, 15));
        flowpane.setAlignment(Pos.CENTER);
        return flowpane;
    }

    public int calculateWPM(int characters, double time) {
        double words = characters / 5.0;
        double wordsOverTime = words / time;
        int rounded = (int) Math.round(wordsOverTime);
        return rounded;
    }

    public VBox leftside(Stage primaryStage) {
        leftside = new VBox(5);
        RadioButton jumble = jumble(primaryStage);
        RadioButton sentence = sentence();
        ToggleGroup tg = new ToggleGroup();
        Label mode = new Label("Mode");
        mode.setFont(Font.font("Times New Roman", 12));
        sentence.setToggleGroup(tg);
        jumble.setToggleGroup(tg);
        leftside.getChildren().addAll(mode, sentence, jumble);
        leftside.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(mode, new Insets(20,0,0,0));
        leftside.setPrefWidth(200);
        addCapitalCustimization();
        leftside.getChildren().remove(6);
        addLenToggle();
        addCharacterSettings(primaryStage);
        return leftside;
    }

    public RadioButton jumble(Stage primaryStage) {
        RadioButton jumble = new RadioButton("Jumble  ");
        jumble.setFont(Font.font("Times New Roman", 12));
        jumble.setSelected(true);
        jumble.setFocusTraversable(false);
        jumble.setOnAction(e -> {
            cap = Cap.NO;
            isJumble = true;
            while (leftside.getChildren().size() > 3) {
                leftside.getChildren().remove(leftside.getChildren().size() - 1);
            }
            addCapitalCustimization();
            leftside.getChildren().remove(6);
            addLenToggle();
            addCharacterSettings(primaryStage);
            updateWords();
            resetData();
            setUpKeyEvent();
        });
        return jumble;
    }

    public RadioButton sentence() {
        RadioButton sentence = new RadioButton("Sentence");
        sentence.setFont(Font.font("Times New Roman", 12));
        sentence.setFocusTraversable(false);
        sentence.setOnAction(e -> {
            cap = Cap.FIRST_WORD;
            isJumble = false;
            while (leftside.getChildren().size() > 3) {
                leftside.getChildren().remove(leftside.getChildren().size() - 1);
            }
            addCapitalCustimization();
            addLenToggle();
            updateWords();
            resetData();
            setUpKeyEvent();
        });
        return sentence;
    }

    public void addCapitalCustimization() {
        Label caps = new Label("Capital letters?");
        caps.setFont(Font.font("Times New Roman", 12));
        RadioButton noCaps = new RadioButton("No Capital Letters");
        RadioButton firstWordCaps = new RadioButton("First Word Capitalized");
        RadioButton firstLetterCaps = new RadioButton("First Letter Capitalized");
        RadioButton allCaps = new RadioButton("All Letters Capitalized");
        noCaps.setFont(Font.font("Times New Roman", 12));
        noCaps.setSelected(cap == Cap.NO);
        noCaps.setOnAction(e -> {
            if (cap == Cap.CUSTOM) {
                int temp = leftside.getChildren().indexOf(allCaps);
                leftside.getChildren().remove(temp + 2);
            }
            cap = Cap.NO;
        });
        noCaps.setFocusTraversable(false);
        firstWordCaps.setFont(Font.font("Times New Roman", 12));
        firstWordCaps.setOnAction(e -> {
            if (cap == Cap.CUSTOM) {
                int temp = leftside.getChildren().indexOf(allCaps);
                leftside.getChildren().remove(temp + 2);
            }
            cap = Cap.FIRST_WORD;
        });
        firstWordCaps.setFocusTraversable(false);
        firstWordCaps.setSelected(cap == Cap.FIRST_WORD);
        firstLetterCaps.setFont(Font.font("Times New Roman", 12));
        firstLetterCaps.setFocusTraversable(false);
        firstLetterCaps.setSelected(cap == Cap.FIRST_LETTER);
        firstLetterCaps.setOnAction(e -> {
            if (cap == Cap.CUSTOM) {
                int temp = leftside.getChildren().indexOf(allCaps);
                leftside.getChildren().remove(temp + 2);
            }
            cap = Cap.FIRST_LETTER;
        });
        allCaps.setFont(Font.font("Times New Roman", 12));
        allCaps.setSelected(cap == Cap.ALL);
        allCaps.setOnAction(e -> {
            if (cap == Cap.CUSTOM) {
                int temp = leftside.getChildren().indexOf(allCaps);
                leftside.getChildren().remove(temp + 2);
            }
            cap = Cap.ALL;
        });
        allCaps.setFocusTraversable(false);
        RadioButton custom = new RadioButton("Custom");
        custom.setFont(Font.font("Times New Roman", 12));
        custom.setFocusTraversable(false);
        custom.setSelected(cap == Cap.CUSTOM);
        custom.setOnAction(e -> {
            int temp = leftside.getChildren().indexOf(allCaps);
            VBox capSlider = capSlider(capPercentage);
            leftside.getChildren().add(temp + 2, capSlider);
            cap = Cap.CUSTOM;
            VBox.setMargin(capSlider, new Insets(10, 0, 0, 0));
        });
        ToggleGroup capTG = new ToggleGroup();
        firstWordCaps.setToggleGroup(capTG);
        noCaps.setToggleGroup(capTG);
        firstLetterCaps.setToggleGroup(capTG);
        allCaps.setToggleGroup(capTG);
        custom.setToggleGroup(capTG);
        leftside.getChildren().addAll(caps, noCaps, firstLetterCaps, firstWordCaps, allCaps, custom);
        if (cap == Cap.CUSTOM) {
            leftside.getChildren().add(leftside.getChildren().indexOf(allCaps) + 2, capSlider(capPercentage));
            VBox.setMargin(custom, new Insets(0,0,10,0));
        }
        VBox.setMargin(caps, new Insets(10, 0, 0, 0));
    }

    public VBox capSlider(double originalCapPercentage) {
        VBox vbox = new VBox();
        Label lab = new Label("Capital Letter Frequency");
        lab.setFont(Font.font("Times New Roman", 12));
        Slider capSlider = new Slider(0, 100, capPercentage);
        capSlider.setMaxWidth(78);
        capSlider.valueProperty().addListener(e -> {
            capPercentage = capSlider.getValue();
        });
        capSlider.setFocusTraversable(false);
        capSlider.setStyle("-fx-focus-color: red; .track -fx-background-color: red");
        vbox.getChildren().addAll(lab, capSlider);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    public void updateWords() {
        totalWordBox.getChildren().remove(0);
        for (int i = 0; i < 1000; i++) {
            textList[i] = null;
        }
        totalWordBox.getChildren()
            .add(0, makeWords(isJumble ? backend.createJumbleString(options, cap, jumbleCharCount, capPercentage) 
                                        : backend.createParagraph(sentenceCount, cap, capPercentage)));
    }

    public VBox rightside() {
        VBox rightside = new VBox();
        rightside.setPrefWidth(200);
        return rightside;
    }

    public void addCharacterSettings(Stage primaryStage) {
        Label chars = new Label("Character Settings");
        chars.setFont(Font.font("Times New Roman", 12));
        RadioButton basic = new RadioButton("Basic Settings");
        basic.setFont(Font.font("Times New Roman", 12));
        basic.setSelected(usingBasic);
        basic.setFocusTraversable(false);
        basic.setOnAction(e -> {
            addCharCheck();
            options = Backend.multiplyArrayList(new ArrayList<String>(Arrays.asList(Constants.originalOptions)), 50);
            /*numberOptions.clear();
            symbolOptions.clear();
            arrowOptions.clear();*/
            usingBasic = true;
        });
        RadioButton advanced = /*advancedButton(primaryStage)*/ new RadioButton("Button");
        ToggleGroup charTG = new ToggleGroup();
        basic.setToggleGroup(charTG);
        advanced.setToggleGroup(charTG);
        leftside.getChildren().addAll(chars, basic, advanced);
        VBox.setMargin(chars, new Insets(10, 0, 0, 0));
        if (usingBasic) {
            addCharCheck();
        }
    }

    /*public RadioButton advancedButton(Stage primaryStage) {
        RadioButton advanced = new RadioButton("Advanced Settings");
        advanced.setFont(Font.font("Times New Roman", 12));
        advanced.setSelected(!usingBasic);
        advanced.setFocusTraversable(false);
        advanced.setOnAction(e -> {
            Scene newScene = new Scene(advancedPageVBox(primaryStage), 1000, 1000);
            newScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(newScene);
            advancedPage = !advancedPage;
            usingBasic = !usingBasic;
        });
        return advanced;
    }

    public VBox advancedPageVBox(Stage primaryStage) {
        VBox vbox = new VBox();
        Text advancedTitle = new Text("Advanced Settings");
        advancedTitle.setFont(Font.font("Times New Roman", 30));
        Text directions = new Text("Adjust the slider of each key to change the" +
                                    " frequency that the key will appear in the jumble. Keys with higher " +
                                    "frequencies will be displayed more colorfully.");
        directions.setFont(Font.font("Times New Roman", 20));
        directions.setWrappingWidth(primaryStage.getWidth() - 200);
        Accordion accord = advancedSettingsAccord();
        vbox.getChildren().addAll(advancedTopHBox(primaryStage), advancedTitle, directions, accord);
        vbox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(accord, new Insets(10,100,0,100));
        VBox.setMargin(directions, new Insets(10,100,10,100));
        primaryStage.widthProperty().addListener(e -> {
            directions.setWrappingWidth(primaryStage.getWidth() - 200);
        });
        return vbox;
    }

    public HBox advancedTopHBox(Stage primaryStage) {
        HBox hbox = new HBox();
        hbox.getChildren().add(backButton(primaryStage));
        return hbox;
    }

    public Accordion advancedSettingsAccord() {
        TitledPane t1 = new TitledPane("Letters", accordNode(0,26, 0));
        TitledPane t2 = new TitledPane("Numbers", accordNode(26,36, 1));
        TitledPane t3 = new TitledPane("Punctuation", accordNode(36, 45, 2));
        TitledPane t4 = new TitledPane("Symbols", accordNode(45, 68, 3));
        TitledPane t5 = new TitledPane("Arrows", accordNode(68, 72, 4));
        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(t1, t2, t3, t4, t5);
        return accordion;
    }

    public VBox accordNode(int beg, int end, int associatedTitlePane) {
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(sliderFlow(beg, end), advancedButtons(associatedTitlePane));
        return vbox;
    }

    public Button backButton(Stage primaryStage) {
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Times New Roman", 20));
        backButton.setStyle("-fx-background-color: transparent");
        backButton.setOnAction(e -> {
            start(primaryStage);
            resetData();
        });
        return backButton;
    }

    public VBox sliderSquare(String t) {
        int temp = Constants.allKeys.indexOf(t);
        int originalCount = Backend.countItem(options(), t);
        StackPane stack = new StackPane();
        Rectangle rect = new Rectangle(40,40);
        rect.setFill(originalCount != 0 ? Color.color(0,1,0, originalCount * .01) : Color.GRAY);
        rect.setStroke(Color.BLACK);
        Text text = new Text(t);
        text.setFont(Font.font("Times New Roman", 20));
        stack.getChildren().addAll(rect, text);
        Slider slid = new Slider(0, 100, originalCount);
        slid.setMaxWidth(50);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(stack, slid);
        vbox.setAlignment(Pos.CENTER);
        boxes[temp] = vbox;
        slid.valueProperty().addListener(e -> {
            int myNum = (int) slid.getValue();
            int count = originalCount;
            while (count < myNum) {
                getCharCategory(t).add(t);
                count++;
            }
            while (count > myNum) {
                getCharCategory(t).remove(t);
                count--;
            }
            ((Rectangle)(((StackPane)boxes[temp].getChildren().get(0)).getChildren().get(0)))
            .setFill(myNum!= 0 ? Color.color(0,1,0,myNum * .01) : Color.GRAY);
        });
        slid.setStyle("-fx-webkit-appearance: none");
        return vbox;
    }

    public FlowPane sliderFlow(int beg, int end) {
        FlowPane sliderFlow = new FlowPane(5, 5);
        for (int i = beg; i < end; i++) {
            sliderFlow.getChildren().add(sliderSquare(String.valueOf(Constants.allKeys.charAt(i))));
        }
        return sliderFlow;
    }*/

    public void addCharCheck() {
        Label lab = new Label("Include...");
        lab.setFont(Font.font("Times New Roman", 12));
        CheckBox nums = new CheckBox("numbers");
        nums.setFont(Font.font("Times New Roman", 12));
        nums.setFocusTraversable(false);
        nums.setOnAction(e -> {
            if (nums.isSelected()) {
                jumbleNums = true;
                for (int i = 0; i < 25; i++) {
                    //numberOptions.addAll(Constants.numsList);
                    options.addAll(Constants.numsList);
                }
            } else {
                jumbleNums = false;
                for (int i = 0; i < 25; i++) {
                    //numberOptions.removeAll(Constants.numsList);
                    options.removeAll(Constants.numsList);
                }
            }
        });
        CheckBox syms = new CheckBox("symbols");
        syms.setFont(Font.font("Times New Roman", 12));
        syms.setFocusTraversable(false);
        syms.setOnAction(e -> {
            if (syms.isSelected()) {
                jumbleSymbols = false;
                for (int i = 0; i < 15; i++) {
                    //symbolOptions.addAll(Constants.symList);
                    options.addAll(Constants.symList);
                }
            } else {
                jumbleSymbols = true;
                for (int i = 0; i < 15; i++) {
                    //symbolOptions.removeAll(Constants.symList);
                    options.removeAll(Constants.symList);
                }
            }
        });
        CheckBox punc = new CheckBox("punctuation");
        punc.setFont(Font.font("Times New Roman", 12));
        punc.setFocusTraversable(false);
        punc.setOnAction(e -> {
            if (punc.isSelected()) {
                jumblePunc = true;

                for (int i = 0; i < 25; i++) {
                    //symbolOptions.addAll(Constants.puncList);
                    options.addAll(Constants.puncList);
                }
            } else {
                jumblePunc = false;
                for (int i = 0; i < 25; i++) {
                    //symbolOptions.removeAll(Constants.puncList);
                    options.removeAll(Constants.puncList);
                }
            }
        });
        CheckBox arr = new CheckBox("arrows");
        arr.setFont(Font.font("Times New Roman", 12));
        arr.setFocusTraversable(false);
        arr.setOnAction(e -> {
            if (arr.isSelected()) {
                jumbleArrow = true;
                for (int i = 0; i < 25; i++) {
                    //arrowOptions.addAll(Constants.arrowList);
                    options.addAll(Constants.arrowList);
                }
            } else {
                jumbleArrow = false;
                for (int i = 0; i < 25; i++) {
                    //arrowOptions.removeAll(Constants.arrowList);
                    options.removeAll(Constants.arrowList);
                }
            }
        });
        leftside.getChildren().addAll(lab, nums, syms, punc, arr);
        VBox.setMargin(lab, new Insets(10, 0, 0, 0));
    }

    public void addLenToggle() {
        Label length = new Label("Length: ");
        length.setFont(Font.font("Times New Roman", 12));
        Button leftArrow = new Button(" ");
        leftArrow.setId("leftArrow");
        leftArrow.setFont(Font.font("Times New Roman", 2));
        leftArrow.setRotate(270);
        leftArrow.setFocusTraversable(false);
        Button invisibleLeftArrow = new Button(" ");
        invisibleLeftArrow.setFont(Font.font("Times New Roman", 6));
        invisibleLeftArrow.setId("invisibleLeft");
        invisibleLeftArrow.setFocusTraversable(false);
        StackPane leftStack = new StackPane();
        leftStack.getChildren().addAll(leftArrow, invisibleLeftArrow);
        StackPane rightStack = new StackPane();
        Button invisibleRightArrow = new Button(" ");
        invisibleRightArrow.setFont(Font.font("Times New Roman", 5));
        invisibleRightArrow.setId("invisible");
        invisibleRightArrow.setFocusTraversable(false);
        Button rightArrow = new Button(" ");
        rightArrow.setId("rightArrow");
        rightArrow.setFont(Font.font("Times New Roman", 2));
        rightArrow.setRotate(90);
        rightStack.getChildren().addAll(rightArrow, invisibleRightArrow);
        rightArrow.setFocusTraversable(false);
        Label jumbleLen = new Label("Medium");
        jumbleLen.setFont(Font.font("Times New Roman", 12));
        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(length, leftStack, jumbleLen, rightStack);
        invisibleLeftArrow.setOnAction(e -> {
            Label temp = new Label("Frown");
            switch (jumbleCharCount) {
                case 100: 
                    temp = new Label("Short");
                    jumbleCharCount = 50;
                    sentenceCount = 1;
                    hbox.getChildren().remove(leftStack); break;
                case 175: temp = new Label("Medium");
                    jumbleCharCount = 100; 
                    sentenceCount = 2; break;
                case 300: temp = new Label("Long");
                    jumbleCharCount = 175; 
                    sentenceCount = 4;
                    hbox.getChildren().add(rightStack); break;
            }
            temp.setFont(Font.font("Times New Roman", 12));
            hbox.getChildren().add(jumbleCharCount != 50 ? 2 : 1, temp);
            hbox.getChildren().remove(jumbleCharCount != 50 ? 3 : 2);
        });
        invisibleRightArrow.setOnAction(e -> {
            Label temp = new Label("Frown");
            switch (jumbleCharCount) {
                case 50: 
                    temp = new Label("Medium");
                    sentenceCount = 2;
                    jumbleCharCount = 100;
                    hbox.getChildren().add(1,leftStack); break;
                case 100: temp = new Label("Long");
                    jumbleCharCount = 175; 
                    sentenceCount = 4; break;
                case 175: temp = new Label("Extra Long");
                    hbox.getChildren().remove(rightStack); 
                    sentenceCount = 6;
                    jumbleCharCount = 300; break;
            }
            temp.setFont(Font.font("Times New Roman", 12));
            hbox.getChildren().add(jumbleCharCount != 50 ? 2 : 1, temp);
            hbox.getChildren().remove(jumbleCharCount != 50 ? 3 : 2);
        });
        hbox.setAlignment(Pos.CENTER);
        leftside.getChildren().addAll(hbox);
        VBox.setMargin(hbox, new Insets(10,0,0,0));
    }

    /*public Button setValue(int val, String buttonLabel, int associatedTitlePane) {
        Button but = new Button(buttonLabel);
        but.setOnAction(e -> {
            switch (val) {
                case 0: 
                    switch(associatedTitlePane) {
                        case 0: letterOptions.clear(); break;
                        case 1: numberOptions.clear(); break;
                        case 2: puncOptions.clear(); break;
                        case 3: symbolOptions.clear(); break;
                        default: arrowOptions.clear(); break;
                    }
                    break;
                case 50: 
                    switch(associatedTitlePane) {
                        case 0: letterOptions = Constants.fiftyLetters; break;
                        case 1: numberOptions = Constants.fiftyNumbers; break;
                        case 2: puncOptions = Constants.fiftypunc; break;
                        case 3: symbolOptions = Constants.fiftySymbols; break;
                        default: arrowOptions = Constants.fiftyArrows; break;
                    }
                    break;
                case 100: 
                    switch(associatedTitlePane) {
                        case 0: letterOptions = Constants.hundredLetters; break;
                        case 1: numberOptions = Constants.hundredNumbers; break;
                        case 2: puncOptions = Constants.hundredpunc; break;
                        case 3: symbolOptions = Constants.hundredSymbols; break;
                        default: arrowOptions = Constants.hundredArrows; break;
                    }
                    break;
            }
        });
        return but;
    }

    public HBox advancedButtons(int associatedTitlePane) {
        HBox hbox = new HBox(20);
        hbox.getChildren().addAll(setValue(0, "Remove All", associatedTitlePane), 
                                setValue(50, "Set Middle", associatedTitlePane), 
                                setValue(100, "Maximize", associatedTitlePane));
        return hbox;
    }

    public ArrayList<String> options() {
        ArrayList<String> options = new ArrayList<>();
        options.addAll(letterOptions);
        options.addAll(numberOptions);
        options.addAll(puncOptions);
        options.addAll(symbolOptions);
        options.addAll(arrowOptions);
        return options;
    }

    public ArrayList<String> getCharCategory(String astr) {
        if ("qwertyuiopasdfghjklzxcvbnm".contains(astr)) {
            return letterOptions;
        } else if ("1234567890".contains(astr)) {
            return numberOptions;
        } else if ("↓→←↑".contains(astr)) {
            return arrowOptions;
        } else if (",.:;?\'\"!-".contains(astr)) {
            return puncOptions;
        } else {
            return symbolOptions;
        }
    }*/
}