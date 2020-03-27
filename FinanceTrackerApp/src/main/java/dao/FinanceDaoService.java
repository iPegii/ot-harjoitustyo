
package dao;

import domain.Finance;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class FinanceDaoService {
    
    private List<Finance> finances;
    private String file;
    public FinanceDaoService(String file) {
        this.finances = new ArrayList<>();
        this.file = file;
        read();
    }
    
    public void read() {
        try {
            File file = new File("finances.txt");
            Scanner myReader = new Scanner(file);
            while(myReader.hasNextLine()) {
                String[] parts = myReader.nextLine().split(";");
                Finance f = new Finance(Integer.valueOf(parts[0]), parts[1], parts[2]);
                finances.add(f);
            }
            myReader.close();
        } catch(Exception e) {
            System.out.println("Error reading the file: " + e);
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            FileWriter writer = new FileWriter(new File(file));
            for(Finance f: finances) {
                writer.write(f.getPrice()+";"+f.getEvent()+";"+f.getDate());
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to the file: " + e);
            e.printStackTrace();
        }
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
    
    public Finance create(Finance finance) {
        finances.add(finance);
        save();
        return finance;
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
