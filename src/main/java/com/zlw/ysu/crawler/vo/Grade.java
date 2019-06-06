package com.zlw.ysu.crawler.vo;

/**
 * @author Ranger
 * @create 2019-05-16 11:09
 */
public class Grade {

    private String className;       //课程名称
    private String classGrade;      //课程学分
    private String stuGrade;        //学生成绩
    private String makeupGrade;     //补考成绩
    private String rebuildGrade;    //重修成绩

    protected Grade() {
    }

    public Grade(String className, String classGrade, String stuGrade, String makeupGrade, String rebuildGrade) {
        this.className = className;
        this.classGrade = classGrade;
        this.stuGrade = stuGrade;
        this.makeupGrade = makeupGrade;
        this.rebuildGrade = rebuildGrade;
    }

    public String getClassName() {
        return className;
    }

    public String getClassGrade() {
        return classGrade;
    }

    public String getStuGrade() {
        return stuGrade;
    }

    public String getMakeupGrade() {
        return makeupGrade;
    }

    public String getRebuildGrade() {
        return rebuildGrade;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void    setClassGrade(String classGrade) {
        this.classGrade = classGrade;
    }


    public void setStuGrade(String stuGrade) {
        this.stuGrade = stuGrade;
    }


    public void setMakeupGrade(String makeupGrade) {
        this.makeupGrade = makeupGrade;
    }

    public void setRebuildGrade(String rebuildGrade) {
        this.rebuildGrade = rebuildGrade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "className='" + className + '\'' +
                ", classGrade='" + classGrade + '\'' +
                ", stuGrade='" + stuGrade + '\'' +
                ", makeupGrade='" + makeupGrade + '\'' +
                ", rebuildGrade='" + rebuildGrade + '\'' +
                '}';
    }
}
