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
import java.time.ZoneId;

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

    public String toPersonsText(Collection<PersonEntity> persons) {
        StringBuilder msg = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        msg.append("ID, Name, Gender, Birthday, Age, Alive, Death, Child, Spouse\n");

        persons.forEach(entity -> {
            StringBuilder childMsg = new StringBuilder();

            if (entity != null && entity.Families != null) {
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
            }

            StringBuilder spouseMsg = new StringBuilder();
            if (entity != null && entity.Families != null) {
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
            }
            if (entity != null) {
                msg.append(entity.getId() + ", " + entity.FullName + ", " + entity.Gender + ", "
                        + format.format(entity.BirthDate) + ", " + entity.Age + ", " + (entity.DeathDate == null ? true : false) + ", "
                        + (entity.DeathDate != null ? format.format(entity.DeathDate) : "NA") + ", "
                        + (childMsg.length() == 0 ? "NA" : "{" + childMsg.toString() + "}") + ", "
                        + (spouseMsg.length() == 0 ? "NA" : "{" + spouseMsg.toString() + "}") + "\n");
            }
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
    
    //US35	(bella) List recent births	List all people in a GEDCOM file who were born in the last 30 days
    public String listRecentBirths()
    {
   	 List<PersonEntity> listRecentBirths = new ArrayList<PersonEntity>();
   	 Date todaysDate = Utility.getTodaysDate();
   	 
   	 Calendar cal = Calendar.getInstance();
   	 cal.add(Calendar.DATE, -30);
   	 Date dateBefore30Days = cal.getTime();
   	
	     Individuals.forEach((s, entity) -> {
	    	 if (entity.BirthDate != null && entity.BirthDate.before(todaysDate)) {
                 if (entity.BirthDate.after(dateBefore30Days)) {
                	 listRecentBirths.add(entity);
                 }          
   	 }});
   	 return toPersonsText(listRecentBirths);
   }
    
    //US36	(bella) List recent deaths	List all people in a GEDCOM file who died in the last 30 days
    public String listRecentDeaths()
    {
      	 List<PersonEntity> listRecentDeaths = new ArrayList<PersonEntity>();
      	 Date todaysDate = Utility.getTodaysDate();
      	 
      	 Calendar cal = Calendar.getInstance();
      	 cal.add(Calendar.DATE, -30);
      	 Date dateBefore30Days = cal.getTime();
      	 
   	     Individuals.forEach((s, entity) -> {
   	    	if (entity.DeathDate != null && entity.DeathDate.before(todaysDate)) {
   	    		if (entity.DeathDate.after(dateBefore30Days)) {
   	    			listRecentDeaths.add(entity);
   	    		}          
      	 }});
      	 return toPersonsText(listRecentDeaths);
      }

    //US33 (Raj) List orphans
    public String listOrphans() {
        List<PersonEntity> orphans = new ArrayList<PersonEntity>();

        Individuals.forEach((s, entity) -> {
            List<ValidationResult> results = new ArrayList();
            PersonEntityValidator.orphanCheck(entity, results);

            if (!results.isEmpty()) {
                orphans.add(entity);
            }

        });
        if (orphans.isEmpty()) {
            return null;
        } else {
            return toPersonsText(orphans);
        }
    }

    //US37 (Raj) List Recent Survivors
    public String listRecentSurvivors() {
        Map<String, PersonEntity> recentSurvivors = new HashMap<String, PersonEntity>();

        Individuals.forEach((s, entity) -> {
            List<ValidationResult> results = new ArrayList();
            PersonEntityValidator.recentSurvivorsCheck(entity, results);

            if (!results.isEmpty()) {
                results.forEach(result -> {
                    if (!recentSurvivors.containsKey(result.Entity.getId())) {
                        recentSurvivors.put(result.Entity.getId(), (PersonEntity)result.Entity);
                    }
                });

            }

        });
        if (recentSurvivors.isEmpty()) {
            return null;
        } else {
            return toPersonsText(recentSurvivors.values());
        }
    }
	//US38 (Craig) - List upcoming birthdays
    public String listBirthdays() {
    	
    	List<PersonEntity> bdayList = new ArrayList<PersonEntity>();
    	
    	Calendar calendar = Calendar.getInstance();
     	 Date sysdate = calendar.getTime();
    	
     	 
    	Individuals.forEach((ind, entity) -> {
    		int diffDays = entity.BirthDate.getDate() - sysdate.getDate();
    		int diffMonth = entity.BirthDate.getMonth() - sysdate.getMonth();
    		
    		if(entity.BirthDate != null && diffDays <= 30 && diffMonth == 0) {
    		bdayList.add(entity);
    		}
    	});
    	return toPersonsText(bdayList);
    }
}
