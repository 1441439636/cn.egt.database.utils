package servlet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: WYX
 * \* Date: 2017/9/17 0017
 * \* Time: 17:51
 * \* To change this template use File | Settings | File Templates.
 * \* Description:  用于web图表绘制
 * \
 */
@WebServlet(name = "ViewServlet")
public class ViewServlet extends BaseServlet {
    private HashMap<String, ArrayList<String>> colum;//将要进行图表化的数据保存进map中
    private ArrayList<String> main_column_name;//将主列的数据保存进去
    private String chart;//图表的类型

    //生成图表
    public void GetView(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        System.out.println("getview");
        response.setContentType("image/jpeg");//设置输出格式
        CategoryDataset data = GetViewData();//获取数据

        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
        //应用主题样式
        ChartFactory.setChartTheme(standardChartTheme);
        JFreeChart Chart;
        if (chart.equals("BarChart")) {
            //创建柱状图
            Chart = ChartFactory.createBarChart3D(
                    null,
                    null,
                    null,
                    data,
                    PlotOrientation.VERTICAL,
                    true, true, false);
        } else {
            //创建折线图
            Chart = ChartFactory.createLineChart3D(
                    null,
                    null,
                    null,
                    data,
                    PlotOrientation.VERTICAL,
                    true, true, false);
        }


        ChartUtilities.writeChartAsJPEG(response.getOutputStream(),
                0.80f, Chart, 630, 430, null);//输出图表数据

    }

    //创建生成图表的数据
    public String CreateViewData(HttpServletRequest req, HttpServletResponse resp) {
//        System.out.println("get");
        setMap(req, resp);//保存数据进map中
        req.setAttribute("view", true);//设置img可以显示
        chart = req.getParameter("chart");//保存图表的类型

        return "/image.jsp";
    }

    //将原始数据转为一张二维表
    private String[][] Transform(ArrayList<ArrayList<String>> data) {
        int row_num = data.size();//行数
        int column_num = data.get(0).size();//列数
        String[][] table = new String[row_num][column_num];
        for (int i = 0; i < row_num; i++) {
            for (int j = 0; j < column_num; j++) {
                table[i][j] = data.get(i).get(j);
            }
        }
        return table;
    }

    //将原始数据转换为map
    private void setMap(HttpServletRequest req, HttpServletResponse resp) {
        //从session中获取数据来创建图表
        @SuppressWarnings("unchecked")
        ArrayList<ArrayList<String>> resultList = (ArrayList<ArrayList<String>>) req.getSession().getAttribute("resultList");

        String[] choose = req.getParameterValues("choose");//获取要将哪几列进行图表化的列名
        String main_column = req.getParameter("main_column");//获取主列名


        ArrayList<Integer> Num_colum = new ArrayList<Integer>();//存放将要放入map中的列号
        ArrayList<String> first_row = resultList.get(0);//获取原始表格的第一行数据
        for (String str : choose) {
            int number = first_row.indexOf(str);//获取列号
            Num_colum.add(number);//保存
        }

        String[][] table = Transform(resultList);//将原始数据转为一张二维表
        colum = new HashMap<String, ArrayList<String>>();
        for (int num : Num_colum) {//列
            ArrayList<String> temp = new ArrayList<String>();//用于暂时保存一列数据
            for (int j = 1; j < table.length; j++) {//行。  从1开始，是因为第0行元素是列名
                temp.add(table[j][num]);
            }

            colum.put(table[0][num], temp);//将一列数据保存进入map中
        }

        int Num_main_colum = first_row.indexOf(main_column);//获取主列号
        main_column_name = new ArrayList<String>();
        for (int i = 1; i < table.length; i++) {
            main_column_name.add(table[i][Num_main_colum]);
        }
    }

    //将map中的数据转为可以用于绘制图表的数据
    private CategoryDataset GetViewData() {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        Set<String> stringSet = colum.keySet();//获取要图表化的列名set
        //接下来要将每一列的列名和与之对应的数据，再加上主列
        //列名放在x轴，主列作为图例
        for (String name : stringSet) {//列名
            ArrayList<String> colum_value = colum.get(name);//获取一列数据
            for (int index = 0; index < colum_value.size(); index++) {
                double value = Double.parseDouble(colum_value.get(index));
                data.addValue(value, name, main_column_name.get(index));//组合成一条可以显示的数据；注意如果旧的数据的名字一样会被新数据替换
            }
        }
        return data;
    }
}
