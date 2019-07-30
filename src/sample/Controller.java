package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class Controller {

    @FXML
    private TextArea ServerStatusConsole;

    public void initialize() {

        ServerStatusConsole.setText("Server\n");
    }

    public void setServerStatusConsole(String text)
    {
        ServerStatusConsole.appendText(text);
    }


}

