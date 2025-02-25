package com.core;

public class Point {
  private float[] velocity;
  private float[] netForce;
  private float mass;
  private int material;
  private float temp;
  private String name;
  private float[] position;
  
  public void setMass(float m) {
    mass = m;
  }

  public void setForce(float[] f) {
    try {
      netForce = f;
    } catch(IndexOutOfBoundsException e) {
      System.out.println(e);
      System.out.println("Point.java.setForce() error 1");
    }
  }

  public void setVelocity(float[] v) {
    try { 
      velocity = v;
    } catch(IndexOutOfBoundsException e) {
      System.out.println(e);
      System.out.println("Point.java.setVelocity() error 1");
    }
  }

  public void setMaterial(int m) {
    material = m;
  }

  public void setTemp(float t) {
    temp = t;
  }

  public void setName(String n) {
    name = n;
  }

  public Point(float[] v, float[] nf, float[] p, float m, int mat, String n, float t) {
    
    velocity = v;
    netForce = nf;
    mass = m;
    material = mat;
    temp = t;
    name = n;
    position = p;
    
  }
  
  public float[] getV() {
    return velocity;
  }
  
  public float[] getNF() {
    return netForce;
  }
  
  public float[] getPos() {
    return position;
  }
  
  public float getM() {
    return mass;
  }
  
  public int getMat() {
    return material;
  }
  
  public String getName() {
    return name;
  }
  
  public float getT() {
    return temp;
  }
  
}