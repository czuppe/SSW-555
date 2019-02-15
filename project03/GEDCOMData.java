/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author nraj39
 */
public class GEDCOMData {

    public static Map<String, PersonEntity> getIndividuals(){
        return GEDCOMData.Individuals;
    }
    
    public static Map<String, PersonEntity> Individuals;
    public static Map<String, FamilyEntity> Families;

    public GEDCOMData() {
        Individuals = new HashMap<String, PersonEntity>();
        Families = new HashMap<String, FamilyEntity>();
    }

    public void Validate(List<ValidationResult> results) {
        if (results == null) {
            return;
        }

        loadReferenceEntitiesToFamily();

        GEDCOMDataValidator.uniqueIDsCheck(this, results);
        GEDCOMDataValidator.uniqueNameAndBirthDateCheck(this, results);
        
        Iterator individualsIterator = Individuals.entrySet().iterator();
        while (individualsIterator.hasNext()) {
            Map.Entry<String, PersonEntity> individual = (Map.Entry)individualsIterator.next();            
            individual.getValue().validate(results);            
        }
        
        Iterator familiesIterator = Families.entrySet().iterator();
        while (familiesIterator.hasNext()) {
            Map.Entry<String, FamilyEntity> family = (Map.Entry)familiesIterator.next();            
            family.getValue().validate(results);            
        }
    }

    private void loadReferenceEntitiesToFamily() {
        Iterator familiesIterator = Families.entrySet().iterator();
        while (familiesIterator.hasNext()) {
            Map.Entry<String, FamilyEntity> familyEntry = (Map.Entry)familiesIterator.next();
            FamilyEntity family = familyEntry.getValue();
            family.Husband = Individuals.get(family.HusbandId);
            family.Wife = Individuals.get(family.WifeId);
            
            for (String childId : family.ChildrenId) {
                PersonEntity child = Individuals.get(childId);
                if (child != null) {
                    family.Children.add(child);
                }
            }            
        }        
        
        Iterator iterator = Individuals.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PersonEntity> individual = (Map.Entry)iterator.next();            
            Map<String, IEntity> map = new HashMap<String, IEntity>();
            Map<String, FamilyEntity> x = Families.entrySet().stream().filter(fe -> fe.getValue().Husband != null && fe.getValue().Husband.getId().equals(individual.getKey())).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
            
            Iterator i = x.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<String, FamilyEntity> entry = (Map.Entry)i.next();  
                map.put(entry.getKey(), IEntity.class.cast(entry.getValue()));                
            }

            if (individual.getValue().Families == null) {
                //Object[] y = Families.entrySet().stream().filter(fe -> fe.getValue().Wife != null && fe.getValue().Wife.getId().equals(individual.getKey())).toArray();
                Map<String, FamilyEntity> y = Families.entrySet().stream().filter(fe -> fe.getValue().Wife != null && fe.getValue().Wife.getId().equals(individual.getKey())).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
                
                Iterator j = y.entrySet().iterator();
                while (j.hasNext()) {
                    Map.Entry<String, FamilyEntity> entry = (Map.Entry)j.next();  
                    map.put(entry.getKey(), IEntity.class.cast(entry.getValue()));                
                }                
            }
            individual.getValue().Families = new ArrayList(map.values());            
        }        
    }

    @Override

    public String toString() {
        return null;
    }   

    public String toFamiliesText() {
        StringBuilder msg = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        msg.append("ID, Married, Divorced, Husband ID, Husband Name, Wife ID, Wife Name, Children\n");
        
        Iterator familiesIterator = Families.entrySet().iterator();
        while (familiesIterator.hasNext()) {
            Map.Entry<String, FamilyEntity> familyEntry = (Map.Entry)familiesIterator.next();
            FamilyEntity family = familyEntry.getValue();
            StringBuilder childMsg = new StringBuilder();
            if (family.Children != null) {
                for (PersonEntity ce : family.Children) {
                    if (childMsg.length() == 0) {
                        childMsg.append(ce.getId());
                    } else {
                        childMsg.append(", ").append(ce.getId());
                    }
                }
            }
            msg.append(family.getId() + ", "
                    + (family.MarriageDate != null ? format.format(family.MarriageDate) : "NA") + ", "
                    + (family.DivorceDate != null ? format.format(family.DivorceDate) : "NA") + ", "
                    + (family.HusbandId != null ? family.HusbandId : "NA") + ", "
                    + (family.Husband != null ? family.Husband.FullName : "NA") + ", "
                    + (family.WifeId != null ? family.WifeId : "NA") + ", "
                    + (family.Wife != null ? family.Wife.FullName : "NA") + ", "
                    + (childMsg.length() == 0 ? "NA" : "{" + childMsg.toString() + "}") + "\n"
            );
        }        
        return msg.toString();
    }

    public String toPersonsText() {
        StringBuilder msg = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        msg.append("ID, Name, Gender, Birthday, Age, Alive, Death, Child, Spouse\n");
        
        Iterator iterator = Individuals.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PersonEntity> individual = (Map.Entry)iterator.next();
            PersonEntity entity = individual.getValue();
            StringBuilder childMsg = new StringBuilder();
            entity.Families.forEach(fe -> {
                if (fe != null && fe.Children != null && !fe.Children.isEmpty()) {
                    fe.Children.forEach(c -> {
                        if (childMsg.length() == 0) {
                            childMsg.append(c.getId());
                        } else {
                            childMsg.append(", ").append(c.getId());
                        }
                    });
                }
            });

            StringBuilder spouseMsg = new StringBuilder();
            entity.Families.forEach(fe -> {
                if (fe != null) {
                    if (fe.Husband != null && fe.Husband.getId().equals(entity.getId()) && fe.Wife != null) {
                        if (spouseMsg.length() == 0) {
                            spouseMsg.append(fe.Wife.getId());
                        } else {
                            spouseMsg.append(", ").append(fe.Wife.getId());
                        }
                    } else if (fe.Wife != null && fe.Wife.getId().equals(entity.getId()) && fe.Husband != null) {
                        if (spouseMsg.length() == 0) {
                            spouseMsg.append(fe.Husband.getId());
                        } else {
                            spouseMsg.append(", ").append(fe.Husband.getId());
                        }
                    }
                }
            });                  
            
            msg.append(entity.getId() + ", " + entity.FullName + ", " + entity.Gender + ", "
                    + format.format(entity.BirthDate) + ", " + entity.Age + ", " + (entity.DeathDate == null ? true : false) + ", "
                    + (entity.DeathDate != null ? format.format(entity.DeathDate) : "NA") + ", "
                    + (childMsg.length() == 0 ? "NA" : "{" + childMsg.toString() + "}") + ", "
                    + (spouseMsg.length() == 0 ? "NA" : "{" + spouseMsg.toString() + "}") + "\n");         
        }
               
        return msg.toString();
    }
}
