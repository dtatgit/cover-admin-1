package com.jeeplus.modules.cv.utils;


import java.util.*;

public class CollectionUtils {
    //List转化成Set
    public static Set transSet(List list){
        Set set = new HashSet();
        set.addAll(list);
        return set;
    }

    //Set转化成List
    public static List transList(Set set){
        List list = new ArrayList();
        Iterator it=set.iterator();
        while(it.hasNext())
        {
            list.add(it.next());
        }
        return list;
    }
}
