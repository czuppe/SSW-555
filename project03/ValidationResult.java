/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

/**
 *
 * @author nraj39
 */
public class ValidationResult {

    public ValidationResult(String message) {
        Message = message;
    }

    public ValidationResult(String message, IEntity entity) {
        Message = message;
        Entity = entity;
    }

    @Override
    public String toString() {
        if (Entity == null) {
            return Message;
        } else {
            return String.format("[ID: " + Entity.getId() + "]: " + Message);
        }
    }

    public String Message;
    public IEntity Entity;
}
