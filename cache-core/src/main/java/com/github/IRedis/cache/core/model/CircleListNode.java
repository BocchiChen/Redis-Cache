package com.github.IRedis.cache.core.model;

public class CircleListNode<K, V> {
    private K key;

    private V value;

    private boolean accessFlag = false;

    private CircleListNode<K, V> prev;

    private CircleListNode<K, V> next;

    public CircleListNode(){

    }

    public CircleListNode(K key){
        this.key = key;
    }

    public CircleListNode(K key, V value){
        this.key = key;
        this.value = value;
    }

    public CircleListNode(K key, V value, CircleListNode<K, V> prev){
        this.key = key;
        this.value = value;
        this.prev = prev;
    }

    public CircleListNode(K key, V value, CircleListNode<K, V> prev, CircleListNode<K, V> next){
        this.key = key;
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    public CircleListNode<K, V> key(K key){
        this.key = key;
        return this;
    }

    public CircleListNode<K, V> value(V value){
        this.value = value;
        return this;
    }

    public CircleListNode<K, V> prev(CircleListNode<K, V> prev){
        this.prev = prev;
        return this;
    }

    public CircleListNode<K, V> next(CircleListNode<K, V> next){
        this.next = next;
        return this;
    }

    public CircleListNode<K, V> accessFlag(boolean accessFlag){
        this.accessFlag = accessFlag;
        return this;
    }

    public K getKey(){
        return this.key;
    }

    public V getValue(){
        return this.value;
    }

    public CircleListNode<K, V> getPrev(){
        return this.prev;
    }

    public CircleListNode<K, V> getNext(){
        return this.next;
    }

    public boolean getAccessFlag(){
        return this.accessFlag;
    }
}
