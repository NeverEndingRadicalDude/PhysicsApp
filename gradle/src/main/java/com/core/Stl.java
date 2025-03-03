package com.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Stl {
    private ArrayList<float[]> points;
    private int numFaces;
    private String header;
    private ArrayList<float[][]> faces;
    private File file;
    private float[] bounds;
    private String texturePath;

    public Sprite genSprite() {
        float[][] positions = new float[points.size()][3];
        float[][] blank = new float[points.size()][3];
        float[] mass = new float[points.size()];

        for (int x = 0; x < points.size(); x++) {
            positions[x] = new float[]{ points.get(x)[0], points.get(x)[1], points.get(x)[2] };
            blank[x] = new float[]{ 0.0f, 0.0f, 0.0f };
            mass[x] = 0.0f;
        }
        
        return new Sprite(positions, blank, mass, 0, file.getName(), mass, 0, file.getPath(), "");


    }

    private void findBounds() {
        for (int x = 0; x < points.size(); x++) {
            if (points.get(x)[0] < bounds[0]) {
                bounds[0] = points.get(x)[0];
            } else if (points.get(x)[0] > bounds[1]) {
                bounds[1] = points.get(x)[0];
            }
            if (points.get(x)[1] < bounds[2]) {
                bounds[2] = points.get(x)[1];
            } else if (points.get(x)[1] > bounds[3]) {
                bounds[3] = points.get(x)[1];
            }
            if (points.get(x)[2] < bounds[4]) {
                bounds[4] = points.get(x)[2];
            } else if (points.get(x)[2] > bounds[5]) {
                bounds[5] = points.get(x)[2];
            }
        } 
        
        
    }

    private int findClosestIndex(float[] pos) {
        int index = 0;
        float length = 0;
        for (int x = 0; x < faces.size(); x++) {
            float l = findLength(faces.get(x)[4], pos);
            if (l < length) {
                length = l;
                index = x;
            } 
        }
        return index;
    }

    private float findLength(float[] pos1, float[] pos2) {
        float[] tempVec = generateVec(pos1, pos2);
        float sum = tempVec[0] * tempVec[0];
        sum += tempVec[1] * tempVec[1];
        sum += tempVec[2] * tempVec[2];
        return (float) Math.sqrt(sum);
    }

    private float dotProduct(float[] vec1, float[] vec2) {
        float sum = vec1[0] * vec2[0];
        sum += vec1[1] * vec2[1];
        sum += vec1[2] * vec2[2];
        return sum;
    }

    private float[] generateVec(float[] pos1, float[] pos2) {
        float[] temp = new float[3];
        temp[0] = pos2[0] - pos1[0];
        temp[1] = pos2[1] - pos1[1];
        temp[2] = pos2[2] - pos1[2];
        return temp;
    }

    private boolean checkAngle(float[][] face, float[] pos) {
        int closeIn = 0;
        float tempL = 0.0f;
        for (int x = 0; x < 3; x++) {
            float l = findLength(pos, face[x]);
            if (tempL < l) {
                tempL = l;
                closeIn = x;
            }
        }

        float[][] closeFaces = findFace(face[closeIn]);
        float[] tempVec = generateVec(closeFaces[4], pos);
        if (dotProduct(tempVec, closeFaces[0]) < 0) {
            return true;
        } else if (dotProduct(tempVec, closeFaces[0]) == 0) {
            if (dotProduct(tempVec, closeFaces[1]) < 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private float[][] findFace(float[] pos) {
        float[][] temp = new float[5][3];
        for (int x = 0; x < faces.size(); x++) {
            for (int y = 0; y < 3; y++) {
                if (faces.get(x)[y].equals(pos)) {
                    temp = faces.get(x);
                }
            }
        }
        return temp;
    }

    public boolean checkInterior(float[] pos) {
        // check if in the inscribed square
        if (pos[0] < bounds[0] || pos[0] > bounds[1]) {
            return false;
        } else if (pos[1] < bounds[2] || pos[1] > bounds[3]) {
            return false;
        } else if (pos[2] < bounds[4] || pos[2] > bounds[5]) {
            return false;
        } else {
            // check nearestFace
            float[][] nearFace = faces.get(findClosestIndex(pos));
            float[] tempVec = generateVec(pos, nearFace[4]);
            boolean check = false;
            float dot = dotProduct(tempVec, nearFace[3]);
            if (dot < 0) {
                return true;
            } else if (dot == 0) {
                // if 90 degrees to the nearest face, check the closest face
                return checkAngle(nearFace, pos);
            } else {
                return false;
            }

        }
    }

    public ArrayList<float[][]> getFaces() {
        return faces;
    }

    public ArrayList<float[]> getPoints() {
        return points;
    }

    public int numFaces() {
        return numFaces;
    }

    public String getHeader() {
        return header;
    }

    public Stl(String path) {
        InputStream in = null;
        int index = 0;
        int fileLength = 0;
        
        // isolate the file as an InputStream and check if it is large enough to be an stl:
        try {
            file = new File(path);
        } catch(NullPointerException e) {
            System.out.println(e);
            System.out.println("StlConcertError 1");
        }
        try {
            in = new FileInputStream(file);
        } catch(FileNotFoundException e) {
            System.out.println(e);
            System.out.println("StlConvert error 4");
        }
        try {
            fileLength = in.available();
        } catch(IOException e) {
            System.out.println(e);
            System.out.println("StlConvert error 2");
        }
        if (fileLength < 134) {
            System.out.println("StlConvert error 3");
        }

        // creat the byteArray from the stl file:
        byte[] byteArray = new byte[fileLength];
        try {
            in.read(byteArray);
        } catch(IOException e) {
            System.out.println(e);
        }
        header = new String(byteArray);

        // get the num of faces [80,83];
        byte[] temp = new byte[4];
        temp[0] = byteArray[80];
        temp[1] = byteArray[81];
        temp[2] = byteArray[82];
        temp[3] = byteArray[83];
        int numFaces = java.nio.ByteBuffer.wrap(temp).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

        for (int x = 0; x < numFaces; x++) {
            faces.add(generateFace(x, byteArray));
        }
        bounds = new float[6];

        generatePoints();
        findBounds();
        in.close();



    }

    private float[][] generateFace(int ind, byte[] array) {
        int index = 84 + 50 * ind;
        float[][] out = new float[5][3];
        for (int x = 0; x < 4; x++) {
            out[x][0] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(array, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            out[x][1] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(array, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();
            index += 4;
            out[x][2] = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(array, index, (index + 4))).order(java.nio.ByteOrder.LITTLE_ENDIAN).getFloat();        
            index += 4;

            index += 2;
        }
        out[4][0] = (out[1][0] + out[2][0] + out[3][0]) / 3;
        out[4][1] = (out[1][1] + out[2][1] + out[3][1]) / 3;
        out[4][2] = (out[1][2] + out[2][2] + out[3][2]) / 3;
        return out;
    }
    
    private void generatePoints() {
        for (int x = 0; x < numFaces; x++) {
            points.add(faces.get(x)[1]);
            points.add(faces.get(x)[2]);
            points.add(faces.get(x)[3]);   
        }
        removeCopies();
        sort();
    }

    private void removeCopies() {
        Set<float[]> set = new HashSet<>(points);
        points.clear();
        points.addAll(set);
    }

    private void sort() {
        boolean sorted = false;
        int index = 0;
        while (sorted == false) {
            try {
                if (points.get(index)[0] < points.get(index + 1)[0]) {
                    Collections.swap(points, index, index + 1);
                    index--;
                } else if (points.get(index)[0] > points.get(index + 1)[0]) {
                    index++;
                } else {
                    if (points.get(index)[1] < points.get(index + 1)[1]) {
                        Collections.swap(points, index, index + 1);
                        index--;
                    } else if (points.get(index)[1] > points.get(index + 1)[1]) {
                        index++;
                    } else {
                        if (points.get(index)[2] < points.get(index + 1)[2]) {
                            Collections.swap(points, index, index + 1);
                            index--;
                        } else {
                            index++;
                        }
                    }
                }
            } catch(IndexOutOfBoundsException e) {
                sorted = true;
            }
        }
    }

}