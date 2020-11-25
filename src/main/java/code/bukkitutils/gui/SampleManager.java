package code.bukkitutils.gui;

import code.Manager;

public class SampleManager {

    private Manager manager;

    private OnlineSample onlineSample;

    public SampleManager(Manager manager){
        this.manager = manager;
        setup();
    }

    public void setup(){
       onlineSample = new OnlineSample(manager);
    }

    public OnlineSample getOnlineSample(){
        return onlineSample;
    }
}
