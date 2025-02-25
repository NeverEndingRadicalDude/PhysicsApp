package com.core;

public class Functions {
  
  public static double getMag(float[] inVec) {
    if (inVec.length != 3) {
      System.out.println("System.Functions.Error1") ;
      return 0;
    }
    float sum = 0;
    sum = ( inVec[0] * inVec[0] + inVec[1] * inVec[1] + inVec[2] * inVec[2] );
    return Math.sqrt(sum);
  }

  public double sin(float angle) {
    double r = (Math.PI * 2) * (angle / 360.0);
    return Math.sin(r);
  }

  public double cos(float angle) {
    double r = (Math.PI * 2) * (angle / 360.0);
    return Math.cos(r);
  }

  public double tan(float angle) {
    double r = (Math.PI * 2) * (angle / 360.0);
    return Math.tan(r);
  }

  public float[] getVec(Point x, Point y) {
    float i = x.getPos()[0] - y.getPos()[0];
    float j = x.getPos()[1] - y.getPos()[1];
    float k = x.getPos()[2] - y.getPos()[2];
    float[] l = { i, j, k };
    return l;
  }
  
  
}