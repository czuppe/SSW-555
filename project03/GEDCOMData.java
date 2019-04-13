/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

/**
 *
 * @author nraj39
 */
public class GEDCOMData {

    private Map<String, PersonEntity> Individuals;
    private Map<String, FamilyEntity> Families;

    private Map<String, PersonEntity> IndividualsDuplicates;
    private Map<String, FamilyEntity> FamiliesDuplicates;

    public Map<String, PersonEntity> getIndividualsDuplicates() {
        return IndividualsDuplicates;
    }

    public Map<String, FamilyEntity> getFamiliesDuplicates() {
        return FamiliesDuplicates;
    }

    public Map<String, PersonEntity> getIndividuals() {
        return Individuals;
    }
	
    public void setIndividuals(Map<String, PersonEntity> individuals){
        this.Individuals = individuals;
    }

    public Map<String, FamilyEntity> getFamilies() {
        return Families;
    }

    public GEDCOMData() {
        Individuals = new HashMap<String, PersonEntity>();
        Families = new HashMap<String, FamilyEntity>();

        IndividualsDuplicates = new HashMap<String, PersonEntity>();
        FamiliesDuplicates = new HashMap<String, FamilyEntity>();
    }

    public void addFamily(FamilyEntity entity) {
        if (entity == null) {
            return;
        }

        entity.setGEDCOMData(this);
        if (Families.containsKey(entity.getId())) {
            FamiliesDuplicates.put(entity.getId(), entity);
        }
        Families.put(entity.getId(), entity);
    }

    public void addIndividual(PersonEntity entity) {
        if (entity == null) {
            return;
        }

        entity.setGEDCOMData(this);
        if (Individuals.containsKey(entity.getId())) {
            IndividualsDuplicates.put(entity.getId(), entity);
        }
        Individuals.put(entity.getId(), entity);
    }

    public void Validate(List<ValidationResult> results) {
        if (results == null) {
            return;
        }

        //GEDCOM Validations
        GEDCOMDataValidator.maleLastNameCheck(this, results);
        GEDCOMDataValidator.uniqueIDsCheck(this, results);
        GEDCOMDataValidator.uniqueNameAndBirthDateCheck(this, results);
	      GEDCOMDataValidator.uniqueFirstNamesinFamily(this, results);
        GEDCOMDataValidator.noMarriagesToChildrenCheck(this, results);
        GEDCOMDataValidator.siblingsShouldNotMarryCheck(this, results);

        //Individual validations
        Individuals.forEach((k, entity) -> {
            entity.validate(results);
        });

        //Familty validations
        Families.forEach((k, entity) -> {
            entity.validate(results);
        });
    }

