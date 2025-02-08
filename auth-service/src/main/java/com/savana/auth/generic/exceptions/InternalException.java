package com.savana.auth.generic.exceptions;

public class InternalException extends InternalError {

    public InternalException(String message){
        super("Erreur interne : \n"+message+".\n Veillez reessayer.");
    }
}