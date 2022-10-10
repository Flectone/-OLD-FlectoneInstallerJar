package ru.flectone;

public class Main {

    public static void main(String[] args){
        new FSystem();

        new Thread(Utils::parsingWebSite).start();

        FSystem.setSystemLocale(java.lang.System.getProperty("user.language"));

        Frame frame = new Frame();

    }

}
