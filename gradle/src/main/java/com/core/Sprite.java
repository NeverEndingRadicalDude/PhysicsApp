package com.core;

import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.FileOutputStream;

public class Sprite {
    private ArrayList<Point> points;
    private String name;
    private float[] cenOfMass;
    private String texturePath;
    private int referenceID;
    private String stlPath;
    private float[] posOffset;
    private float[] rotationMatrix;
    private float[] movementVector;
    private float[] rotationVector;
    private float[] netAcceleration;


    public Sprite(float[][] pos, float[][] forces, float[] masses, int material, String name, float[] temps, int referenceID, String stlPath, String texturePath) {
        float[] blank = new float[]{0, 0, 0};
        points = new ArrayList<Point>();
        cenOfMass = new float[3];
        this.referenceID = referenceID;
        this.stlPath = stlPath;
        this.texturePath = texturePath;
        for (int x = 0; x < pos.length; x++) {
            
            points.add(new Point(blank, forces[x], pos[x], masses[x], material, name, temps[x], x));
        }
        calCenMass();
    }

    public void setReferenceID(int id) {
        referenceID = id;
    }

    public void setTexturePath(String texPath) {
        texturePath = texPath;
    }

    public byte[] toByteArray() {
        int length = 0;
        length += 4; // refID
        length += 20; // name;
        length += 50; // stlPath
        length += 50; // texturePath
        length += 4; // objPointNum
        length += 4; // objFaceNum
        length += 12; // objPosOffset
        length += 12; // objRotationMatrix
        length += 12; // objMovementVec
        length += 12; // objRotationVec
        length += 2; // $fffe
        for (int x = 0; x < points.size(); x++) {
            length += 4; // referenceIndex
            length += 12; // forceVector
            length += 4; // pointMass
            length += 12; // velocityVector
            length += 20; // name
            length += 12; // positionVector
            length += 4; // material
            length += 4; // temp
            length += 2; // $0000
        }
        length += 2; // $fffd
        ByteBuffer temp = ByteBuffer.allocate(length);
        
        char[] nameArray = name.toCharArray();
        char[] texPathArray = texturePath.toCharArray();
        char[] stlPathArray = stlPath.toCharArray();

        temp.putInt(referenceID);
        for (char ch : nameArray) {
            temp.putChar(ch);
        }
        for (char ch : stlPathArray) {
            temp.putChar(ch);
        }
        for (char ch : texturePath) {
            temp.putChar(ch);
        }
        temp.putInt(points.size());
        temp.putFloat(posOffset[0]);
        temp.putFloat(posOffset[1]);
        temp.putFloat(posOffset[2]);
        temp.putFloat(rotationMatrix[0]);
        temp.putFloat(rotationMatrix[1]);
        temp.putFloat(rotationMatrix[2]);
        temp.putFloat(movementVector[0]);
        temp.putFloat(movementVector[1]);
        temp.putFloat(movementVector[2]);
        temp.putFloat(rotationVector[0]);
        temp.putFloat(rotationVector[1]);
        temp.putFloat(rotationVector[2]);
        temp.putShort((short) 65534);
        for (Point p : points) {
            temp.putInt(p.getReferenceID());
            temp.putFloat(p.getForce()[0]);
            temp.putFloat(p.getForce()[1]);
            temp.putFloat(p.getForce()[2]);
            temp.putInt(p.getMass());
            temp.putFloat(p.getVelocity()[0]);
            temp.putFloat(p.getVelocity()[1]);
            temp.putFloat(p.getVelocity()[2]);
            
            char[] pointNameArray = p.getName().toCharArray();
            for (char ch : pointNameArray) {
                temp.putChar(ch);
            }

            temp.putFloat(p.getPosition()[0]);
            temp.putFloat(p.getPosition()[1]);
            temp.putFloat(p.getPosition()[2]);
            temp.putInt(p.getMaterial());
            temp.putFloat(p.getTemp());
            temp.putShort((short) 0);

        }
        temp.putShort((short) 65533);
        return temp.array();
    }

    public void writeToFile(byte[] bytes, File file) {
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
        } catch(FileNotFoundException e) {
            System.out.println("com.core.Sprite.writeToFile() error: file not found, stackTrace: ");
            e.printStackTrace();
            System.exit(0);
        }

        try {
            out.write(bytes);
        } catch(IOException e) {
            System.out.println(e);
            System.out.println("stackTrace: ");
            e.printStackTrace();
            System.exit(0);
        }
        out.close();
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

    private float calculateRadius(float[] pos1, float[] pos2) {
        float xDis = (pos1[0] - pos2[0]);
        float yDis = (pos1[1] - pos2[1]);
        xDis = xDis * xDis;
        yDis = yDis * yDis;
        return (float) Math.sqrt(xDis + yDis);
    }

    private float[] calculateMoment(float[] pos) {
        float[] totalMoment = new float[]{0.0f, 0.0f, 0.0f};
        for (Point point : points) {
            totalMoment[0] += point.getMass * calculateRadius(new float[]{pos[1], pos[2]}, new float[]{point.getPosition()[1], point.getPosition()[2]});
            totalMoment[1] += point.getMass * calculateRadius(new float[]{pos[0], pos[2]}, new float[]{point.getPosition()[0], point.getPosition()[2]});
            totalMoment[2] += point.getMass * calculateRadius(new float[]{pos[0], pos[1]}, new float[]{point.getPosition()[0], point.getPosition()[1]});
        }
        return totalMoment;
    }

    public void init() {
        for (Point point : points) {
            point.setMoment(calculateMoment(point.getPosition()));
        }
    }

    public float[] calculateTorqueScale(Point point) {
        float[] netTorque = new float[]{0.0f, 0.0f, 0.0f};
        float[] pointPos = p.getPosition();
        float[] force = p.getForce();
        for (Point p : points) {
            // finish
        }
    }

    private void calculateForces() {
        System.out.println("debug: calculating forces");
        float[] netAccelerationTemp = new float[]{0.0f, 0.0f, 0.0f};
        for (Point point : points) {
            float[] netTorque = calculateTorque(point);
            
        }
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