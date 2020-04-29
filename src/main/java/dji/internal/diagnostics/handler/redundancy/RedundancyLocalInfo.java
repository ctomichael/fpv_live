package dji.internal.diagnostics.handler.redundancy;

import android.support.annotation.Keep;

@Keep
public class RedundancyLocalInfo {
    private String detail_ch_tips;
    private int dev_type;
    private int err_type;
    private int free_repair;
    private String ground_tips_id;
    private int history_enable;
    private String in_air_tips_id;
    private int usr_show_enable;

    public int getDev_type() {
        return this.dev_type;
    }

    public void setDev_type(int dev_type2) {
        this.dev_type = dev_type2;
    }

    public int getErr_type() {
        return this.err_type;
    }

    public void setErr_type(int err_type2) {
        this.err_type = err_type2;
    }

    public int getHistory_enable() {
        return this.history_enable;
    }

    public void setHistory_enable(int history_enable2) {
        this.history_enable = history_enable2;
    }

    public int getUsr_show_enable() {
        return this.usr_show_enable;
    }

    public void setUsr_show_enable(int usr_show_enable2) {
        this.usr_show_enable = usr_show_enable2;
    }

    public int getFree_repair() {
        return this.free_repair;
    }

    public void setFree_repair(int free_repair2) {
        this.free_repair = free_repair2;
    }

    public String getDetail_ch_tips() {
        return this.detail_ch_tips;
    }

    public void setDetail_ch_tips(String detail_ch_tips2) {
        this.detail_ch_tips = detail_ch_tips2;
    }

    public String getGround_tips_id() {
        return this.ground_tips_id;
    }

    public void setGround_tips_id(String ground_tips_id2) {
        this.ground_tips_id = ground_tips_id2;
    }

    public String getIn_air_tips_id() {
        return this.in_air_tips_id;
    }

    public void setIn_air_tips_id(String in_air_tips_id2) {
        this.in_air_tips_id = in_air_tips_id2;
    }
}
