package com.adobe.xmp.impl.xpath;

public class XMPPathSegment {
    private boolean alias;
    private int aliasForm;
    private int kind;
    private String name;

    public XMPPathSegment(String str) {
        this.name = str;
    }

    public XMPPathSegment(String str, int i) {
        this.name = str;
        this.kind = i;
    }

    public int getAliasForm() {
        return this.aliasForm;
    }

    public int getKind() {
        return this.kind;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlias() {
        return this.alias;
    }

    public void setAlias(boolean z) {
        this.alias = z;
    }

    public void setAliasForm(int i) {
        this.aliasForm = i;
    }

    public void setKind(int i) {
        this.kind = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String toString() {
        switch (this.kind) {
            case 1:
            case 2:
            case 3:
            case 4:
                return this.name;
            case 5:
            case 6:
                return this.name;
            default:
                return this.name;
        }
    }
}
