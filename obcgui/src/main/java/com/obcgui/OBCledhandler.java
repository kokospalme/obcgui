package com.obcgui;
import com.diozero.ws281xj.LedDriverInterface;
import com.diozero.ws281xj.PixelAnimations;
import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.rpiws281x.WS281x;


public class OBCledhandler {
    		//int gpio_num = 18;
		static final int LED_PIN = 18;
		static int brightness = 130;	// 0..255
        static int brightnessMaster = 160;
		//int num_pixels = 12;
		static int numLeds = 3;
		static LedDriverInterface ledDriver;
        static final int ledMaster = 2; //leds
        static final int ledUp = 0;
        static final int ledDown = 1;
		
        public void init(){
            ledDriver = new WS281x(LED_PIN, brightness, numLeds);
        }

        public void ledtest(){
            ledDriver.setPixelColourRGB(ledUp, dimColor(brightness, 255),dimColor(brightness, 255), dimColor(brightness, 255));
            ledDriver.setPixelColourRGB(ledDown, dimColor(brightness, 255),dimColor(brightness, 255), dimColor(brightness, 255));
            ledDriver.setPixelColourRGB(ledMaster, dimColor(brightnessMaster, 255),dimColor(brightnessMaster, 145), dimColor(brightnessMaster, 0));

            ledDriver.render();

        }

        public static void setUp(boolean active){
            if(active){ //bright
                ledDriver.setPixelColourRGB(ledUp, dimColor(brightness, 255),dimColor(brightness, 255), dimColor(brightness, 255));
            }else{  //dark
                ledDriver.setPixelColourRGB(ledDown, 0,0,0);
            }
            ledDriver.render();
        }

        public static void setDown(boolean active){
            if(active){ //bright
                ledDriver.setPixelColourRGB(ledDown, dimColor(brightness, 255),dimColor(brightness, 255), dimColor(brightness, 255));
            }else{  //dark
                ledDriver.setPixelColourRGB(ledDown, 0,0,0);
            }
            ledDriver.render();
        }

        public static void setMaster(boolean isMaster){
            if(isMaster == true){
                ledDriver.setPixelColourRGB(ledMaster, dimColor(brightnessMaster, 255),dimColor(brightnessMaster, 145), dimColor(brightnessMaster, 0));
            }else{
                ledDriver.setPixelColourRGB(ledMaster, dimColor(brightnessMaster/3, 255),dimColor(brightnessMaster/3, 255), dimColor(brightnessMaster/3, 255));
            }
            ledDriver.render();
        }

        public static int dimColor(int brightness, int color){
            int value = map(color,0,255,0,brightness);
            return value;
        }

        public static int map(int input, int inputFrom, int inputTo, int outFrom, int outTo) {
            double inputPercentage = (double)(input - inputFrom) / (inputTo - inputFrom);
            int output = (int)(outFrom + inputPercentage * (outTo - outFrom));
            return output;
        }
}
