/**
 * Created by ingridng on 02.02.15.
 */

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Date;
import java.util.Stack;

public class Survey extends Application {
    public static void main(String[] args) throws Exception { launch(args); }

    public void start(Stage stage) throws Exception {
        // Skjerm:
        stage.setScene(new Scene(new SurveyWizard(stage), 330, 150));
        stage.show();
    }
}

/** basic wizard infrastructure class */
class Wizard extends StackPane {
    private static final int UNDEFINED = -1;
    private ObservableList<WizardPage> pages = FXCollections.observableArrayList();
    private Stack<Integer> history = new Stack();
    private int  curPageIdx = UNDEFINED;

    Wizard(WizardPage... nodes) {
        pages.addAll(nodes);
        navTo(0);
        //bakgrunn:
        setStyle("-fx-padding: 10; -fx-background-color: WHITE;");
    }

    void nextPage() {
        if (hasNextPage()) {
            navTo(curPageIdx + 1);
        }
    }

    void priorPage() {
        if (hasPriorPage()) {
            navTo(history.pop(), false);
        }
    }

    boolean hasNextPage() {
        return (curPageIdx < pages.size() - 1);
    }

    boolean hasPriorPage() {
        return !history.isEmpty();
    }

    void navTo(int nextPageIdx, boolean pushHistory) {
        if (nextPageIdx < 0 || nextPageIdx >= pages.size()) return;
        if (curPageIdx != UNDEFINED) {
            if (pushHistory) {
                history.push(curPageIdx);
            }
        }

        WizardPage nextPage = pages.get(nextPageIdx);
        curPageIdx = nextPageIdx;
        getChildren().clear();
        getChildren().add(nextPage);
        nextPage.manageButtons();
    }

    void navTo(int nextPageIdx) {
        navTo(nextPageIdx, true);
    }

    void navTo(String id) {
        Node page = lookup("#" + id);
        if (page != null) {
            int nextPageIdx = pages.indexOf(page);
            if (nextPageIdx != UNDEFINED) {
                navTo(nextPageIdx);
            }
        }
    }

    public void finish() {}
    public void cancel() {}
}

abstract class WizardPage extends VBox {
    Button forrigeButton   = new Button("Forrige");
    Button nesteButton    = new Button("Neste");
    Button avbrytButton  = new Button("Avbryt");
    Button fullførButton  = new Button("Fullfør");

    WizardPage(String title) {
        getChildren().add(LabelBuilder.create().text(title).style("-fx-font-weight: bold; -fx-padding: 0 0 5 0;").build());
        setId(title);
        setSpacing(5);
        setStyle("-fx-padding:10; -fx-background-color: lightblue; -fx-border-width: 2;");

        Region spring = new Region();
        VBox.setVgrow(spring, Priority.ALWAYS);
        getChildren().addAll(getContent(), spring, getButtons());

        forrigeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                priorPage();
            }
        });
        nesteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                nextPage();
            }
        });
        avbrytButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getWizard().cancel();
            }
        });
        fullførButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getWizard().finish();
            }
        });
    }

    HBox getButtons() {
        Region spring = new Region();
        HBox.setHgrow(spring, Priority.ALWAYS);
        HBox buttonBar = new HBox(5);
        avbrytButton.setCancelButton(true);
        fullførButton.setDefaultButton(true);
        buttonBar.getChildren().addAll(spring, forrigeButton, nesteButton, avbrytButton, fullførButton);
        return buttonBar;
    }

    abstract Parent getContent();

    boolean hasNextPage() {
        return getWizard().hasNextPage();
    }

    boolean hasPriorPage() {
        return getWizard().hasPriorPage();
    }

    void nextPage() {
        getWizard().nextPage();
    }

    void priorPage() {
        getWizard().priorPage();
    }

    void navTo(String id) {
        getWizard().navTo(id);
    }

    Wizard getWizard() {
        return (Wizard) getParent();
    }

    public void manageButtons() {
        if (!hasPriorPage()) {
            forrigeButton.setDisable(true);
        }

        if (!hasNextPage()) {
            nesteButton.setDisable(true);
        }
    }
}

