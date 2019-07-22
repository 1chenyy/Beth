package com.chen.beth.Utils;

import java.text.NumberFormat;

public class ConvertUtil {
    public static long HexStringToLong(String hex){
        if (hex.startsWith("0x"))
            hex = hex.substring(2);
        long result = -1;
        try{
            result = Long.parseLong(hex,16);
        }catch (Exception e){
            result = -1;
        }
        return result;
    }

    public static NumberFormat NF_WEI = NumberFormat.getInstance();
    static {
        NF_WEI.setGroupingUsed(true);
    }

    public static String HexStringToWei(String hex){
        long v = HexStringToLong(hex);
        if (v>=0)
            return NF_WEI.format(v);
        return "";
    }

}
