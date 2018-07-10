package servlet;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

@WebServlet(name = "DownloadServlet", urlPatterns = "/download")
public class DownloadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            ExportExcel(request, response);//先将数据写出
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("开始下载");
        String FileName = "Output.xls";//下载文件名
        String mimeType = getServletContext().getMimeType(FileName);//获取mimeType
        response.setContentType(mimeType);//设置文件的MIME类型
        response.setHeader("content-disposition", "attachment;filename=" + FileName);//下载
        //定义输入流
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("F:\\zuomian\\Output.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //定义输出流
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            int len = -1;
            byte[] bytes = new byte[1024];
            while ((len = inputStream != null ? inputStream.read(bytes) : 0) != -1) {
                outputStream.write(bytes, 0, len);
            }
            //关闭流
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    //输出excel文件
    private void ExportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        @SuppressWarnings("unchecked")
        ArrayList<ArrayList<String>> resultList = (ArrayList<ArrayList<String>>) request.getSession().getAttribute("resultList");//从session中读取数据
        HSSFWorkbook workbook = new HSSFWorkbook();//创建工作表单
        HSSFSheet sheet = workbook.createSheet("数据");
        sheet.setDefaultColumnWidth(15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        int row_index = 0;//行号
        for (ArrayList<String> row : resultList) {
            HSSFRow row1 = sheet.createRow(row_index++);//一行数据
            int cell_index = 0;
            for (String data : row) {
                HSSFCell cell = row1.createCell(cell_index++);//一列数据
                cell.setCellValue(data);
            }
        }
        FileOutputStream outputStream = new FileOutputStream("F:\\zuomian\\Output.xls");//文件输出路径
        workbook.write(outputStream);
        outputStream.close();

    }
}
