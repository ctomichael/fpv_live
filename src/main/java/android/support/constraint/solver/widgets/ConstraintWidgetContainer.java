package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstraintWidgetContainer extends WidgetContainer {
    private static final boolean DEBUG = false;
    static final boolean DEBUG_GRAPH = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final int MAX_ITERATIONS = 8;
    private static final boolean USE_SNAPSHOT = true;
    int mDebugSolverPassCount = 0;
    public boolean mGroupsWrapOptimized = false;
    private boolean mHeightMeasuredTooSmall = false;
    ChainHead[] mHorizontalChainsArray = new ChainHead[4];
    int mHorizontalChainsSize = 0;
    public boolean mHorizontalWrapOptimized = false;
    private boolean mIsRtl = false;
    private int mOptimizationLevel = 7;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    public boolean mSkipSolver = false;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem = new LinearSystem();
    ChainHead[] mVerticalChainsArray = new ChainHead[4];
    int mVerticalChainsSize = 0;
    public boolean mVerticalWrapOptimized = false;
    public List<ConstraintWidgetGroup> mWidgetGroups = new ArrayList();
    private boolean mWidthMeasuredTooSmall = false;
    public int mWrapFixedHeight = 0;
    public int mWrapFixedWidth = 0;

    public void fillMetrics(Metrics metrics) {
        this.mSystem.fillMetrics(metrics);
    }

    public ConstraintWidgetContainer() {
    }

    public ConstraintWidgetContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public ConstraintWidgetContainer(int width, int height) {
        super(width, height);
    }

    public void setOptimizationLevel(int value) {
        this.mOptimizationLevel = value;
    }

    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }

    public boolean optimizeFor(int feature) {
        return (this.mOptimizationLevel & feature) == feature;
    }

    public String getType() {
        return "ConstraintLayout";
    }

    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mWidgetGroups.clear();
        this.mSkipSolver = false;
        super.reset();
    }

    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }

    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }

    public boolean addChildrenToSolver(LinearSystem system) {
        addToSolver(system);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof ConstraintWidgetContainer) {
                ConstraintWidget.DimensionBehaviour horizontalBehaviour = widget.mListDimensionBehaviors[0];
                ConstraintWidget.DimensionBehaviour verticalBehaviour = widget.mListDimensionBehaviors[1];
                if (horizontalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                }
                if (verticalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                }
                widget.addToSolver(system);
                if (horizontalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    widget.setHorizontalDimensionBehaviour(horizontalBehaviour);
                }
                if (verticalBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    widget.setVerticalDimensionBehaviour(verticalBehaviour);
                }
            } else {
                Optimizer.checkMatchParent(this, system, widget);
                widget.addToSolver(system);
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, system, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, system, 1);
        }
        return true;
    }

    public void updateChildrenFromSolver(LinearSystem system, boolean[] flags) {
        flags[2] = false;
        updateFromSolver(system);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            widget.updateFromSolver(system);
            if (widget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widget.getWidth() < widget.getWrapWidth()) {
                flags[2] = true;
            }
            if (widget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widget.getHeight() < widget.getWrapHeight()) {
                flags[2] = true;
            }
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
    }

    public void setRtl(boolean isRtl) {
        this.mIsRtl = isRtl;
    }

    public boolean isRtl() {
        return this.mIsRtl;
    }

    public void analyze(int optimizationLevel) {
        super.analyze(optimizationLevel);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).analyze(optimizationLevel);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x011d, code lost:
        r24 = getVerticalDimensionBehaviour();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layout() {
        /*
            r28 = this;
            r0 = r28
            int r0 = r0.mX
            r19 = r0
            r0 = r28
            int r0 = r0.mY
            r20 = r0
            r24 = 0
            int r25 = r28.getWidth()
            int r18 = java.lang.Math.max(r24, r25)
            r24 = 0
            int r25 = r28.getHeight()
            int r17 = java.lang.Math.max(r24, r25)
            r24 = 0
            r0 = r24
            r1 = r28
            r1.mWidthMeasuredTooSmall = r0
            r24 = 0
            r0 = r24
            r1 = r28
            r1.mHeightMeasuredTooSmall = r0
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget r0 = r0.mParent
            r24 = r0
            if (r24 == 0) goto L_0x014e
            r0 = r28
            android.support.constraint.solver.widgets.Snapshot r0 = r0.mSnapshot
            r24 = r0
            if (r24 != 0) goto L_0x004f
            android.support.constraint.solver.widgets.Snapshot r24 = new android.support.constraint.solver.widgets.Snapshot
            r0 = r24
            r1 = r28
            r0.<init>(r1)
            r0 = r24
            r1 = r28
            r1.mSnapshot = r0
        L_0x004f:
            r0 = r28
            android.support.constraint.solver.widgets.Snapshot r0 = r0.mSnapshot
            r24 = r0
            r0 = r24
            r1 = r28
            r0.updateFrom(r1)
            r0 = r28
            int r0 = r0.mPaddingLeft
            r24 = r0
            r0 = r28
            r1 = r24
            r0.setX(r1)
            r0 = r28
            int r0 = r0.mPaddingTop
            r24 = r0
            r0 = r28
            r1 = r24
            r0.setY(r1)
            r28.resetAnchors()
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem
            r24 = r0
            android.support.constraint.solver.Cache r24 = r24.getCache()
            r0 = r28
            r1 = r24
            r0.resetSolverVariables(r1)
        L_0x008a:
            r0 = r28
            int r0 = r0.mOptimizationLevel
            r24 = r0
            if (r24 == 0) goto L_0x0160
            r24 = 8
            r0 = r28
            r1 = r24
            boolean r24 = r0.optimizeFor(r1)
            if (r24 != 0) goto L_0x00a1
            r28.optimizeReset()
        L_0x00a1:
            r24 = 32
            r0 = r28
            r1 = r24
            boolean r24 = r0.optimizeFor(r1)
            if (r24 != 0) goto L_0x00b0
            r28.optimize()
        L_0x00b0:
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem
            r24 = r0
            r25 = 1
            r0 = r25
            r1 = r24
            r1.graphOptimizer = r0
        L_0x00be:
            r23 = 0
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 1
            r16 = r24[r25]
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 0
            r15 = r24[r25]
            r28.resetChains()
            r0 = r28
            java.util.List<android.support.constraint.solver.widgets.ConstraintWidgetGroup> r0 = r0.mWidgetGroups
            r24 = r0
            int r24 = r24.size()
            if (r24 != 0) goto L_0x0102
            r0 = r28
            java.util.List<android.support.constraint.solver.widgets.ConstraintWidgetGroup> r0 = r0.mWidgetGroups
            r24 = r0
            r24.clear()
            r0 = r28
            java.util.List<android.support.constraint.solver.widgets.ConstraintWidgetGroup> r0 = r0.mWidgetGroups
            r24 = r0
            r25 = 0
            android.support.constraint.solver.widgets.ConstraintWidgetGroup r26 = new android.support.constraint.solver.widgets.ConstraintWidgetGroup
            r0 = r28
            java.util.ArrayList r0 = r0.mChildren
            r27 = r0
            r26.<init>(r27)
            r24.add(r25, r26)
        L_0x0102:
            r5 = 0
            r0 = r28
            java.util.List<android.support.constraint.solver.widgets.ConstraintWidgetGroup> r0 = r0.mWidgetGroups
            r24 = r0
            int r8 = r24.size()
            r0 = r28
            java.util.ArrayList r3 = r0.mChildren
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r24 = r28.getHorizontalDimensionBehaviour()
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r25 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r0 = r24
            r1 = r25
            if (r0 == r1) goto L_0x0129
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r24 = r28.getVerticalDimensionBehaviour()
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r25 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x0170
        L_0x0129:
            r9 = 1
        L_0x012a:
            r7 = 0
        L_0x012b:
            if (r7 >= r8) goto L_0x048c
            r0 = r28
            boolean r0 = r0.mSkipSolver
            r24 = r0
            if (r24 != 0) goto L_0x048c
            r0 = r28
            java.util.List<android.support.constraint.solver.widgets.ConstraintWidgetGroup> r0 = r0.mWidgetGroups
            r24 = r0
            r0 = r24
            java.lang.Object r24 = r0.get(r7)
            android.support.constraint.solver.widgets.ConstraintWidgetGroup r24 = (android.support.constraint.solver.widgets.ConstraintWidgetGroup) r24
            r0 = r24
            boolean r0 = r0.mSkipSolver
            r24 = r0
            if (r24 == 0) goto L_0x0172
        L_0x014b:
            int r7 = r7 + 1
            goto L_0x012b
        L_0x014e:
            r24 = 0
            r0 = r24
            r1 = r28
            r1.mX = r0
            r24 = 0
            r0 = r24
            r1 = r28
            r1.mY = r0
            goto L_0x008a
        L_0x0160:
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem
            r24 = r0
            r25 = 0
            r0 = r25
            r1 = r24
            r1.graphOptimizer = r0
            goto L_0x00be
        L_0x0170:
            r9 = 0
            goto L_0x012a
        L_0x0172:
            r24 = 32
            r0 = r28
            r1 = r24
            boolean r24 = r0.optimizeFor(r1)
            if (r24 == 0) goto L_0x01b0
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r24 = r28.getHorizontalDimensionBehaviour()
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r25 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x01df
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r24 = r28.getVerticalDimensionBehaviour()
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r25 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x01df
            r0 = r28
            java.util.List<android.support.constraint.solver.widgets.ConstraintWidgetGroup> r0 = r0.mWidgetGroups
            r24 = r0
            r0 = r24
            java.lang.Object r24 = r0.get(r7)
            android.support.constraint.solver.widgets.ConstraintWidgetGroup r24 = (android.support.constraint.solver.widgets.ConstraintWidgetGroup) r24
            java.util.List r24 = r24.getWidgetsToSolve()
            java.util.ArrayList r24 = (java.util.ArrayList) r24
            r0 = r24
            r1 = r28
            r1.mChildren = r0
        L_0x01b0:
            r28.resetChains()
            r0 = r28
            java.util.ArrayList r0 = r0.mChildren
            r24 = r0
            int r4 = r24.size()
            r5 = 0
            r11 = 0
        L_0x01bf:
            if (r11 >= r4) goto L_0x01fc
            r0 = r28
            java.util.ArrayList r0 = r0.mChildren
            r24 = r0
            r0 = r24
            java.lang.Object r21 = r0.get(r11)
            android.support.constraint.solver.widgets.ConstraintWidget r21 = (android.support.constraint.solver.widgets.ConstraintWidget) r21
            r0 = r21
            boolean r0 = r0 instanceof android.support.constraint.solver.widgets.WidgetContainer
            r24 = r0
            if (r24 == 0) goto L_0x01dc
            android.support.constraint.solver.widgets.WidgetContainer r21 = (android.support.constraint.solver.widgets.WidgetContainer) r21
            r21.layout()
        L_0x01dc:
            int r11 = r11 + 1
            goto L_0x01bf
        L_0x01df:
            r0 = r28
            java.util.List<android.support.constraint.solver.widgets.ConstraintWidgetGroup> r0 = r0.mWidgetGroups
            r24 = r0
            r0 = r24
            java.lang.Object r24 = r0.get(r7)
            android.support.constraint.solver.widgets.ConstraintWidgetGroup r24 = (android.support.constraint.solver.widgets.ConstraintWidgetGroup) r24
            r0 = r24
            java.util.List<android.support.constraint.solver.widgets.ConstraintWidget> r0 = r0.mConstrainedGroup
            r24 = r0
            java.util.ArrayList r24 = (java.util.ArrayList) r24
            r0 = r24
            r1 = r28
            r1.mChildren = r0
            goto L_0x01b0
        L_0x01fc:
            r14 = 1
        L_0x01fd:
            if (r14 == 0) goto L_0x0479
            int r5 = r5 + 1
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem     // Catch:{ Exception -> 0x02b2 }
            r24 = r0
            r24.reset()     // Catch:{ Exception -> 0x02b2 }
            r28.resetChains()     // Catch:{ Exception -> 0x02b2 }
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem     // Catch:{ Exception -> 0x02b2 }
            r24 = r0
            r0 = r28
            r1 = r24
            r0.createObjectVariables(r1)     // Catch:{ Exception -> 0x02b2 }
            r11 = 0
        L_0x021b:
            if (r11 >= r4) goto L_0x023b
            r0 = r28
            java.util.ArrayList r0 = r0.mChildren     // Catch:{ Exception -> 0x02b2 }
            r24 = r0
            r0 = r24
            java.lang.Object r21 = r0.get(r11)     // Catch:{ Exception -> 0x02b2 }
            android.support.constraint.solver.widgets.ConstraintWidget r21 = (android.support.constraint.solver.widgets.ConstraintWidget) r21     // Catch:{ Exception -> 0x02b2 }
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem     // Catch:{ Exception -> 0x02b2 }
            r24 = r0
            r0 = r21
            r1 = r24
            r0.createObjectVariables(r1)     // Catch:{ Exception -> 0x02b2 }
            int r11 = r11 + 1
            goto L_0x021b
        L_0x023b:
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem     // Catch:{ Exception -> 0x02b2 }
            r24 = r0
            r0 = r28
            r1 = r24
            boolean r14 = r0.addChildrenToSolver(r1)     // Catch:{ Exception -> 0x02b2 }
            if (r14 == 0) goto L_0x0254
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem     // Catch:{ Exception -> 0x02b2 }
            r24 = r0
            r24.minimize()     // Catch:{ Exception -> 0x02b2 }
        L_0x0254:
            if (r14 == 0) goto L_0x02d2
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem
            r24 = r0
            boolean[] r25 = android.support.constraint.solver.widgets.Optimizer.flags
            r0 = r28
            r1 = r24
            r2 = r25
            r0.updateChildrenFromSolver(r1, r2)
        L_0x0267:
            r14 = 0
            if (r9 == 0) goto L_0x03a5
            r24 = 8
            r0 = r24
            if (r5 >= r0) goto L_0x03a5
            boolean[] r24 = android.support.constraint.solver.widgets.Optimizer.flags
            r25 = 2
            boolean r24 = r24[r25]
            if (r24 == 0) goto L_0x03a5
            r12 = 0
            r13 = 0
            r11 = 0
        L_0x027b:
            if (r11 >= r4) goto L_0x0347
            r0 = r28
            java.util.ArrayList r0 = r0.mChildren
            r24 = r0
            r0 = r24
            java.lang.Object r21 = r0.get(r11)
            android.support.constraint.solver.widgets.ConstraintWidget r21 = (android.support.constraint.solver.widgets.ConstraintWidget) r21
            r0 = r21
            int r0 = r0.mX
            r24 = r0
            int r25 = r21.getWidth()
            int r24 = r24 + r25
            r0 = r24
            int r12 = java.lang.Math.max(r12, r0)
            r0 = r21
            int r0 = r0.mY
            r24 = r0
            int r25 = r21.getHeight()
            int r24 = r24 + r25
            r0 = r24
            int r13 = java.lang.Math.max(r13, r0)
            int r11 = r11 + 1
            goto L_0x027b
        L_0x02b2:
            r6 = move-exception
            r6.printStackTrace()
            java.io.PrintStream r24 = java.lang.System.out
            java.lang.StringBuilder r25 = new java.lang.StringBuilder
            r25.<init>()
            java.lang.String r26 = "EXCEPTION : "
            java.lang.StringBuilder r25 = r25.append(r26)
            r0 = r25
            java.lang.StringBuilder r25 = r0.append(r6)
            java.lang.String r25 = r25.toString()
            r24.println(r25)
            goto L_0x0254
        L_0x02d2:
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem
            r24 = r0
            r0 = r28
            r1 = r24
            r0.updateFromSolver(r1)
            r11 = 0
        L_0x02e0:
            if (r11 >= r4) goto L_0x0267
            r0 = r28
            java.util.ArrayList r0 = r0.mChildren
            r24 = r0
            r0 = r24
            java.lang.Object r21 = r0.get(r11)
            android.support.constraint.solver.widgets.ConstraintWidget r21 = (android.support.constraint.solver.widgets.ConstraintWidget) r21
            r0 = r21
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 0
            r24 = r24[r25]
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r25 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x031a
            int r24 = r21.getWidth()
            int r25 = r21.getWrapWidth()
            r0 = r24
            r1 = r25
            if (r0 >= r1) goto L_0x031a
            boolean[] r24 = android.support.constraint.solver.widgets.Optimizer.flags
            r25 = 2
            r26 = 1
            r24[r25] = r26
            goto L_0x0267
        L_0x031a:
            r0 = r21
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 1
            r24 = r24[r25]
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r25 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x0344
            int r24 = r21.getHeight()
            int r25 = r21.getWrapHeight()
            r0 = r24
            r1 = r25
            if (r0 >= r1) goto L_0x0344
            boolean[] r24 = android.support.constraint.solver.widgets.Optimizer.flags
            r25 = 2
            r26 = 1
            r24[r25] = r26
            goto L_0x0267
        L_0x0344:
            int r11 = r11 + 1
            goto L_0x02e0
        L_0x0347:
            r0 = r28
            int r0 = r0.mMinWidth
            r24 = r0
            r0 = r24
            int r12 = java.lang.Math.max(r0, r12)
            r0 = r28
            int r0 = r0.mMinHeight
            r24 = r0
            r0 = r24
            int r13 = java.lang.Math.max(r0, r13)
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r24 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r0 = r24
            if (r15 != r0) goto L_0x0381
            int r24 = r28.getWidth()
            r0 = r24
            if (r0 >= r12) goto L_0x0381
            r0 = r28
            r0.setWidth(r12)
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 0
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r26 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r24[r25] = r26
            r23 = 1
            r14 = 1
        L_0x0381:
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r24 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r0 = r16
            r1 = r24
            if (r0 != r1) goto L_0x03a5
            int r24 = r28.getHeight()
            r0 = r24
            if (r0 >= r13) goto L_0x03a5
            r0 = r28
            r0.setHeight(r13)
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 1
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r26 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r24[r25] = r26
            r23 = 1
            r14 = 1
        L_0x03a5:
            r0 = r28
            int r0 = r0.mMinWidth
            r24 = r0
            int r25 = r28.getWidth()
            int r22 = java.lang.Math.max(r24, r25)
            int r24 = r28.getWidth()
            r0 = r22
            r1 = r24
            if (r0 <= r1) goto L_0x03d3
            r0 = r28
            r1 = r22
            r0.setWidth(r1)
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 0
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r26 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r24[r25] = r26
            r23 = 1
            r14 = 1
        L_0x03d3:
            r0 = r28
            int r0 = r0.mMinHeight
            r24 = r0
            int r25 = r28.getHeight()
            int r10 = java.lang.Math.max(r24, r25)
            int r24 = r28.getHeight()
            r0 = r24
            if (r10 <= r0) goto L_0x03fd
            r0 = r28
            r0.setHeight(r10)
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 1
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r26 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r24[r25] = r26
            r23 = 1
            r14 = 1
        L_0x03fd:
            if (r23 != 0) goto L_0x01fd
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 0
            r24 = r24[r25]
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r25 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x043b
            if (r18 <= 0) goto L_0x043b
            int r24 = r28.getWidth()
            r0 = r24
            r1 = r18
            if (r0 <= r1) goto L_0x043b
            r24 = 1
            r0 = r24
            r1 = r28
            r1.mWidthMeasuredTooSmall = r0
            r23 = 1
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 0
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r26 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r24[r25] = r26
            r0 = r28
            r1 = r18
            r0.setWidth(r1)
            r14 = 1
        L_0x043b:
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 1
            r24 = r24[r25]
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r25 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x01fd
            if (r17 <= 0) goto L_0x01fd
            int r24 = r28.getHeight()
            r0 = r24
            r1 = r17
            if (r0 <= r1) goto L_0x01fd
            r24 = 1
            r0 = r24
            r1 = r28
            r1.mHeightMeasuredTooSmall = r0
            r23 = 1
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 1
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r26 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r24[r25] = r26
            r0 = r28
            r1 = r17
            r0.setHeight(r1)
            r14 = 1
            goto L_0x01fd
        L_0x0479:
            r0 = r28
            java.util.List<android.support.constraint.solver.widgets.ConstraintWidgetGroup> r0 = r0.mWidgetGroups
            r24 = r0
            r0 = r24
            java.lang.Object r24 = r0.get(r7)
            android.support.constraint.solver.widgets.ConstraintWidgetGroup r24 = (android.support.constraint.solver.widgets.ConstraintWidgetGroup) r24
            r24.updateUnresolvedWidgets()
            goto L_0x014b
        L_0x048c:
            java.util.ArrayList r3 = (java.util.ArrayList) r3
            r0 = r28
            r0.mChildren = r3
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget r0 = r0.mParent
            r24 = r0
            if (r24 == 0) goto L_0x0526
            r0 = r28
            int r0 = r0.mMinWidth
            r24 = r0
            int r25 = r28.getWidth()
            int r22 = java.lang.Math.max(r24, r25)
            r0 = r28
            int r0 = r0.mMinHeight
            r24 = r0
            int r25 = r28.getHeight()
            int r10 = java.lang.Math.max(r24, r25)
            r0 = r28
            android.support.constraint.solver.widgets.Snapshot r0 = r0.mSnapshot
            r24 = r0
            r0 = r24
            r1 = r28
            r0.applyTo(r1)
            r0 = r28
            int r0 = r0.mPaddingLeft
            r24 = r0
            int r24 = r24 + r22
            r0 = r28
            int r0 = r0.mPaddingRight
            r25 = r0
            int r24 = r24 + r25
            r0 = r28
            r1 = r24
            r0.setWidth(r1)
            r0 = r28
            int r0 = r0.mPaddingTop
            r24 = r0
            int r24 = r24 + r10
            r0 = r28
            int r0 = r0.mPaddingBottom
            r25 = r0
            int r24 = r24 + r25
            r0 = r28
            r1 = r24
            r0.setHeight(r1)
        L_0x04f1:
            if (r23 == 0) goto L_0x0507
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 0
            r24[r25] = r15
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r24 = r0
            r25 = 1
            r24[r25] = r16
        L_0x0507:
            r0 = r28
            android.support.constraint.solver.LinearSystem r0 = r0.mSystem
            r24 = r0
            android.support.constraint.solver.Cache r24 = r24.getCache()
            r0 = r28
            r1 = r24
            r0.resetSolverVariables(r1)
            android.support.constraint.solver.widgets.ConstraintWidgetContainer r24 = r28.getRootConstraintContainer()
            r0 = r28
            r1 = r24
            if (r0 != r1) goto L_0x0525
            r28.updateDrawPosition()
        L_0x0525:
            return
        L_0x0526:
            r0 = r19
            r1 = r28
            r1.mX = r0
            r0 = r20
            r1 = r28
            r1.mY = r0
            goto L_0x04f1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidgetContainer.layout():void");
    }

    public void preOptimize() {
        optimizeReset();
        analyze(this.mOptimizationLevel);
    }

    public void solveGraph() {
        ResolutionAnchor leftNode = getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
        ResolutionAnchor topNode = getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
        leftNode.resolve(null, 0.0f);
        topNode.resolve(null, 0.0f);
    }

    public void resetGraph() {
        ResolutionAnchor leftNode = getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
        ResolutionAnchor topNode = getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
        leftNode.invalidateAnchors();
        topNode.invalidateAnchors();
        leftNode.resolve(null, 0.0f);
        topNode.resolve(null, 0.0f);
    }

    public void optimizeForDimensions(int width, int height) {
        if (!(this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || this.mResolutionWidth == null)) {
            this.mResolutionWidth.resolve(width);
        }
        if (this.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionHeight != null) {
            this.mResolutionHeight.resolve(height);
        }
    }

    public void optimizeReset() {
        int count = this.mChildren.size();
        resetResolutionNodes();
        for (int i = 0; i < count; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).resetResolutionNodes();
        }
    }

    public void optimize() {
        if (!optimizeFor(8)) {
            analyze(this.mOptimizationLevel);
        }
        solveGraph();
    }

    public boolean handlesInternalConstraints() {
        return false;
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList<Guideline> guidelines = new ArrayList<>();
        int mChildrenSize = this.mChildren.size();
        for (int i = 0; i < mChildrenSize; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof Guideline) {
                Guideline guideline = (Guideline) widget;
                if (guideline.getOrientation() == 1) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList<Guideline> guidelines = new ArrayList<>();
        int mChildrenSize = this.mChildren.size();
        for (int i = 0; i < mChildrenSize; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof Guideline) {
                Guideline guideline = (Guideline) widget;
                if (guideline.getOrientation() == 0) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    /* access modifiers changed from: package-private */
    public void addChain(ConstraintWidget constraintWidget, int type) {
        ConstraintWidget widget = constraintWidget;
        if (type == 0) {
            addHorizontalChain(widget);
        } else if (type == 1) {
            addVerticalChain(widget);
        }
    }

    private void addHorizontalChain(ConstraintWidget widget) {
        if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length) {
            this.mHorizontalChainsArray = (ChainHead[]) Arrays.copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(widget, 0, isRtl());
        this.mHorizontalChainsSize++;
    }

    private void addVerticalChain(ConstraintWidget widget) {
        if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length) {
            this.mVerticalChainsArray = (ChainHead[]) Arrays.copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(widget, 1, isRtl());
        this.mVerticalChainsSize++;
    }

    public List<ConstraintWidgetGroup> getWidgetGroups() {
        return this.mWidgetGroups;
    }
}
