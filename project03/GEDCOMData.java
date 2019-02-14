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
    //public static List<PersonEntity> Individuals;
    public List<FamilyEntity> Families;

    public GEDCOMData() {
        Individuals = new HashMap<String, PersonEntity>();
        //Individuals = new ArrayList<PersonEntity>();
        Families = new ArrayList<FamilyEntity>();
    }

    public void Validate(List<ValidationResult> results) {
        if (results == null) {
            return;
        }

        loadReferenceEntitiesToFamily();

        GEDCOMDataValidator.uniqueIDsCheck(this, results);
        GEDCOMDataValidator.uniqueNameAndBirthDateCheck(this, results);
        
        Iterator iterator = Individuals.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PersonEntity> individual = (Map.Entry)iterator.next();            
            individual.getValue().validate(results);            
        }

        Families.forEach((entity) -> {
            entity.validate(results);
        });
    }

    private void loadReferenceEntitiesToFamily() {
        Families.forEach((family) -> {
            family.Husband = Individuals.get(family.HusbandId);
            family.Wife = Individuals.get(family.WifeId);

            for (String childId : family.ChildrenId) {
                PersonEntity child = Individuals.get(childId);
                if (child != null) {
                    family.Children.add(child);
                }
            }
        });
        
        Iterator iterator = Individuals.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PersonEntity> individual = (Map.Entry)iterator.next();            
            Map<String, IEntity> map = new HashMap<String, IEntity>();
            Object[] x = Families.stream().filter(fe -> fe.Husband != null && fe.Husband.getId().equals(individual.getKey())).toArray();
            for (Object entity : x) {
                map.put(IEntity.class.cast(entity).getId(), IEntity.class.cast(entity));
            }

            if (individual.getValue().Families == null) {
                Object[] y = Families.stream().filter(fe -> fe.Wife != null && fe.Wife.getId().equals(individual.getKey())).toArray();
                for (Object entity : y) {
                    if (!map.containsKey(IEntity.class.cast(entity).getId())) {
                        map.put(IEntity.class.cast(entity).getId(), IEntity.class.cast(entity));
                    }
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
        for (FamilyEntity entity : Families) {
            StringBuilder childMsg = new StringBuilder();
            if (entity.Children != null) {
                for (PersonEntity ce : entity.Children) {
                    if (childMsg.length() == 0) {
                        childMsg.append(ce.getId());
                    } else {
                        childMsg.append(", ").append(ce.getId());
                    }
                }
            }
            msg.append(entity.getId() + ", "
                    + (entity.MarriageDate != null ? format.format(entity.MarriageDate) : "NA") + ", "
                    + (entity.DivorceDate != null ? format.format(entity.DivorceDate) : "NA") + ", "
                    + (entity.HusbandId != null ? entity.HusbandId : "NA") + ", "
                    + (entity.Husband != null ? entity.Husband.FullName : "NA") + ", "
                    + (entity.WifeId != null ? entity.WifeId : "NA") + ", "
                    + (entity.Wife != null ? entity.Wife.FullName : "NA") + ", "
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
