package org.bouncycastle.pqc.crypto.sphincs;

class Tree {

    static class leafaddr {
        int level;
        long subleaf;
        long subtree;

        public leafaddr() {
        }

        public leafaddr(leafaddr leafaddr) {
            this.level = leafaddr.level;
            this.subtree = leafaddr.subtree;
            this.subleaf = leafaddr.subleaf;
        }
    }

    Tree() {
    }

    static void gen_leaf_wots(HashFunctions hashFunctions, byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, leafaddr leafaddr2) {
        byte[] bArr4 = new byte[32];
        byte[] bArr5 = new byte[2144];
        Wots wots = new Wots();
        Seed.get_seed(hashFunctions, bArr4, 0, bArr3, leafaddr2);
        wots.wots_pkgen(hashFunctions, bArr5, 0, bArr4, 0, bArr2, i2);
        l_tree(hashFunctions, bArr, i, bArr5, 0, bArr2, i2);
    }

    static void l_tree(HashFunctions hashFunctions, byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int i3) {
        int i4;
        int i5 = 0;
        int i6 = 67;
        while (i5 < 7) {
            int i7 = 0;
            while (true) {
                int i8 = i7;
                if (i8 >= (i6 >>> 1)) {
                    break;
                }
                hashFunctions.hash_2n_n_mask(bArr2, i2 + (i8 * 32), bArr2, i2 + (i8 * 2 * 32), bArr3, i3 + (i5 * 2 * 32));
                i7 = i8 + 1;
            }
            if ((i6 & 1) != 0) {
                System.arraycopy(bArr2, ((i6 - 1) * 32) + i2, bArr2, ((i6 >>> 1) * 32) + i2, 32);
                i4 = (i6 >>> 1) + 1;
            } else {
                i4 = i6 >>> 1;
            }
            i5++;
            i6 = i4;
        }
        System.arraycopy(bArr2, i2, bArr, i, 32);
    }

    static void treehash(HashFunctions hashFunctions, byte[] bArr, int i, int i2, byte[] bArr2, leafaddr leafaddr2, byte[] bArr3, int i3) {
        leafaddr leafaddr3 = new leafaddr(leafaddr2);
        byte[] bArr4 = new byte[((i2 + 1) * 32)];
        int[] iArr = new int[(i2 + 1)];
        int i4 = 0;
        int i5 = (int) (leafaddr3.subleaf + ((long) (1 << i2)));
        while (true) {
            int i6 = i4;
            if (leafaddr3.subleaf >= ((long) i5)) {
                break;
            }
            gen_leaf_wots(hashFunctions, bArr4, i6 * 32, bArr3, i3, bArr2, leafaddr3);
            iArr[i6] = 0;
            i4 = i6 + 1;
            while (i4 > 1 && iArr[i4 - 1] == iArr[i4 - 2]) {
                hashFunctions.hash_2n_n_mask(bArr4, (i4 - 2) * 32, bArr4, (i4 - 2) * 32, bArr3, i3 + ((iArr[i4 - 1] + 7) * 2 * 32));
                int i7 = i4 - 2;
                iArr[i7] = iArr[i7] + 1;
                i4--;
            }
            leafaddr3.subleaf++;
        }
        for (int i8 = 0; i8 < 32; i8++) {
            bArr[i + i8] = bArr4[i8];
        }
    }
}
