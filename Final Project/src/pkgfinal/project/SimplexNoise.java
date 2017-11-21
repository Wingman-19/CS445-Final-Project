/*******************************************************************************
* File: SimplexNoise.java 
* Group: The RenderMen
* Members:
*       * Marc Deaso
*       * Omar Rodriguez
*       * Nick Spencer
*       * Luke Walsh
*       * Alex Winger
* Class: CS 445: – Computer Graphics 
* 
* Assignment: Final Project 
* Date Last Modified: 11/20/2017
* 
* Purpose: A pre-written class that will generate noise so that the Minecraft
*           world contains natural hills and valleys
* 
*******************************************************************************/
package pkgfinal.project;

import java.util.Random;

public class SimplexNoise {

//  octaves of noise to create fractal noise
    SimplexNoise_octave[] octaves;
//  frequencies and amplitudes created for the waves that create the noise
    double[] frequencys;
    double[] amplitudes;

//    the values that are used for the constructor in the SimplexNoise class
    int largestFeature;
    double persistence;
    int seed;

//    SimplexNoise constructor
    public SimplexNoise(int largestFeature,double persistence, int seed){
        this.largestFeature=largestFeature;
        this.persistence=persistence;
        this.seed=seed;

        //recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
        int numberOfOctaves=(int)Math.ceil(Math.log10(largestFeature)/Math.log10(2));

//        creates arrays for the number of octaves, frequencies, and amplitudes
        octaves=new SimplexNoise_octave[numberOfOctaves];
        frequencys=new double[numberOfOctaves];
        amplitudes=new double[numberOfOctaves];

//        creates a random seed
        Random rnd=new Random(seed);

        for(int i=0;i<numberOfOctaves;i++){
            octaves[i]=new SimplexNoise_octave(rnd.nextInt());

            frequencys[i] = Math.pow(2,i);
            amplitudes[i] = Math.pow(persistence,octaves.length-i);




        }

    }

//Method: getNoise
//    Purpose: returns the Noise for a 2D height
    public double getNoise(int x, int y){

        double result=0;

        for(int i=0;i<octaves.length;i++){
          //double frequency = Math.pow(2,i);
          //double amplitude = Math.pow(persistence,octaves.length-i);

          result=result+octaves[i].noise(x/frequencys[i], y/frequencys[i])* amplitudes[i];
        }


        return result;

    }

    //Method: getNoise
//    Purpose: returns the Noise for a 3D height
    public double getNoise(int x,int y, int z){

        double result=0;

        for(int i=0;i<octaves.length;i++){
          double frequency = Math.pow(2,i);
          double amplitude = Math.pow(persistence,octaves.length-i);

          result=result+octaves[i].noise(x/frequency, y/frequency,z/frequency)* amplitude;
        }


        return result;

    }
} 
