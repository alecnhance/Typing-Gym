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

public class Type extends Application {
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
    private ArrayList<String> letterOptions = Backend.multiplyArrayList(new ArrayList<String>(Arrays.asList(Constants.originalOptions)), 50);
    private ArrayList<String> numberOptions = new ArrayList<String>();
    private ArrayList<String> symbolOptions = new ArrayList<String>();
    private ArrayList<String> arrowOptions = new ArrayList<String>();
    private ArrayList<String> puncOptions = new ArrayList<String>();
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
    private String[] colorList = Constants.buchanonColors;
    private RadioButton allCaps;
    private StackPane mainPane;
    private VBox rightside;
    private Scene mainScene;
    private VBox centerScreen;

    public void start(Stage primaryStage) {
        mainPane = new StackPane();
        mainPane.getChildren().addAll(createBackgroundImage(), makeBP(primaryStage));
        mainScene = new Scene(mainPane, 1000, 1000);
        mainScene.getStylesheets().add(getClass().getResource("buchanon.css").toExternalForm());
        primaryStage.setScene(mainScene);
        primaryStage.show();
        setUpKeyEvent();
    }

    public ImageView createBackgroundImage() {
        ImageView iv1 = new ImageView();
        iv1.setImage(new Image(colorList[6]));
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
                    .setFill(((Text)textList[cursor].getChildren().get(0)).getText().equals("␣") ? null : Color.web(colorList[5]));
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
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? Color.web(colorList[1]) : null);
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(Color.web(colorList[2]));
                    colorHolder.add(0);
                    mistakes.add(cursor);
                    wrongs++;
                    reds++;
                }
            } else if (e.getCharacter().equals(((Text)textList[cursor].getChildren().get(0)).getText())
                        && !e.getCharacter().equals("␣")) {
                types++;
                //textList[cursor++].setStyle(mistakes.contains(cursor) ? yellow : green);
                ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? Color.web(colorList[1]) : Color.web(colorList[0]));
                colorHolder.add(1);
                greens++;
            } else {
                types++;
                ((Text)textList[cursor++].getChildren().get(0)).setFill(Color.web(colorList[2]));
                colorHolder.add(0);
                mistakes.add(cursor);
                wrongs++;
                reds++;
            }
            if (textList[cursor] == null) {
                if (reds == 0 && !finished) {
                    finished = true;
                    time = (System.nanoTime() - start) / ((Math.pow(10, 9)) * 60.0);
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
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? Color.web(colorList[1]) : Color.web(colorList[0]));
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(Color.web(colorList[2]));
                    colorHolder.add(0);
                    mistakes.add(cursor);
                    wrongs++;
                    reds++;
                }
            } else if (e.getCode().equals(KeyCode.DOWN)) {
                if (((Text)textList[cursor].getChildren().get(0)).getText().equals("↓")) {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? Color.web(colorList[1]) : Color.web(colorList[0]));
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(Color.web(colorList[2]));
                    colorHolder.add(0);
                    mistakes.add(cursor);
                    wrongs++;
                    reds++;
                }
            } else if (e.getCode().equals(KeyCode.RIGHT)) {
                if (((Text)textList[cursor].getChildren().get(0)).getText().equals("→")) {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? Color.web(colorList[1]) : Color.web(colorList[0]));
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(Color.web(colorList[2]));
                    colorHolder.add(0);
                    mistakes.add(cursor);
                    wrongs++;
                    reds++;
                }
            } else if (e.getCode().equals(KeyCode.LEFT)) {
                if (((Text)textList[cursor].getChildren().get(0)).getText().equals("←")) {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(mistakes.contains(cursor) ? Color.web(colorList[1]) : Color.web(colorList[0]));
                    colorHolder.add(1);
                    greens++;
                } else {
                    ((Text)textList[cursor++].getChildren().get(0)).setFill(Color.web(colorList[2]));
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
        centerScreen = new VBox(makeTopBar(), totalWordBox);
        centerScreen.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(centerScreen.getChildren().get(0), new Insets(0, 0, 10, 0));
        border.setCenter(centerScreen);
        Label text = new Label("Typing Gym");
        text.setFont(Font.font("Times New Roman", 35));
        text.setStyle(String.format("-fx-text-fill: %s", colorList[0]));
        border.setTop(text);
        BorderPane.setAlignment(text, Pos.TOP_CENTER);
        BorderPane.setAlignment(centerScreen, Pos.TOP_CENTER);
        BorderPane.setMargin(text, new Insets(25, 0, 0, 0));
        BorderPane.setMargin(centerScreen, new Insets(25, 0, 0, 0));
        border.setLeft(leftside(primaryStage));
        border.setRight(rightside(primaryStage));
        return border;
    }

    public void updateTopBar() {
        centerScreen.getChildren().remove(0);
        FlowPane topBar = makeTopBar();
        centerScreen.getChildren().add(0, topBar);
        VBox.setMargin(topBar, new Insets(0, 0, 10, 0));
    }

    public void makeTotalWordBox() {
        totalWordBox = new VBox();
        FlowPane flowpane = makeWords(isJumble ? backend.createJumbleString(options(), cap, jumbleCharCount, capPercentage) 
                                                : backend.createParagraph(sentenceCount, cap, capPercentage));
        flowpane.setMaxWidth(800);
        flowpane.setMaxHeight(1);
        HBox hbox = new HBox();
        hbox.getChildren().add(makeRedo());
        hbox.setAlignment(Pos.CENTER);
        totalWordBox.getChildren().add(flowpane);
        totalWordBox.getChildren().add(hbox);
        totalWordBox.setStyle(String.format("-fx-background-color: %s; -fx-border-color: black", colorList[4]));
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
        redoButton.setStyle(String.format("-fx-border-color: black; -fx-background-color: transparent; -fx-border-radius: 3px; -fx-text-fill: %s", colorList[5]));
        redoButton.setFocusTraversable(false);
        redoButton.setOnAction(e -> {
            for (int i = 0; i < cursor; i++) {
                textList[i] = null;
            }
            updateWords();
            updateTopBar();
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
        highScoreData = new Label(best != 0 ? String.format("%d", best) : "N/A");
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
        Label mode = new Label("Mode");
        mode.setFont(Font.font("Times New Roman", 15));   
        leftside.getChildren().addAll(mode);
        addCapToggle();
        leftside.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(mode, new Insets(40,0,0,0));
        leftside.setPrefWidth(300);
        /*if (cap == Cap.CUSTOM) {
            VBox capSlider = capSlider(capPercentage);
            leftside.getChildren().add(capSlider);
            VBox.setMargin(capSlider, new Insets(10, 0, 0, 0));
        }*/
        addLenToggle();
        if (isJumble) {
            addCharacterSettings(primaryStage);
        }
        leftside.getChildren().add(1, modeButton(primaryStage));
        return leftside;
    }

    public VBox capSlider(double originalCapPercentage) {
        VBox vbox = new VBox();
        Label lab = new Label("Capital Letter Frequency");
        lab.setFont(Font.font("Times New Roman", 15));
        Slider capSlider = new Slider(0, 100, capPercentage);
        capSlider.setMaxWidth(90);
        capSlider.valueProperty().addListener(e -> {
            capPercentage = capSlider.getValue();
        });
        capSlider.setId("capslider");
        capSlider.setFocusTraversable(false);
        vbox.getChildren().addAll(lab, capSlider);
        vbox.setAlignment(Pos.CENTER);
        VBox.setMargin(capSlider, new Insets(3, 0, 0, 0));
        return vbox;
    }

    public void updateWords() {
        totalWordBox.getChildren().remove(0);
        for (int i = 0; i < 1000; i++) {
            textList[i] = null;
        }
        FlowPane temp = makeWords(isJumble ? backend.createJumbleString(options(), cap, jumbleCharCount, capPercentage) 
                                        : backend.createParagraph(sentenceCount, cap, capPercentage));
        totalWordBox.getChildren().add(0, temp);
        VBox.setMargin(temp, new Insets(10, 0, 15, 15));
    }

    public VBox rightside(Stage primaryStage) {
        rightside = new VBox();
        rightside.setPrefWidth(300);
        rightside.setAlignment(Pos.TOP_CENTER);
        addThemeOptions(primaryStage);
        return rightside;
    }

    public void addThemeOptions(Stage primaryStage) {
        Label theme = new Label("Theme");
        theme.setFont(Font.font("Times New Roman", 17));
        theme.setAlignment(Pos.CENTER);
        theme.setId("theme");
        ToggleGroup tg = new ToggleGroup();
        RadioButton arnold = new RadioButton("Arnold");
        arnold.setOnAction(e -> {
            colorList = Constants.arnoldColors;
            mainScene.getStylesheets().remove(0);
            mainScene.getStylesheets().add(getClass().getResource("arnold.css").toExternalForm());
            resetAll(primaryStage);
        });
        RadioButton cbum = new RadioButton("CBum");
        cbum.setOnAction(e -> {
            colorList = Constants.cbumColors;
            mainScene.getStylesheets().remove(0);
            mainScene.getStylesheets().add(getClass().getResource("cbum.css").toExternalForm());
            resetAll(primaryStage);
        });
        RadioButton ronnie = new RadioButton("Ronnie");
        ronnie.setOnAction(e -> {
            colorList = Constants.ronnieColors;
            mainScene.getStylesheets().remove(0);
            mainScene.getStylesheets().add(getClass().getResource("ronnie.css").toExternalForm());
            resetAll(primaryStage);
        });
        RadioButton buchanon = new RadioButton("Buchanon");
        buchanon.setOnAction(e -> {
            colorList = Constants.buchanonColors;
            mainScene.getStylesheets().remove(0);
            mainScene.getStylesheets().add(getClass().getResource("buchanon.css").toExternalForm());
            resetAll(primaryStage);
        });
        HBox topBox = new HBox(5, buchanon, arnold);
        topBox.setAlignment(Pos.CENTER);
        HBox botBox = new HBox(5, ronnie, cbum);
        botBox.setAlignment(Pos.CENTER);
        arnold.setToggleGroup(tg);
        arnold.setFocusTraversable(false);
        ronnie.setToggleGroup(tg);
        ronnie.setFocusTraversable(false);
        cbum.setToggleGroup(tg);
        cbum.setFocusTraversable(false);
        buchanon.setToggleGroup(tg);
        buchanon.setFocusTraversable(false);
        switch(colorList[6]) {
            case "buchanon.jpg": buchanon.setSelected(true); break;
            case "coleman.jpg": ronnie.setSelected(true); break;
            case "cbum.jpg": cbum.setSelected(true); break;
            case "arnold.jpg": arnold.setSelected(true); break;
        }
        rightside.getChildren().addAll(theme, topBox, botBox);
        VBox.setMargin(theme, new Insets(40, 0, 4, 0));
    }

    public void addCharacterSettings(Stage primaryStage) {
        Label chars = new Label("Character Settings");
        chars.setFont(Font.font("Times New Roman", 15));
        leftside.getChildren().addAll(chars, settingsButton(primaryStage));
        VBox.setMargin(chars, new Insets(10, 0, 0, 0));
        if (usingBasic) {
            addCharCheck();
        }
    }

    public VBox advancedPageVBox(Stage primaryStage) {
        VBox vbox = new VBox();
        Text advancedTitle = new Text("Advanced Settings");
        advancedTitle.setFont(Font.font("Times New Roman", 30));
        advancedTitle.setStyle(String.format("-fx-fill: %s", colorList[0]));
        Text directions = new Text("Adjust the slider of each key to change the" +
                                    " frequency that the key will appear in the jumble. Keys with higher " +
                                    "frequencies will be displayed more colorfully.");
        directions.setFont(Font.font("Times New Roman", 20));
        directions.setWrappingWidth(primaryStage.getWidth() - 200);
        directions.setStyle(String.format("-fx-fill: %s", colorList[3]));
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
        vbox.getChildren().addAll(sliderFlow(beg, end), advancedButtons(associatedTitlePane, vbox));
        return vbox;
    }

    public Button backButton(Stage primaryStage) {
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Times New Roman", 20));
        backButton.setStyle(String.format("-fx-background-color: transparent; -fx-text-fill: %s", colorList[3]));
        backButton.setOnAction(e -> {
            resetAll(primaryStage);
        });
        return backButton;
    }

    public void resetAll(Stage primaryStage) {
        for (int i = 0; i < 500; i++) {
                textList[i] = null;
        }
        resetData();
        mainPane.getChildren().clear();
        mainPane.getChildren().addAll(createBackgroundImage(), makeBP(primaryStage));
        setUpKeyEvent();
    }

    public VBox sliderSquare(String t) {
        int temp = Constants.allKeys.indexOf(t);
        int originalCount = Backend.countItem(options(), t);
        StackPane stack = new StackPane();
        Rectangle rect = new Rectangle(40,40);
        rect.setFill(originalCount != 0 ? Color.web(colorList[0], originalCount * .01) : Color.GRAY);
        rect.setStroke(Color.BLACK);
        Text text = new Text(t);
        text.setFont(Font.font("Times New Roman", 20));
        stack.getChildren().addAll(rect, text);
        Slider slid = new Slider(0, 100, originalCount);
        slid.setMaxWidth(45);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(stack, slid);
        vbox.setAlignment(Pos.CENTER);
        VBox.setMargin(slid, new Insets(1, 0, 0, 0));
        boxes[temp] = vbox;
        slid.valueProperty().addListener(e -> {
            int myNum = (int) slid.getValue();
            int count = Backend.countItem(options(), t);
            while (count < myNum) {
                getCharCategory(t).add(t);
                count++;
            }
            while (count > myNum) {
                getCharCategory(t).remove(t);
                count--;
            }
            ((Rectangle)(((StackPane)boxes[temp].getChildren().get(0)).getChildren().get(0)))
            .setFill(myNum!= 0 ? Color.web(colorList[0], myNum * .01) : Color.GRAY);
            best = 0;
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
    }

    public void addCharCheck() {
        Label lab = new Label("Include...");
        lab.setFont(Font.font("Times New Roman", 15));
        CheckBox nums = new CheckBox("numbers");
        nums.setFont(Font.font("Times New Roman", 15));
        nums.setFocusTraversable(false);
        nums.setSelected(jumbleNums);
        nums.setOnAction(e -> {
            if (nums.isSelected()) {
                jumbleNums = true;
                for (int i = 0; i < 25; i++) {
                    numberOptions.addAll(Constants.numsList);
                }
            } else {
                jumbleNums = false;
                for (int i = 0; i < 25; i++) {
                    numberOptions.removeAll(Constants.numsList);
                }
            }
            best = 0;
        });
        nums.setId("nums");
        CheckBox syms = new CheckBox("symbols");
        syms.setFont(Font.font("Times New Roman", 15));
        syms.setFocusTraversable(false);
        syms.setSelected(jumbleSymbols);
        syms.setOnAction(e -> {
            if (syms.isSelected()) {
                jumbleSymbols = true;
                for (int i = 0; i < 15; i++) {
                    symbolOptions.addAll(Constants.symList);
                }
            } else {
                jumbleSymbols = false;
                for (int i = 0; i < 15; i++) {
                    symbolOptions.removeAll(Constants.symList);
                }
            }
            best = 0;
        });
        syms.setPrefWidth(95);
        CheckBox punc = new CheckBox("punctuation");
        punc.setFont(Font.font("Times New Roman", 15));
        punc.setFocusTraversable(false);
        punc.setSelected(jumblePunc);
        punc.setOnAction(e -> {
            if (punc.isSelected()) {
                jumblePunc = true;
                for (int i = 0; i < 25; i++) {
                    symbolOptions.addAll(Constants.puncList);
                }
            } else {
                jumblePunc = false;
                for (int i = 0; i < 25; i++) {
                    symbolOptions.removeAll(Constants.puncList);
                }
            }
            best = 0;
        });
        punc.setPrefWidth(95);
        CheckBox arr = new CheckBox("arrows");
        arr.setFont(Font.font("Times New Roman", 15));
        arr.setFocusTraversable(false);
        arr.setSelected(jumbleArrow);
        arr.setOnAction(e -> {
            if (arr.isSelected()) {
                jumbleArrow = true;
                for (int i = 0; i < 25; i++) {
                    arrowOptions.addAll(Constants.arrowList);
                }
            } else {
                jumbleArrow = false;
                for (int i = 0; i < 25; i++) {
                    arrowOptions.removeAll(Constants.arrowList);
                }
            }
            best = 0;
        });
        arr.setId("arr");
        HBox topRow = new HBox(nums, syms);
        HBox botRow = new HBox(arr, punc);
        topRow.setAlignment(Pos.CENTER);
        botRow.setAlignment(Pos.CENTER);
        leftside.getChildren().addAll(lab, topRow, botRow);
        VBox.setMargin(lab, new Insets(10, 0, 0, 0));
    }

    public void addLenToggle() {
        Label length = new Label("Length: ");
        length.setFont(Font.font("Times New Roman", 15));
        Button leftArrow = leftArrow();
        Button invisibleLeftArrow = invisibleLeftArrow();
        StackPane leftStack = new StackPane();
        leftStack.getChildren().addAll(leftArrow, invisibleLeftArrow);
        StackPane rightStack = new StackPane();
        Button invisibleRightArrow = invisibleRightArrow();
        Button rightArrow = rightArrow();
        rightStack.getChildren().addAll(rightArrow, invisibleRightArrow);
        Label jumbleLen = new Label(getLenLabel());
        jumbleLen.setFont(Font.font("Times New Roman", 15));
        jumbleLen.setMinWidth(60);
        jumbleLen.setAlignment(Pos.CENTER);
        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(length, leftStack, jumbleLen, rightStack);
        invisibleLeftArrow.setOnAction(e -> {
            Label temp = new Label("Frown");
            temp.setMinWidth(60);
            temp.setAlignment(Pos.CENTER);
            switch (jumbleCharCount) {
                case 175: temp.setText("Medium");
                    jumbleCharCount = 100; 
                    sentenceCount = 2; break;
                case 300: temp.setText("Long");
                    jumbleCharCount = 175; 
                    sentenceCount = 4;
                    //((Button)((StackPane)hbox.getChildren().get(3)).getChildren().get(0)).setStyle("-fx-background-color: white");
                    break;
                case 50: temp.setText("Extensive");
                    jumbleCharCount = 300;
                    sentenceCount = 6; break;
                default: 
                    temp.setText("Short");
                    jumbleCharCount = 50;
                    sentenceCount = 1;
                    //((Button)((StackPane)hbox.getChildren().get(1)).getChildren().get(0)).setStyle("-fx-background-color: transparent");
                    break;
            }
            temp.setFont(Font.font("Times New Roman", 15));
            hbox.getChildren().add(2, temp);
            hbox.getChildren().remove(3);
        });
        invisibleRightArrow.setOnAction(e -> {
            Label temp = new Label("Frown");
            temp.setMinWidth(60);
            temp.setAlignment(Pos.CENTER);
            switch (jumbleCharCount) {
                case 50: 
                    temp.setText("Medium");
                    sentenceCount = 2;
                    jumbleCharCount = 100;
                    //((Button)((StackPane)hbox.getChildren().get(1)).getChildren().get(0)).setStyle("-fx-background-color: white");
                    break;
                case 100: temp.setText("Long");
                    jumbleCharCount = 175; 
                    sentenceCount = 4; break;
                case 300: temp.setText("Short");
                    sentenceCount = 1;
                    jumbleCharCount = 50; break;
                default: temp.setText("Extensive");
                    //((Button)((StackPane)hbox.getChildren().get(3)).getChildren().get(0)).setStyle("-fx-background-color: transparent"); 
                    sentenceCount = 6;
                    jumbleCharCount = 300; break;
            }
            temp.setFont(Font.font("Times New Roman", 15));
            hbox.getChildren().add(2, temp);
            hbox.getChildren().remove(3);
        });
        hbox.setAlignment(Pos.CENTER);
        leftside.getChildren().addAll(hbox);
        VBox.setMargin(hbox, new Insets(10,0,0,0));
    }

    public Button setValue(int val, String buttonLabel, int associatedTitlePane, VBox vbox) {
        Button but = new Button(buttonLabel);
        but.setOnAction(e -> {
            switch (val) {
                case 0: 
                    switch(associatedTitlePane) {
                        case 0: letterOptions = new ArrayList<>(); break;
                        case 1: numberOptions = new ArrayList<>(); break;
                        case 2: puncOptions = new ArrayList<>(); break;
                        case 3: symbolOptions = new ArrayList<>(); break;
                        default: arrowOptions = new ArrayList<>(); break;
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
            switch (associatedTitlePane) {
                case 0:
                    vbox.getChildren().remove(0);
                    vbox.getChildren().add(0, sliderFlow(0,26)); break;
                case 1:
                    vbox.getChildren().remove(0);
                    vbox.getChildren().add(0, sliderFlow(26,36)); break;
                case 2:
                    vbox.getChildren().remove(0);
                    vbox.getChildren().add(0, sliderFlow(36,45)); break; 
                case 3:
                    vbox.getChildren().remove(0);
                    vbox.getChildren().add(0, sliderFlow(45,68)); break;
                case 4:
                    vbox.getChildren().remove(0);
                    vbox.getChildren().add(0, sliderFlow(68,72)); break;
            }
            best = 0;
        });
        but.setFont(Font.font("Times New Roman", 15));
        return but;
    }

    public HBox advancedButtons(int associatedTitlePane, VBox vbox) {
        HBox hbox = new HBox(20);
        hbox.getChildren().addAll(setValue(0, "Remove All", associatedTitlePane, vbox), 
                                setValue(50, "Set Middle", associatedTitlePane, vbox), 
                                setValue(100, "Maximize", associatedTitlePane, vbox));
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    public StackPane modeButton(Stage primaryStage) {
        Button background = new Button("");
        background.setId("background");
        background.setFocusTraversable(false);
        Button jumble = new Button(" Jumble ");
        jumble.setFont(Font.font("Times New Roman", 13));
        jumble.setFocusTraversable(false);
        Button sentence = new Button("Sentence");
        sentence.setFont(Font.font("Times New Roman", 13));
        sentence.setFocusTraversable(false);
        if (isJumble) {
            jumble.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: %s; -fx-text-fill: %s; -fx-pref-width: 106;", colorList[0], colorList[3]));
            sentence.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: transparent; -fx-text-fill: %s; -fx-pref-width: 78", colorList[3]));
            StackPane.setMargin(jumble, new Insets(0,81,0,0));
            StackPane.setMargin(sentence, new Insets(0,0,0,109));
        } else {
            jumble.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: transparent; -fx-text-fill: %s; -fx-pref-width: 78;", colorList[3]));
            sentence.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: %s; -fx-text-fill: %s; -fx-pref-width: 106", colorList[0], colorList[3]));
            StackPane.setMargin(jumble, new Insets(0,109,0,0));
            StackPane.setMargin(sentence, new Insets(0,0,0,81));
        }
        StackPane stack = new StackPane();
        stack.getChildren().addAll(background, jumble, sentence);
        StackPane.setMargin(background, new Insets(1,0,0,0));
        sentence.setOnAction(e -> {
            sentence.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: %s; -fx-text-fill: %s; -fx-pref-width: 106;", colorList[0], colorList[3]));
            jumble.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: transparent; -fx-text-fill: %s; -fx-pref-width: 78", colorList[3]));
            StackPane.setMargin(jumble, new Insets(0,109,0,0));
            StackPane.setMargin(sentence, new Insets(0,0,0,81));
            isJumble = false;
            while (leftside.getChildren().size() > 2) {
                leftside.getChildren().remove(leftside.getChildren().size() - 1);
            }
            addCapToggle();
            addLenToggle();
            updateWords();
            resetData();
            setUpKeyEvent();
        });
        jumble.setOnAction(e -> {
            sentence.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: transparent; -fx-text-fill: %s; -fx-pref-width: 78", colorList[3]));
            jumble.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: %s; -fx-text-fill: %s; -fx-pref-width: 106;", colorList[0], colorList[3]));
            StackPane.setMargin(jumble, new Insets(0,81,0,0));
            StackPane.setMargin(sentence, new Insets(0,0,0,109));
            if (cap == Cap.FIRST_WORD) {
                cap = Cap.NO;
            }
            isJumble = true;
            while (leftside.getChildren().size() > 2) {
                leftside.getChildren().remove(leftside.getChildren().size() - 1);
            }
            addCapToggle();
            addLenToggle();
            addCharacterSettings(primaryStage);
            updateWords();
            resetData();
            setUpKeyEvent();
        });
        return stack;
    }

    public void addCapToggle() {
        Label length = new Label("Capitalization: ");
        length.setFont(Font.font("Times New Roman", 15));
        Button leftArrow = leftArrow();
        Button invisibleLeftArrow = invisibleLeftArrow();
        StackPane leftStack = new StackPane();
        leftStack.getChildren().addAll(leftArrow, invisibleLeftArrow);
        StackPane rightStack = new StackPane();
        Button invisibleRightArrow = invisibleRightArrow();
        Button rightArrow = rightArrow();
        rightStack.getChildren().addAll(rightArrow, invisibleRightArrow);
        Label jumbleLen = new Label(getCapLabel());
        jumbleLen.setFont(Font.font("Times New Roman", 15));
        jumbleLen.setMinWidth(70);
        jumbleLen.setAlignment(Pos.CENTER);
        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(length, leftStack, jumbleLen, rightStack);
        invisibleRightArrow.setOnAction(e -> {
            Label temp = new Label("Frown");
            temp.setMinWidth(70);
            temp.setAlignment(Pos.CENTER);
            switch (cap) {
                case NO: 
                    if (!isJumble) {
                        temp.setText("First Word");
                        cap = Cap.FIRST_WORD; break;
                    } else {
                        temp.setText("First Letter");
                        cap = Cap.FIRST_LETTER; break;
                    }
                case FIRST_WORD: temp.setText("First Letter");
                        cap = Cap.FIRST_LETTER; break;
                case CUSTOM: temp.setText("None");
                    cap = Cap.NO; 
                    leftside.getChildren().remove(3); break;
                case FIRST_LETTER: temp.setText("All");
                    cap = Cap.ALL; break;
                case ALL: temp.setText("Custom");
                    VBox capSlider = capSlider(capPercentage);
                    leftside.getChildren().add(3, capSlider);
                    cap = Cap.CUSTOM;
                    VBox.setMargin(capSlider, new Insets(10, 0, 0, 0)); break;
            }
            temp.setFont(Font.font("Times New Roman", 15));
            hbox.getChildren().add(2, temp);
            hbox.getChildren().remove(3);
            best = 0;
        });
        invisibleLeftArrow.setOnAction(e -> {
            Label temp = new Label("Frown");
            temp.setMinWidth(70);
            temp.setAlignment(Pos.CENTER);
            switch (cap) {
                case NO: 
                    temp.setText("Custom");
                    int index = leftside.getChildren().indexOf(allCaps);
                    VBox capSlider = capSlider(capPercentage);
                    leftside.getChildren().add(3, capSlider);
                    cap = Cap.CUSTOM;
                    VBox.setMargin(capSlider, new Insets(10, 0, 0, 0)); break;
                case CUSTOM: temp.setText("All");
                    cap = Cap.ALL; 
                    leftside.getChildren().remove(3); break;
                case FIRST_LETTER: 
                    if (isJumble) {
                        temp.setText("None");
                        cap = Cap.NO; break;
                    } else {
                        temp.setText("First Word");
                        cap = Cap.FIRST_WORD; break;
                    }
                case ALL: temp.setText("First Letter");
                    cap = Cap.FIRST_LETTER; break;
                case FIRST_WORD: temp.setText("None");
                cap = Cap.NO; break;
            }
            temp.setFont(Font.font("Times New Roman", 15));
            hbox.getChildren().add( 2, temp);
            hbox.getChildren().remove(3);
            best = 0;
        });
        hbox.setAlignment(Pos.CENTER);
        leftside.getChildren().addAll(hbox);
        if (cap == Cap.CUSTOM) {
            VBox capSlider = capSlider(capPercentage);
            leftside.getChildren().add(capSlider);
            VBox.setMargin(capSlider, new Insets(10, 0, 0, 0));
        }
        VBox.setMargin(hbox, new Insets(10,0,0,0));
    }

    public StackPane settingsButton(Stage primaryStage) {
        Button background = new Button("");
        background.setId("background");
        background.setFocusTraversable(false);
        Button basic = new Button(" Basic  ");
        basic.setStyle(usingBasic ? String.format("-fx-background-radius: 15px; -fx-background-color: %s; -fx-text-fill: %s; -fx-pref-width: 106;", colorList[0], colorList[3])
                                    : String.format("-fx-background-radius: 15px; -fx-background-color: transparent; -fx-text-fill: %s; -fx-pref-width: 78", colorList[3]));
        basic.setFont(Font.font("Times New Roman", 13));
        basic.setFocusTraversable(false);
        Button advanced = new Button("Advanced");
        advanced.setStyle(usingBasic ? String.format("-fx-background-radius: 15px; -fx-background-color: transparent; -fx-text-fill: %s; -fx-pref-width: 78", colorList[3])
                                        : String.format("-fx-background-radius: 15px; -fx-background-color: %s; -fx-text-fill: %s; -fx-pref-width: 106;", colorList[0], colorList[3]));
        advanced.setFont(Font.font("Times New Roman", 13));
        advanced.setFocusTraversable(false);
        StackPane stack = new StackPane();
        stack.getChildren().addAll(background, basic, advanced);
        StackPane.setMargin(basic, usingBasic ? new Insets(0,81,0,0) : new Insets(0,109,0,0));
        StackPane.setMargin(advanced, usingBasic ? new Insets(0,0,0,109) : new Insets(0,0,0,81));
        StackPane.setMargin(background, new Insets(1,0,0,0));
        basic.setOnAction(e -> {
            advanced.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: transparent; -fx-text-fill: %s; -fx-pref-width: 78", colorList[3]));
            basic.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: %s; -fx-text-fill: %s; -fx-pref-width: 106;", colorList[0], colorList[3]));
            StackPane.setMargin(basic, new Insets(0,81,0,0));
            StackPane.setMargin(advanced, new Insets(0,0,0,109));
            best = usingBasic ? best : 0;
            jumbleNums = false;
            jumbleSymbols = false;
            jumblePunc = false;
            jumbleArrow = false;
            if (!usingBasic) {
                addCharCheck();
                letterOptions = Backend.multiplyArrayList(new ArrayList<String>(Arrays.asList(Constants.originalOptions)), 50);
                numberOptions.clear();
                symbolOptions.clear();
                arrowOptions.clear();
            }
            usingBasic = true;
        });
        advanced.setOnAction(e -> {
            advanced.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: %s; -fx-text-fill: %s; -fx-pref-width: 106;", colorList[0], colorList[3]));
            basic.setStyle(String.format("-fx-background-radius: 15px; -fx-background-color: transparent; -fx-text-fill: %s; -fx-pref-width: 78", colorList[3]));
            StackPane.setMargin(basic, new Insets(0,109,0,0));
            StackPane.setMargin(advanced, new Insets(0,0,0,81));
            mainPane.getChildren().remove(1);
            mainPane.getChildren().add(advancedPageVBox(primaryStage));
            advancedPage = true;
            usingBasic = false;
        });
        return stack;
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
    }

    public Button rightArrow() {
        Button rightArrow = new Button(" ");
        rightArrow.setId("rightArrow");
        rightArrow.setFont(Font.font("Times New Roman", 4));
        rightArrow.setRotate(90);
        rightArrow.setFocusTraversable(false);
        return rightArrow;
    }

    public Button invisibleRightArrow() {
        Button invisibleRightArrow = new Button(" ");
        invisibleRightArrow.setFont(Font.font("Times New Roman", 5));
        invisibleRightArrow.setId("invisible");
        invisibleRightArrow.setFocusTraversable(false);
        return invisibleRightArrow;
    }

    public Button invisibleLeftArrow() {
        Button invisibleLeftArrow = new Button(" ");
        invisibleLeftArrow.setFont(Font.font("Times New Roman", 6));
        invisibleLeftArrow.setId("invisibleLeft");
        invisibleLeftArrow.setFocusTraversable(false);
        return invisibleLeftArrow;
    }

    public Button leftArrow() {
        Button leftArrow = new Button(" ");
        leftArrow.setId("leftArrow");
        leftArrow.setFont(Font.font("Times New Roman", 4));
        leftArrow.setRotate(270);
        leftArrow.setFocusTraversable(false);
        return leftArrow;
    }

    public String getLenLabel() {
        switch (jumbleCharCount) {
            case 50: return "Short";
            case 100: return "Medium";
            case 175: return "Long";
            default: return "Extensive";
        }
    }

    public String getCapLabel() {
        switch (cap) {
            case NO: return "None";
            case FIRST_LETTER: return "First Letter";
            case ALL: return "All";
            case FIRST_WORD: return "First Word";
            default: return "Custom";
        }
    }


}
