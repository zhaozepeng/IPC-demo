package com.android.shareuserid_b;

/**
 * Description: Method
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-04
 */
public class Method {

    /**
     * 加法操作
     */
    public int add(int... integers){
        int sum = 0;
        for (int integer : integers)
            sum += integer;
        return sum;
    }
}
