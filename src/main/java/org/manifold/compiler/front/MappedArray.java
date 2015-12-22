package org.manifold.compiler.front;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.*;

public class MappedArray<K, V> implements Iterable<MappedArray<K, V>.Entry> {

  private List<V> data;
  private BiMap<K, Integer> keyedIndices = HashBiMap.create();

  public MappedArray(Map<K, V> data) {
    this.data = new ArrayList<>(data.size());

    int i = 0;
    for (Map.Entry<K, V> entry : data.entrySet()) {
      this.data.add(entry.getValue());
      keyedIndices.put(entry.getKey(), i);
    }
  }

  public MappedArray(List<V> data) {
    this.data = new ArrayList<>(data);
  }

  public MappedArray() {
    this(new ArrayList<>());
  }

  public MappedArray(Map<K, Integer> keyedIndices, List<V> data) {
    this.keyedIndices = HashBiMap.create(keyedIndices);
    this.data = new ArrayList<>(data);
  }

  public class Entry {
    private K key;
    private V value;
    private int index;

    public Entry(K key, V value, int index) {
      this.key = key;
      this.value = value;
      this.index = index;
    }

    public K getKey() {
      return key;
    }

    public V getValue() {
      return value;
    }

    public int getIndex() {
      return index;
    }
  }

  public boolean contains(V o) {
    return data.contains(o);
  }

  public boolean containsKey(K key) {
    return keyedIndices.containsKey(key);
  }

  @Override
  public Iterator<Entry> iterator() {
    Iterator<Entry> it = new Iterator<Entry>() {
      private int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex < size();
      }

      @Override
      public Entry next() {
        K key = keyedIndices.inverse().get(currentIndex);
        V value = get(currentIndex);
        return new Entry(key, value, currentIndex++);
      }
    };

    return it;
  }

  public List<V> values() {
    return this.data;
  }

  public boolean add(V t) {
    return data.add(t);
  }

  public void add(int index, V element) {
    data.add(index, element);
  }

  public V put(K key, V value) {
    if (keyedIndices.containsKey(key)) {
      V prevValue = this.data.get(keyedIndices.get(key));
      this.data.set(keyedIndices.get(key), value);
      return prevValue;
    } else if (key != null) {
      keyedIndices.put(key, data.size());
    }

    this.data.add(value);
    return null;
  }

  public V get(int index) {
    return data.get(index);
  }

  public V get(K key) {
    Integer k = keyedIndices.get(key);
    return k != null ? data.get(k) : null;
  }

  public V set(int index, V element) {
    return data.set(index, element);
  }

  public V remove(int index) {
    K key = keyedIndices.inverse().get(index);
    return key != null ? remove(key) : null;
  }

  public V remove(K key) {
    if (!containsKey(key)) {
      return null;
    }
    V prevValue = get(key);
    data.remove(keyedIndices.get(key));
    keyedIndices.remove(key);
    return prevValue;
  }

  public int size() {
    return data.size();
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  public static <K, V> MappedArray<K, V> copyOf(MappedArray<K, V> mappedArray) {
    return new MappedArray<>(mappedArray.keyedIndices, mappedArray.data);
  }

  public static <K extends Object, V> Map<String, V> toMap(MappedArray<K, V> mappedArray) {
    Map<String, V> map = new LinkedHashMap<>(mappedArray.size());
    for (MappedArray<K, V>.Entry entry : mappedArray) {
      K key = entry.getKey();
      String newKey = key != null ? key.toString() : Integer.toString(entry.getIndex());
      map.put(newKey, entry.getValue());
    }
    return map;
  }
}
