package org.bouncycastle.pqc.crypto.xmss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import org.bouncycastle.pqc.crypto.xmss.HashTreeAddress;
import org.bouncycastle.pqc.crypto.xmss.LTreeAddress;
import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;

public final class BDS implements Serializable {
    private static final long serialVersionUID = 1;
    private List<XMSSNode> authenticationPath;
    private int index;
    private int k;
    private Map<Integer, XMSSNode> keep;
    private Map<Integer, LinkedList<XMSSNode>> retain;
    private XMSSNode root;
    /* access modifiers changed from: private */
    public Stack<XMSSNode> stack;
    private List<TreeHash> treeHashInstances;
    private final int treeHeight;
    /* access modifiers changed from: private */
    public transient WOTSPlus wotsPlus;
    /* access modifiers changed from: private */
    public transient XMSS xmss;

    private final class TreeHash implements Serializable {
        private static final long serialVersionUID = 1;
        private boolean finished;
        private int height;
        private final int initialHeight;
        private boolean initialized;
        private int nextIndex;
        /* access modifiers changed from: private */
        public XMSSNode tailNode;

        private TreeHash(int i) {
            this.initialHeight = i;
            this.initialized = false;
            this.finished = false;
        }

        /* access modifiers changed from: private */
        public int getHeight() {
            if (!this.initialized || this.finished) {
                return Integer.MAX_VALUE;
            }
            return this.height;
        }

        /* access modifiers changed from: private */
        public int getIndexLeaf() {
            return this.nextIndex;
        }

        /* access modifiers changed from: private */
        public void initialize(int i) {
            this.tailNode = null;
            this.height = this.initialHeight;
            this.nextIndex = i;
            this.initialized = true;
            this.finished = false;
        }

        /* access modifiers changed from: private */
        public boolean isFinished() {
            return this.finished;
        }

        /* access modifiers changed from: private */
        public boolean isInitialized() {
            return this.initialized;
        }

        /* access modifiers changed from: private */
        public void setNode(XMSSNode xMSSNode) {
            this.tailNode = xMSSNode;
            this.height = xMSSNode.getHeight();
            if (this.height == this.initialHeight) {
                this.finished = true;
            }
        }

