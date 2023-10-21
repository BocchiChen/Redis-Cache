package com.github.IRedis.cache.core.model;

import java.util.Objects;

public class FreqNode<K, V> {
    private K key;

    private V value;

    private long frequency = 1;

    public FreqNode(){}

    public FreqNode(K key, V value){
        this.key = key;
        this.value = value;
    }

    public FreqNode<K, V> key(K key){
        this.key = key;
        return this;
    }

    public FreqNode<K, V> value(V value){
        this.value = value;
        return this;
    }

    public FreqNode<K, V> frequency(long freq){
        this.frequency = freq;
        return this;
    }

    public K getKey(){
        return this.key;
    }

    public V getValue(){
        return this.value;
    }

    public long getFrequency(){
        return this.frequency;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || this.getClass() != o.getClass()){
            return false;
        }
        FreqNode<?, ?> freqNode = (FreqNode<?, ?>) o;
        return freqNode.frequency == this.frequency
                && freqNode.key == this.key
                && freqNode.value == this.value;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.key, this.value, this.frequency);
    }
}
