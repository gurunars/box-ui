package com.gurunars.leaflet_view;

import java.util.List;

public class Finder {

    public interface Equator<T> {
        boolean equal(T one, T two);
    }

    public static <T> boolean contains(List<? extends T> list, T item, Equator<T> equator){
        return indexOf(list, item, equator) > 0;
    }

    public static <T> int indexOf(List<? extends T> list, T item, Equator<T> equator){
        for(int i=0; i < list.size(); i++) {
            T cursor = list.get(i);
            if (equator.equal(cursor, item)) {
                return i;
            }
        }
        return -1;
    }

}
