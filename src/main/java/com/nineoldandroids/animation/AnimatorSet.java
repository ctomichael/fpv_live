package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Animator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class AnimatorSet extends Animator {
    private ValueAnimator mDelayAnim = null;
    private long mDuration = -1;
    private boolean mNeedsSort = true;
    /* access modifiers changed from: private */
    public HashMap<Animator, Node> mNodeMap = new HashMap<>();
    /* access modifiers changed from: private */
    public ArrayList<Node> mNodes = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<Animator> mPlayingSet = new ArrayList<>();
    private AnimatorSetListener mSetListener = null;
    /* access modifiers changed from: private */
    public ArrayList<Node> mSortedNodes = new ArrayList<>();
    private long mStartDelay = 0;
    /* access modifiers changed from: private */
    public boolean mStarted = false;
    boolean mTerminated = false;

    public void playTogether(Animator... items) {
        if (items != null) {
            this.mNeedsSort = true;
            Builder builder = play(items[0]);
            for (int i = 1; i < items.length; i++) {
                builder.with(items[i]);
            }
        }
    }

    public void playTogether(Collection<Animator> items) {
        if (items != null && items.size() > 0) {
            this.mNeedsSort = true;
            Builder builder = null;
            for (Animator anim : items) {
                if (builder == null) {
                    builder = play(anim);
                } else {
                    builder.with(anim);
                }
            }
        }
    }

    public void playSequentially(Animator... items) {
        if (items != null) {
            this.mNeedsSort = true;
            if (items.length == 1) {
                play(items[0]);
                return;
            }
            for (int i = 0; i < items.length - 1; i++) {
                play(items[i]).before(items[i + 1]);
            }
        }
    }

    public void playSequentially(List<Animator> items) {
        if (items != null && items.size() > 0) {
            this.mNeedsSort = true;
            if (items.size() == 1) {
                play(items.get(0));
                return;
            }
            for (int i = 0; i < items.size() - 1; i++) {
                play(items.get(i)).before(items.get(i + 1));
            }
        }
    }

    public ArrayList<Animator> getChildAnimations() {
        ArrayList<Animator> childList = new ArrayList<>();
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            childList.add(i$.next().animation);
        }
        return childList;
    }

    public void setTarget(Object target) {
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            Animator animation = i$.next().animation;
            if (animation instanceof AnimatorSet) {
                ((AnimatorSet) animation).setTarget(target);
            } else if (animation instanceof ObjectAnimator) {
                ((ObjectAnimator) animation).setTarget(target);
            }
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            i$.next().animation.setInterpolator(interpolator);
        }
    }

    public Builder play(Animator anim) {
        if (anim == null) {
            return null;
        }
        this.mNeedsSort = true;
        return new Builder(anim);
    }

    public void cancel() {
        this.mTerminated = true;
        if (isStarted()) {
            ArrayList<Animator.AnimatorListener> tmpListeners = null;
            if (this.mListeners != null) {
                tmpListeners = (ArrayList) this.mListeners.clone();
                Iterator i$ = tmpListeners.iterator();
                while (i$.hasNext()) {
                    ((Animator.AnimatorListener) i$.next()).onAnimationCancel(this);
                }
            }
            if (this.mDelayAnim != null && this.mDelayAnim.isRunning()) {
                this.mDelayAnim.cancel();
            } else if (this.mSortedNodes.size() > 0) {
                Iterator i$2 = this.mSortedNodes.iterator();
                while (i$2.hasNext()) {
                    i$2.next().animation.cancel();
                }
            }
            if (tmpListeners != null) {
                Iterator i$3 = tmpListeners.iterator();
                while (i$3.hasNext()) {
                    ((Animator.AnimatorListener) i$3.next()).onAnimationEnd(this);
                }
            }
            this.mStarted = false;
        }
    }

    public void end() {
        this.mTerminated = true;
        if (isStarted()) {
            if (this.mSortedNodes.size() != this.mNodes.size()) {
                sortNodes();
                Iterator i$ = this.mSortedNodes.iterator();
                while (i$.hasNext()) {
                    Node node = i$.next();
                    if (this.mSetListener == null) {
                        this.mSetListener = new AnimatorSetListener(this);
                    }
                    node.animation.addListener(this.mSetListener);
                }
            }
            if (this.mDelayAnim != null) {
                this.mDelayAnim.cancel();
            }
            if (this.mSortedNodes.size() > 0) {
                Iterator i$2 = this.mSortedNodes.iterator();
                while (i$2.hasNext()) {
                    i$2.next().animation.end();
                }
            }
            if (this.mListeners != null) {
                Iterator i$3 = ((ArrayList) this.mListeners.clone()).iterator();
                while (i$3.hasNext()) {
                    ((Animator.AnimatorListener) i$3.next()).onAnimationEnd(this);
                }
            }
            this.mStarted = false;
        }
    }

    public boolean isRunning() {
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            if (i$.next().animation.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public void setStartDelay(long startDelay) {
        this.mStartDelay = startDelay;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public AnimatorSet setDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("duration must be a value of zero or greater");
        }
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            i$.next().animation.setDuration(duration);
        }
        this.mDuration = duration;
        return this;
    }

    public void setupStartValues() {
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            i$.next().animation.setupStartValues();
        }
    }

    public void setupEndValues() {
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            i$.next().animation.setupEndValues();
        }
    }

    public void start() {
        this.mTerminated = false;
        this.mStarted = true;
        sortNodes();
        int numSortedNodes = this.mSortedNodes.size();
        for (int i = 0; i < numSortedNodes; i++) {
            Node node = this.mSortedNodes.get(i);
            ArrayList<Animator.AnimatorListener> oldListeners = node.animation.getListeners();
            if (oldListeners != null && oldListeners.size() > 0) {
                Iterator i$ = new ArrayList<>(oldListeners).iterator();
                while (i$.hasNext()) {
                    Animator.AnimatorListener listener = (Animator.AnimatorListener) i$.next();
                    if ((listener instanceof DependencyListener) || (listener instanceof AnimatorSetListener)) {
                        node.animation.removeListener(listener);
                    }
                }
            }
        }
        final ArrayList<Node> nodesToStart = new ArrayList<>();
        for (int i2 = 0; i2 < numSortedNodes; i2++) {
            Node node2 = this.mSortedNodes.get(i2);
            if (this.mSetListener == null) {
                this.mSetListener = new AnimatorSetListener(this);
            }
            if (node2.dependencies == null || node2.dependencies.size() == 0) {
                nodesToStart.add(node2);
            } else {
                int numDependencies = node2.dependencies.size();
                for (int j = 0; j < numDependencies; j++) {
                    Dependency dependency = node2.dependencies.get(j);
                    dependency.node.animation.addListener(new DependencyListener(this, node2, dependency.rule));
                }
                node2.tmpDependencies = (ArrayList) node2.dependencies.clone();
            }
            node2.animation.addListener(this.mSetListener);
        }
        if (this.mStartDelay <= 0) {
            Iterator i$2 = nodesToStart.iterator();
            while (i$2.hasNext()) {
                Node node3 = (Node) i$2.next();
                node3.animation.start();
                this.mPlayingSet.add(node3.animation);
            }
        } else {
            this.mDelayAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mDelayAnim.setDuration(this.mStartDelay);
            this.mDelayAnim.addListener(new AnimatorListenerAdapter() {
                /* class com.nineoldandroids.animation.AnimatorSet.AnonymousClass1 */
                boolean canceled = false;

                public void onAnimationCancel(Animator anim) {
                    this.canceled = true;
                }

                public void onAnimationEnd(Animator anim) {
                    if (!this.canceled) {
                        int numNodes = nodesToStart.size();
                        for (int i = 0; i < numNodes; i++) {
                            Node node = (Node) nodesToStart.get(i);
                            node.animation.start();
                            AnimatorSet.this.mPlayingSet.add(node.animation);
                        }
                    }
                }
            });
            this.mDelayAnim.start();
        }
        if (this.mListeners != null) {
            ArrayList<Animator.AnimatorListener> tmpListeners = (ArrayList) this.mListeners.clone();
            int numListeners = tmpListeners.size();
            for (int i3 = 0; i3 < numListeners; i3++) {
                ((Animator.AnimatorListener) tmpListeners.get(i3)).onAnimationStart(this);
            }
        }
        if (this.mNodes.size() == 0 && this.mStartDelay == 0) {
            this.mStarted = false;
            if (this.mListeners != null) {
                ArrayList<Animator.AnimatorListener> tmpListeners2 = (ArrayList) this.mListeners.clone();
                int numListeners2 = tmpListeners2.size();
                for (int i4 = 0; i4 < numListeners2; i4++) {
                    ((Animator.AnimatorListener) tmpListeners2.get(i4)).onAnimationEnd(this);
                }
            }
        }
    }

    public AnimatorSet clone() {
        AnimatorSet anim = (AnimatorSet) super.clone();
        anim.mNeedsSort = true;
        anim.mTerminated = false;
        anim.mStarted = false;
        anim.mPlayingSet = new ArrayList<>();
        anim.mNodeMap = new HashMap<>();
        anim.mNodes = new ArrayList<>();
        anim.mSortedNodes = new ArrayList<>();
        HashMap<Node, Node> nodeCloneMap = new HashMap<>();
        Iterator<Node> it2 = this.mNodes.iterator();
        while (it2.hasNext()) {
            Node node = it2.next();
            Node nodeClone = node.clone();
            nodeCloneMap.put(node, nodeClone);
            anim.mNodes.add(nodeClone);
            anim.mNodeMap.put(nodeClone.animation, nodeClone);
            nodeClone.dependencies = null;
            nodeClone.tmpDependencies = null;
            nodeClone.nodeDependents = null;
            nodeClone.nodeDependencies = null;
            ArrayList<Animator.AnimatorListener> cloneListeners = nodeClone.animation.getListeners();
            if (cloneListeners != null) {
                ArrayList<Animator.AnimatorListener> listenersToRemove = null;
                Iterator i$ = cloneListeners.iterator();
                while (i$.hasNext()) {
                    Animator.AnimatorListener listener = i$.next();
                    if (listener instanceof AnimatorSetListener) {
                        if (listenersToRemove == null) {
                            listenersToRemove = new ArrayList<>();
                        }
                        listenersToRemove.add(listener);
                    }
                }
                if (listenersToRemove != null) {
                    Iterator i$2 = listenersToRemove.iterator();
                    while (i$2.hasNext()) {
                        cloneListeners.remove((Animator.AnimatorListener) i$2.next());
                    }
                }
            }
        }
        Iterator<Node> it3 = this.mNodes.iterator();
        while (it3.hasNext()) {
            Node node2 = it3.next();
            Node nodeClone2 = (Node) nodeCloneMap.get(node2);
            if (node2.dependencies != null) {
                Iterator i$3 = node2.dependencies.iterator();
                while (i$3.hasNext()) {
                    Dependency dependency = i$3.next();
                    nodeClone2.addDependency(new Dependency((Node) nodeCloneMap.get(dependency.node), dependency.rule));
                }
            }
        }
        return anim;
    }

    private static class DependencyListener implements Animator.AnimatorListener {
        private AnimatorSet mAnimatorSet;
        private Node mNode;
        private int mRule;

        public DependencyListener(AnimatorSet animatorSet, Node node, int rule) {
            this.mAnimatorSet = animatorSet;
            this.mNode = node;
            this.mRule = rule;
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            if (this.mRule == 1) {
                startIfReady(animation);
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
            if (this.mRule == 0) {
                startIfReady(animation);
            }
        }

        private void startIfReady(Animator dependencyAnimation) {
            if (!this.mAnimatorSet.mTerminated) {
                Dependency dependencyToRemove = null;
                int numDependencies = this.mNode.tmpDependencies.size();
                int i = 0;
                while (true) {
                    if (i >= numDependencies) {
                        break;
                    }
                    Dependency dependency = this.mNode.tmpDependencies.get(i);
                    if (dependency.rule == this.mRule && dependency.node.animation == dependencyAnimation) {
                        dependencyToRemove = dependency;
                        dependencyAnimation.removeListener(this);
                        break;
                    }
                    i++;
                }
                this.mNode.tmpDependencies.remove(dependencyToRemove);
                if (this.mNode.tmpDependencies.size() == 0) {
                    this.mNode.animation.start();
                    this.mAnimatorSet.mPlayingSet.add(this.mNode.animation);
                }
            }
        }
    }

    private class AnimatorSetListener implements Animator.AnimatorListener {
        private AnimatorSet mAnimatorSet;

        AnimatorSetListener(AnimatorSet animatorSet) {
            this.mAnimatorSet = animatorSet;
        }

        public void onAnimationCancel(Animator animation) {
            if (!AnimatorSet.this.mTerminated && AnimatorSet.this.mPlayingSet.size() == 0 && AnimatorSet.this.mListeners != null) {
                int numListeners = AnimatorSet.this.mListeners.size();
                for (int i = 0; i < numListeners; i++) {
                    ((Animator.AnimatorListener) AnimatorSet.this.mListeners.get(i)).onAnimationCancel(this.mAnimatorSet);
                }
            }
        }

        public void onAnimationEnd(Animator animation) {
            animation.removeListener(this);
            AnimatorSet.this.mPlayingSet.remove(animation);
            ((Node) this.mAnimatorSet.mNodeMap.get(animation)).done = true;
            if (!AnimatorSet.this.mTerminated) {
                ArrayList<Node> sortedNodes = this.mAnimatorSet.mSortedNodes;
                boolean allDone = true;
                int numSortedNodes = sortedNodes.size();
                int i = 0;
                while (true) {
                    if (i >= numSortedNodes) {
                        break;
                    } else if (!((Node) sortedNodes.get(i)).done) {
                        allDone = false;
                        break;
                    } else {
                        i++;
                    }
                }
                if (allDone) {
                    if (AnimatorSet.this.mListeners != null) {
                        ArrayList<Animator.AnimatorListener> tmpListeners = (ArrayList) AnimatorSet.this.mListeners.clone();
                        int numListeners = tmpListeners.size();
                        for (int i2 = 0; i2 < numListeners; i2++) {
                            ((Animator.AnimatorListener) tmpListeners.get(i2)).onAnimationEnd(this.mAnimatorSet);
                        }
                    }
                    boolean unused = this.mAnimatorSet.mStarted = false;
                }
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
        }
    }

    private void sortNodes() {
        if (this.mNeedsSort) {
            this.mSortedNodes.clear();
            ArrayList<Node> roots = new ArrayList<>();
            int numNodes = this.mNodes.size();
            for (int i = 0; i < numNodes; i++) {
                Node node = this.mNodes.get(i);
                if (node.dependencies == null || node.dependencies.size() == 0) {
                    roots.add(node);
                }
            }
            ArrayList<Node> tmpRoots = new ArrayList<>();
            while (roots.size() > 0) {
                int numRoots = roots.size();
                for (int i2 = 0; i2 < numRoots; i2++) {
                    Node root = (Node) roots.get(i2);
                    this.mSortedNodes.add(root);
                    if (root.nodeDependents != null) {
                        int numDependents = root.nodeDependents.size();
                        for (int j = 0; j < numDependents; j++) {
                            Node node2 = root.nodeDependents.get(j);
                            node2.nodeDependencies.remove(root);
                            if (node2.nodeDependencies.size() == 0) {
                                tmpRoots.add(node2);
                            }
                        }
                    }
                }
                roots.clear();
                roots.addAll(tmpRoots);
                tmpRoots.clear();
            }
            this.mNeedsSort = false;
            if (this.mSortedNodes.size() != this.mNodes.size()) {
                throw new IllegalStateException("Circular dependencies cannot exist in AnimatorSet");
            }
            return;
        }
        int numNodes2 = this.mNodes.size();
        for (int i3 = 0; i3 < numNodes2; i3++) {
            Node node3 = this.mNodes.get(i3);
            if (node3.dependencies != null && node3.dependencies.size() > 0) {
                int numDependencies = node3.dependencies.size();
                for (int j2 = 0; j2 < numDependencies; j2++) {
                    Dependency dependency = node3.dependencies.get(j2);
                    if (node3.nodeDependencies == null) {
                        node3.nodeDependencies = new ArrayList<>();
                    }
                    if (!node3.nodeDependencies.contains(dependency.node)) {
                        node3.nodeDependencies.add(dependency.node);
                    }
                }
            }
            node3.done = false;
        }
    }

    private static class Dependency {
        static final int AFTER = 1;
        static final int WITH = 0;
        public Node node;
        public int rule;

        public Dependency(Node node2, int rule2) {
            this.node = node2;
            this.rule = rule2;
        }
    }

    private static class Node implements Cloneable {
        public Animator animation;
        public ArrayList<Dependency> dependencies = null;
        public boolean done = false;
        public ArrayList<Node> nodeDependencies = null;
        public ArrayList<Node> nodeDependents = null;
        public ArrayList<Dependency> tmpDependencies = null;

        public Node(Animator animation2) {
            this.animation = animation2;
        }

        public void addDependency(Dependency dependency) {
            if (this.dependencies == null) {
                this.dependencies = new ArrayList<>();
                this.nodeDependencies = new ArrayList<>();
            }
            this.dependencies.add(dependency);
            if (!this.nodeDependencies.contains(dependency.node)) {
                this.nodeDependencies.add(dependency.node);
            }
            Node dependencyNode = dependency.node;
            if (dependencyNode.nodeDependents == null) {
                dependencyNode.nodeDependents = new ArrayList<>();
            }
            dependencyNode.nodeDependents.add(this);
        }

        public Node clone() {
            try {
                Node node = (Node) super.clone();
                node.animation = this.animation.clone();
                return node;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    public class Builder {
        private Node mCurrentNode;

        Builder(Animator anim) {
            this.mCurrentNode = (Node) AnimatorSet.this.mNodeMap.get(anim);
            if (this.mCurrentNode == null) {
                this.mCurrentNode = new Node(anim);
                AnimatorSet.this.mNodeMap.put(anim, this.mCurrentNode);
                AnimatorSet.this.mNodes.add(this.mCurrentNode);
            }
        }

        public Builder with(Animator anim) {
            Node node = (Node) AnimatorSet.this.mNodeMap.get(anim);
            if (node == null) {
                node = new Node(anim);
                AnimatorSet.this.mNodeMap.put(anim, node);
                AnimatorSet.this.mNodes.add(node);
            }
            node.addDependency(new Dependency(this.mCurrentNode, 0));
            return this;
        }

        public Builder before(Animator anim) {
            Node node = (Node) AnimatorSet.this.mNodeMap.get(anim);
            if (node == null) {
                node = new Node(anim);
                AnimatorSet.this.mNodeMap.put(anim, node);
                AnimatorSet.this.mNodes.add(node);
            }
            node.addDependency(new Dependency(this.mCurrentNode, 1));
            return this;
        }

        public Builder after(Animator anim) {
            Node node = (Node) AnimatorSet.this.mNodeMap.get(anim);
            if (node == null) {
                node = new Node(anim);
                AnimatorSet.this.mNodeMap.put(anim, node);
                AnimatorSet.this.mNodes.add(node);
            }
            this.mCurrentNode.addDependency(new Dependency(node, 1));
            return this;
        }

        public Builder after(long delay) {
            ValueAnimator anim = ValueAnimator.ofFloat(0.0f, 1.0f);
            anim.setDuration(delay);
            after(anim);
            return this;
        }
    }
}
