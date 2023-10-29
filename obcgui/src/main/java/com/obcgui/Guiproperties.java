package com.obcgui;

import java.util.Arrays;

public class Guiproperties {
    public String[] playerName = new String[4];
    public boolean[] playerOnline = new boolean[4];
    public boolean[] isVirtualCdj = new boolean[4];
    public boolean[] isMaster = new boolean[4];
    public double tempo = 0.0;
    public double phase = 0.0;
    public int latency = 0;

    public void main(String[] args){
        Arrays.fill(playerName, "");
        Arrays.fill(playerOnline, false);
        Arrays.fill(isVirtualCdj, false);
        Arrays.fill(isMaster, false);
    }
}
