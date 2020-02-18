package com.custom.dialog.lab.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


/**
 * Props
 */
public class Props {

    private Properties core;

    private String flowloc;

    public Props(){
		setProps();
		setup();
	}

    private void setProps(){
		File file = new File(System.getProperty("user.dir")+"/configs/app.properties");
		core = new Properties();
		try {
			core.load(new FileInputStream(file));	
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}	
    }
    
    private void setup() {
		this.flowloc = core.getProperty("dialogue.flows.location");
    }
    
    public String getFlowLoc(){
        return flowloc;
    }
}