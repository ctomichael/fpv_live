package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.ArrayList;

class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem system, int orientation) {
        int offset;
        int chainsSize;
        ChainHead[] chainsArray;
        if (orientation == 0) {
            offset = 0;
            chainsSize = constraintWidgetContainer.mHorizontalChainsSize;
            chainsArray = constraintWidgetContainer.mHorizontalChainsArray;
        } else {
            offset = 2;
            chainsSize = constraintWidgetContainer.mVerticalChainsSize;
            chainsArray = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i = 0; i < chainsSize; i++) {
            ChainHead first = chainsArray[i];
            first.define();
            if (!constraintWidgetContainer.optimizeFor(4)) {
                applyChainConstraints(constraintWidgetContainer, system, orientation, offset, first);
            } else if (!Optimizer.applyChainOptimized(constraintWidgetContainer, system, orientation, offset, first)) {
                applyChainConstraints(constraintWidgetContainer, system, orientation, offset, first);
            }
        }
    }

    static void applyChainConstraints(ConstraintWidgetContainer container, LinearSystem system, int orientation, int offset, ChainHead chainHead) {
        boolean isChainSpreadInside;
        boolean isChainSpread;
        boolean isChainPacked;
        ConstraintWidget next;
        ConstraintAnchor beginNextAnchor;
        SolverVariable beginNextTarget;
        ConstraintAnchor beginNextAnchor2;
        SolverVariable beginNextTarget2;
        float bias;
        int count;
        ConstraintWidget next2;
        ConstraintWidget first = chainHead.mFirst;
        ConstraintWidget last = chainHead.mLast;
        ConstraintWidget firstVisibleWidget = chainHead.mFirstVisibleWidget;
        ConstraintWidget lastVisibleWidget = chainHead.mLastVisibleWidget;
        ConstraintWidget head = chainHead.mHead;
        ConstraintWidget widget = first;
        boolean done = false;
        float totalWeights = chainHead.mTotalWeight;
        ConstraintWidget constraintWidget = chainHead.mFirstMatchConstraintWidget;
        ConstraintWidget constraintWidget2 = chainHead.mLastMatchConstraintWidget;
        boolean isWrapContent = container.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (orientation == 0) {
            isChainSpread = head.mHorizontalChainStyle == 0;
            isChainSpreadInside = head.mHorizontalChainStyle == 1;
            isChainPacked = head.mHorizontalChainStyle == 2;
        } else {
            isChainSpread = head.mVerticalChainStyle == 0;
            isChainSpreadInside = head.mVerticalChainStyle == 1;
            isChainPacked = head.mVerticalChainStyle == 2;
        }
        while (!done) {
            ConstraintAnchor begin = widget.mListAnchors[offset];
            int strength = 4;
            if (isWrapContent || isChainPacked) {
                strength = 1;
            }
            int margin = begin.getMargin();
            if (!(begin.mTarget == null || widget == first)) {
                margin += begin.mTarget.getMargin();
            }
            if (isChainPacked && widget != first && widget != firstVisibleWidget) {
                strength = 6;
            } else if (isChainSpread && isWrapContent) {
                strength = 4;
            }
            if (begin.mTarget != null) {
                if (widget == firstVisibleWidget) {
                    system.addGreaterThan(begin.mSolverVariable, begin.mTarget.mSolverVariable, margin, 5);
                } else {
                    system.addGreaterThan(begin.mSolverVariable, begin.mTarget.mSolverVariable, margin, 6);
                }
                system.addEquality(begin.mSolverVariable, begin.mTarget.mSolverVariable, margin, strength);
            }
            if (isWrapContent) {
                if (widget.getVisibility() != 8 && widget.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    system.addGreaterThan(widget.mListAnchors[offset + 1].mSolverVariable, widget.mListAnchors[offset].mSolverVariable, 0, 5);
                }
                system.addGreaterThan(widget.mListAnchors[offset].mSolverVariable, container.mListAnchors[offset].mSolverVariable, 0, 6);
            }
            ConstraintAnchor nextAnchor = widget.mListAnchors[offset + 1].mTarget;
            if (nextAnchor != null) {
                next2 = nextAnchor.mOwner;
                if (next2.mListAnchors[offset].mTarget == null || next2.mListAnchors[offset].mTarget.mOwner != widget) {
                    next2 = null;
                }
            } else {
                next2 = null;
            }
            if (next2 != null) {
                widget = next2;
            } else {
                done = true;
            }
        }
        if (!(lastVisibleWidget == null || last.mListAnchors[offset + 1].mTarget == null)) {
            ConstraintAnchor end = lastVisibleWidget.mListAnchors[offset + 1];
            system.addLowerThan(end.mSolverVariable, last.mListAnchors[offset + 1].mTarget.mSolverVariable, -end.getMargin(), 5);
        }
        if (isWrapContent) {
            system.addGreaterThan(container.mListAnchors[offset + 1].mSolverVariable, last.mListAnchors[offset + 1].mSolverVariable, last.mListAnchors[offset + 1].getMargin(), 6);
        }
        ArrayList<ConstraintWidget> listMatchConstraints = chainHead.mWeightedMatchConstraintsWidgets;
        if (listMatchConstraints != null && (count = listMatchConstraints.size()) > 1) {
            ConstraintWidget lastMatch = null;
            float lastWeight = 0.0f;
            if (chainHead.mHasUndefinedWeights && !chainHead.mHasComplexMatchWeights) {
                totalWeights = (float) chainHead.mWidgetsMatchCount;
            }
            for (int i = 0; i < count; i++) {
                ConstraintWidget match = listMatchConstraints.get(i);
                float currentWeight = match.mWeight[orientation];
                if (currentWeight < 0.0f) {
                    if (chainHead.mHasComplexMatchWeights) {
                        system.addEquality(match.mListAnchors[offset + 1].mSolverVariable, match.mListAnchors[offset].mSolverVariable, 0, 4);
                    } else {
                        currentWeight = 1.0f;
                    }
                }
                if (currentWeight == 0.0f) {
                    system.addEquality(match.mListAnchors[offset + 1].mSolverVariable, match.mListAnchors[offset].mSolverVariable, 0, 6);
                } else {
                    if (lastMatch != null) {
                        SolverVariable begin2 = lastMatch.mListAnchors[offset].mSolverVariable;
                        SolverVariable end2 = lastMatch.mListAnchors[offset + 1].mSolverVariable;
                        SolverVariable nextBegin = match.mListAnchors[offset].mSolverVariable;
                        SolverVariable nextEnd = match.mListAnchors[offset + 1].mSolverVariable;
                        ArrayRow row = system.createRow();
                        row.createRowEqualMatchDimensions(lastWeight, totalWeights, currentWeight, begin2, end2, nextBegin, nextEnd);
                        system.addConstraint(row);
                    }
                    lastMatch = match;
                    lastWeight = currentWeight;
                }
            }
        }
        if (firstVisibleWidget != null && (firstVisibleWidget == lastVisibleWidget || isChainPacked)) {
            ConstraintAnchor begin3 = first.mListAnchors[offset];
            ConstraintAnchor end3 = last.mListAnchors[offset + 1];
            SolverVariable beginTarget = first.mListAnchors[offset].mTarget != null ? first.mListAnchors[offset].mTarget.mSolverVariable : null;
            SolverVariable endTarget = last.mListAnchors[offset + 1].mTarget != null ? last.mListAnchors[offset + 1].mTarget.mSolverVariable : null;
            if (firstVisibleWidget == lastVisibleWidget) {
                begin3 = firstVisibleWidget.mListAnchors[offset];
                end3 = firstVisibleWidget.mListAnchors[offset + 1];
            }
            if (!(beginTarget == null || endTarget == null)) {
                if (orientation == 0) {
                    bias = head.mHorizontalBiasPercent;
                } else {
                    bias = head.mVerticalBiasPercent;
                }
                system.addCentering(begin3.mSolverVariable, beginTarget, begin3.getMargin(), bias, endTarget, end3.mSolverVariable, end3.getMargin(), 5);
            }
        } else if (isChainSpread && firstVisibleWidget != null) {
            ConstraintWidget widget2 = firstVisibleWidget;
            ConstraintWidget previousVisibleWidget = firstVisibleWidget;
            boolean applyFixedEquality = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            while (widget2 != null) {
                ConstraintWidget next3 = widget2.mNextChainWidget[orientation];
                while (next3 != null && next3.getVisibility() == 8) {
                    next3 = next3.mNextChainWidget[orientation];
                }
                if (next3 != null || widget2 == lastVisibleWidget) {
                    ConstraintAnchor beginAnchor = widget2.mListAnchors[offset];
                    SolverVariable begin4 = beginAnchor.mSolverVariable;
                    SolverVariable beginTarget2 = beginAnchor.mTarget != null ? beginAnchor.mTarget.mSolverVariable : null;
                    if (previousVisibleWidget != widget2) {
                        beginTarget2 = previousVisibleWidget.mListAnchors[offset + 1].mSolverVariable;
                    } else if (widget2 == firstVisibleWidget && previousVisibleWidget == widget2) {
                        beginTarget2 = first.mListAnchors[offset].mTarget != null ? first.mListAnchors[offset].mTarget.mSolverVariable : null;
                    }
                    SolverVariable beginNext = null;
                    int beginMargin = beginAnchor.getMargin();
                    int nextMargin = widget2.mListAnchors[offset + 1].getMargin();
                    if (next3 != null) {
                        beginNextAnchor2 = next3.mListAnchors[offset];
                        beginNext = beginNextAnchor2.mSolverVariable;
                        beginNextTarget2 = widget2.mListAnchors[offset + 1].mSolverVariable;
                    } else {
                        beginNextAnchor2 = last.mListAnchors[offset + 1].mTarget;
                        if (beginNextAnchor2 != null) {
                            beginNext = beginNextAnchor2.mSolverVariable;
                        }
                        beginNextTarget2 = widget2.mListAnchors[offset + 1].mSolverVariable;
                    }
                    if (beginNextAnchor2 != null) {
                        nextMargin += beginNextAnchor2.getMargin();
                    }
                    if (previousVisibleWidget != null) {
                        beginMargin += previousVisibleWidget.mListAnchors[offset + 1].getMargin();
                    }
                    if (!(begin4 == null || beginTarget2 == null || beginNext == null || beginNextTarget2 == null)) {
                        int margin1 = beginMargin;
                        if (widget2 == firstVisibleWidget) {
                            margin1 = firstVisibleWidget.mListAnchors[offset].getMargin();
                        }
                        int margin2 = nextMargin;
                        if (widget2 == lastVisibleWidget) {
                            margin2 = lastVisibleWidget.mListAnchors[offset + 1].getMargin();
                        }
                        int strength2 = 4;
                        if (applyFixedEquality) {
                            strength2 = 6;
                        }
                        system.addCentering(begin4, beginTarget2, margin1, 0.5f, beginNext, beginNextTarget2, margin2, strength2);
                    }
                }
                if (widget2.getVisibility() != 8) {
                    previousVisibleWidget = widget2;
                }
                widget2 = next3;
            }
        } else if (isChainSpreadInside && firstVisibleWidget != null) {
            ConstraintWidget previousVisibleWidget2 = firstVisibleWidget;
            boolean applyFixedEquality2 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            for (ConstraintWidget widget3 = firstVisibleWidget; widget3 != null; widget3 = next) {
                next = widget3.mNextChainWidget[orientation];
                while (next != null && next.getVisibility() == 8) {
                    next = next.mNextChainWidget[orientation];
                }
                if (!(widget3 == firstVisibleWidget || widget3 == lastVisibleWidget || next == null)) {
                    if (next == lastVisibleWidget) {
                        next = null;
                    }
                    ConstraintAnchor beginAnchor2 = widget3.mListAnchors[offset];
                    SolverVariable begin5 = beginAnchor2.mSolverVariable;
                    if (beginAnchor2.mTarget != null) {
                        SolverVariable solverVariable = beginAnchor2.mTarget.mSolverVariable;
                    }
                    SolverVariable beginTarget3 = previousVisibleWidget2.mListAnchors[offset + 1].mSolverVariable;
                    SolverVariable beginNext2 = null;
                    int beginMargin2 = beginAnchor2.getMargin();
                    int nextMargin2 = widget3.mListAnchors[offset + 1].getMargin();
                    if (next != null) {
                        beginNextAnchor = next.mListAnchors[offset];
                        beginNext2 = beginNextAnchor.mSolverVariable;
                        beginNextTarget = beginNextAnchor.mTarget != null ? beginNextAnchor.mTarget.mSolverVariable : null;
                    } else {
                        beginNextAnchor = widget3.mListAnchors[offset + 1].mTarget;
                        if (beginNextAnchor != null) {
                            beginNext2 = beginNextAnchor.mSolverVariable;
                        }
                        beginNextTarget = widget3.mListAnchors[offset + 1].mSolverVariable;
                    }
                    if (beginNextAnchor != null) {
                        nextMargin2 += beginNextAnchor.getMargin();
                    }
                    if (previousVisibleWidget2 != null) {
                        beginMargin2 += previousVisibleWidget2.mListAnchors[offset + 1].getMargin();
                    }
                    int strength3 = 4;
                    if (applyFixedEquality2) {
                        strength3 = 6;
                    }
                    if (!(begin5 == null || beginTarget3 == null || beginNext2 == null || beginNextTarget == null)) {
                        system.addCentering(begin5, beginTarget3, beginMargin2, 0.5f, beginNext2, beginNextTarget, nextMargin2, strength3);
                    }
                }
                if (widget3.getVisibility() != 8) {
                    previousVisibleWidget2 = widget3;
                }
            }
            ConstraintAnchor begin6 = firstVisibleWidget.mListAnchors[offset];
            ConstraintAnchor beginTarget4 = first.mListAnchors[offset].mTarget;
            ConstraintAnchor end4 = lastVisibleWidget.mListAnchors[offset + 1];
            ConstraintAnchor endTarget2 = last.mListAnchors[offset + 1].mTarget;
            if (beginTarget4 != null) {
                if (firstVisibleWidget != lastVisibleWidget) {
                    system.addEquality(begin6.mSolverVariable, beginTarget4.mSolverVariable, begin6.getMargin(), 5);
                } else if (endTarget2 != null) {
                    system.addCentering(begin6.mSolverVariable, beginTarget4.mSolverVariable, begin6.getMargin(), 0.5f, end4.mSolverVariable, endTarget2.mSolverVariable, end4.getMargin(), 5);
                }
            }
            if (!(endTarget2 == null || firstVisibleWidget == lastVisibleWidget)) {
                system.addEquality(end4.mSolverVariable, endTarget2.mSolverVariable, -end4.getMargin(), 5);
            }
        }
        if ((isChainSpread || isChainSpreadInside) && firstVisibleWidget != null) {
            ConstraintAnchor begin7 = firstVisibleWidget.mListAnchors[offset];
            ConstraintAnchor end5 = lastVisibleWidget.mListAnchors[offset + 1];
            SolverVariable beginTarget5 = begin7.mTarget != null ? begin7.mTarget.mSolverVariable : null;
            SolverVariable endTarget3 = end5.mTarget != null ? end5.mTarget.mSolverVariable : null;
            if (last != lastVisibleWidget) {
                ConstraintAnchor realEnd = last.mListAnchors[offset + 1];
                endTarget3 = realEnd.mTarget != null ? realEnd.mTarget.mSolverVariable : null;
            }
            if (firstVisibleWidget == lastVisibleWidget) {
                begin7 = firstVisibleWidget.mListAnchors[offset];
                end5 = firstVisibleWidget.mListAnchors[offset + 1];
            }
            if (beginTarget5 != null && endTarget3 != null) {
                int beginMargin3 = begin7.getMargin();
                if (lastVisibleWidget == null) {
                    lastVisibleWidget = last;
                }
                system.addCentering(begin7.mSolverVariable, beginTarget5, beginMargin3, 0.5f, endTarget3, end5.mSolverVariable, lastVisibleWidget.mListAnchors[offset + 1].getMargin(), 5);
            }
        }
    }
}
