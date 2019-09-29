package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.*;


public class Controller {
    Client client;
    long starttime;
    long finishtime;
    int time;
    @FXML
    public TextArea servertext;
    @FXML
    public TextArea usertext;
    @FXML
    public Label lblStatus;
    @FXML
    public Button btnready;
    @FXML
    public Button btntrain;


    @FXML
    private void initialize() {
        lblStatus.setText("Choose the mode");
        usertext.setEditable(false);
    }

    public void btnreadyclick(ActionEvent actionEvent) {
        client = new Client();
        client.sendmyIP();
        client.socket = client.getsock();
        client.initio(client.socket);
        usertext.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals(servertext.getText())) {
                    finishtime = System.currentTimeMillis();
                    sendfinish();
                } else {
                    if (newValue.length() > 2) {
                        String servertextpart = servertext.getText(0, newValue.length());
                        if (newValue.equals(servertextpart)) {
                            usertext.setStyle("-fx-background-color: green");
                            usertext.setStyle("-fx-control-inner-background: #c6efa0");
                        } else {
                            usertext.setStyle("-fx-background-color: red");
                            usertext.setStyle("-fx-control-inner-background: #efa0a0");
                        }
                    }
                }
            }
        });
        client.sendready(client.out);
        lblStatus.setText("Waiting for other user");
        servertext.setText(client.gettext(client.in));
        lblStatus.setText("RACE!");
        usertext.setEditable(true);
        starttime = System.currentTimeMillis();
    }

    void sendfinish() {
        lblStatus.setText("Waiting for other user");
        usertext.setEditable(false);
        time = (int) (finishtime - starttime) / 1000;
        try {
            client.out.write(Integer.toString(time));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = "";
        try {
            result = client.in.readLine();
        } catch (IOException e) {
            lblStatus.setText("Error");
            return;
        }
        lblStatus.setText(result + "Your time is " + time + " seconds");
    }

    public void trainclick(ActionEvent actionEvent) {
        lblStatus.setText("Train Mode");
        usertext.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals(servertext.getText())) {
                    finishtime = System.currentTimeMillis();
                    finishtrain();
                } else {
                    if (newValue.length() > 2) {
                        String servertextpart = servertext.getText(0, newValue.length());
                        if (newValue.equals(servertextpart)) {
                            usertext.setStyle("-fx-background-color: green");
                            usertext.setStyle("-fx-control-inner-background: #c6efa0");
                        } else {
                            usertext.setStyle("-fx-background-color: red");
                            usertext.setStyle("-fx-control-inner-background: #efa0a0");
                        }
                    }
                }
            }
        });
        String path = "./src/texts/traintext.txt";
        BufferedReader filereader = null;
        try {
            filereader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        try {
            String line = filereader.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = filereader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        servertext.setText(sb.toString());
        usertext.setEditable(true);
        starttime = System.currentTimeMillis();
    }

    void finishtrain() {
        usertext.setEditable(false);
        time = (int) (finishtime - starttime) / 1000;
        lblStatus.setText("Your time is " + time + " seconds");
    }
}
