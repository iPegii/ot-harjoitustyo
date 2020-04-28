/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.ui;


import financetrackerapp.dao.FinanceDao;
import financetrackerapp.dao.FinanceDaoReader;
import financetrackerapp.dao.UserDao;
import financetrackerapp.dao.UserDaoReader;
import financetrackerapp.domain.DaoService;
import financetrackerapp.mongodb.FinanceService;
import financetrackerapp.mongodb.UserService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
        
public class FinanceUi extends Application {
    
    private DaoService daoService;
    private static Stage mainStage;
    private static LoginUi loginUi;
    private static FinanceTableUi financeTableUi;
    
    @Override
    public void init() {
        
        try {
            File folder = new File("resources");
            if(!folder.exists() && !folder.isDirectory()) {
            folder.mkdir();
            }
            Properties prop=new Properties();
            prop.load(new FileInputStream("config.properties"));
            String userFile= prop.getProperty("userFile");
            String financeFile = prop.getProperty("financeFile");
            String[] usersSettings = new String[] {"resources/", userFile};
            String[] financesSettings = new String[] {"resources/", financeFile};
            UserService userService = new UserService("ohte");
            FinanceService financeService = new FinanceService("ohte");
            UserDao userDao = new UserDaoReader(usersSettings, userService);
            FinanceDao financeDao = new FinanceDaoReader(financesSettings, financeService);
            
            this.daoService = new DaoService(userDao, financeDao);
        } catch (FileNotFoundException ex) {
            System.out.println("Error: Files missing: " + ex.getMessage());
        } catch (IOException e) {    
            System.out.println("Error: Failed accessing file: " + e.getMessage());
        } catch (Exception e) {    
            System.out.println("Error: Something unexpected happened: " + e.getMessage());
            e.printStackTrace();
        }
        loginUi = new LoginUi(daoService);
        financeTableUi = new FinanceTableUi(daoService);

    }

    @Override
    public void start(Stage finance) {
        this.mainStage = finance;
        Scene loginScene = new Scene(loginUi.getLoginScreen(mainStage));
        finance.setTitle("Finance Tracker");
        finance.setScene(loginScene);
        finance.setWidth(300);
        finance.setHeight(180);
        finance.show();
    }
    
    public static void setLoginScene() {
        Scene loginScene = new Scene(loginUi.getLoginScreen(mainStage));
        FinanceUi.mainStage.setWidth(300);
        FinanceUi.mainStage.setHeight(200);
        FinanceUi.mainStage.setX(800);
        FinanceUi.mainStage.setY(350);
        FinanceUi.mainStage.setScene(loginScene);
    }
    
    public static void setFinanceScene() {
        Scene financeScene = new Scene(financeTableUi.getFinanceTable(mainStage));
        FinanceUi.mainStage.setX(100);
        FinanceUi.mainStage.setY(50);
        FinanceUi.mainStage.setWidth(1600);
        FinanceUi.mainStage.setHeight(800);
        FinanceUi.mainStage.setScene(financeScene);
    }

    
}
