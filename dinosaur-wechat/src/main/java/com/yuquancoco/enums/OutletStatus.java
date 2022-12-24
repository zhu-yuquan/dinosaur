package com.ruoyi.enums;

/**
 * @author cj
 */
public enum OutletStatus {

    /**
     * 开，关
     */
    ON("POWER_ON", "开") ,

    OFF("POWER_OFF", "关");

    /**
     * 状态码
     */
    private final String code;
    /**
     * 备注
     */
    private final String info;

    OutletStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
