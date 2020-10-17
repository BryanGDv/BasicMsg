package code.interfaces;

import java.util.Map;

public interface Cache<K, V>{

    Map<K, V> get();

    default void remove(K key){
        get().remove(key);
    }
    default void add(K key, V value){
        get().put(key, value);
    }

    default boolean contains(K key){
        return get().containsKey(key);
    }


}
