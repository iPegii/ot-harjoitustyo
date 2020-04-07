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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
        
public class FinanceUi extends Application {
    
    private DaoService daoService;
    private static Stage mainStage;
    private static LoginUi loginUi;
    private static FinanceTableUi financeTableUi;
    
    @Override
    public void init() {
        
        try {
            Properties prop=new Properties();
            prop.load(new FileInputStream("config.properties"));
            String userFile= prop.getProperty("userFile");
            String financeFile = prop.getProperty("financeFile");
            UserDao userDao = new UserDaoReader(userFile);
            FinanceDao financeDao = new FinanceDaoReader(financeFile);
            this.daoService = new DaoService(userDao, financeDao);
        } catch (FileNotFoundException ex) {
            System.out.println("Error: Files missing");
        } catch (IOException e) {    
            System.out.println("Error: Failed accessing file");
        } catch (Exception e) {    
            System.out.println("Error: Something unexpected happened" + e.getMessage());
        }
        loginUi = new LoginUi(daoService);
        financeTableUi = new FinanceTableUi(daoService);

    }
    
    @Override
    public void start(Stage finance) {
        this.mainStage = finance;
      //  daoService.login("Pegi");
    //    String username = daoService.loggedIn().getUsername();
    //    User userStatus = daoService.loggedIn();
    
        Scene loginScene = new Scene(loginUi.getLoginScreen(mainStage));
        finance.setTitle("Finance Tracker");
        finance.setScene(loginScene);
        finance.setWidth(300);
        finance.setHeight(200);
        finance.show();
    }
    
    public static void setLoginScene() {
        Scene loginScene = new Scene(loginUi.getLoginScreen(mainStage));
        FinanceUi.mainStage.setWidth(300);
        FinanceUi.mainStage.setHeight(200);
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
