package com.zlw.ysu.crawler.crawler;

import com.zlw.ysu.crawler.pojo.User;
import com.zlw.ysu.crawler.utils.HttpUtils;
import com.zlw.ysu.crawler.vo.Grade;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抓取成绩的工具类
 *
 * @author Ranger
 * @create 2019-05-16 11:51
 */
public class QueryGrade {

    public static List<Grade> query(CloseableHttpClient httpClient, User user) throws Exception {

        List<Grade> listGrade = new ArrayList<>();
        String querycjUrl = "http://202.206.243.62/xscj_gc2.aspx?xh=" + user.getUsername() + "&xm=" + URLEncoder.encode(user.getRealname(), "gb2312") + "&gnmkdm=N121605";

        String cjhtml = HttpUtils.getHtml(httpClient, querycjUrl, querycjUrl);

        String cj__VIEWSTATE = Jsoup.parse(cjhtml).getElementById("Form1").select("input[name=__VIEWSTATE]").attr("value");

        Map<String, String> params = new HashMap<>();
        params.put("__VIEWSTATE", cj__VIEWSTATE);
        params.put("ddlXN", "");
        params.put("ddlXQ", "");
        params.put("txtQSCJ", "0");
        params.put("txtZZCJ", "100");
        params.put("Button2", URLEncoder.encode("在校学习成绩查询", "gb2312"));
        CloseableHttpResponse httpResponse = HttpUtils.post(httpClient, params, querycjUrl, querycjUrl);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String querybody = EntityUtils.toString(httpResponse.getEntity(), "gb2312");
            EntityUtils.consume(httpResponse.getEntity());
            Document doc = Jsoup.parse(querybody);
            //获取表格
            Element dataGrid1 = doc.getElementById("Datagrid1");
            //获取tr[]
            Elements trs = dataGrid1.select("tr");
            // 按需求解析html<tr>标签内容并输出
            for (Element tr : trs) {
                //创建结果对象
                Grade grade = new Grade(null, null, null, null, null);
                Elements tds = tr.select("td");
                for (int i = 0; i < tds.size(); i++) {
                    switch (i){
                        case 3:     //课程名称
                            grade.setClassName(tds.get(i).text());
                            break;
                        case 6:     //学分
                            grade.setClassGrade(tds.get(i).text());
                            break;
                        case 7:     //成绩
                            grade.setStuGrade(tds.get(i).text());
                            break;
                        case 11:     //补考成绩
                            grade.setMakeupGrade(tds.get(i).text());
                            break;
                        case 14:    //重修成绩
                            grade.setRebuildGrade(tds.get(i).text());
                            break;
                        default:
                            break;
                    }
                }
                listGrade.add(grade);
            }
        }
        listGrade.remove(0);
        listGrade.add(new Grade("课程名称", "课程学分", "学生成绩", "开课学院", "是否学位课"));
        Collections.reverse(listGrade);

        httpResponse.close();

        return listGrade;
    }
}
