package com.google.protobuf;

import com.google.protobuf.Internal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class ListFieldSchema {
    private static final ListFieldSchema FULL_INSTANCE = new ListFieldSchemaFull();
    private static final ListFieldSchema LITE_INSTANCE = new ListFieldSchemaLite();

    /* access modifiers changed from: package-private */
    public abstract void makeImmutableListAt(Object obj, long j);

    /* access modifiers changed from: package-private */
    public abstract <L> void mergeListsAt(Object obj, Object obj2, long j);

    /* access modifiers changed from: package-private */
    public abstract <L> List<L> mutableListAt(Object obj, long j);

    private ListFieldSchema() {
    }

    static ListFieldSchema full() {
        return FULL_INSTANCE;
    }

    static ListFieldSchema lite() {
        return LITE_INSTANCE;
    }

    private static final class ListFieldSchemaFull extends ListFieldSchema {
        private static final Class<?> UNMODIFIABLE_LIST_CLASS = Collections.unmodifiableList(Collections.emptyList()).getClass();

        private ListFieldSchemaFull() {
            super();
        }

        /* access modifiers changed from: package-private */
        public <L> List<L> mutableListAt(Object message, long offset) {
            return mutableListAt(message, offset, 10);
        }

        /* access modifiers changed from: package-private */
        public void makeImmutableListAt(Object message, long offset) {
            Object immutable;
            List<?> list = (List) UnsafeUtil.getObject(message, offset);
            if (list instanceof LazyStringList) {
                immutable = ((LazyStringList) list).getUnmodifiableView();
            } else if (UNMODIFIABLE_LIST_CLASS.isAssignableFrom(list.getClass())) {
                return;
            } else {
                if (!(list instanceof PrimitiveNonBoxingCollection) || !(list instanceof Internal.ProtobufList)) {
                    immutable = Collections.unmodifiableList(list);
                } else if (((Internal.ProtobufList) list).isModifiable()) {
                    ((Internal.ProtobufList) list).makeImmutable();
                    return;
                } else {
                    return;
                }
            }
            UnsafeUtil.putObject(message, offset, immutable);
        }

        private static <L> List<L> mutableListAt(Object message, long offset, int additionalCapacity) {
            List<L> list;
            List<L> list2 = getList(message, offset);
            if (list2.isEmpty()) {
                if (list2 instanceof LazyStringList) {
                    list = new LazyStringArrayList(additionalCapacity);
                } else if (!(list2 instanceof PrimitiveNonBoxingCollection) || !(list2 instanceof Internal.ProtobufList)) {
                    list = new ArrayList<>(additionalCapacity);
                } else {
                    list = ((Internal.ProtobufList) list2).mutableCopyWithCapacity(additionalCapacity);
                }
                UnsafeUtil.putObject(message, offset, list);
                return list;
            } else if (UNMODIFIABLE_LIST_CLASS.isAssignableFrom(list2.getClass())) {
                List<L> newList = new ArrayList<>(list2.size() + additionalCapacity);
                newList.addAll(list2);
                List<L> list3 = newList;
                UnsafeUtil.putObject(message, offset, list3);
                return list3;
            } else if (list2 instanceof UnmodifiableLazyStringList) {
                LazyStringArrayList newList2 = new LazyStringArrayList(list2.size() + additionalCapacity);
                newList2.addAll((UnmodifiableLazyStringList) list2);
                List<L> list4 = newList2;
                UnsafeUtil.putObject(message, offset, list4);
                return list4;
            } else if (!(list2 instanceof PrimitiveNonBoxingCollection) || !(list2 instanceof Internal.ProtobufList) || ((Internal.ProtobufList) list2).isModifiable()) {
                return list2;
            } else {
                List<L> list5 = ((Internal.ProtobufList) list2).mutableCopyWithCapacity(list2.size() + additionalCapacity);
                UnsafeUtil.putObject(message, offset, list5);
                return list5;
            }
        }

        /* access modifiers changed from: package-private */
        public <E> void mergeListsAt(Object msg, Object otherMsg, long offset) {
            List<E> merged;
            List<E> other = getList(otherMsg, offset);
            List<E> mine = mutableListAt(msg, offset, other.size());
            int size = mine.size();
            int otherSize = other.size();
            if (size > 0 && otherSize > 0) {
                mine.addAll(other);
            }
            if (size > 0) {
                merged = mine;
            } else {
                merged = other;
            }
            UnsafeUtil.putObject(msg, offset, merged);
        }

        static <E> List<E> getList(Object message, long offset) {
            return (List) UnsafeUtil.getObject(message, offset);
        }
    }

    private static final class ListFieldSchemaLite extends ListFieldSchema {
        private ListFieldSchemaLite() {
            super();
        }

        /* access modifiers changed from: package-private */
        public <L> List<L> mutableListAt(Object message, long offset) {
            Internal.ProtobufList<L> list = getProtobufList(message, offset);
            if (list.isModifiable()) {
                return list;
            }
            int size = list.size();
            Internal.ProtobufList<L> list2 = list.mutableCopyWithCapacity(size == 0 ? 10 : size * 2);
            UnsafeUtil.putObject(message, offset, list2);
            return list2;
        }

        /* access modifiers changed from: package-private */
        public void makeImmutableListAt(Object message, long offset) {
            getProtobufList(message, offset).makeImmutable();
        }

        /* access modifiers changed from: package-private */
        public <E> void mergeListsAt(Object msg, Object otherMsg, long offset) {
            Internal.ProtobufList<E> merged;
            Internal.ProtobufList<E> mine = getProtobufList(msg, offset);
            Internal.ProtobufList<E> other = getProtobufList(otherMsg, offset);
            int size = mine.size();
            int otherSize = other.size();
            if (size > 0 && otherSize > 0) {
                if (!mine.isModifiable()) {
                    mine = mine.mutableCopyWithCapacity(size + otherSize);
                }
                mine.addAll(other);
            }
            if (size > 0) {
                merged = mine;
            } else {
                merged = other;
            }
            UnsafeUtil.putObject(msg, offset, merged);
        }

        static <E> Internal.ProtobufList<E> getProtobufList(Object message, long offset) {
            return (Internal.ProtobufList) UnsafeUtil.getObject(message, offset);
        }
    }
}
