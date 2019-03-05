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
enum ErrorType {
    ERROR, ANOMALY;
}

public class ValidationResult {

    public ValidationResult(String message) {
        Message = message;
        ErrorType = ErrorType.ERROR;
    }

    public ValidationResult(String message, IEntity entity) {
        Message = message;
        Entity = entity;
        ErrorType = ErrorType.ERROR;
    }

    public ValidationResult(String message, IEntity entity, String userStoryID) {
        Message = message;
        Entity = entity;
        UserStoryID = userStoryID;
        ErrorType = ErrorType.ERROR;
    }

    public ValidationResult(String message, IEntity entity, String userStoryID, ErrorType errorType) {
        Message = message;
        Entity = entity;
        UserStoryID = userStoryID;
        ErrorType = errorType == null ? ErrorType.ERROR : errorType;
    }

    public ValidationResult(String message, IEntity entity, String userStoryID) {
        Message = message;
        Entity = entity;
        UserStoryID = userStoryID;
    }

    @Override
    public String toString() {

        if (Entity == null) {
            return Message;
        } else if (UserStoryID != null) {
            return String.format(ErrorType + ": " + Entity.getEntityName() + ": " + UserStoryID + ": ID: " + Entity.getId() + ": " + Message);
        } else {
            return String.format(ErrorType + ": " + Entity.getEntityName() + ": ID: " + Entity.getId() + ": " + Message);
        }
    }

    public String Message;
    public IEntity Entity;
    public String UserStoryID;
    public ErrorType ErrorType;
}
