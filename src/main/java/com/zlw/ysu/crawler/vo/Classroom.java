package com.zlw.ysu.crawler.vo;

/**
 * @author Ranger
 * @create 2019-05-18 15:06
 */
public class Classroom {
    private String classroomName;   //教室名称
    private String classroomType;   //教室类别
    private String campus;          //所属校区
    private String seatNum;         //座位数
    private String userDepter;      //使用部门

    protected Classroom() {
    }

    public Classroom(String classroomName, String classroomType, String campus, String seatNum, String userDepter) {
        this.classroomName = classroomName;
        this.classroomType = classroomType;
        this.campus = campus;
        this.seatNum = seatNum;
        this.userDepter = userDepter;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getClassroomType() {
        return classroomType;
    }

    public void setClassroomType(String classroomType) {
        this.classroomType = classroomType;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }

    public String getUserDepter() {
        return userDepter;
    }

    public void setUserDepter(String userDepter) {
        this.userDepter = userDepter;
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "classroomName='" + classroomName + '\'' +
                ", classroomType='" + classroomType + '\'' +
                ", campus='" + campus + '\'' +
                ", seatNum='" + seatNum + '\'' +
                ", userDepter='" + userDepter + '\'' +
                '}';
    }
}
