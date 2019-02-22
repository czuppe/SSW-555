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
public interface IEntity {
    String getId();
    GEDCOMData getGEDCOMData();
    void setGEDCOMData(GEDCOMData value);
    void validate(List<ValidationResult> results);
    String getEntityName();
}