class SurveyWizard extends Wizard {

    Stage owner;
    public SurveyWizard(Stage owner) {
        super(new Formål(),new RomNr(),new Dato(), new Fra(),new Til(), new Sluttdato(), new Fullført());
        this.owner = owner;
    }
    public void finish() {
       System.out.println(String.format( "Avtale:"));
        System.out.println(SurveyData.repFrekvens);
        System.out.println("Dato: " + SurveyData.dato);
        System.out.println("Romnummer: " + SurveyData.Romnr);
        System.out.println("Tidspunkt: " + SurveyData.fra +"-"+ SurveyData.til);


        owner.close();
    }
    public void cancel() {
        System.out.println("Avbrutt");
        owner.close();
    }
}

class SurveyData {

    public static int repFrekvens;
    public static String dato;
    public static String fra;
    public static String til;
    public static String formål;
    public static String Romnr;

}

/** SAMLER INFORMASJON ON AVTALEN: */

class Dato extends WizardPage {
    public Dato() {
        super("Dato");
    }

    Parent getContent() {
        TextArea textArea = TextAreaBuilder.create().wrapText(false).promptText("").build();
        nesteButton.setDisable(true);
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                nesteButton.setDisable(newValue.charAt(6)!='2'||newValue.charAt(7)!='0'||newValue.length()<10||Integer.parseInt(newValue.substring(0, 2))>31
                        ||(Integer.parseInt(newValue.substring(3,5)))>12 ||(Integer.parseInt(newValue.substring(6,10))<2015)||((newValue.charAt(2)!='.')
                        || newValue.charAt(5)!='.') );

            SurveyData.dato = newValue;
            }
        });
        return VBoxBuilder.create().spacing(5).children(
                new Label("Oppgi dato: (dd.mm.yyyy)"),
                textArea
        ).build();

    }
}
//Formatert String, først bygg som består av bokstaver, mellomrom, bindestrek og tall, og så romnummer, som er et heltall. Eksempel (kontoret mitt): IT-vest 115
class RomNr extends WizardPage {
    public RomNr() {
        super("Romnr");
    }

    Parent getContent() {
        TextArea textArea = TextAreaBuilder.create().wrapText(false).promptText("..").build();
        nesteButton.setDisable(false);
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                nesteButton.setDisable(!newValue.contains("-")||!newValue.contains(" "));
                SurveyData.Romnr = newValue;
            }
        });

        return VBoxBuilder.create().spacing(5).children(
                new Label("Oppgi Romnummer?"),
                textArea
        ).build();

    }
}

class Formål extends WizardPage {
    public Formål() {
        super("Formål");
    }

    Parent getContent() {
        TextArea textArea = TextAreaBuilder.create().wrapText(false).promptText("Formål for avtale").build();
        nesteButton.setDisable(false);
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                nesteButton.setDisable(newValue.isEmpty());
            }
        });
        return VBoxBuilder.create().spacing(5).children(
                new Label("Hva er avtalen sitt formål? (String)"),
                textArea
        ).build();

    }
}
//time:min. Eksempel: 10:15
class Fra extends WizardPage {
    public Fra() {
        super("Fra");
    }

    Parent getContent() {
        TextArea textArea = TextAreaBuilder.create().wrapText(false).promptText("Oppgi statitidspunk (hh:mm)?").build();
        nesteButton.setDisable(false);
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                nesteButton.setDisable(newValue.length()!=5||Integer.parseInt(newValue.substring(0,2))>24||Integer.parseInt(newValue.substring(3,5))>59);
                SurveyData.fra=newValue;
            }
        });
        return VBoxBuilder.create().spacing(5).children(
                new Label("Starttidspunkt?"),
                textArea
        ).build();

    }
}
// time:min, må være etter fra-tidspunkt (møter over midnatt støttes ikke, så begge er på samme dato. Eksempel: 12:00
class Til extends WizardPage {
    public Til() {
        super("sluttidspunkt");
    }

