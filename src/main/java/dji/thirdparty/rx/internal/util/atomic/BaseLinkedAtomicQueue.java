package dji.thirdparty.rx.internal.util.atomic;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

abstract class BaseLinkedAtomicQueue<E> extends AbstractQueue<E> {
    private final AtomicReference<LinkedQueueNode<E>> consumerNode = new AtomicReference<>();
    private final AtomicReference<LinkedQueueNode<E>> producerNode = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lvProducerNode() {
        return this.producerNode.get();
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lpProducerNode() {
        return this.producerNode.get();
    }

    /* access modifiers changed from: protected */
    public final void spProducerNode(LinkedQueueNode<E> node) {
        this.producerNode.lazySet(node);
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> xchgProducerNode(LinkedQueueNode<E> node) {
        return this.producerNode.getAndSet(node);
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lvConsumerNode() {
        return this.consumerNode.get();
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lpConsumerNode() {
        return this.consumerNode.get();
    }

    /* access modifiers changed from: protected */
    public final void spConsumerNode(LinkedQueueNode<E> node) {
        this.consumerNode.lazySet(node);
    }

    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public final int size() {
        LinkedQueueNode<E> next;
        LinkedQueueNode<E> chaserNode = lvConsumerNode();
        LinkedQueueNode<E> producerNode2 = lvProducerNode();
        int size = 0;
        while (chaserNode != producerNode2 && size < Integer.MAX_VALUE) {
            do {
                next = chaserNode.lvNext();
            } while (next == null);
            chaserNode = next;
            size++;
        }
        return size;
    }

    public final boolean isEmpty() {
        return lvConsumerNode() == lvProducerNode();
    }
}
