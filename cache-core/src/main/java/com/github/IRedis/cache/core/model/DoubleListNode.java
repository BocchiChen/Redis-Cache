package com.github.IRedis.cache.core.model;

public class DoubleListNode<K, V> {
    private K key;

    private V value;

    private DoubleListNode<K, V> prev;

    private DoubleListNode<K, V> next;

    public DoubleListNode(){

    }

    public DoubleListNode(K key, V value){
        this.key = key;
        this.value = value;
        this.prev = null;
        this.next = null;
    }

    public DoubleListNode(K key, V value, DoubleListNode<K, V> prev, DoubleListNode<K, V> next){
        this.key = key;
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    public DoubleListNode<K, V> key(K key){
        this.key = key;
        return this;
    }

    public DoubleListNode<K, V> value(V value){
        this.value = value;
        return this;
    }

    public DoubleListNode<K, V> prev(DoubleListNode<K, V> prev){
        this.prev = prev;
        return this;
    }

    public DoubleListNode<K, V> next(DoubleListNode<K, V> next){
        this.next = next;
        return this;
    }

    public K getKey(){
        return this.key;
    }

    public V getValue(){
        return this.value;
    }

    public DoubleListNode<K, V> getPrev(){
        return this.prev;
    }

    public DoubleListNode<K, V> getNext(){
        return this.next;
    }
}
