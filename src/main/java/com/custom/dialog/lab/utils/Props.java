package com.custom.dialog.lab.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import lombok.Data;


/**
 * Props
 */
@Data
public class Props {

    private Properties core;

	private String flowloc;
	private String redis_host;
	private int redis_port;
	private String redis_password;

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
		this.redis_host = core.getProperty("redis.host");
		this.redis_port = Integer.parseInt(core.getProperty("redis.port")) ;
		this.redis_password = core.getProperty("redis.password");
    }
    
//    public String getFlowLoc(){
//        return flowloc;
//	}
//	
	/**
	 * @return the redis_host
	 */
	public String getRedis_host() {
		return redis_host;
	}


	/**
	 * @return the redis_port
	 */
	public int getRedis_port() {
		return redis_port;
	}

	/**
	 * @return the redis_password
	 */
	public String getRedis_password() {
		return redis_password;
	}
}