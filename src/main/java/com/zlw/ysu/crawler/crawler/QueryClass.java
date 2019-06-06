package com.zlw.ysu.crawler.crawler;

import com.zlw.ysu.crawler.vo.Classes;
import com.zlw.ysu.crawler.pojo.User;
import com.zlw.ysu.crawler.utils.HttpUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 抓取课表工具类
 *
 * @author Ranger
 * @create 2019-05-17 7:50
 */
public class QueryClass {

    public static List<Classes> query(CloseableHttpClient httpClient, User user, int weekNum) throws Exception {

        List<Classes> classesList = new ArrayList<>();
        String querycjUrl = "http://202.206.243.62/xskbcx.aspx?xh=" + user.getUsername() + "&xm=" + URLEncoder.encode(user.getRealname(), "gb2312") + "&gnmkdm=N121605";
        String refererUrl = "http://202.206.243.62/xs_main.aspx?xh=" + user.getUsername();
        String cjhtml = HttpUtils.getHtml(httpClient, querycjUrl, refererUrl);

        Element table1 = Jsoup.parse(cjhtml).getElementById("Table1");
        Elements trs = table1.select("tr");
        for (int i = 0; i < trs.size(); i += 2) {
            if (i == 0) {
                //跨过表头
                continue;
            }
            Classes classes = new Classes(null, null, null, null, null, null, null);
            Elements tds = trs.get(i).select("td");
            for (int j = 0; j < tds.size(); j++) {
                if ((i == 2 || i == 6 || i == 10) && (j == 0 || j == 1)) {
                    //跨过时间
                    continue;
                }
                if (i == 2 || i == 6 || i == 10) {
                    switch (j) {
                        case 2:     //星期一
                            classes.setMon(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 3:
                            classes.setTues(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 4:
                            classes.setWed(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 5:
                            classes.setThur(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 6:
                            classes.setFri(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 7:
                            classes.setSat(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 8:
                            classes.setSun(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (j) {
                        case 1:     //星期一
                            classes.setMon(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 2:
                            classes.setTues(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 3:
                            classes.setWed(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 4:
                            classes.setThur(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 5:
                            classes.setFri(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 6:
                            classes.setSat(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        case 7:
                            classes.setSun(exmClassMsg(tds.get(j).text(), weekNum));
                            break;
                        default:
                            break;
                    }
                }

            }

            classesList.add(classes);
        }
        return classesList;
    }

    /**
     * 解析课表信息
     * <p>
     * 专业外语
     * 限选
     * 周二第1,2节{第10-17周}
     * 余靖
     * （里）B202多媒体
     *
     * @param classMsg
     * @return
     */
    private static String exmClassMsg(String classMsg, int weekNum) {
        if ("".equals(classMsg) || classMsg.length() == 0) {
            return "";
        }
        String[] strs = classMsg.split(" ");
        /**
         * 根据抓取每节课程的信息长度进行分类解析
         * 5：专业外语 限选 周二第1,2节{第10-17周} 宇乾 202教室
         * 7：5
         * 10：5+5
         * 12：
         */
        switch (strs.length) {
            case 5:
            case 7:  //忽略后两行
                String[] weks = strs[2].split("节")[1].split("-");
                String[] startWeek = weks[0].split("第");
                String[] endWeek = weks[1].split("周");

                if (weekNum >= Integer.parseInt(startWeek[1]) && weekNum <= Integer.parseInt(endWeek[0])) {
                    return strs[0] + "<br/>" + strs[4] + "\n";
                } else {
                    return "";
                }
            case 12:
                //判断是7+5还是5+7
                if ("第".equals(strs[5].substring(0, 1))) {
                    //7+5，循环将无用的两行退到最后
                    for (int i = 5; i < strs.length - 2; i++) {
                        strs[i] = strs[i + 2];
                    }
                }
            case 10:
                String[] weks2 = strs[2].split("节")[1].split("-");
                String[] startWeek2 = weks2[0].split("第");
                String[] endWeek2 = weks2[1].split("周");

                if (weekNum >= Integer.parseInt(startWeek2[1]) && weekNum <= Integer.parseInt(endWeek2[0])) {
                    return strs[0] + "<br/>" + strs[4] + "\n";
                } else {

                    String[] weks22 = strs[7].split("节")[1].split("-");
                    String[] startWeek22 = weks22[0].split("第");
                    String[] endWeek22 = weks22[1].split("周");

                    if (weekNum >= Integer.parseInt(startWeek22[1]) && weekNum <= Integer.parseInt(endWeek22[0])) {
                        return strs[5] + "<br/>" + strs[9] + "\n";
                    } else {
                        return "";
                    }

                }
            default:
                break;
        }

        return "";

    }

    public static void main(String[] args) {

        String[] arr = new String[]{"1", "2", "3"};

    }

    public static int differentDays(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);

        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);

        if (year1 != year2)  //不同年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) { //闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else { // 不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {// 同年
            return day2 - day1;
        }

    }
}
