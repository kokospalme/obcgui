package com.obcgui;

import java.util.Timer;
import java.util.TimerTask;

import net.fauxpark.oled.Graphics;
import net.fauxpark.oled.SSD1306;
import net.fauxpark.oled.font.CodePage1252;
import net.fauxpark.oled.font.CodePage437;
import net.fauxpark.oled.transport.Transport;



public class OBCdisplay {
    static Transport transport;
    static SSD1306 ssd1306;
    static Graphics graphics;

    static Guiproperties properties;
    static boolean updatePlayer = true;
    static boolean updateTempo = true;
    static boolean initMain = true;

    private static Timer timer;
    static int showIntervall_ms = 30;

    /*
     * initializes Display
     */
    public static void init(Transport trans){
        transport = trans;  //init i2c-transport
        ssd1306 = new SSD1306(128, 64, transport);  //init display
        graphics = ssd1306.getGraphics();   //init graphics
        properties = new Guiproperties();   //init player-properties
        ssd1306.startup(false); //startup display
        System.out.println("display initialized.");

        timer = new Timer();    //initialize timer
        timer.scheduleAtFixedRate(new TimerTask() { //set timer
            @Override
            public void run() {
                showDisplay(); // show 
            }
        }, 0, showIntervall_ms); //no delay, then call method every x ms
    }

    /*
     * updates display
     */
    private static void showDisplay() {
        showMain(); //currently only mainmenu is available
    }

    /*
     * sets current tempo
     */
    public static void setTempo(double tempo){

        double _tempo = Math.round(tempo * 100);
        _tempo = _tempo / 100;
        if(properties.tempo != _tempo){
            properties.tempo = _tempo;
            updateTempo = true;
        }
        
    }

    /*
     * sets beat/bar-phase
     */
    public static void setBarphase(double phase){
        if(properties.phase != phase)properties.phase = phase;
    }

    /*
     * sets playerstatus online
     */
    public static void setPlayerOnline(int no, String name, boolean isVirtualCdj){
        if(no < 1 || no > 4) return;
        properties.playerName[no-1] = name;
        properties.playerOnline[no-1] = true;
        properties.isVirtualCdj[no-1] = isVirtualCdj;
        updatePlayer = true; 
    }
    /*
     * sets playerstatus offline
     */
    public static void setPlayerOffline(int no) {
        if(no < 1 || no > 4) return;
        properties.playerName[no-1] = "";
        properties.playerOnline[no-1] = false;
        properties.isVirtualCdj[no-1] = false;
        updatePlayer = true; 
    }

    /*
     * sets which player is master
     */
    public static void setPlayerMaster(int no, boolean master){
        if(no < 1 || no > 4) return;
        for(int i = 0; i < 4;i++){
            properties.isMaster[i] = false;
        }
        properties.isMaster[no-1] = master;
        updatePlayer = true; 
    }



    /*
     * shows mainMenu 
     */
    public static void showMain(){

        //phase-bar
        for(int i = 0; i < 128; i++){   //blank phase-bar
            for(int j = 0; j < 24; j++){
                ssd1306.setPixel(i, j, false);
            }
        }
        graphics.text(40, 0, new CodePage1252(), Double.toString(properties.phase));    //draw phase

        //tempo in bpm
        if(updateTempo){
            for(int i = 0; i < 128; i++){   //blank playerOnOff-bar
                for(int j = 25; j < 37; j++){
                    ssd1306.setPixel(i, j, false);
                }
            }
            System.out.println("tempo:" + properties.tempo);

            if(properties.tempo < 10 || properties.tempo > 999){
                graphics.text(40, 25, new CodePage1252(), "---.--");
            }
            else if(properties.tempo < 100){
                if(Double.toString(properties.tempo).length() > 4) graphics.text(46, 25, new CodePage1252(), Double.toString(properties.tempo).substring(0,5));
                else graphics.text(46, 25, new CodePage1252(), Double.toString(properties.tempo));
                
            }else{
                if(Double.toString(properties.tempo).length() > 5) graphics.text(40, 25, new CodePage1252(), Double.toString(properties.tempo).substring(0,6));
                else graphics.text(40, 25, new CodePage1252(), Double.toString(properties.tempo));
            }
            graphics.text(79, 25, new CodePage1252(), "BPM");
            // graphics.text(64, 25, new CodePage1252(), Integer.toString(_tempDecimalsnt));   //write decimals
            
            updateTempo = false;
            ssd1306.display();
        }

        //player-stuff
        if(initMain == true){
            for(int i = 0; i < 4; i++){ //draw player-frames
                graphics.rectangle(i*32, 50, 32, 14, false);
                graphics.text(i*32 + 13, 53 , new CodePage1252(), Integer.toString(i+1));   //draw playernumbers
            }

            initMain = false;
            ssd1306.display();
        }


        if(updatePlayer){   //update player information

            for(int i = 0; i < 128; i++){   //blank playerOnOff-bar
                for(int j = 38; j < 50; j++){
                    ssd1306.setPixel(i, j, false);
                }
            }
            for(int i = 0; i < 4; i++){
                if(properties.playerOnline[i]){
                    if(properties.isMaster[i]){
                        graphics.rectangle(i*32, 47, 32, 3, true);  //white bar == player online
                        // System.out.println(i + "isMaster!");
                    }
                    if(properties.isVirtualCdj[i])drawAbleton(i*32 + 8, 38);    //draw Ableton logo if player is a virtualCDJ
                    else graphics.text(i*32, 38 , new CodePage1252(), properties.playerName[i].substring(0, 5));   //draw player names if not
                }
            }
            updatePlayer = false;   
            ssd1306.display();
        }

        
    }

    private static void drawAbleton(int x, int y){
        for(int i = 0; i < 4; i++){ //draw horizontal lines
            graphics.line(x, y+(i * 2), x + 5, y+(i * 2));
        }

        x+= 9;
        for(int i = 0; i < 4; i++){ //draw horizontal lines
            graphics.line(x+(i * 2), y, x+(i * 2), y + 6);
        }
        
    }


    
}
