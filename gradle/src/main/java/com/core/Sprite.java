package com.core;

import java.util.ArrayList;

public class Sprite {
    private ArrayList<Point> points;
    private String name;
    private float[] cenOfMass;
    public Sprite(float[][] pos, float[][] forces, float[] masses, int material, String name, float[] temps) {
        float[] blank = {0, 0, 0};
        for (int x = 0; x < pos.length; x++) {
            
            points.add(new Point(blank, forces[x], pos[x], masses[x], material, name, temps[x]));
        }
        calCenMass();


        
    }

    public void setMasses(float[] m) {
        for (int x = 0; x < points.size(); x++) {
            points.get(x).setMass(m[x]);
        }
    }

    public void setForces(float[][] forces) {
        for (int x = 0; x < points.size(); x++) {
            points.get(x).setForce(forces[x]);
        }
    }

    public void setMat(int[] text) {
        if (text.length != points.size()) {
            System.out.println("java.Sprite.setMat() error 1");
        } else {
            for (int x = 0; x < points.size(); x++) {
                points.get(x).setMaterial(text[x]);
            }    
        }

    }

    public void setName(String n) {
        name = n;
    }

    public void setTemp(float[] temp) {
        if (temp.length != points.size()) {
            System.out.println("java.Sprite.setTemp() error 1");
        } else {
            for (int x = 0; x < points.size(); x++) {
                points.get(x).setTemp(temp[x]);
            }
        }
    }

    private void calculateForces() {

    }

    private void checkPointForInterior(double[] p) {
        
    }

    private void calCenMass() {
        float mSum = 0;
        float xMSum = 0;
        float yMSum = 0;
        float zMSum = 0;
        for (int i = 0; i < points.size(); i++) {
            xMSum = xMSum + (points.get(i).getPos()[0] * points.get(i).getM());
            yMSum = yMSum + (points.get(i).getPos()[1] * points.get(i).getM());
            zMSum = zMSum + (points.get(i).getPos()[2] * points.get(i).getM());
            mSum = mSum + points.get(i).getM();
        }
        float[] cenM = new float[]{ 0.0f, 0.0f, 0.0f };
        cenM[0] = xMSum / mSum;
        cenM[1] = yMSum / mSum;
        cenM[2] = zMSum / mSum;
        cenOfMass = cenM;
    }
}