package com.motorola.fmradio;

import com.motorola.fmradio.IFMRadioPlayerServiceCallbacks;

interface IFMRadioPlayerService {
    boolean powerOn();
    void powerOff();
    boolean isPowered();

    boolean tune(int freq);
    boolean seek(int freq, boolean upward);
    boolean stopSeek();

    boolean scan();
    boolean stopScan();

    void setAudioRouting(int mode);
    int getAudioRouting();

    void registerCallbacks(IFMRadioPlayerServiceCallbacks cb);
    void unregisterCallbacks();
}
