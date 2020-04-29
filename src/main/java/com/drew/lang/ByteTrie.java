package com.drew.lang;

import com.drew.lang.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ByteTrie<T> {
    private int _maxDepth;
    private final ByteTrieNode<T> _root = new ByteTrieNode<>();

    static class ByteTrieNode<T> {
        /* access modifiers changed from: private */
        public final Map<Byte, ByteTrieNode<T>> _children = new HashMap();
        /* access modifiers changed from: private */
        public T _value = null;

        ByteTrieNode() {
        }

        public void setValue(T value) {
            if (this._value != null) {
                throw new RuntimeException("Value already set for this trie node");
            }
            this._value = value;
        }
    }

    @Nullable
    public T find(byte[] bytes) {
        ByteTrieNode<T> node = this._root;
        T value = node._value;
        for (byte b : bytes) {
            ByteTrieNode<T> child = (ByteTrieNode) node._children.get(Byte.valueOf(b));
            if (child == null) {
                break;
            }
            node = child;
            if (node._value != null) {
                value = node._value;
            }
        }
        return value;
    }

    public void addPath(T value, byte[]... parts) {
        int depth = 0;
        ByteTrieNode<T> node = this._root;
        byte[][] arr$ = parts;
        for (byte[] arr$2 : arr$) {
            for (byte b : arr$2) {
                ByteTrieNode<T> child = (ByteTrieNode) node._children.get(Byte.valueOf(b));
                if (child == null) {
                    child = new ByteTrieNode<>();
                    node._children.put(Byte.valueOf(b), child);
                }
                node = child;
                depth++;
            }
        }
        node.setValue(value);
        this._maxDepth = Math.max(this._maxDepth, depth);
    }

    public void setDefaultValue(T defaultValue) {
        this._root.setValue(defaultValue);
    }

    public int getMaxDepth() {
        return this._maxDepth;
    }
}
