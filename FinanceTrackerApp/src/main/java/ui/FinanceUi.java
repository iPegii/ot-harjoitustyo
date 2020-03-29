/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;


import domain.Finance;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
        
public class FinanceUi extends Application {
    
    public void start(Stage finance) {
        
        finance.setTitle("Finance Tracker");
        BorderPane page = new BorderPane();
        
        // top bar seup
        BorderPane topBar = new BorderPane();
        page.setTop(topBar);
        Button button = new Button("Press this");
        Label label = new Label("Welcome to Finance Tracker");
        topBar.setRight(button);
        topBar.setPadding(new Insets(10));
        topBar.setLeft(label);
        label.setFont(new Font("Arial", 17));
        
        // display fields for creating more events
        
        
        // display finance stats
        BorderPane financeStats = new BorderPane();
        page.setBottom(financeStats);
        Label balance = new Label("Balance: 1500");
        balance.setFont( new Font("Arial", 20));

        TableView tableView = new TableView();
        TableColumn<String, Finance> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<String, Finance> event = new TableColumn<>("Event");
        event.setCellValueFactory(new PropertyValueFactory<>("event"));
        TableColumn<String, Finance> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        tableView.getColumns().add(price);
        tableView.getColumns().add(event);
        tableView.getColumns().add(date);
        
        tableView.getItems().add(new Finance(-50,"Bought very special coffee", "29.3.2020"));
        tableView.getItems().add(new Finance(-40,"Bought very special tea", "28.3.2020"));
        financeStats.setCenter(tableView);
        financeStats.setTop(balance);
        financeStats.setMargin(balance,new Insets(10));
        
 
        Scene layout = new Scene(page);
        finance.setScene(layout);
        finance.setWidth(1600);
        finance.setHeight(800);
        finance.show();
    }
}
