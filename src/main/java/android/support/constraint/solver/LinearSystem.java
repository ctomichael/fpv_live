package android.support.constraint.solver;

import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import dji.component.accountcenter.IMemberProtocol;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
    private static final boolean DEBUG = false;
    public static final boolean FULL_DEBUG = false;
    private static int POOL_SIZE = 1000;
    public static Metrics sMetrics;
    private int TABLE_SIZE;
    public boolean graphOptimizer;
    private boolean[] mAlreadyTestedCandidates;
    final Cache mCache;
    private Row mGoal;
    private int mMaxColumns;
    private int mMaxRows;
    int mNumColumns;
    int mNumRows;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    ArrayRow[] mRows;
    private final Row mTempGoal;
    private HashMap<String, SolverVariable> mVariables;
    int mVariablesID;
    private ArrayRow[] tempClientsCopy;

    interface Row {
        void addError(SolverVariable solverVariable);

        void clear();

        SolverVariable getKey();

        SolverVariable getPivotCandidate(LinearSystem linearSystem, boolean[] zArr);

        void initFromRow(Row row);

        boolean isEmpty();
    }

    public LinearSystem() {
        this.mVariablesID = 0;
        this.mVariables = null;
        this.TABLE_SIZE = 32;
        this.mMaxColumns = this.TABLE_SIZE;
        this.mRows = null;
        this.graphOptimizer = false;
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = this.TABLE_SIZE;
        this.mPoolVariables = new SolverVariable[POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.tempClientsCopy = new ArrayRow[this.TABLE_SIZE];
        this.mRows = new ArrayRow[this.TABLE_SIZE];
        releaseRows();
        this.mCache = new Cache();
        this.mGoal = new GoalRow(this.mCache);
        this.mTempGoal = new ArrayRow(this.mCache);
    }

    public void fillMetrics(Metrics metrics) {
        sMetrics = metrics;
    }

    public static Metrics getMetrics() {
        return sMetrics;
    }

    private void increaseTableSize() {
        this.TABLE_SIZE *= 2;
        this.mRows = (ArrayRow[]) Arrays.copyOf(this.mRows, this.TABLE_SIZE);
        this.mCache.mIndexedVariables = (SolverVariable[]) Arrays.copyOf(this.mCache.mIndexedVariables, this.TABLE_SIZE);
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mMaxColumns = this.TABLE_SIZE;
        this.mMaxRows = this.TABLE_SIZE;
        if (sMetrics != null) {
            sMetrics.tableSizeIncrease++;
            sMetrics.maxTableSize = Math.max(sMetrics.maxTableSize, (long) this.TABLE_SIZE);
            sMetrics.lastTableSize = sMetrics.maxTableSize;
        }
    }

    private void releaseRows() {
        for (int i = 0; i < this.mRows.length; i++) {
            ArrayRow row = this.mRows[i];
            if (row != null) {
                this.mCache.arrayRowPool.release(row);
            }
            this.mRows[i] = null;
        }
    }

    public void reset() {
        for (int i = 0; i < this.mCache.mIndexedVariables.length; i++) {
            SolverVariable variable = this.mCache.mIndexedVariables[i];
            if (variable != null) {
                variable.reset();
            }
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, (Object) null);
        if (this.mVariables != null) {
            this.mVariables.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.clear();
        this.mNumColumns = 1;
        for (int i2 = 0; i2 < this.mNumRows; i2++) {
            this.mRows[i2].used = false;
        }
        releaseRows();
        this.mNumRows = 0;
    }

    public SolverVariable createObjectVariable(Object anchor) {
        if (anchor == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        if (!(anchor instanceof ConstraintAnchor)) {
            return null;
        }
        SolverVariable variable = ((ConstraintAnchor) anchor).getSolverVariable();
        if (variable == null) {
            ((ConstraintAnchor) anchor).resetSolverVariable(this.mCache);
            variable = ((ConstraintAnchor) anchor).getSolverVariable();
        }
        if (variable.id != -1 && variable.id <= this.mVariablesID && this.mCache.mIndexedVariables[variable.id] != null) {
            return variable;
        }
        if (variable.id != -1) {
            variable.reset();
        }
        this.mVariablesID++;
        this.mNumColumns++;
        variable.id = this.mVariablesID;
        variable.mType = SolverVariable.Type.UNRESTRICTED;
        this.mCache.mIndexedVariables[this.mVariablesID] = variable;
        return variable;
    }

    public ArrayRow createRow() {
        ArrayRow row = this.mCache.arrayRowPool.acquire();
        if (row == null) {
            row = new ArrayRow(this.mCache);
        } else {
            row.reset();
        }
        SolverVariable.increaseErrorId();
        return row;
    }

    public SolverVariable createSlackVariable() {
        if (sMetrics != null) {
            sMetrics.slackvariables++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable variable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
        this.mVariablesID++;
        this.mNumColumns++;
        variable.id = this.mVariablesID;
        this.mCache.mIndexedVariables[this.mVariablesID] = variable;
        return variable;
    }

    public SolverVariable createExtraVariable() {
        if (sMetrics != null) {
            sMetrics.extravariables++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable variable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
        this.mVariablesID++;
        this.mNumColumns++;
        variable.id = this.mVariablesID;
        this.mCache.mIndexedVariables[this.mVariablesID] = variable;
        return variable;
    }

    private void addError(ArrayRow row) {
        row.addError(this, 0);
    }

    private void addSingleError(ArrayRow row, int sign) {
        addSingleError(row, sign, 0);
    }

    /* access modifiers changed from: package-private */
    public void addSingleError(ArrayRow row, int sign, int strength) {
        row.addSingleError(createErrorVariable(strength, null), sign);
    }

    private SolverVariable createVariable(String name, SolverVariable.Type type) {
        if (sMetrics != null) {
            sMetrics.variables++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable variable = acquireSolverVariable(type, null);
        variable.setName(name);
        this.mVariablesID++;
        this.mNumColumns++;
        variable.id = this.mVariablesID;
        if (this.mVariables == null) {
            this.mVariables = new HashMap<>();
        }
        this.mVariables.put(name, variable);
        this.mCache.mIndexedVariables[this.mVariablesID] = variable;
        return variable;
    }

    public SolverVariable createErrorVariable(int strength, String prefix) {
        if (sMetrics != null) {
            sMetrics.errors++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable variable = acquireSolverVariable(SolverVariable.Type.ERROR, prefix);
        this.mVariablesID++;
        this.mNumColumns++;
        variable.id = this.mVariablesID;
        variable.strength = strength;
        this.mCache.mIndexedVariables[this.mVariablesID] = variable;
        this.mGoal.addError(variable);
        return variable;
    }

    private SolverVariable acquireSolverVariable(SolverVariable.Type type, String prefix) {
        SolverVariable variable = this.mCache.solverVariablePool.acquire();
        if (variable == null) {
            variable = new SolverVariable(type, prefix);
            variable.setType(type, prefix);
        } else {
            variable.reset();
            variable.setType(type, prefix);
        }
        if (this.mPoolVariablesCount >= POOL_SIZE) {
            POOL_SIZE *= 2;
            this.mPoolVariables = (SolverVariable[]) Arrays.copyOf(this.mPoolVariables, POOL_SIZE);
        }
        SolverVariable[] solverVariableArr = this.mPoolVariables;
        int i = this.mPoolVariablesCount;
        this.mPoolVariablesCount = i + 1;
        solverVariableArr[i] = variable;
        return variable;
    }

    /* access modifiers changed from: package-private */
    public Row getGoal() {
        return this.mGoal;
    }

    /* access modifiers changed from: package-private */
    public ArrayRow getRow(int n) {
        return this.mRows[n];
    }

    /* access modifiers changed from: package-private */
    public float getValueFor(String name) {
        SolverVariable v = getVariable(name, SolverVariable.Type.UNRESTRICTED);
        if (v == null) {
            return 0.0f;
        }
        return v.computedValue;
    }

    public int getObjectVariableValue(Object anchor) {
        SolverVariable variable = ((ConstraintAnchor) anchor).getSolverVariable();
        if (variable != null) {
            return (int) (variable.computedValue + 0.5f);
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public SolverVariable getVariable(String name, SolverVariable.Type type) {
        if (this.mVariables == null) {
            this.mVariables = new HashMap<>();
        }
        SolverVariable variable = this.mVariables.get(name);
        if (variable == null) {
            return createVariable(name, type);
        }
        return variable;
    }

    public void minimize() throws Exception {
        if (sMetrics != null) {
            sMetrics.minimize++;
        }
        if (this.graphOptimizer) {
            if (sMetrics != null) {
                sMetrics.graphOptimizer++;
            }
            boolean fullySolved = true;
            int i = 0;
            while (true) {
                if (i >= this.mNumRows) {
                    break;
                } else if (!this.mRows[i].isSimpleDefinition) {
                    fullySolved = false;
                    break;
                } else {
                    i++;
                }
            }
            if (!fullySolved) {
                minimizeGoal(this.mGoal);
                return;
            }
            if (sMetrics != null) {
                sMetrics.fullySolved++;
            }
            computeValues();
            return;
        }
        minimizeGoal(this.mGoal);
    }

    /* access modifiers changed from: package-private */
    public void minimizeGoal(Row goal) throws Exception {
        if (sMetrics != null) {
            sMetrics.minimizeGoal++;
            sMetrics.maxVariables = Math.max(sMetrics.maxVariables, (long) this.mNumColumns);
            sMetrics.maxRows = Math.max(sMetrics.maxRows, (long) this.mNumRows);
        }
        updateRowFromVariables((ArrayRow) goal);
        enforceBFS(goal);
        optimize(goal, false);
        computeValues();
    }

    private final void updateRowFromVariables(ArrayRow row) {
        if (this.mNumRows > 0) {
            row.variables.updateFromSystem(row, this.mRows);
            if (row.variables.currentSize == 0) {
                row.isSimpleDefinition = true;
            }
        }
    }

    public void addConstraint(ArrayRow row) {
        SolverVariable pivotCandidate;
        if (row != null) {
            if (sMetrics != null) {
                sMetrics.constraints++;
                if (row.isSimpleDefinition) {
                    sMetrics.simpleconstraints++;
                }
            }
            if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
                increaseTableSize();
            }
            boolean added = false;
            if (!row.isSimpleDefinition) {
                updateRowFromVariables(row);
                if (!row.isEmpty()) {
                    row.ensurePositiveConstant();
                    if (row.chooseSubject(this)) {
                        SolverVariable extra = createExtraVariable();
                        row.variable = extra;
                        addRow(row);
                        added = true;
                        this.mTempGoal.initFromRow(row);
                        optimize(this.mTempGoal, true);
                        if (extra.definitionId == -1) {
                            if (row.variable == extra && (pivotCandidate = row.pickPivot(extra)) != null) {
                                if (sMetrics != null) {
                                    sMetrics.pivots++;
                                }
                                row.pivot(pivotCandidate);
                            }
                            if (!row.isSimpleDefinition) {
                                row.variable.updateReferencesWithNewDefinition(row);
                            }
                            this.mNumRows--;
                        }
                    }
                    if (!row.hasKeyVariable()) {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (!added) {
                addRow(row);
            }
        }
    }

    private final void addRow(ArrayRow row) {
        if (this.mRows[this.mNumRows] != null) {
            this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
        }
        this.mRows[this.mNumRows] = row;
        row.variable.definitionId = this.mNumRows;
        this.mNumRows++;
        row.variable.updateReferencesWithNewDefinition(row);
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 156 */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00f8, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00fb, code lost:
        r4 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int optimize(android.support.constraint.solver.LinearSystem.Row r21, boolean r22) {
        /*
            r20 = this;
            android.support.constraint.solver.Metrics r15 = android.support.constraint.solver.LinearSystem.sMetrics
            if (r15 == 0) goto L_0x0012
            android.support.constraint.solver.Metrics r15 = android.support.constraint.solver.LinearSystem.sMetrics
            long r0 = r15.optimize
            r16 = r0
            r18 = 1
            long r16 = r16 + r18
            r0 = r16
            r15.optimize = r0
        L_0x0012:
            r4 = 0
            r10 = 0
            r5 = 0
        L_0x0015:
            r0 = r20
            int r15 = r0.mNumColumns
            if (r5 >= r15) goto L_0x0055
            r0 = r20
            boolean[] r15 = r0.mAlreadyTestedCandidates
            r16 = 0
            r15[r5] = r16
            int r5 = r5 + 1
            goto L_0x0015
        L_0x0026:
            r15 = -1
            if (r9 <= r15) goto L_0x00f8
            r0 = r20
            android.support.constraint.solver.ArrayRow[] r15 = r0.mRows
            r8 = r15[r9]
            android.support.constraint.solver.SolverVariable r15 = r8.variable
            r16 = -1
            r0 = r16
            r15.definitionId = r0
            android.support.constraint.solver.Metrics r15 = android.support.constraint.solver.LinearSystem.sMetrics
            if (r15 == 0) goto L_0x0049
            android.support.constraint.solver.Metrics r15 = android.support.constraint.solver.LinearSystem.sMetrics
            long r0 = r15.pivots
            r16 = r0
            r18 = 1
            long r16 = r16 + r18
            r0 = r16
            r15.pivots = r0
        L_0x0049:
            r8.pivot(r7)
            android.support.constraint.solver.SolverVariable r15 = r8.variable
            r15.definitionId = r9
            android.support.constraint.solver.SolverVariable r15 = r8.variable
            r15.updateReferencesWithNewDefinition(r8)
        L_0x0055:
            if (r4 != 0) goto L_0x00fe
            android.support.constraint.solver.Metrics r15 = android.support.constraint.solver.LinearSystem.sMetrics
            if (r15 == 0) goto L_0x0069
            android.support.constraint.solver.Metrics r15 = android.support.constraint.solver.LinearSystem.sMetrics
            long r0 = r15.iterations
            r16 = r0
            r18 = 1
            long r16 = r16 + r18
            r0 = r16
            r15.iterations = r0
        L_0x0069:
            int r10 = r10 + 1
            r0 = r20
            int r15 = r0.mNumColumns
            int r15 = r15 * 2
            if (r10 < r15) goto L_0x0076
            r11 = r10
            r12 = r10
        L_0x0075:
            return r12
        L_0x0076:
            android.support.constraint.solver.SolverVariable r15 = r21.getKey()
            if (r15 == 0) goto L_0x008e
            r0 = r20
            boolean[] r15 = r0.mAlreadyTestedCandidates
            android.support.constraint.solver.SolverVariable r16 = r21.getKey()
            r0 = r16
            int r0 = r0.id
            r16 = r0
            r17 = 1
            r15[r16] = r17
        L_0x008e:
            r0 = r20
            boolean[] r15 = r0.mAlreadyTestedCandidates
            r0 = r21
            r1 = r20
            android.support.constraint.solver.SolverVariable r7 = r0.getPivotCandidate(r1, r15)
            if (r7 == 0) goto L_0x00b7
            r0 = r20
            boolean[] r15 = r0.mAlreadyTestedCandidates
            int r0 = r7.id
            r16 = r0
            boolean r15 = r15[r16]
            if (r15 == 0) goto L_0x00ab
            r11 = r10
            r12 = r10
            goto L_0x0075
        L_0x00ab:
            r0 = r20
            boolean[] r15 = r0.mAlreadyTestedCandidates
            int r0 = r7.id
            r16 = r0
            r17 = 1
            r15[r16] = r17
        L_0x00b7:
            if (r7 == 0) goto L_0x00fb
            r6 = 2139095039(0x7f7fffff, float:3.4028235E38)
            r9 = -1
            r5 = 0
        L_0x00be:
            r0 = r20
            int r15 = r0.mNumRows
            if (r5 >= r15) goto L_0x0026
            r0 = r20
            android.support.constraint.solver.ArrayRow[] r15 = r0.mRows
            r3 = r15[r5]
            android.support.constraint.solver.SolverVariable r14 = r3.variable
            android.support.constraint.solver.SolverVariable$Type r15 = r14.mType
            android.support.constraint.solver.SolverVariable$Type r16 = android.support.constraint.solver.SolverVariable.Type.UNRESTRICTED
            r0 = r16
            if (r15 != r0) goto L_0x00d7
        L_0x00d4:
            int r5 = r5 + 1
            goto L_0x00be
        L_0x00d7:
            boolean r15 = r3.isSimpleDefinition
            if (r15 != 0) goto L_0x00d4
            boolean r15 = r3.hasVariable(r7)
            if (r15 == 0) goto L_0x00d4
            android.support.constraint.solver.ArrayLinkedVariables r15 = r3.variables
            float r2 = r15.get(r7)
            r15 = 0
            int r15 = (r2 > r15 ? 1 : (r2 == r15 ? 0 : -1))
            if (r15 >= 0) goto L_0x00d4
            float r15 = r3.constantValue
            float r15 = -r15
            float r13 = r15 / r2
            int r15 = (r13 > r6 ? 1 : (r13 == r6 ? 0 : -1))
            if (r15 >= 0) goto L_0x00d4
            r6 = r13
            r9 = r5
            goto L_0x00d4
        L_0x00f8:
            r4 = 1
            goto L_0x0055
        L_0x00fb:
            r4 = 1
            goto L_0x0055
        L_0x00fe:
            r11 = r10
            r12 = r10
            goto L_0x0075
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.LinearSystem.optimize(android.support.constraint.solver.LinearSystem$Row, boolean):int");
    }

    private int enforceBFS(Row goal) throws Exception {
        int tries = 0;
        boolean infeasibleSystem = false;
        int i = 0;
        while (true) {
            if (i >= this.mNumRows) {
                break;
            } else if (this.mRows[i].variable.mType != SolverVariable.Type.UNRESTRICTED && this.mRows[i].constantValue < 0.0f) {
                infeasibleSystem = true;
                break;
            } else {
                i++;
            }
        }
        if (infeasibleSystem) {
            boolean done = false;
            tries = 0;
            while (!done) {
                if (sMetrics != null) {
                    sMetrics.bfs++;
                }
                tries++;
                float min = Float.MAX_VALUE;
                int strength = 0;
                int pivotRowIndex = -1;
                int pivotColumnIndex = -1;
                for (int i2 = 0; i2 < this.mNumRows; i2++) {
                    ArrayRow current = this.mRows[i2];
                    if (current.variable.mType != SolverVariable.Type.UNRESTRICTED && !current.isSimpleDefinition && current.constantValue < 0.0f) {
                        for (int j = 1; j < this.mNumColumns; j++) {
                            SolverVariable candidate = this.mCache.mIndexedVariables[j];
                            float a_j = current.variables.get(candidate);
                            if (a_j > 0.0f) {
                                for (int k = 0; k < 7; k++) {
                                    float value = candidate.strengthVector[k] / a_j;
                                    if ((value < min && k == strength) || k > strength) {
                                        min = value;
                                        pivotRowIndex = i2;
                                        pivotColumnIndex = j;
                                        strength = k;
                                    }
                                }
                            }
                        }
                    }
                }
                if (pivotRowIndex != -1) {
                    ArrayRow pivotEquation = this.mRows[pivotRowIndex];
                    pivotEquation.variable.definitionId = -1;
                    if (sMetrics != null) {
                        sMetrics.pivots++;
                    }
                    pivotEquation.pivot(this.mCache.mIndexedVariables[pivotColumnIndex]);
                    pivotEquation.variable.definitionId = pivotRowIndex;
                    pivotEquation.variable.updateReferencesWithNewDefinition(pivotEquation);
                } else {
                    done = true;
                }
                if (tries > this.mNumColumns / 2) {
                    done = true;
                }
            }
        }
        return tries;
    }

    private void computeValues() {
        for (int i = 0; i < this.mNumRows; i++) {
            ArrayRow row = this.mRows[i];
            row.variable.computedValue = row.constantValue;
        }
    }

    private void displayRows() {
        displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; i++) {
            s = (s + this.mRows[i]) + "\n";
        }
        System.out.println(s + this.mGoal + "\n");
    }

    /* access modifiers changed from: package-private */
    public void displayReadableRows() {
        displaySolverVariables();
        String s = " #  ";
        for (int i = 0; i < this.mNumRows; i++) {
            s = (s + this.mRows[i].toReadableString()) + "\n #  ";
        }
        if (this.mGoal != null) {
            s = s + this.mGoal + "\n";
        }
        System.out.println(s);
    }

    public void displayVariablesReadableRows() {
        displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; i++) {
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                s = (s + this.mRows[i].toReadableString()) + "\n";
            }
        }
        System.out.println(s + this.mGoal + "\n");
    }

    public int getMemoryUsed() {
        int actualRowSize = 0;
        for (int i = 0; i < this.mNumRows; i++) {
            if (this.mRows[i] != null) {
                actualRowSize += this.mRows[i].sizeInBytes();
            }
        }
        return actualRowSize;
    }

    public int getNumEquations() {
        return this.mNumRows;
    }

    public int getNumVariables() {
        return this.mVariablesID;
    }

    /* access modifiers changed from: package-private */
    public void displaySystemInformations() {
        int rowSize = 0;
        for (int i = 0; i < this.TABLE_SIZE; i++) {
            if (this.mRows[i] != null) {
                rowSize += this.mRows[i].sizeInBytes();
            }
        }
        int actualRowSize = 0;
        for (int i2 = 0; i2 < this.mNumRows; i2++) {
            if (this.mRows[i2] != null) {
                actualRowSize += this.mRows[i2].sizeInBytes();
            }
        }
        System.out.println("Linear System -> Table size: " + this.TABLE_SIZE + " (" + getDisplaySize(this.TABLE_SIZE * this.TABLE_SIZE) + ") -- row sizes: " + getDisplaySize(rowSize) + ", actual size: " + getDisplaySize(actualRowSize) + " rows: " + this.mNumRows + IMemberProtocol.PARAM_SEPERATOR + this.mMaxRows + " cols: " + this.mNumColumns + IMemberProtocol.PARAM_SEPERATOR + this.mMaxColumns + " " + 0 + " occupied cells, " + getDisplaySize(0));
    }

    private void displaySolverVariables() {
        System.out.println("Display Rows (" + this.mNumRows + "x" + this.mNumColumns + ")\n");
    }

    private String getDisplaySize(int n) {
        int mb = ((n * 4) / 1024) / 1024;
        if (mb > 0) {
            return "" + mb + " Mb";
        }
        int kb = (n * 4) / 1024;
        if (kb > 0) {
            return "" + kb + " Kb";
        }
        return "" + (n * 4) + " bytes";
    }

    public Cache getCache() {
        return this.mCache;
    }

    private String getDisplayStrength(int strength) {
        if (strength == 1) {
            return "LOW";
        }
        if (strength == 2) {
            return "MEDIUM";
        }
        if (strength == 3) {
            return "HIGH";
        }
        if (strength == 4) {
            return "HIGHEST";
        }
        if (strength == 5) {
            return "EQUALITY";
        }
        if (strength == 6) {
            return "FIXED";
        }
        return "NONE";
    }

    public void addGreaterThan(SolverVariable a, SolverVariable b, int margin, int strength) {
        ArrayRow row = createRow();
        SolverVariable slack = createSlackVariable();
        slack.strength = 0;
        row.createRowGreaterThan(a, b, slack, margin);
        if (strength != 6) {
            addSingleError(row, (int) (-1.0f * row.variables.get(slack)), strength);
        }
        addConstraint(row);
    }

    public void addGreaterThan(SolverVariable a, int b) {
        ArrayRow row = createRow();
        SolverVariable slack = createSlackVariable();
        slack.strength = 0;
        row.createRowGreaterThan(a, b, slack);
        addConstraint(row);
    }

    public void addGreaterBarrier(SolverVariable a, SolverVariable b, boolean hasMatchConstraintWidgets) {
        ArrayRow row = createRow();
        SolverVariable slack = createSlackVariable();
        slack.strength = 0;
        row.createRowGreaterThan(a, b, slack, 0);
        if (hasMatchConstraintWidgets) {
            addSingleError(row, (int) (-1.0f * row.variables.get(slack)), 1);
        }
        addConstraint(row);
    }

    public void addLowerThan(SolverVariable a, SolverVariable b, int margin, int strength) {
        ArrayRow row = createRow();
        SolverVariable slack = createSlackVariable();
        slack.strength = 0;
        row.createRowLowerThan(a, b, slack, margin);
        if (strength != 6) {
            addSingleError(row, (int) (-1.0f * row.variables.get(slack)), strength);
        }
        addConstraint(row);
    }

    public void addLowerBarrier(SolverVariable a, SolverVariable b, boolean hasMatchConstraintWidgets) {
        ArrayRow row = createRow();
        SolverVariable slack = createSlackVariable();
        slack.strength = 0;
        row.createRowLowerThan(a, b, slack, 0);
        if (hasMatchConstraintWidgets) {
            addSingleError(row, (int) (-1.0f * row.variables.get(slack)), 1);
        }
        addConstraint(row);
    }

    public void addCentering(SolverVariable a, SolverVariable b, int m1, float bias, SolverVariable c, SolverVariable d, int m2, int strength) {
        ArrayRow row = createRow();
        row.createRowCentering(a, b, m1, bias, c, d, m2);
        if (strength != 6) {
            row.addError(this, strength);
        }
        addConstraint(row);
    }

    public void addRatio(SolverVariable a, SolverVariable b, SolverVariable c, SolverVariable d, float ratio, int strength) {
        ArrayRow row = createRow();
        row.createRowDimensionRatio(a, b, c, d, ratio);
        if (strength != 6) {
            row.addError(this, strength);
        }
        addConstraint(row);
    }

    public ArrayRow addEquality(SolverVariable a, SolverVariable b, int margin, int strength) {
        ArrayRow row = createRow();
        row.createRowEquals(a, b, margin);
        if (strength != 6) {
            row.addError(this, strength);
        }
        addConstraint(row);
        return row;
    }

    public void addEquality(SolverVariable a, int value) {
        int idx = a.definitionId;
        if (a.definitionId != -1) {
            ArrayRow row = this.mRows[idx];
            if (row.isSimpleDefinition) {
                row.constantValue = (float) value;
            } else if (row.variables.currentSize == 0) {
                row.isSimpleDefinition = true;
                row.constantValue = (float) value;
            } else {
                ArrayRow newRow = createRow();
                newRow.createRowEquals(a, value);
                addConstraint(newRow);
            }
        } else {
            ArrayRow row2 = createRow();
            row2.createRowDefinition(a, value);
            addConstraint(row2);
        }
    }

    public void addEquality(SolverVariable a, int value, int strength) {
        int idx = a.definitionId;
        if (a.definitionId != -1) {
            ArrayRow row = this.mRows[idx];
            if (row.isSimpleDefinition) {
                row.constantValue = (float) value;
                return;
            }
            ArrayRow newRow = createRow();
            newRow.createRowEquals(a, value);
            newRow.addError(this, strength);
            addConstraint(newRow);
            return;
        }
        ArrayRow row2 = createRow();
        row2.createRowDefinition(a, value);
        row2.addError(this, strength);
        addConstraint(row2);
    }

    public static ArrayRow createRowEquals(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int margin, boolean withError) {
        ArrayRow row = linearSystem.createRow();
        row.createRowEquals(variableA, variableB, margin);
        if (withError) {
            linearSystem.addSingleError(row, 1);
        }
        return row;
    }

    public static ArrayRow createRowDimensionPercent(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, SolverVariable variableC, float percent, boolean withError) {
        ArrayRow row = linearSystem.createRow();
        if (withError) {
            linearSystem.addError(row);
        }
        return row.createRowDimensionPercent(variableA, variableB, variableC, percent);
    }

    public static ArrayRow createRowGreaterThan(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int margin, boolean withError) {
        SolverVariable slack = linearSystem.createSlackVariable();
        ArrayRow row = linearSystem.createRow();
        row.createRowGreaterThan(variableA, variableB, slack, margin);
        if (withError) {
            linearSystem.addSingleError(row, (int) (-1.0f * row.variables.get(slack)));
        }
        return row;
    }

    public static ArrayRow createRowLowerThan(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int margin, boolean withError) {
        SolverVariable slack = linearSystem.createSlackVariable();
        ArrayRow row = linearSystem.createRow();
        row.createRowLowerThan(variableA, variableB, slack, margin);
        if (withError) {
            linearSystem.addSingleError(row, (int) (-1.0f * row.variables.get(slack)));
        }
        return row;
    }

    public static ArrayRow createRowCentering(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int marginA, float bias, SolverVariable variableC, SolverVariable variableD, int marginB, boolean withError) {
        ArrayRow row = linearSystem.createRow();
        row.createRowCentering(variableA, variableB, marginA, bias, variableC, variableD, marginB);
        if (withError) {
            row.addError(linearSystem, 4);
        }
        return row;
    }

    public void addCenterPoint(ConstraintWidget widget, ConstraintWidget target, float angle, int radius) {
        SolverVariable Al = createObjectVariable(widget.getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable At = createObjectVariable(widget.getAnchor(ConstraintAnchor.Type.TOP));
        SolverVariable Ar = createObjectVariable(widget.getAnchor(ConstraintAnchor.Type.RIGHT));
        SolverVariable Ab = createObjectVariable(widget.getAnchor(ConstraintAnchor.Type.BOTTOM));
        SolverVariable Bl = createObjectVariable(target.getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable Bt = createObjectVariable(target.getAnchor(ConstraintAnchor.Type.TOP));
        SolverVariable Br = createObjectVariable(target.getAnchor(ConstraintAnchor.Type.RIGHT));
        SolverVariable Bb = createObjectVariable(target.getAnchor(ConstraintAnchor.Type.BOTTOM));
        ArrayRow row = createRow();
        row.createRowWithAngle(At, Ab, Bt, Bb, (float) (Math.sin((double) angle) * ((double) radius)));
        addConstraint(row);
        ArrayRow row2 = createRow();
        row2.createRowWithAngle(Al, Ar, Bl, Br, (float) (Math.cos((double) angle) * ((double) radius)));
        addConstraint(row2);
    }
}
