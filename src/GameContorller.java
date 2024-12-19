import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.*;

public class GameContorller {

    private static final int code_length = 4; //Secret code length
    private static final int max_attempts = 10;               //عدد المحاولات
    final   private List<Color> colors = Arrays.asList(
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE, Color.ORANGE);//الالوان اللي هتوقع منها


    private List<Color> secretcode;  //        هنا الالوان اللي انا هتوقعها منها
    private int attempts;
    final   private Color[] guess = new Color[code_length];
    private VBox mainlayout;                                      //لرص الحاجة تحت بعضها بالطول
    private GridPane guessgrid;              //organize the game's interface elements in a grid layout.  بيظبط شكل الزراير ز   ابرنامج اللي هي واجهة البرنامج
    private Label feedbacklabel; //                                               بيعرفني ايه اللي هعمله في البرنامج زي الرسالة اللي موجودة لما يتقولك  الالوان صح لكن مش بنفس الترتيب زي برضو رسالة اللي بيطلب فيها تسجيل دخول وخروج
    private int seconds = 0;
    private int mimutes;
    private Timeline timer;//

    private Label resultlabel;//      بتحدد الوقت الي استغرقته اذا كان فوز او خسارة في التخمين
    private Label timerlabel;

    public GameContorller() {
        attempts = 0;       //  ده لما البرنامج بيبدا بيقول
        generateSecretCode();


    }

    private void generateSecretCode() { //              بتخلي البرنامج يخلط الالوان مع بعض
        secretcode = new ArrayList<>(colors);
        Collections.shuffle(secretcode);         //       بيخلط الالون عشوائي
        secretcode = secretcode.subList(0, code_length);


    }

    public VBox createGamelayout() {
        mainlayout = new VBox(20); //                                     هنا علشان يعمل ال container عمودي وتعمل مقدار تباعد بمقدار 20
        mainlayout.setAlignment(Pos.CENTER);//
        timerlabel = new Label("00.00");
        timerlabel.setId("timerlabel");
        resultlabel = new Label();
        resultlabel.setFont(new javafx.scene.text.Font("Arial", 20)); // و ده حجم الخط ونوعه
        resultlabel.setTextFill(Color.WHITE);
        Label titlelabel = new Label("Welcome to Mastermind ");
        titlelabel.setFont(new javafx.scene.text.Font("Arial", 24));
        titlelabel.setTextFill(Color.GREENYELLOW);
        Label instruactionlabel = new Label("You have 10  attempts to guess the secret code.");
        instruactionlabel.setFont(new javafx.scene.text.Font("Arial", 12));
        instruactionlabel.setTextFill(Color.WHITE);
        guessgrid = new GridPane();
        guessgrid.setHgap(20);
        guessgrid.setVgap(10);
        guessgrid.setAlignment(Pos.CENTER);
        for (int i = 0; i < code_length; i++) {
            Button button = new Button("Choose Color");
            button.setPrefSize(130, 60); // حجم الزر
            button.setId("Color button");
           final int index = i;
            button.setOnAction( e -> pickColor(button, index));//لتغير اللون لكل زر موجود
            guessgrid.add(button, i, 0); //// بيحط الازرار ورا بعض

        }
        Button submitbutton = new Button("Submit");
        submitbutton.setId("submit button");
        submitbutton.setOnAction(e -> submitGuess());
        feedbacklabel = new Label();
        feedbacklabel.setId("feedback label");
        feedbacklabel.setTextFill(Color.WHITE);
        feedbacklabel.setFont(new javafx.scene.text.Font("Arial", 24));
        HBox timerBOX = new HBox(20);
        timerBOX.setAlignment(Pos.TOP_LEFT);
        timerBOX.getChildren().addAll(timerlabel); // اللي هو المكان اللي بيبقي فيع التايمر
        mainlayout.getChildren().addAll(titlelabel, guessgrid, instruactionlabel, feedbacklabel,submitbutton);
        return mainlayout;


    }

    private void submitGuess() {
        if (attempts < max_attempts) {
            boolean guessed = true;// بتتاكد ان المستخدم مسابش ولا لون فاضي
            for (Color guess : guess) {
                if (guess == null) {
                    guessed = false;
                    break;
                }
            }
            if (!guessed) {
                feedbacklabel.setTextFill(Color.GOLD);
                feedbacklabel.setText(" نسيت لون يا بشمهندس");
                return;

            }
            String feedback = giveFeedBack(guess);
            feedbacklabel.setText(feedback);
            attempts++;
            if (isCorrectGuess(guess)){
                feedbacklabel.setText("متوقعتش اقولها مبروك");
                feedbacklabel.setTextFill(Color.GREEN);
                end(true);
            } else if (attempts == max_attempts) {
                feedbacklabel.setText("لقد نفدت محاولاتك حاول من جديد");
                feedbacklabel.setTextFill(Color.RED);
                end(false);
            }
        }

    }
    private void end(boolean win) {
        timer.stop();
        if (win) {
            resultlabel.setText("you won in:"+String.format("%02d:%02d",mimutes, seconds));
            //  استغرقت وقت كد ايه علشان تفوز
            resultlabel.setTextFill(Color.GREEN);
        }else {
            resultlabel.setText("you lost after "+String.format("%02d:%02d",mimutes, seconds));
            resultlabel.setTextFill(Color.RED);
        }


    }


    private boolean isCorrectGuess(Color[] guess) {
        return Arrays.equals(guess,secretcode.toArray());
    }

    private String giveFeedBack(Color[] guess) {
        int correct_pos = 0;// counter to count num of valid pos
        int correct_col = 0;// counter to count num of colors
        for (int i = 0; i < code_length; i++) {
            if (guess[i] != null && guess[i].equals(secretcode.get(i))) {
                correct_pos++;

            } else if (guess[i] == null && secretcode.contains(guess[i])) {
                correct_col++;


            }
        }
        return "Correct position: " + correct_pos + ", Correct color but wrong position: " + correct_col;
    }
    private void pickColor(Button button, int index) {
        Color chosen = chooseRandomColor();
        button.setStyle("-fx-background-color: " + toHexString(chosen) + ";");
        guess[index] = chosen;
    }

    private String toHexString (Color c){
        int r = (int) (c.getRed() * 255);
        int g = (int) (c.getGreen() * 255);
        int b = (int) (c.getBlue() * 255);
        return String.format("#%02x%02x%02x", r, g, b);// بتحول الالوان ل hexa decmical


    }
    private Color chooseRandomColor() {
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));// معناها تختار لون عشوائي منالالوان اللي محطوطة بيتعمله ال return فوق

    }


    public void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1),e -> {
            seconds++;
            if (seconds == 60) {// هنا لو عدي ال 60 ثانية يبقي دقيقة يعني يزود
                seconds = 0;
                mimutes++;

            }
            updateTimerLabel();

        }));
        timer.setCycleCount(Timeline.INDEFINITE); // الوقت هنا مفتوح براحته
        timer.play();

    }

    private void updateTimerLabel() {
        String time = String.format("%02d:%02d", seconds, mimutes); //

    }
}






