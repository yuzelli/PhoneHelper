package com.example.administrator.phonehelper.utils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by asus on 2016/10/12.
 */
public class FileUtils {


    public static void deleteFile(File file){
        if(file!=null){
            if(file.isFile()){
                file.delete();
            }else{
                File files[]=file.listFiles();
                if(files!=null){
                    for(int i=0;i<files.length;i++){
                        deleteFile(files[i]);
                    }
                }
            }
        }
    }

    public  static long getFileSize(File file){
        long size=0;
        if(file!=null){
            //如果是文件直接返回大小
            if(file.isFile()){
                return  file.length();
            }else{//文件夹
                File files[]=file.listFiles();
                if(files!=null){
                    for(int i=0;i<files.length;i++){
                        if(files[i].isDirectory()){
                            size= size+getFileSize(files[i]);
                        }else{
                            size=size+files[i].length();
                        }
                    }
                }
            }
        }
        return size;
    }


    public static String getFileSizeString(long size){

        DecimalFormat df=new DecimalFormat("#.00");
        StringBuffer sb=new StringBuffer();
        if(size<1024){
            sb.append(size).append("B");
        }else if(size<1024*1024) {
            sb.append(df.format((double) size / 1024)).append("KB");
        }else if(size<1024*1024*1024){
            sb.append(df.format((double) size/(1024*1024))).append("M");
        }else{
            sb.append(df.format((double) size/(1024*1024*1024))).append("G");
        }

         return  sb.toString();

    }



}
