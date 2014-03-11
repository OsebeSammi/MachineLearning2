package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import org.ejml.simple.SimpleMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.TextField;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Controller
{
    public static Train terain;
    public javafx.scene.control.TextField input1;
    public javafx.scene.control.TextField input2;


    public void file(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("chooseFile.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void train(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("MLType.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void chooseFile(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        File file = new FileChooser().showOpenDialog(stage);
        Train train = new Train(file);
        terain = train;
    }

    public void clustering(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("cluster.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void predict(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("predict.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void backPropagation(ActionEvent actionEvent)
    {
        try
        {
            terain.backPropagation();
        }

        catch (Exception ex) {
            ex.printStackTrace(); // Or better logging.
        }
    }

    public void kMeans(ActionEvent actionEvent) {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void start(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void user(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();



        try {
            Parent root = FXMLLoader.load(getClass().getResource("input.fxml"));
            //adding text fields



            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void predictRun(ActionEvent actionEvent)
    {




        SimpleMatrix userInput = new SimpleMatrix(2,1);


        userInput.set(0,0,Double.parseDouble(input1.getText()));
        userInput.set(1,0,Double.parseDouble(input2.getText()));
        userInput.print();
        JOptionPane.showMessageDialog(null,terain.work(userInput));
    }
}
