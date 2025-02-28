package com.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Situation {
    private ArrayList<ArrayList<bytes>> objects;
    private String path;
    private File file;
    private String name;
    private String discription;
    private int objNum;
    private ArrayList<Sprite> sprites;


    private float convertFloat(byte[] num) {
        return java.nio.ByteBuffer.wrap(num).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public Situation(String p) throws Exception {
        path = p;
        file = new File(path);
        read();
    }

    private boolean read() throws Exception {
        Scanner scan;
        scan = new Scanner(file);
        FileInputStream in = null;
        in = new FileInputStream(file);

        byte[] bytes = new byte[(int) file.length()];
        in.read(bytes);
        System.out.println(e);

        int index = 124;
        boolean check = false;

        name = new String(Arrays.copyOfRange(bytes, 0, 20), "UTF-8");
        discription = new String(Arrays.copyOfRange(bytes, 20, 120), "UTF-8");

        objNum = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, 120, 122)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
        if (java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, 122, 124)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt() != 65535) {
            System.out.println("SituationError 2");
        }
        boolean check2 = false;
        while (!check2) {
            if (java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 2))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt() == 65533) {
                check2 = true;
                break;
            }
            index += 2;
            short spriteRef = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 2))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
            index += 2;
            String spriteName = new String(Arrays.copyOfRange(bytes, index, (int) (index + 20)));
            index += 20;
            String spriteStlPath = new String(Arrays.copyOfRange(bytes, index, (index + 50)));
            index += 50;
            String savedSpritePath = new String(Arrays.copyOfRange(bytes,index, (index + 50)));
            index += 50;
            int numPoints = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 2))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
            index += 2;
            int numFaces = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 2))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
            index += 2;

            float[] posOffSet = new float[3];
            posOffSet[0] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            posOffSet[1] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            posOffSet[2] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;

            float[] rot = new float[3];
            rot[0] =java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            rot[1] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            rot[2] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;

            float[] vel = new float[3];
            vel[0] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            vel[1] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            vel[2] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;

            float[] rotMov = new float[3];
            rotMov[0] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            rotMov[1] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            rotMov[2] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;

            if (java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 2))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt() != 65534) {
                System.out.println("SituationError 3");
                System.exit(0);
            }

            short[] pointRef = new int[numPoints];
            float[][] pointF = new float[numPoints][3];
            float[] pointMass = new float[numPoints];
            float[][] pointV = new float[numPoints][3];
            String[] pointName = new String[numPoints];
            float[][] pointP = new float[numPoints][3];
            int[] pointMat = new int[numPoints];
            float[] pointTemp = new float[numPoints];
            int ind = 0;
            while (!check) {
            
                byte[] tempBuffer = new byte[70];
                try {
                    tempBuffer = Arrays.copyOfRange(bytes, index, (index + 68));
                } catch(IndexOutOfBoundsException e) {
                    check = true;
                    break;
                }
                String[] obj = new String[14];
                pointRef[ind] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 0, 2)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
                // fx fy fz
                pointF[ind][0] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 2, 6)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                pointF[ind][1] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 6, 10)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                pointF[ind][2] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 10, 14)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                // mass
                pointMass[ind] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 14, 18)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                //vx vy vz
                pointV[ind][0] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 18, 22)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                pointV[ind][1] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 22, 26)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                pointV[ind][2] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 26, 30)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                // name
                pointName[ind] = new String(Arrays.copyOfRange(tempBuffer, 30, 50));
                // px py pz
                pointP[ind][0] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 50, 54)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                pointP[ind][1] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 54, 58)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                pointP[ind][2] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 58, 62)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                // mat
                pointMat[ind] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 62, 64)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
                // temperature
                pointTemp[ind] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 64, 68)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
                int temp2 = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(tempBuffer, 68, 70)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
                if (temp2 != 0) {
                    System.out.println("SituationError 1");
                    System.exit(0);
                }
                ind++;
                index += 70;

            }
            if (java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, index, (index + 2))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt() == 65533) {
                sprites.add(new Sprite(pointP, pointF, pointMass, pointMat[0], spriteName, pointTemp));
                // add object function adding
                objects.add(sprites.get(sprites.size() - 1).toBytes());
            }


        }
        for (int x = 0; x < objects.size(); x++) {
                
        }
        return true;   
    }

    public ArrayList<> getSprites() {
        return sprites;
    }

    public void setSprites(ArrayList<Sprites> newSprites) {
        sprites.clear();
        for (Sprite sprite : newSprites) {
            sprites.add(sprite);
        }
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getDiscription() {
        return discription;
    }

    public void writeBytesToList() throws Exception {
        objects.clear();
        for (int x = 0; x < sprites.size(); x++) {
            objects.add(sprites.get(x).toByteList());
        }
    }
    
}