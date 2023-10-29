package com.obcgui;

import net.fauxpark.oled.Graphics;
import net.fauxpark.oled.SSD1306;
import net.fauxpark.oled.font.CodePage1252;
import net.fauxpark.oled.transport.I2CTransport;
import net.fauxpark.oled.transport.Transport;


public class OBCgui
{
    static Transport transport;
    static OBCdisplay display;
    public static void main( String[] args )
    {
        System.out.println("starting OBCgui(main)...");
        init();
        // OBCdisplay.init(transport);
        // try {
        //     Thread.sleep(4000);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        //
        // System.out.println( "set Player" );
        OBCdisplay.setBarphase(123.45);
        OBCdisplay.setPlayerOnline(1, "XDJ1000MK2", false);
        OBCdisplay.setPlayerOnline(2, "AbletonLink", true);
        OBCdisplay.setPlayerMaster(2, true);
        
        try {
            OBCdisplay.setTempo(0);
            Thread.sleep(3000);
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
        //     Thread.sleep(5000);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // System.out.println( "exit application!" );
        // System.exit(0);
    }

    public static void init(){
        System.out.println("init gui");
        transport = new I2CTransport(0, 1, 0x3C);
        OBCdisplay.init(transport);
    }
}
