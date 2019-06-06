package com.zlw.ysu.crawler.crawler;

import com.zlw.ysu.crawler.pojo.User;
import com.zlw.ysu.crawler.utils.HttpUtils;
import com.zlw.ysu.crawler.vo.Classroom;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抓取教室
 *
 * @author Ranger
 * @create 2019-05-18 15:12
 */
public class QueryClassroom {

    public static List<Classroom> query(CloseableHttpClient httpClient, User user,String select) throws Exception {
        List<Classroom> classroomList = new ArrayList<>();
        String querycjUrl = "http://202.206.243.62/xxjsjy.aspx?xh=" + user.getUsername() + "&xm=" + URLEncoder.encode(user.getRealname(), "gb2312") + "&gnmkdm=N121605";
        String refererUrl = "http://202.206.243.62/xs_main.aspx?xh=" + user.getUsername();
        //查询东校区
        String xiaoq = "1";
        runQuery(httpClient, classroomList, querycjUrl, refererUrl, xiaoq,select);
        //查询西校区
        xiaoq = "4";
        runQuery(httpClient, classroomList, querycjUrl, refererUrl, xiaoq,select);

        //遍历结果集

        return classroomList;
    }

    /**
     * 执行查询
     *
     * @param httpClient
     * @param classroomList
     * @param querycjUrl
     * @param refererUrl
     * @param xiaoq         1：东校区   4：西校区
     * @throws Exception
     */
    private static void runQuery(CloseableHttpClient httpClient,
                                 List<Classroom> classroomList,
                                 String querycjUrl,
                                 String refererUrl,
                                 String xiaoq,
                                 String select) throws Exception {

        String cjhtml = HttpUtils.getHtml(httpClient, querycjUrl, refererUrl);
        Document cjdoc = Jsoup.parse(cjhtml);
        //在cjhtml中获取表单信息
        Element form1 = cjdoc.getElementById("Form1");
        String __EVENTTARGET = form1.select("input[name=__EVENTTARGET]").attr("value");
        String __EVENTARGUMENT = form1.select("input[name=__EVENTARGUMENT]").attr("value");
        String __VIEWSTATE = form1.select("input[name=__VIEWSTATE]").attr("value");

        //添加参数
        Map<String, String> params = new HashMap<>();
        params.put("__EVENTTARGET", __EVENTTARGET);
        params.put("__EVENTARGUMENT", __EVENTARGUMENT);
        params.put("__VIEWSTATE", __VIEWSTATE);
        params.put("xiaoq", xiaoq);   //校区
        params.put("jslb", "");     //教室类别
        params.put("min_zws", "0");
        params.put("max_zws", "");
        params.put("kssj", "612");
        params.put("jssj", "612");
        params.put("xqj", "6");
        params.put("ddlDsz", "");
        params.put("sjd", select);
        params.put("Button2", URLEncoder.encode("空教室查询", "gb2312"));
        params.put("xn", "2018-2019");
        params.put("xq", "2");
        params.put("jsbh", "");     //尚不明确
        params.put("ddlSyXn", "2018-2019");
        params.put("ddlSyxq", "2");

        CloseableHttpResponse httpResponse = HttpUtils.post(httpClient, params, querycjUrl, querycjUrl);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String querybody = EntityUtils.toString(httpResponse.getEntity(), "gb2312");
            EntityUtils.consume(httpResponse.getEntity());
            Document doc = Jsoup.parse(querybody);

            //递归遍历分页
            String dpDataGrid1_lblCurrentPage = doc.getElementById("dpDataGrid1_lblCurrentPage").text();
            int currentPage = Integer.parseInt(dpDataGrid1_lblCurrentPage);
            String dpDataGrid1_lblTotalPages = doc.getElementById("dpDataGrid1_lblTotalPages").text();
            int totalPage = Integer.parseInt(dpDataGrid1_lblTotalPages);
            if (currentPage < totalPage) {
                runQueryRec(httpClient, classroomList, querycjUrl, refererUrl, xiaoq, doc, "2");
            }else{
                doDocJsoup(doc, classroomList);
            }
        }

        //关闭response
        httpResponse.close();

    }

    /**
     * 解析html
     * @param doc
     * @param classroomList
     */
    private static void doDocJsoup(Document doc,List<Classroom> classroomList){
        //获取表格
        Element dataGrid1 = doc.getElementById("DataGrid1");
        //获取tr[]
        Elements trs = dataGrid1.select("tr");
        trs.remove(0);
        for (Element tr : trs) {
            Classroom classroom = new Classroom(null, null, null, null, null);
            Elements tds = tr.select("td");
            for (int i = 1; i < tds.size(); i++) {
                switch (i) {
                    case 1:
                        classroom.setClassroomName(tds.get(i).text());
                        break;
                    case 2:
                        classroom.setClassroomType(tds.get(i).text());
                        break;
                    case 3:
                        switch (tds.get(i).text()) {
                            case "1":
                                classroom.setCampus("东校区");
                                break;
                            case "4":
                                classroom.setCampus("西校区（第二组团）");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 4:
                        classroom.setSeatNum(tds.get(i).text());
                        break;
                    case 5:
                        classroom.setUserDepter(tds.get(i).text());
                        break;
                    default:
                        break;
                }

            }
            classroomList.add(classroom);
        }
    }

    /**
     * 处理多页
     * @param httpClient
     * @param classroomList
     * @param querycjUrl
     * @param refererUrl
     * @param xiaoq
     * @param cjdoc
     * @param select
     * @throws Exception
     */
    private static void runQueryRec(CloseableHttpClient httpClient,
                                    List<Classroom> classroomList,
                                    String querycjUrl,
                                    String refererUrl,
                                    String xiaoq,
                                    Document cjdoc,
                                    String select) throws Exception {
        //在cjhtml中获取表单信息
        Element form1 = cjdoc.getElementById("Form1");
        String __EVENTTARGET = form1.select("input[name=__EVENTTARGET]").attr("value");
        String __EVENTARGUMENT = form1.select("input[name=__EVENTARGUMENT]").attr("value");
        String __VIEWSTATE = form1.select("input[name=__VIEWSTATE]").attr("value");

        //添加参数
        Map<String, String> params = new HashMap<>();
        params.put("__EVENTTARGET", __EVENTTARGET);
        params.put("__EVENTARGUMENT", __EVENTARGUMENT);
        params.put("__VIEWSTATE", __VIEWSTATE);
        params.put("xiaoq", xiaoq);   //校区
        params.put("jslb", "");     //教室类别
        params.put("min_zws", "0");
        params.put("max_zws", "");
        params.put("kssj", "612");
        params.put("jssj", "612");
        params.put("xqj", "6");
        params.put("ddlDsz", "");
        params.put("sjd", select);
        params.put("Button2", URLEncoder.encode("空教室查询", "gb2312"));
        params.put("dpDataGrid1:txtChoosePage", "1");
        params.put("dpDataGrid1:txtPageSize", "500");
        params.put("xn", "2018-2019");
        params.put("xq", "2");
        params.put("jsbh", "");     //尚不明确
        params.put("ddlSyXn", "2018-2019");
        params.put("ddlSyxq", "2");

        CloseableHttpResponse httpResponse = HttpUtils.post(httpClient, params, querycjUrl, querycjUrl);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String querybody = EntityUtils.toString(httpResponse.getEntity(), "gb2312");
            EntityUtils.consume(httpResponse.getEntity());
            Document doc = Jsoup.parse(querybody);

            doDocJsoup(doc, classroomList);
        }

        //关闭response
        httpResponse.close();
    }
}
