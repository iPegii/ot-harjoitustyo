
package financetrackerapp.dao;

import financetrackerapp.domain.Finance;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class FinanceDaoReader implements FinanceDao {
    
    private List<Finance> finances;
    private String fileName;
    public FinanceDaoReader(String fileName) {
        this.finances = new ArrayList<>();
        this.fileName = fileName;
        read();
    }
    
    public void read() {
        
        try {
            File file = new File(fileName);
            // make sure file exists
            if(!file.exists()) {
            file.createNewFile(); 
            }
            
            Scanner myReader = new Scanner(file);
            while(myReader.hasNextLine()) {
                String[] parts = myReader.nextLine().split(";");
                Finance f = new Finance(parts[0],Integer.valueOf(parts[1]), parts[2], parts[3]);
                finances.add(f);
            }
            myReader.close();
        } catch(Exception e) {
            System.out.println("Error reading the file: " + e);
            e.printStackTrace();
        }
    }
    
    public String save() {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(fileName));
            for(Finance f: finances) {
                writer.write(f.getUsername()+";"+f.getPrice()+";"+f.getEvent()+";"+f.getDate() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e);
            return "Error writing to file" + e;
        } finally {
            if(writer != null) {
                try {
                writer.close(); 
                } catch(IOException ex) {
                    System.out.println("Error while trying to access the file: " + ex);
                    return "Error accessing finances file: " + ex;
                }  
        }
    }
        return "New event added";
    }
    
    public List<Finance> getAll() {
        return finances;
    }
    
    public List<Finance> findByDate(String date) {
        String dateFormatted = date.trim();
        return finances.stream()
                .map(finance -> finance)
                .filter(finance -> finance.getDate().equals(dateFormatted))
                .collect(Collectors.toList());
    }
    
    public String create(Finance finance) {
        finances.add(finance);
        String response = save();
        return response;
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
