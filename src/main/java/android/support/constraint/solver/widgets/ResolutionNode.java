package android.support.constraint.solver.widgets;

import java.util.HashSet;
import java.util.Iterator;

public class ResolutionNode {
    public static final int REMOVED = 2;
    public static final int RESOLVED = 1;
    public static final int UNRESOLVED = 0;
    HashSet<ResolutionNode> dependents = new HashSet<>(2);
    int state = 0;

    public void addDependent(ResolutionNode node) {
        this.dependents.add(node);
    }

    public void reset() {
        this.state = 0;
        this.dependents.clear();
    }

    public void invalidate() {
        this.state = 0;
        Iterator<ResolutionNode> it2 = this.dependents.iterator();
        while (it2.hasNext()) {
            it2.next().invalidate();
        }
    }

    public void invalidateAnchors() {
        if (this instanceof ResolutionAnchor) {
            this.state = 0;
        }
        Iterator<ResolutionNode> it2 = this.dependents.iterator();
        while (it2.hasNext()) {
            it2.next().invalidateAnchors();
        }
    }

    public void didResolve() {
        this.state = 1;
        Iterator<ResolutionNode> it2 = this.dependents.iterator();
        while (it2.hasNext()) {
            it2.next().resolve();
        }
    }

    public boolean isResolved() {
        return this.state == 1;
    }

    public void resolve() {
    }

    public void remove(ResolutionDimension resolutionDimension) {
    }
}
