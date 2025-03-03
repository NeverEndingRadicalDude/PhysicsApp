package com.core;

public class Point {
  private float[] velocity;
  private float[] force;
  private float mass;
  private int material;
  private float temp;
  private String name;
  private float[] position;
  private int referenceID;
  private float[] moment;
  
  public void setMass(float m) {
    mass = m;
  }

  public float[] getMoment() {
    return moment;
  }

  public void setMoment(float[] moment) {
    this.moment = moment;
  }

  public void setReferenceID(int id) {
    referenceID = id;
  }

  public int getReferenceID() {
    return referenceID;
  }

  public void setForce(float[] f) {
    try {
      force = f;
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

  public Point(float[] v, float[] f, float[] p, float m, int mat, String n, float t, int id) {
    
    velocity = v;
    force = f;
    mass = m;
    material = mat;
    temp = t;
    name = n;
    position = p;
    referenceID = id;
    
  }

  public Point(float[] v, float[] f, float[] p, float m, int mat, String n, float t, int id, float[] moment) {
    
    velocity = v;
    force = f;
    mass = m;
    material = mat;
    temp = t;
    name = n;
    position = p;
    referenceID = id;
    this.moment = moment;
    
  }
  
  public float[] getVelocity() {
    return velocity;
  }
  
  public float[] getForce() {
    return force;
  }
  
  public float[] getPosition() {
    return position;
  }
  
  public float getMass() {
    return mass;
  }
  
  public int getMaterial() {
    return material;
  }
  
  public String getName() {
    return name;
  }
  
  public float getTemp() {
    return temp;
  }
  
}