    public void loadReferenceEntitiesToFamily() {
        Families.forEach((k, family) -> {
            family.Husband = Individuals.get(family.HusbandId);
            family.Wife = Individuals.get(family.WifeId);

            for (String childId : family.ChildrenId) {
                PersonEntity child = Individuals.get(childId);
                if (child != null) {
                    family.Children.add(child);
                    child.ChildOfFamily = family;
                }
            }
        });

        Individuals.forEach((k, individual) -> {
            Map<String, IEntity> map = new HashMap<String, IEntity>();
            Map<String, FamilyEntity> x = Families.entrySet().stream().filter(fe -> fe.getValue().Husband != null && fe.getValue().Husband.getId().equals(k)).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

            Iterator i = x.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<String, FamilyEntity> entry = (Map.Entry) i.next();
                map.put(entry.getKey(), IEntity.class.cast(entry.getValue()));
            }

            if (individual.Families == null) {
                Map<String, FamilyEntity> y = Families.entrySet().stream().filter(fe -> fe.getValue().Wife != null && fe.getValue().Wife.getId().equals(k)).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

                Iterator j = y.entrySet().iterator();
                while (j.hasNext()) {
                    Map.Entry<String, FamilyEntity> entry = (Map.Entry) j.next();
                    map.put(entry.getKey(), IEntity.class.cast(entry.getValue()));
                }
            }
            individual.Families = new ArrayList(map.values());
        });
    }

    @Override

    public String toString() {
        return null;
    }

    public String toFamiliesText() {
        
        StringBuilder msg = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        msg.append("ID, Married, Divorced, Husband ID, Husband Name, Wife ID, Wife Name, Children\n");
        
        Families.forEach((k, entity) -> {
            
            StringBuilder childMsg = new StringBuilder();
            
            if (entity.ChildrenId != null) {
                Utility.SortChildrenByBirthdate(entity);
                childMsg.append(entity.ChildrenId);
                
            }
            msg.append(entity.getId() + ", "
                    + (entity.MarriageDate != null ? format.format(entity.MarriageDate) : "NA") + ", "
                    + (entity.DivorceDate != null ? format.format(entity.DivorceDate) : "NA") + ", "
                    + (entity.HusbandId != null ? entity.HusbandId : "NA") + ", "
                    + (entity.Husband != null ? entity.Husband.FullName : "NA") + ", "
                    + (entity.WifeId != null ? entity.WifeId : "NA") + ", "
                    + (entity.Wife != null ? entity.Wife.FullName : "NA") + ", "
                    + (entity.ChildrenId.size() == 0 ? "NA" : childMsg.toString()) + "\n"
            );
        });
        return msg.toString();
    }
    
    public String toPersonsText(Collection<PersonEntity> persons)
    {
        StringBuilder msg = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        msg.append("ID, Name, Gender, Birthday, Age, Alive, Death, Child, Spouse\n");

        persons.forEach(entity -> {
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
        });

        return msg.toString();    	
    }

    public String toPersonsText() {
        return toPersonsText(Individuals.values());
    }
    
    //US29 List Deceased 
    public String listDeceased()
    {
    	 List<PersonEntity> listDeceased = new ArrayList<PersonEntity>();
    	 
	     Individuals.forEach((s, entity) -> {
	    	 if (entity.DeathDate != null ) {
	    		 listDeceased.add(entity);
	    		 
	    	 }	          
    	 });
    	 return toPersonsText(listDeceased);
    }
    
    //US30 (bella) List Living Married Individuals
    public String listLivingMarried()
    {
    	 List<PersonEntity> livingMarriedPersons = new ArrayList<PersonEntity>();
    	 
    	 Families.forEach((s, entity) -> {
	    	 LocalDate marriageDate = entity.MarriageDate != null ? Utility.ToLocalDate(entity.MarriageDate) : null; 
	         LocalDate divorceDate = entity.DivorceDate != null ? Utility.ToLocalDate(entity.DivorceDate) : null; 
	
	         LocalDate husbandDeathdate = entity.Husband != null ? Utility.ToLocalDate(entity.Husband.DeathDate) : null;            
	         LocalDate wifeDeathdate = entity.Wife != null ? Utility.ToLocalDate(entity.Wife.DeathDate) : null;
	         
	         //check if wife is alive and married
	         if (wifeDeathdate == null & marriageDate != null & divorceDate == null) {
	             livingMarriedPersons.add(entity.Wife);   
	         }
	         
	         //check if husband is alive and married
	         if (husbandDeathdate == null & marriageDate != null & divorceDate == null) {
	        	 livingMarriedPersons.add(entity.Husband);
	         }
    	 });
    	 return toPersonsText(livingMarriedPersons);
    }	
    
    //US31 (bella) List Living Single Individuals
    public String listLivingSingle()
    {
    	 List<PersonEntity> livingSinglePersons = new ArrayList<PersonEntity>();
    	 
	     Individuals.forEach((s, entity) -> {
	    	 
	    	 if (entity.DeathDate == null ) {
	    		 boolean single[] = {true};
	    		 entity.Families.forEach(fe -> {
	    			 if (fe.DivorceDate == null)
	    				 single[0] = false;
	    		 });
	    		 if (single[0]) {
	    			 livingSinglePersons.add(entity);
	    		 }
	    	 }
	         
    	 });
    	 return toPersonsText(livingSinglePersons);
    }
	
//US39 (Craig) - List upcoming anniversaries
public String toMarriageText(Collection<FamilyEntity> family) {
        
        StringBuilder msg = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        msg.append("ID, Married, Divorced, Husband ID, Husband Name, Wife ID, Wife Name, Children\n");
        
        Families.forEach((k, entity) -> {
            
            StringBuilder childMsg = new StringBuilder();
            
            if (entity.ChildrenId != null) {
                Utility.SortChildrenByBirthdate(entity);
                childMsg.append(entity.ChildrenId);
                
            }
            if(entity.Marriage != null && entity.Divorce == null) {
            msg.append(entity.getId() + ", "
                    + (entity.MarriageDate != null ? format.format(entity.MarriageDate) : "NA") + ", "
                    + (entity.DivorceDate != null ? format.format(entity.DivorceDate) : "NA") + ", "
                    + (entity.HusbandId != null ? entity.HusbandId : "NA") + ", "
                    + (entity.Husband != null ? entity.Husband.FullName : "NA") + ", "
                    + (entity.WifeId != null ? entity.WifeId : "NA") + ", "
                    + (entity.Wife != null ? entity.Wife.FullName : "NA") + ", "
                    + (entity.ChildrenId.size() == 0 ? "NA" : childMsg.toString()) + "\n"
            );
            }
        });
        return msg.toString();
    }
    
public String toMarriageText() {
    return toMarriageText(Families.values());
}

//List anniversaries
    public String listAnniversaries() {
    	
    	Collection<FamilyEntity> annivList = new ArrayList<FamilyEntity>();
    	
    	Calendar calendar = Calendar.getInstance();
     	 Date sysdate = calendar.getTime();
     	 
     	 
     	Families.forEach((k, entity) -> {
     		 
     		 if(entity.Marriage == null || entity.Divorce != null) {
     			 return;
     		 }
     		 
    		int diffDays = entity.MarriageDate.getDate() - sysdate.getDate();
    		int diffMonth = entity.MarriageDate.getMonth() - sysdate.getMonth();
    		
    		if(diffDays < 30 && diffMonth == 0) {
    		annivList.add(entity);
    			}
    	});
     	
    	return toMarriageText(annivList);
    }	
}
