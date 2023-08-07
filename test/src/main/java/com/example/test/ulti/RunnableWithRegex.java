package com.example.test.ulti;

import java.util.Map;
import java.util.regex.Pattern;

public class RunnableWithRegex extends RunnableCustom{
    private  final  String regex;

    public RunnableWithRegex(String regex, String filedName, Map<String, String> errors, String message){
        this.regex = regex;
        this.fieldName = filedName;
        this.errors = errors;
        this.message = message;
    }
    public RunnableWithRegex(String regex, String filedName, Map<String, String> errors){
        this.regex = regex;
        this.fieldName = filedName;
        this.errors = errors;
        this.message = filedName + " invalid";
    }
    @Override
    public void run() {
        Pattern pattern = Pattern.compile(regex);
        boolean valid = pattern.matcher(value).matches();
        if(!valid){
            errors.put(fieldName,  message);
        }
    }
}
