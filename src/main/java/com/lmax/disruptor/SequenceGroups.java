package com.lmax.disruptor;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

class SequenceGroups {
    SequenceGroups() {
    }

    static <T> void addSequences(T holder, AtomicReferenceFieldUpdater<T, Sequence[]> updater, Cursored cursor, Sequence... sequencesToAdd) {
        Sequence[] currentSequences;
        Sequence[] updatedSequences;
        do {
            currentSequences = updater.get(holder);
            updatedSequences = (Sequence[]) Arrays.copyOf(currentSequences, currentSequences.length + sequencesToAdd.length);
            long cursorSequence = cursor.getCursor();
            int index = currentSequences.length;
            int length = sequencesToAdd.length;
            int i = 0;
            int index2 = index;
            while (i < length) {
                Sequence sequence = sequencesToAdd[i];
                sequence.set(cursorSequence);
                updatedSequences[index2] = sequence;
                i++;
                index2++;
            }
        } while (!updater.compareAndSet(holder, currentSequences, updatedSequences));
        long cursorSequence2 = cursor.getCursor();
        for (Sequence sequence2 : sequencesToAdd) {
            sequence2.set(cursorSequence2);
        }
    }

    static <T> boolean removeSequence(T holder, AtomicReferenceFieldUpdater<T, Sequence[]> sequenceUpdater, Sequence sequence) {
        Sequence[] oldSequences;
        int numToRemove;
        Sequence[] newSequences;
        int pos;
        do {
            oldSequences = sequenceUpdater.get(holder);
            numToRemove = countMatching(oldSequences, sequence);
            if (numToRemove == 0) {
                break;
            }
            int oldSize = oldSequences.length;
            newSequences = new Sequence[(oldSize - numToRemove)];
            int i = 0;
            int pos2 = 0;
            while (i < oldSize) {
                Sequence testSequence = oldSequences[i];
                if (sequence != testSequence) {
                    pos = pos2 + 1;
                    newSequences[pos2] = testSequence;
                } else {
                    pos = pos2;
                }
                i++;
                pos2 = pos;
            }
        } while (!sequenceUpdater.compareAndSet(holder, oldSequences, newSequences));
        if (numToRemove != 0) {
            return true;
        }
        return false;
    }

    private static <T> int countMatching(T[] values, T toMatch) {
        int numToRemove = 0;
        for (T value : values) {
            if (value == toMatch) {
                numToRemove++;
            }
        }
        return numToRemove;
    }
}
