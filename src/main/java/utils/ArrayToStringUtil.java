package utils;

import java.util.ArrayList;

/**
 * Created by ZLS on 2017/5/11.
 */
public class ArrayToStringUtil {
    public static void toString(ArrayList<String> list) {
        if (list != null)
            for (String s : list) {
                System.out.print(s + "  ");
            }
        System.out.println();
    }

    public static void toString(String[] ss) {
        if (ss != null)
            for (String s : ss) {
                System.out.print(s + "  ");
            }
        System.out.println();
    }
}
