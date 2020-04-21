
package financetrackerapp.dao;

import financetrackerapp.domain.Finance;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import financetrackerapp.mongodb.FinanceService;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;



public class FinanceDaoReader implements FinanceDao {
    private List<Finance> finances;
    private String[] financeSettings;
    private FinanceService financeService;
    
    public FinanceDaoReader(String[] fileSettings, FinanceService financeService) {
        this.finances = new ArrayList<>();
        this.financeSettings = fileSettings;
        this.financeService = financeService;
        try {
            read();
        } catch (IOException ex) {
            System.out.println("IO-error while reading finances file" + ex.getMessage());
        }
    }
    
    public void init() {
        List<Finance> financesList = financeService.getAll();
        finances.addAll(financesList);
        save();
    }
    public void read() throws IOException {
        // make sure file exists
        File file = new File(financeSettings[0] + financeSettings[1]);
        if (!file.exists()) {
            file.createNewFile();
            init();
        } else {
            try (Reader reader = new FileReader(financeSettings[0] + financeSettings[1])) {
                Gson gson = new Gson();
                //CHECKSTYLE.OFF: WhitespaceAround - Curly braces need whitespace, ignoring makes this more readable
                Type financeListType = new TypeToken<ArrayList<Finance>>(){}.getType();
                //CHECKSTYLE.ON: WhitespaceAround
                List<Finance> financeList = gson.fromJson(reader, financeListType);
                finances.clear();
                if (financeList != null) {
                    finances.addAll(financeList);
                }
                init();
            } catch (Exception e) {
                System.out.println("Error while reading finances file: " + e);
            }
        }
    }
    public String save() {
        try (FileWriter writer = new FileWriter(new File(financeSettings[0] + financeSettings[1]))) {
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
    public String create(Finance finance) {
        finances.add(finance);
        String response = save();
        return response;
    }
    
    public String[] getFileName() {
        return financeSettings;
    }
    
    public FinanceService getDatabase() {
        return financeService;
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
