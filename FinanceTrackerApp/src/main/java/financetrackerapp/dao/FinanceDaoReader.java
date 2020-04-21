
package financetrackerapp.dao;

import financetrackerapp.domain.Finance;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.bson.Document;
import com.mongodb.client.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import financetrackerapp.mongodb.FinanceService;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;



public class FinanceDaoReader implements FinanceDao {
    private List<Finance> finances;
    private String[] financeSettings;
    public FinanceDaoReader(String[] fileSettings) {
        this.finances = new ArrayList<>();
        this.financeSettings = fileSettings;
        try {
            read();
        } catch (IOException ex) {
            System.out.println("IO-error while reading finances file" + ex.getMessage());
        }
    }
    
    public void init() {
        FinanceService service = new FinanceService("ohte");
        List<Finance> financesList = service.getAll();
        finances.addAll(financesList);
        save();
    }
    public void read() throws IOException {
        // make sure file exists
        File file = new File(financeSettings[0]+financeSettings[1]);
        if (!file.exists()) {
            file.createNewFile();
            init();
        } else {
            try (Reader reader = new FileReader(financeSettings[0]+financeSettings[1])) {
                Gson gson = new Gson();
                //CHECKSTYLE.OFF: WhitespaceAround - Curly braces need whitespace, ignoring makes this more readable
                Type financeListType = new TypeToken<ArrayList<Finance>>(){}.getType();
                //CHECKSTYLE.ON: WhitespaceAround
                List<Finance> financeList = gson.fromJson(reader, financeListType);
                finances.clear();
                if (financeList != null) {
                    finances.addAll(financeList);
                }
            } catch (Exception e) {
                System.out.println("Error while reading finances file: " + e);
            }
        }
    }
    public String save() {
        try (FileWriter writer = new FileWriter(new File(financeSettings[0]+financeSettings[1]))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement jsonTree = gson.toJsonTree(finances);
            gson.toJson(jsonTree, writer);
        } catch (Exception e) {
            System.out.println("Error writing to finances file: " + e);
            return "Error writing to finances file: " + e;
        } 
        return "New event added";
    }
    public List<Finance> getAll() {
        return finances;
    }
    /*
    public List<Finance> findByDate(String date) {
        String dateFormatted = date.trim();
        return finances.stream()
                .map(finance -> finance)
                .filter(finance -> finance.getDate().equals(dateFormatted))
                .collect(Collectors.toList());
    }
    */
    public String create(Finance finance) {
        finances.add(finance);
        String response = save();
        return response;
    }
    
    public String[] getFileName() {
        return financeSettings;
    }
    /*
    public void delete(String id) {
        Finance financeToRemove = null;
        for(Finance f: finances) {
            if(f.getId().equals(id)) {
              financeToRemove = f;
              break;
            }
        }
        if(financeToRemove != null) {
            finances.remove(financeToRemove);
        } else {
            System.out.println("Finance not found: " + id);
        }
        save();
    }
    */
}
