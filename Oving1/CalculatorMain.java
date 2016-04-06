package Calculator;

/**
 * Created by ingridng on 29.01.15.
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Kalkulator");
        Group root = new Group();
        Canvas canvas = new Canvas(250, 418);
        Button button = new Button("sha-la-la-la-laaa");

        final Scene scene = new Scene(root, 250, 418, Color.BLACK);


        final Text text1 = new Text(95, 90, "1,478");
        final Text text2 = new Text(207, 150, "÷");
        final Text text3 = new Text(207, 212, "×");
        final Text text4 = new Text(207, 275, "-");
        final Text text5 = new Text(207, 335, "+");
        final Text text6 = new Text(207, 400, "=");

        final Text text7 = new Text(23, 210, "7  8  9 ");
        final Text text8 = new Text(23, 270, "4  5  6 ");
        final Text text9 = new Text(23, 336, "1  2  3 ");
        final Text text10 = new Text(23, 398, "0     . ");

        final Text text11 = new Text(23, 150, "C     ");
        final Text text12 = new Text(76, 145, "+/-  %");


        Color BLACK = new Color(0.3,0.3,0.3,1);

        text1.setFill(Color.WHITE);
        text1.setFont(Font.font(java.awt.Font.DIALOG, 60));
        root.getChildren().add(text1);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(scene);

        text2.setFill(Color.WHITE);
        text2.setFont(Font.font(java.awt.Font.MONOSPACED, 38));
        root.getChildren().add(text2);

        text3.setFill(Color.WHITE);
        text3.setFont(Font.font(java.awt.Font.MONOSPACED, 38));
        root.getChildren().add(text3);

        text4.setFill(Color.WHITE);
        text4.setFont(Font.font(java.awt.Font.MONOSPACED, 38));
        root.getChildren().add(text4);

        text5.setFill(Color.WHITE);
        text5.setFont(Font.font(java.awt.Font.MONOSPACED, 38));
        root.getChildren().add(text5);

        text6.setFill(Color.WHITE);
        text6.setFont(Font.font(java.awt.Font.MONOSPACED, 38));
        root.getChildren().add(text6);
        primaryStage.show();

        text7.setFill(BLACK);
        text7.setFont(Font.font(java.awt.Font.MONOSPACED, 34));
        root.getChildren().add(text7);

        text8.setFill(BLACK);
        text8.setFont(Font.font(java.awt.Font.MONOSPACED, 34));
        root.getChildren().add(text8);

        text9.setFill(BLACK);
        text9.setFont(Font.font(java.awt.Font.MONOSPACED, 34));
        root.getChildren().add(text9);

        text10.setFill(BLACK);
        text10.setFont(Font.font(java.awt.Font.MONOSPACED, 34));
        root.getChildren().add(text10);

        text11.setFill(BLACK);
        text11.setFont(Font.font(java.awt.Font.MONOSPACED, 34));
        root.getChildren().add(text11);

        text12.setFill(BLACK);
        text12.setFont(Font.font(java.awt.Font.MONOSPACED, 24));
        root.getChildren().add(text12);
    }

    private void drawShapes(GraphicsContext gc) {
        Color gray = new Color(0.805,0.806,0.808,1);
        Color orange = new Color(0.960,0.580,0,1);

        gc.setFill(orange);
        gc.fillRect(188, 108, 61, 61);
        gc.fillRect(188, 170, 61, 61);
        gc.fillRect(188,232,61,61);
        gc.fillRect(188,294,61,61);
        gc.fillRect(188,356,61,61);

        gc.setFill(gray);
        gc.fillRect(126, 108, 61, 61);
        gc.fillRect(126, 170, 61, 61);
        gc.fillRect(126,232,61,61);
        gc.fillRect(126,294,61,61);
        gc.fillRect(126,356,61,61);

        gc.fillRect(64, 108, 61, 61);
        gc.fillRect(64, 170, 61, 61);
        gc.fillRect(64,232,61,61);
        gc.fillRect(64,294,61,61);
        gc.fillRect(64,356,61,61);

        gc.fillRect(1, 108, 62, 61);
        gc.fillRect(1, 170, 62, 61);
        gc.fillRect(1,232,62,61);
        gc.fillRect(1,294,62,61);
        gc.fillRect(1,356,64,61);



    }
}
