package com.zlw.ysu.crawler.crawler;

import com.zlw.ysu.crawler.pojo.User;
import com.zlw.ysu.crawler.utils.HttpUtils;
import com.zlw.ysu.crawler.vo.Classes;
import com.zlw.ysu.crawler.vo.Exam;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 抓取考试工具类
 *
 * @author Ranger
 * @create 2019-05-17 7:50
 */
public class QueryExam {

    public static List<Exam> query(CloseableHttpClient httpClient, User user) throws Exception {

        List<Exam> examList = new ArrayList<>();
        String querycjUrl = "http://202.206.243.62/xskscx.aspx?xh=" + user.getUsername() + "&xm=" + URLEncoder.encode(user.getRealname(), "gb2312") + "&gnmkdm=N121605";
        String refererUrl = "http://202.206.243.62/xs_main.aspx?xh=" + user.getUsername();
        String cjhtml = HttpUtils.getHtml(httpClient, querycjUrl, refererUrl);

        Document doc = Jsoup.parse(cjhtml);
        Element table = doc.getElementById("DataGrid1");
        Elements trs = table.select("tr");
        for (Element tr : trs) {
            //创建结果对象
            Exam exam = new Exam(null, null, null, null, null);
            Elements tds = tr.select("td");
            for (int i = 0; i < tds.size(); i++) {
                switch (i){
                    case 0:
                        break;
                    case 1:
                        exam.setClassName(tds.get(i).text());
                        break;
                    case 2:
                        exam.setStuName(tds.get(i).text());
                        break;
                    case 3:
                        exam.setExamTime(tds.get(i).text());
                        break;
                    case 4:
                        exam.setEamAddr(tds.get(i).text());
                        break;
                    case 5:
                        break;
                    case 6:
                        exam.setStusEat(tds.get(i).text());
                        break;
                    case 7:
                        break;
                    default:
                        break;
                }
            }
            examList.add(exam);
        }
        examList.remove(0);
        return examList;
    }

    /**
     * 解析课表信息
     *
     * @param classMsg
     * @return
     */
    private static String exmClassMsg(String classMsg) {
        if ("".equals(classMsg) || classMsg.length() == 0) {
            return "";
        }
        String[] strs = classMsg.split(" ");

        return strs[0] + "<br/>" + strs[2].split("节")[1] + "<br/>" + strs[strs.length - 1] + "\n";
    }

}
