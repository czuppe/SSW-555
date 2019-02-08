/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.util.*;

/**
 *
 * @author nraj39
 */
public class FactEntity {

    public FactEntity() {
        this.SourceIds = new ArrayList();
        this.SharesFactWith = new ArrayList();
    }
    public String Id;
    public String Type;
    public String TypeName;
    public Date Date;
    public String Location;
    public String Description;
    public List<String> SharesFactWith;
    public List<String> SourceIds;
    public String FootNote;

}
