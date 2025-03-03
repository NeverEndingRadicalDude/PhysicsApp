package com.core;

public class PhysicsManager {

    private ArrayList<Sprite> renderedObjects;
    private Situation situation;


    private static String resourcePath = (System.getProperty("user.dir") + "/resources");

    public PhysicsManager(String[] args) {
        if (args.length == 0) {
            return null;
        }
        try {
            situation = new Situation(resourcePath + args[0]);
        } catch(Exception e) {
            System.out.println("Error reading Situation " + args[0] + ", error message: " + e + ", stackTrace: ");
            e.printStackTrace(System.out);
        }
    }

    





}