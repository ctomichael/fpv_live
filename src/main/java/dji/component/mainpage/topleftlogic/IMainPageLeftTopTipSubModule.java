package dji.component.mainpage.topleftlogic;

public interface IMainPageLeftTopTipSubModule {
    void clearLeftTopTipMgr();

    String getModuleName();

    int getPriority();

    boolean isFinishChecking();

    void onAllModuleFinishChecking(boolean z);

    void setLeftTopTipMgr(IMainPageLeftTopTipLogicMgr iMainPageLeftTopTipLogicMgr);

    void setVisibility(boolean z);
}
