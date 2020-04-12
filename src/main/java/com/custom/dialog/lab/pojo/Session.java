package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Props;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.util.HashMap;
import java.util.Map;

public class Session {

    private static final Props SETTINGS = new Props();
    private RedisCommands<String, String> syncCommands;

    public Session() {
        RedisURI redisURI = RedisURI.Builder.
                redis(SETTINGS.getRedis_host(), SETTINGS.getRedis_port())
                .withPassword(SETTINGS.getRedis_password()).build();
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        syncCommands = connection.sync();
    }
    
    public Map<String,String> testRedis(){
        syncCommands.hset("recordName", "FirstName", "John");
        syncCommands.hset("recordName", "LastName", "Smith");
        Map<String, String> record = syncCommands.hgetall("recordNam");
        System.out.println(record);
        if(record.isEmpty()){
            HashMap<String,String> error = new HashMap<>();
            error.put("1002", "Not found");
            return error;
            
        }
        return record;
    }
    
    

}
