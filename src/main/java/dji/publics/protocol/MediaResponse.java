package dji.publics.protocol;

import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MediaResponse extends ResponseBase {
    public int mCurrentPage = 0;
    public int mPageSize = 0;
    public int mTotalCount = 0;
    public int mTotalPage = 0;

    public boolean equals(Object o) {
        return super.equals(o);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString() + "curpage[" + String.valueOf(this.mCurrentPage) + "]psize[" + String.valueOf(this.mPageSize) + "]count[" + String.valueOf(this.mTotalCount) + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }
}
