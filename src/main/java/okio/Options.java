package okio;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public final class Options extends AbstractList<ByteString> implements RandomAccess {
    final ByteString[] byteStrings;
    final int[] trie;

    private Options(ByteString[] byteStrings2, int[] trie2) {
        this.byteStrings = byteStrings2;
        this.trie = trie2;
    }

    public static Options of(ByteString... byteStrings2) {
        if (byteStrings2.length == 0) {
            return new Options(new ByteString[0], new int[]{0, -1});
        }
        List<ByteString> list = new ArrayList<>(Arrays.asList(byteStrings2));
        Collections.sort(list);
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            indexes.add(-1);
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            indexes.set(Collections.binarySearch(list, byteStrings2[i2]), Integer.valueOf(i2));
        }
        if (((ByteString) list.get(0)).size() == 0) {
            throw new IllegalArgumentException("the empty byte string is not a supported option");
        }
        for (int a = 0; a < list.size(); a++) {
            ByteString prefix = (ByteString) list.get(a);
            int b = a + 1;
            while (b < list.size()) {
                ByteString byteString = (ByteString) list.get(b);
                if (!byteString.startsWith(prefix)) {
                    continue;
                    break;
                } else if (byteString.size() == prefix.size()) {
                    throw new IllegalArgumentException("duplicate option: " + byteString);
                } else if (((Integer) indexes.get(b)).intValue() > ((Integer) indexes.get(a)).intValue()) {
                    list.remove(b);
                    indexes.remove(b);
                } else {
                    b++;
                }
            }
        }
        Buffer trieBytes = new Buffer();
        buildTrieRecursive(0, trieBytes, 0, list, 0, list.size(), indexes);
        int[] trie2 = new int[intCount(trieBytes)];
        for (int i3 = 0; i3 < trie2.length; i3++) {
            trie2[i3] = trieBytes.readInt();
        }
        if (trieBytes.exhausted()) {
            return new Options((ByteString[]) byteStrings2.clone(), trie2);
        }
        throw new AssertionError();
    }

    private static void buildTrieRecursive(long nodeOffset, Buffer node, int byteStringOffset, List<ByteString> byteStrings2, int fromIndex, int toIndex, List<Integer> indexes) {
        int rangeEnd;
        if (fromIndex >= toIndex) {
            throw new AssertionError();
        }
        for (int i = fromIndex; i < toIndex; i++) {
            if (byteStrings2.get(i).size() < byteStringOffset) {
                throw new AssertionError();
            }
        }
        ByteString from = byteStrings2.get(fromIndex);
        ByteString to = byteStrings2.get(toIndex - 1);
        int prefixIndex = -1;
        if (byteStringOffset == from.size()) {
            prefixIndex = indexes.get(fromIndex).intValue();
            fromIndex++;
            from = byteStrings2.get(fromIndex);
        }
        if (from.getByte(byteStringOffset) != to.getByte(byteStringOffset)) {
            int selectChoiceCount = 1;
            for (int i2 = fromIndex + 1; i2 < toIndex; i2++) {
                if (byteStrings2.get(i2 - 1).getByte(byteStringOffset) != byteStrings2.get(i2).getByte(byteStringOffset)) {
                    selectChoiceCount++;
                }
            }
            long childNodesOffset = ((long) intCount(node)) + nodeOffset + 2 + ((long) (selectChoiceCount * 2));
            node.writeInt(selectChoiceCount);
            node.writeInt(prefixIndex);
            for (int i3 = fromIndex; i3 < toIndex; i3++) {
                byte rangeByte = byteStrings2.get(i3).getByte(byteStringOffset);
                if (i3 != fromIndex) {
                    if (rangeByte == byteStrings2.get(i3 - 1).getByte(byteStringOffset)) {
                    }
                }
                node.writeInt((int) (rangeByte & 255));
            }
            Buffer childNodes = new Buffer();
            for (int rangeStart = fromIndex; rangeStart < toIndex; rangeStart = rangeEnd) {
                byte rangeByte2 = byteStrings2.get(rangeStart).getByte(byteStringOffset);
                rangeEnd = toIndex;
                int i4 = rangeStart + 1;
                while (true) {
                    if (i4 >= toIndex) {
                        break;
                    } else if (rangeByte2 != byteStrings2.get(i4).getByte(byteStringOffset)) {
                        rangeEnd = i4;
                        break;
                    } else {
                        i4++;
                    }
                }
                if (rangeStart + 1 == rangeEnd && byteStringOffset + 1 == byteStrings2.get(rangeStart).size()) {
                    node.writeInt(indexes.get(rangeStart).intValue());
                } else {
                    node.writeInt((int) (-1 * (((long) intCount(childNodes)) + childNodesOffset)));
                    buildTrieRecursive(childNodesOffset, childNodes, byteStringOffset + 1, byteStrings2, rangeStart, rangeEnd, indexes);
                }
            }
            node.write(childNodes, childNodes.size());
            return;
        }
        int scanByteCount = 0;
        int i5 = byteStringOffset;
        int max = Math.min(from.size(), to.size());
        while (i5 < max && from.getByte(i5) == to.getByte(i5)) {
            scanByteCount++;
            i5++;
        }
        long childNodesOffset2 = ((long) intCount(node)) + nodeOffset + 2 + ((long) scanByteCount) + 1;
        node.writeInt(-scanByteCount);
        node.writeInt(prefixIndex);
        for (int i6 = byteStringOffset; i6 < byteStringOffset + scanByteCount; i6++) {
            node.writeInt((int) (from.getByte(i6) & 255));
        }
        if (fromIndex + 1 != toIndex) {
            Buffer childNodes2 = new Buffer();
            node.writeInt((int) (-1 * (((long) intCount(childNodes2)) + childNodesOffset2)));
            buildTrieRecursive(childNodesOffset2, childNodes2, byteStringOffset + scanByteCount, byteStrings2, fromIndex, toIndex, indexes);
            node.write(childNodes2, childNodes2.size());
        } else if (byteStringOffset + scanByteCount != byteStrings2.get(fromIndex).size()) {
            throw new AssertionError();
        } else {
            node.writeInt(indexes.get(fromIndex).intValue());
        }
    }

    public ByteString get(int i) {
        return this.byteStrings[i];
    }

    public final int size() {
        return this.byteStrings.length;
    }

    private static int intCount(Buffer trieBytes) {
        return (int) (trieBytes.size() / 4);
    }
}