    Parent getContent() {
        TextArea textArea = TextAreaBuilder.create().wrapText(false).promptText("Sluttidspunkt").build();
        nesteButton.setDisable(false);
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                nesteButton.setDisable((newValue.length() != 5) || (Integer.parseInt(newValue.substring(0, 2)) > 24) || Integer.parseInt(newValue.substring(3, 5))>12);
                //MÅ OGSÅ VÆRE SENERE
            }
        });
        return VBoxBuilder.create().spacing(5).children(
                new Label("Sluttidspunkt"),
                textArea
        ).build();

    }
}
//Brukes hvis hendelse er repeterende (altså Repetisjonsfrekvens > 0) og angir etter hvilken dato repetisjonen er over.
class Sluttdato extends WizardPage {
    public Sluttdato() {
        super("Sluttdato");
    }
    boolean senere;

    Parent getContent() {
        TextArea textArea = TextAreaBuilder.create().wrapText(false).promptText("Formål for avtale").build();
        nesteButton.setDisable(false);
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                nesteButton.setDisable(senere||newValue.charAt(6)!='2'||newValue.charAt(7)!='0'||newValue.length()<10||Integer.parseInt(newValue.substring(0, 2))>31
                        ||(Integer.parseInt(newValue.substring(3,5)))>12 ||(Integer.parseInt(newValue.substring(6,10))<2015)||((newValue.charAt(2)!='.')
                        || newValue.charAt(5)!='.'));
                if(Integer.parseInt(newValue.substring(6,10))==Integer.parseInt(SurveyData.dato.substring(6,10))){
                    if(Integer.parseInt(newValue.substring(3,5))>Integer.parseInt(SurveyData.dato.substring(3,5))){
                        if(Integer.parseInt(newValue.substring(0,2))>Integer.parseInt(SurveyData.dato.substring(0,2))){senere=true;}}}
            }
        });
        return VBoxBuilder.create().spacing(5).children(
                new Label("Oppgi sluttdato? (dd.mm.yyyy)"),
                textArea
        ).build();

    }
}
//tall som angir tid (dager) til neste i den repeterende rekka. 0 eller negativt tall angir ikke-repeterende.
class RepFrekvens extends WizardPage {
    public RepFrekvens() {
        super("RepFrekvens");
    }

    Parent getContent() {
        TextArea textArea = TextAreaBuilder.create().wrapText(false).promptText(".").build();
        nesteButton.setDisable(false);
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                nesteButton.setDisable(newValue.charAt(0)!='1'||newValue.charAt(0)!='2'||newValue.charAt(0)!='3'||newValue.charAt(0)!='4'
                        ||newValue.charAt(0)!='5'||newValue.charAt(0)!='6'||newValue.charAt(0)!='7'||newValue.charAt(0)!='8'||newValue.charAt(0)!='9'||
                        newValue.charAt(1)!='1'||newValue.charAt(1)!='2'||newValue.charAt(1)!='3'||newValue.charAt(1)!='4'
                                ||newValue.charAt(1)!='5'||newValue.charAt(1)!='6'||newValue.charAt(1)!='7'||newValue.charAt(1)!='8'||newValue.charAt(1)!='9');
                SurveyData.repFrekvens = Integer.parseInt(newValue);

            }
        });
        return VBoxBuilder.create().spacing(5).children(
                new Label("Repetisjonsfrekvens"),
                textArea
        ).build();

    }
}


//Avsluttningside: "Avtalen er registrert":
class Fullført extends WizardPage {
    public Fullført() {
        super("Fullført");
    }

    Parent getContent() {
        StackPane stack = StackPaneBuilder.create().children(
                new Label("Avtalen er registrert!")).build();
        VBox.setVgrow(stack, Priority.ALWAYS);
        return stack;
    }
}
