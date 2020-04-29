package dji.component.mainpage.topleftlogic;

public interface IMainPageLeftTopTipLogicMgr {
    void clearLeftTopTipSubModule(IMainPageLeftTopTipSubModule iMainPageLeftTopTipSubModule);

    void onOneModuleReady(IMainPageLeftTopTipSubModule iMainPageLeftTopTipSubModule);

    void onSubModuleFinishCheckNotShow(IMainPageLeftTopTipSubModule iMainPageLeftTopTipSubModule);

    void onSubModuleStartCheck(IMainPageLeftTopTipSubModule iMainPageLeftTopTipSubModule);

    void requestHide(IMainPageLeftTopTipSubModule iMainPageLeftTopTipSubModule);

    void requestShow(IMainPageLeftTopTipSubModule iMainPageLeftTopTipSubModule);
}
