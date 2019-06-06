package com.zlw.ysu.crawler.vo;

/**
 * 课程
 * @author Ranger
 * @create 2019-05-17 7:58
 */
public class Classes {

    private String mon;
    private String tues;
    private String wed;
    private String thur;
    private String fri;
    private String sat;
    private String sun;

    protected Classes() {
    }

    public Classes(String mon, String tues, String wed, String thur, String fri, String sat, String sun) {
        this.mon = mon;
        this.tues = tues;
        this.wed = wed;
        this.thur = thur;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    public String getMon() {
        return mon;
    }

    public String getTues() {
        return tues;
    }

    public String getWed() {
        return wed;
    }

    public String getThur() {
        return thur;
    }

    public String getFri() {
        return fri;
    }

    public String getSat() {
        return sat;
    }

    public String getSun() {
        return sun;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public void setTues(String tues) {
        this.tues = tues;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public void setThur(String thur) {
        this.thur = thur;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    @Override
    public String toString() {
        return "Classes{" +
                "mon='" + mon + '\'' +
                ", tues='" + tues + '\'' +
                ", wed='" + wed + '\'' +
                ", thur='" + thur + '\'' +
                ", fri='" + fri + '\'' +
                ", sat='" + sat + '\'' +
                ", sun='" + sun + '\'' +
                '}';
    }
}
