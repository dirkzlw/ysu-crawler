package com.zlw.ysu.crawler.vo;

/**
 * 考试信息
 * @author Ranger
 * @create 2019-05-17 15:31
 */
public class Exam {

    private String className;
    private String stuName;
    private String examTime;
    private String eamAddr;
    private String stusEat;

    protected Exam() {
    }

    public Exam(String className, String stuName, String examTime, String eamAddr, String stusEat) {
        this.className = className;
        this.stuName = stuName;
        this.examTime = examTime;
        this.eamAddr = eamAddr;
        this.stusEat = stusEat;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getEamAddr() {
        return eamAddr;
    }

    public void setEamAddr(String eamAddr) {
        this.eamAddr = eamAddr;
    }

    public String getStusEat() {
        return stusEat;
    }

    public void setStusEat(String stusEat) {
        this.stusEat = stusEat;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "className='" + className + '\'' +
                ", stuName='" + stuName + '\'' +
                ", examTime='" + examTime + '\'' +
                ", eamAddr='" + eamAddr + '\'' +
                ", stusEat='" + stusEat + '\'' +
                '}';
    }
}
