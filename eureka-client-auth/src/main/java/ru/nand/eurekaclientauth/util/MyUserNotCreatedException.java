package ru.nand.eurekaclientauth.util;

public class MyUserNotCreatedException extends RuntimeException{
    public MyUserNotCreatedException(String message){
        super(message); // Передаем сообщение в RuntimeException
    }
}
