package com.obcgui;

import java.util.Timer;
import java.util.TimerTask;

import com.diozero.api.DigitalInputEvent;
import com.diozero.api.function.DeviceEventConsumer;
import com.diozero.devices.Button;
import com.diozero.devices.LED;
import com.diozero.util.SleepUtil;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.PullResistance;

import net.fauxpark.oled.Graphics;
import net.fauxpark.oled.SSD1306;
import net.fauxpark.oled.font.CodePage1252;
import net.fauxpark.oled.transport.I2CTransport;
import net.fauxpark.oled.transport.Transport;


public class OBCgui
{
    static Transport transport;
    static OBCdisplay display;
    static Button buttonMaster;
    static Button buttonLatUp;
    static Button buttonLatDown;
    static final int BTN_MA = 4;
    static final int BTN_LUP = 25;
    static final int BTN_LDDOWN = 17;

    static Timer timer;
    static int intervall = 60;

    static boolean testMaster = false;
    static int testLatency = 0;
    //LED stuff
    static OBCledhandler ledhandler;

    public static void main( String[] args )
    {
        System.out.println("starting OBCgui(main)...");
        init();
        initButtons();

        timer = new Timer();    //initialize timer
        timer.scheduleAtFixedRate(new TimerTask() { //set timer
            @Override
            public void run() {

            if(!buttonMaster.isPressed()){
                if(testMaster == true)testMaster = false;
                else testMaster = true;
                OBCledhandler.setMaster(testMaster);

                while(!buttonMaster.isPressed()){
                    if(!buttonLatDown.isPressed() && !buttonLatDown.isPressed()){
                        System.exit(0);
                    }
                }    //wait for button to get released
            }

            if(!buttonLatUp.isPressed()){
                // System.out.println("up is pressed");
                testLatency++;
                testLatency = checkLatency(testLatency, -20, 20);
                OBCdisplay.setLatency(testLatency);

                while(!buttonLatUp.isPressed()){}    //wait for button to get released
            }
            if(!buttonLatDown.isPressed()){
                // System.out.println("down is pressed");
                testLatency--;
                testLatency = checkLatency(testLatency, -20, 20);
                OBCdisplay.setLatency(testLatency);

                while(!buttonLatDown.isPressed()){}    //wait for button to get released
            }
            }

        }, 0, intervall); //no delay, then call method every x ms

                    


        // OBCdisplay.init(transport);
        // try {
        //     Thread.sleep(4000);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        //
        // System.out.println( "set Player" );
        
        OBCdisplay.setPlayerOnline(1, "XDJ1000MK2", false);
        OBCdisplay.setPlayerOnline(2, "AbletonLink", true);
        OBCdisplay.setPlayerMaster(2, true);

        try {
            OBCdisplay.setTempo(10.1);
            Thread.sleep(7000);
            OBCdisplay.setTempo(10.15);
            Thread.sleep(7000);
            OBCdisplay.setTempo(123);
            Thread.sleep(3000);
            OBCdisplay.setTempo(67.45);


        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // System.out.println( "player1 offline" );
        // OBCdisplay.setPlayerOffline(1);
        // try {
        //     Thread.sleep(2000);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        //
        // System.out.println( "player1 online" );
        // OBCdisplay.setPlayerOnline(1, "XDJ2000nxs2", false);
        // OBCdisplay.setPlayerMaster(1, true);


        // try {    //!!! only for testing!
        //     Thread.sleep(10000);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // System.out.println( "exit application!" );
        // System.exit(0);
    }

    public static int checkLatency(int latency, int min, int max){
        int _latency = latency;
        if(latency <= min){
            _latency = min;
            OBCledhandler.setDown(false);
            OBCledhandler.setUp(true);
        }else if(latency >= max){
            _latency = max;
            OBCledhandler.setDown(true);
            OBCledhandler.setUp(false);
        }else{
            _latency = latency;
            OBCledhandler.setDown(true);
            OBCledhandler.setUp(true);
        }

        return _latency;
    }

    public static void initButtons() {
        buttonMaster  = new Button(BTN_MA);
        buttonLatUp  = new Button(BTN_LUP);
        buttonLatDown  = new Button(BTN_LDDOWN);
    }

    public static void init(){
        System.out.println("init gui");
        transport = new I2CTransport(0, 1, 0x3C);
        OBCdisplay.init(transport);

        System.out.println("init leds.");
        ledhandler = new OBCledhandler();
        ledhandler.init();
    }
}
