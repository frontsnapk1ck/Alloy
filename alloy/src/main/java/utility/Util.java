package utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Util {

    public static int parseInt(String num) throws NumberFormatException
    {
        try {
            return Integer.parseInt(num);
        }
        catch (NumberFormatException e) 
        {
            throw e;
        } 
    }

    public static int parseInt(String num, int defaultNum) 
    {
		try {
            return Integer.parseInt(num);
        }
        catch (Exception e) 
        {
            return defaultNum;
        }
    }

    public static <T> List<T> copy(List<T> put, List<? extends T> from) {
        for (T obj : from)
            put.add(obj);
        return put;
    }

    public static <H, K> Map<H, K> copy(Map<H, K> put, Map<? extends H, ? extends K> from) 
    {
        Set<? extends H> keys = from.keySet();
        for (H key : keys)
            put.put(key, from.get(key));
        
        return put;
    }
    
    public static <T> List<T> arrayToList(T[] arr)
    {
        List<T> list = new ArrayList<T>();
        for (T obj : arr) 
            list.add(obj);
        
        return list;
    }

    public static <T> T[] addOneToArray(T[] arr)
    {
        List<T> list = new ArrayList<T>();
        for (T t : arr)
            list.add(t);
        list.add(null);
        T[] newArr = list.toArray(arr);
        return newArr;
    }

    public static boolean validInt(String num) 
    {
        try 
        {
            parseInt(num);
            return true;
        }
        catch (NumberFormatException e) 
        {
            return false;
        }
	}

    public static <T> T[] arrRange( T[] arr , int beginIndex) 
    {
		return arrRange(arr , beginIndex, arr.length);
	}

    private static <T> T[] arrRange(T[] arr, int beginIndex, int endIndex) 
    {
        List<T> list = new ArrayList<T>();
        for (int i = beginIndex; i < endIndex; i++) 
            list.add(arr[i]);
        
        return list.toArray(arr);
    }


}