        /* access modifiers changed from: private */
        public void update(OTSHashAddress oTSHashAddress) {
            if (oTSHashAddress == null) {
                throw new NullPointerException("otsHashAddress == null");
            } else if (this.finished || !this.initialized) {
                throw new IllegalStateException("finished or not initialized");
            } else {
                OTSHashAddress oTSHashAddress2 = (OTSHashAddress) ((OTSHashAddress.Builder) ((OTSHashAddress.Builder) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(this.nextIndex).withChainAddress(oTSHashAddress.getChainAddress()).withHashAddress(oTSHashAddress.getHashAddress()).withKeyAndMask(oTSHashAddress.getKeyAndMask())).build();
                BDS.this.wotsPlus.importKeys(BDS.this.xmss.getWOTSPlusSecretKey(oTSHashAddress2), BDS.this.xmss.getPublicSeed());
                XMSSNode lTree = BDS.this.xmss.lTree(BDS.this.wotsPlus.getPublicKey(oTSHashAddress2), (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(oTSHashAddress2.getLayerAddress())).withTreeAddress(oTSHashAddress2.getTreeAddress())).withLTreeAddress(this.nextIndex).build());
                HashTreeAddress hashTreeAddress = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(oTSHashAddress2.getLayerAddress())).withTreeAddress(oTSHashAddress2.getTreeAddress())).withTreeIndex(this.nextIndex).build();
                while (!BDS.this.stack.isEmpty() && ((XMSSNode) BDS.this.stack.peek()).getHeight() == lTree.getHeight() && ((XMSSNode) BDS.this.stack.peek()).getHeight() != this.initialHeight) {
                    HashTreeAddress hashTreeAddress2 = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex((hashTreeAddress.getTreeIndex() - 1) / 2).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
                    XMSSNode randomizeHash = BDS.this.xmss.randomizeHash((XMSSNode) BDS.this.stack.pop(), lTree, hashTreeAddress2);
                    lTree = new XMSSNode(randomizeHash.getHeight() + 1, randomizeHash.getValue());
                    hashTreeAddress = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeHeight(hashTreeAddress2.getTreeHeight() + 1).withTreeIndex(hashTreeAddress2.getTreeIndex()).withKeyAndMask(hashTreeAddress2.getKeyAndMask())).build();
                }
                if (this.tailNode == null) {
                    this.tailNode = lTree;
                } else if (this.tailNode.getHeight() == lTree.getHeight()) {
                    HashTreeAddress hashTreeAddress3 = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex((hashTreeAddress.getTreeIndex() - 1) / 2).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
                    lTree = new XMSSNode(this.tailNode.getHeight() + 1, BDS.this.xmss.randomizeHash(this.tailNode, lTree, hashTreeAddress3).getValue());
                    this.tailNode = lTree;
                    HashTreeAddress hashTreeAddress4 = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress3.getLayerAddress())).withTreeAddress(hashTreeAddress3.getTreeAddress())).withTreeHeight(hashTreeAddress3.getTreeHeight() + 1).withTreeIndex(hashTreeAddress3.getTreeIndex()).withKeyAndMask(hashTreeAddress3.getKeyAndMask())).build();
                } else {
                    BDS.this.stack.push(lTree);
                }
                if (this.tailNode.getHeight() == this.initialHeight) {
                    this.finished = true;
                    return;
                }
                this.height = lTree.getHeight();
                this.nextIndex++;
            }
        }
    }

    protected BDS(XMSS xmss2) {
        if (xmss2 == null) {
            throw new NullPointerException("xmss == null");
        }
        this.xmss = xmss2;
        this.wotsPlus = xmss2.getWOTSPlus();
        this.treeHeight = xmss2.getParams().getHeight();
        this.k = xmss2.getParams().getK();
        if (this.k > this.treeHeight || this.k < 2 || (this.treeHeight - this.k) % 2 != 0) {
            throw new IllegalArgumentException("illegal value for BDS parameter k");
        }
        this.authenticationPath = new ArrayList();
        this.retain = new TreeMap();
        this.stack = new Stack<>();
        initializeTreeHashInstances();
        this.keep = new TreeMap();
        this.index = 0;
    }

    private TreeHash getTreeHashInstanceForUpdate() {
        TreeHash treeHash = null;
        for (TreeHash treeHash2 : this.treeHashInstances) {
            if (!treeHash2.isFinished() && treeHash2.isInitialized()) {
                if (treeHash == null) {
                    treeHash = treeHash2;
                } else if (treeHash2.getHeight() < treeHash.getHeight()) {
                    treeHash = treeHash2;
                } else {
                    if (treeHash2.getHeight() != treeHash.getHeight() || treeHash2.getIndexLeaf() >= treeHash.getIndexLeaf()) {
                        treeHash2 = treeHash;
                    }
                    treeHash = treeHash2;
                }
            }
        }
        return treeHash;
    }

    private void initializeTreeHashInstances() {
        this.treeHashInstances = new ArrayList();
        for (int i = 0; i < this.treeHeight - this.k; i++) {
            this.treeHashInstances.add(new TreeHash(i));
        }
    }

    /* access modifiers changed from: protected */
    public List<XMSSNode> getAuthenticationPath() {
        ArrayList arrayList = new ArrayList();
        for (XMSSNode xMSSNode : this.authenticationPath) {
            arrayList.add(xMSSNode.clone());
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public int getIndex() {
        return this.index;
    }

    /* access modifiers changed from: protected */
    public XMSSNode getRoot() {
        return this.root.clone();
    }

    /* access modifiers changed from: protected */
    public int getTreeHeight() {
        return this.treeHeight;
    }

    /* access modifiers changed from: protected */
    public XMSSNode initialize(OTSHashAddress oTSHashAddress) {
        if (oTSHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        }
        int i = 0;
        HashTreeAddress hashTreeAddress = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).build();
        LTreeAddress lTreeAddress = (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).build();
        while (i < (1 << this.treeHeight)) {
            OTSHashAddress oTSHashAddress2 = (OTSHashAddress) ((OTSHashAddress.Builder) ((OTSHashAddress.Builder) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(i).withChainAddress(oTSHashAddress.getChainAddress()).withHashAddress(oTSHashAddress.getHashAddress()).withKeyAndMask(oTSHashAddress.getKeyAndMask())).build();
            this.wotsPlus.importKeys(this.xmss.getWOTSPlusSecretKey(oTSHashAddress2), this.xmss.getPublicSeed());
            WOTSPlusPublicKeyParameters publicKey = this.wotsPlus.getPublicKey(oTSHashAddress2);
            LTreeAddress lTreeAddress2 = (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(i).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(lTreeAddress.getKeyAndMask())).build();
            XMSSNode lTree = this.xmss.lTree(publicKey, lTreeAddress2);
            XMSSAddress build = ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeIndex(i).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
            while (true) {
                hashTreeAddress = (HashTreeAddress) build;
                if (this.stack.isEmpty() || this.stack.peek().getHeight() != lTree.getHeight()) {
                    this.stack.push(lTree);
                    i++;
                    lTreeAddress = lTreeAddress2;
                    oTSHashAddress = oTSHashAddress2;
                } else {
                    int floor = (int) Math.floor((double) (i / (1 << lTree.getHeight())));
                    if (floor == 1) {
                        this.authenticationPath.add(lTree.clone());
                    }
                    if (floor == 3 && lTree.getHeight() < this.treeHeight - this.k) {
                        this.treeHashInstances.get(lTree.getHeight()).setNode(lTree.clone());
                    }
                    if (floor >= 3 && (floor & 1) == 1 && lTree.getHeight() >= this.treeHeight - this.k && lTree.getHeight() <= this.treeHeight - 2) {
                        if (this.retain.get(Integer.valueOf(lTree.getHeight())) == null) {
                            LinkedList linkedList = new LinkedList();
                            linkedList.add(lTree.clone());
                            this.retain.put(Integer.valueOf(lTree.getHeight()), linkedList);
                        } else {
                            this.retain.get(Integer.valueOf(lTree.getHeight())).add(lTree.clone());
                        }
                    }
                    HashTreeAddress hashTreeAddress2 = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex((hashTreeAddress.getTreeIndex() - 1) / 2).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
                    XMSSNode randomizeHash = this.xmss.randomizeHash(this.stack.pop(), lTree, hashTreeAddress2);
                    lTree = new XMSSNode(randomizeHash.getHeight() + 1, randomizeHash.getValue());
                    build = ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeHeight(hashTreeAddress2.getTreeHeight() + 1).withTreeIndex(hashTreeAddress2.getTreeIndex()).withKeyAndMask(hashTreeAddress2.getKeyAndMask())).build();
                }
            }
            this.stack.push(lTree);
            i++;
            lTreeAddress = lTreeAddress2;
            oTSHashAddress = oTSHashAddress2;
        }
        this.root = this.stack.pop();
        return this.root.clone();
    }

    /* access modifiers changed from: protected */
    public void nextAuthenticationPath(OTSHashAddress oTSHashAddress) {
        OTSHashAddress oTSHashAddress2;
        if (oTSHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        } else if (this.index > (1 << this.treeHeight) - 2) {
            throw new IllegalStateException("index out of bounds");
        } else {
            LTreeAddress lTreeAddress = (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).build();
            HashTreeAddress hashTreeAddress = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).build();
            int calculateTau = XMSSUtil.calculateTau(this.index, this.treeHeight);
            if (((this.index >> (calculateTau + 1)) & 1) == 0 && calculateTau < this.treeHeight - 1) {
                this.keep.put(Integer.valueOf(calculateTau), this.authenticationPath.get(calculateTau).clone());
            }
            if (calculateTau == 0) {
                oTSHashAddress2 = (OTSHashAddress) ((OTSHashAddress.Builder) ((OTSHashAddress.Builder) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(this.index).withChainAddress(oTSHashAddress.getChainAddress()).withHashAddress(oTSHashAddress.getHashAddress()).withKeyAndMask(oTSHashAddress.getKeyAndMask())).build();
                this.wotsPlus.importKeys(this.xmss.getWOTSPlusSecretKey(oTSHashAddress2), this.xmss.getPublicSeed());
                this.authenticationPath.set(0, this.xmss.lTree(this.wotsPlus.getPublicKey(oTSHashAddress2), (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(this.index).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(lTreeAddress.getKeyAndMask())).build()));
            } else {
                XMSSNode randomizeHash = this.xmss.randomizeHash(this.authenticationPath.get(calculateTau - 1), this.keep.get(Integer.valueOf(calculateTau - 1)), (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(calculateTau - 1).withTreeIndex(this.index >> calculateTau).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build());
                this.authenticationPath.set(calculateTau, new XMSSNode(randomizeHash.getHeight() + 1, randomizeHash.getValue()));
                this.keep.remove(Integer.valueOf(calculateTau - 1));
                for (int i = 0; i < calculateTau; i++) {
                    if (i < this.treeHeight - this.k) {
                        this.authenticationPath.set(i, this.treeHashInstances.get(i).tailNode.clone());
                    } else {
                        this.authenticationPath.set(i, this.retain.get(Integer.valueOf(i)).removeFirst());
                    }
                }
                int min = Math.min(calculateTau, this.treeHeight - this.k);
                for (int i2 = 0; i2 < min; i2++) {
                    int i3 = ((1 << i2) * 3) + this.index + 1;
                    if (i3 < (1 << this.treeHeight)) {
                        this.treeHashInstances.get(i2).initialize(i3);
                    }
                }
                oTSHashAddress2 = oTSHashAddress;
            }
            for (int i4 = 0; i4 < ((this.treeHeight - this.k) >> 1); i4++) {
                TreeHash treeHashInstanceForUpdate = getTreeHashInstanceForUpdate();
                if (treeHashInstanceForUpdate != null) {
                    treeHashInstanceForUpdate.update(oTSHashAddress2);
                }
            }
            this.index++;
        }
    }

    /* access modifiers changed from: protected */
    public void setXMSS(XMSS xmss2) {
        this.xmss = xmss2;
        this.wotsPlus = xmss2.getWOTSPlus();
    }

    /* access modifiers changed from: protected */
    public void validate() {
        if (this.treeHeight != this.xmss.getParams().getHeight()) {
            throw new IllegalStateException("wrong height");
        } else if (this.authenticationPath == null) {
            throw new IllegalStateException("authenticationPath == null");
        } else if (this.retain == null) {
            throw new IllegalStateException("retain == null");
        } else if (this.stack == null) {
            throw new IllegalStateException("stack == null");
        } else if (this.treeHashInstances == null) {
            throw new IllegalStateException("treeHashInstances == null");
        } else if (this.keep == null) {
            throw new IllegalStateException("keep == null");
        } else if (!XMSSUtil.isIndexValid(this.treeHeight, (long) this.index)) {
            throw new IllegalStateException("index in BDS state out of bounds");
        }
    }
}
