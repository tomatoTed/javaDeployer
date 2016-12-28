package com.gx.javaFX;

/**
 * Created by gaoxiang on 2016/12/27.
 */
public class Log {
    public static void debug(Object... obj){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<obj.length;i++){
            sb.append(obj.toString());
            if(i<obj.length-1){
                sb.append(",");
            }
        }
        System.out.println(sb.toString());
    }
}
