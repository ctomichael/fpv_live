package android.support.constraint.solver.widgets;

public class ConstraintHorizontalLayout extends ConstraintWidgetContainer {
    private ContentAlignment mAlignment = ContentAlignment.MIDDLE;

    public enum ContentAlignment {
        BEGIN,
        MIDDLE,
        END,
        TOP,
        VERTICAL_MIDDLE,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public ConstraintHorizontalLayout() {
    }

    public ConstraintHorizontalLayout(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public ConstraintHorizontalLayout(int width, int height) {
        super(width, height);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: android.support.constraint.solver.widgets.ConstraintHorizontalLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: android.support.constraint.solver.widgets.ConstraintHorizontalLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: android.support.constraint.solver.widgets.ConstraintWidget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: android.support.constraint.solver.widgets.ConstraintHorizontalLayout} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addToSolver(android.support.constraint.solver.LinearSystem r15) {
        /*
            r14 = this;
            r4 = 0
            java.util.ArrayList r1 = r14.mChildren
            int r1 = r1.size()
            if (r1 == 0) goto L_0x0066
            r2 = r14
            r12 = 0
            java.util.ArrayList r1 = r14.mChildren
            int r13 = r1.size()
        L_0x0011:
            if (r12 >= r13) goto L_0x004f
            java.util.ArrayList r1 = r14.mChildren
            java.lang.Object r0 = r1.get(r12)
            android.support.constraint.solver.widgets.ConstraintWidget r0 = (android.support.constraint.solver.widgets.ConstraintWidget) r0
            if (r2 == r14) goto L_0x003d
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r3 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT
            r0.connect(r1, r2, r3)
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r3 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT
            r2.connect(r1, r0, r3)
        L_0x002b:
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r3 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP
            r0.connect(r1, r14, r3)
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r3 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM
            r0.connect(r1, r14, r3)
            r2 = r0
            int r12 = r12 + 1
            goto L_0x0011
        L_0x003d:
            android.support.constraint.solver.widgets.ConstraintAnchor$Strength r5 = android.support.constraint.solver.widgets.ConstraintAnchor.Strength.STRONG
            android.support.constraint.solver.widgets.ConstraintHorizontalLayout$ContentAlignment r1 = r14.mAlignment
            android.support.constraint.solver.widgets.ConstraintHorizontalLayout$ContentAlignment r3 = android.support.constraint.solver.widgets.ConstraintHorizontalLayout.ContentAlignment.END
            if (r1 != r3) goto L_0x0047
            android.support.constraint.solver.widgets.ConstraintAnchor$Strength r5 = android.support.constraint.solver.widgets.ConstraintAnchor.Strength.WEAK
        L_0x0047:
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r3 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT
            r0.connect(r1, r2, r3, r4, r5)
            goto L_0x002b
        L_0x004f:
            if (r2 == r14) goto L_0x0066
            android.support.constraint.solver.widgets.ConstraintAnchor$Strength r5 = android.support.constraint.solver.widgets.ConstraintAnchor.Strength.STRONG
            android.support.constraint.solver.widgets.ConstraintHorizontalLayout$ContentAlignment r1 = r14.mAlignment
            android.support.constraint.solver.widgets.ConstraintHorizontalLayout$ContentAlignment r3 = android.support.constraint.solver.widgets.ConstraintHorizontalLayout.ContentAlignment.BEGIN
            if (r1 != r3) goto L_0x005b
            android.support.constraint.solver.widgets.ConstraintAnchor$Strength r5 = android.support.constraint.solver.widgets.ConstraintAnchor.Strength.WEAK
        L_0x005b:
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r7 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r9 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT
            r6 = r2
            r8 = r14
            r10 = r4
            r11 = r5
            r6.connect(r7, r8, r9, r10, r11)
        L_0x0066:
            super.addToSolver(r15)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintHorizontalLayout.addToSolver(android.support.constraint.solver.LinearSystem):void");
    }
}
