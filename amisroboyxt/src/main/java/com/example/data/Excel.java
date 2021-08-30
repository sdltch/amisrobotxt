package com.example.data;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Excel {
    public static String fileName= "";
    public static List<String[]> judgeExcel() throws Exception {
        String paths= null;
        //获取当前系统
        String mySystems = System.getProperties().getProperty("os.name");
        //System.out.println("===========os.namesxcel:"+mySystems);
        if(mySystems.contains("Windows")){
            //windows
            //paths = "D:\\testdata\\github\\amisroboyxt\\amisrobot\\excel\\amirobot.xls";
            paths = "D:\\testdata\\github\\amisroboyxt\\amisrobot\\excel\\amirobotclound.xls";
            System.out.println("当前在excel1:"+mySystems+" 系统操作");
        }else if(mySystems.contains("Linux")){
            //linux路径
            //paths = "/amisbook/amis/amisrobot/linuxexcel/testamirobot_linux.xls";
            paths = "/root/.jenkins/workspace/amisbook002/amisrobot/linuxexcel/testamirobot_linux.xls";
            System.out.println("当前在excel2:"+mySystems+" 系统操作");
        }else {
            System.out.println("当前在excel3:"+mySystems+" 系统操作；且当前不支持该操作系统");
        }

        //获取文件名称和后缀
        File tempFile =new File(paths.trim());
        String fileNames = tempFile.getName();
        System.out.println("文件后缀名称= " + fileNames);
        //获取文件名称
        String fileName = fileNames.substring(0,fileNames.lastIndexOf("."));
        System.out.println("文件名称 = " + fileName);
        //根据包头进行判断文件类型
        String suffix = fileTypeJuedg.getFileType(paths);
        System.out.println("文件真实类型 = "+suffix);
        System.out.println("文件路径 = "+paths);
//       //获取文件的后缀名 .jpg（以后缀名判断文件类型，
        String suff = paths.substring(paths.lastIndexOf("."));
        //System.out.println("文件后缀名称 = "+suff);
        String ssuffixName = suff.toLowerCase();//使用toLowerCase()方法实现小写转换
        //System.out.println("后缀名处理成小写 = "+ssuffixName);
        //当文件真实类型为xls或者xlsx时，doc和xls；docx和xlsx的魔数一样，会判断错误
        //文件真实类型为xls或者xlsx时,以后缀名为准
        if(suffix.equals("xls") && ssuffixName.equals(".doc")){
            suffix = ssuffixName;
        }else if(suffix.equals("xlsx") && ssuffixName.equals(".docx")){
            suffix = ssuffixName;
        }if(suffix.equals("null")){
            suffix = ssuffixName;
        }
        //ArrayList<String[]> all = null;
        List<String[]> all = null;
//        //获取文件的后缀名 .jpg（以后缀名判断文件类型，如果用户更改文件后缀，会导致处理错误
//        String suffix = paths.substring(paths.lastIndexOf("."));
        if(suffix.equals("xls")){
            all = readExcel(paths);
        }else if(suffix.equals("xlsx")){
            all = readExcelx(paths);
        }else {
            System.out.println("暂时不支持该格式："+suffix);
        }
        //.out.println("文件真实后缀名称："+suffix);
        return all;
    }
    //xsl
    public static ArrayList<String[]> readExcel(String mypath) {
        //创建workbook
        Workbook workbook = null;
        try {
            //只支持xls文件
            //mypath = "D:\\testdata\\amisbook002\\amisrobot\\excel\\testamirobot.xls";
            File filepath = new File(mypath);
            //workbook = Workbook.getWorkbook(new File("D:\\Users\\Administrator\\Desktop\\RC(包)\\TestPHPYun.xls"));
            workbook = Workbook.getWorkbook(filepath);


        } catch (BiffException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //创建一个列表集合用于存放所有的表格数据
        ArrayList<String[]> al = new ArrayList<String[]>();
        //获取第一个工作表sheet
        Sheet sheet = workbook.getSheet(0);
        System.out.println("xls总行数为：" + sheet.getRows());
        for (int i = 0; i < sheet.getRows(); i++) {
            //创建一个String数组存放每行
            String[] rowData = new String[sheet.getColumns()];
            //System.out.println("xls第"+i+"行有:"+sheet.getColumns()+"格");
            for (int j = 0; j < sheet.getColumns(); j++) {
                Cell cell = sheet.getCell(j, i);
                //System.out.print(sheet.getCell(j, i) + "  ");
                //将每行数据保存在String数组中
                rowData[j] = cell.getContents();
                //System.out.println( "xls第："+i+"行；第： "+j+"格："+cell.getContents());
            }

            //将每行保存在列表中。
            al.add(rowData);
        }

        workbook.close();
        return al;
    }
    //xlsx
    public static ArrayList<String[]> readExcelx(String mypathx) throws Exception {
        //创建XSSFWorkbook
        XSSFWorkbook xssfWorkbook = null;
        try {
            //mypathx = "D:\\testdata\\amisbook002\\amisrobot\\excel\\testamirobot.xlsx";
            //创建工作簿
            xssfWorkbook = new XSSFWorkbook(new FileInputStream(mypathx));
            //System.out.println("xssfWorkbook对象：" + xssfWorkbook);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建一个列表集合用于存放所有的表格数据
        ArrayList<String[]> alx = new ArrayList<String[]>();
        //List<Object[]> alx = new ArrayList<Object[]>();
        //读取第一个工作表
        XSSFSheet sheetx = xssfWorkbook.getSheetAt(0);
        //System.out.println("sheet对象：" + sheetx);
        //获取最后一行的num，即总行数。此处从0开始计数
        int maxRow = sheetx.getLastRowNum();
        System.out.println("xlsx总行数为：" + (maxRow+1));
        for (int row = 0; row <= maxRow; row++) {
            //获取最后单元格num，即总单元格数 ***注意：此处从1开始计数***
            int maxRol = sheetx.getRow(row).getLastCellNum();
            //创建一个String数组存放每行
            String[] rowDatax = new String[sheetx.getPhysicalNumberOfRows()];
            //System.out.println("--------第" + (row+1) + "行的数据如下-------共："+maxRol+"格");
            for (int rol = 0; rol < maxRol; rol++){
               // System.out.println(sheetx.getRow(row).getCell(rol) + " ");
                XSSFCell cellx = sheetx.getRow(row).getCell(rol);

                //将每行数据保存在String数组中
//                if (cellx == null) {
//                    rowDatax[rol] = "";
//                }else {
//                    rowDatax[rol] = cellx.getStringCellValue();
//                }
                rowDatax[rol] = cellx.getStringCellValue();
//                if(cellx.getStringCellValue() != ""){
//                    rowDatax[rol] = cellx.getStringCellValue();
//                }
                //System.out.println( "第："+(row+1)+"行；第： "+(rol+1)+"格："+sheetx.getRow(row).getCell(rol));
            }
            //将每行保存在列表中。
            alx.add(rowDatax);
            //System.out.println();
        }
        xssfWorkbook.close();
        return alx;
    }
    //xls
    public static List<String[]> readExcelxx(String mypathx) throws Exception {
        //创建XSSFWorkbook
        HSSFWorkbook hssfWorkbook = null;
        //System.out.println("poi包：xls");
        try {
            //mypathx = "D:\\testdata\\amisbook002\\amisrobot\\excel\\testamirobot.xlsx";
            //创建工作簿
            hssfWorkbook = new HSSFWorkbook(new FileInputStream(mypathx));
            //System.out.println("xssfWorkbook对象：" + hssfWorkbook);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建一个列表集合用于存放所有的表格数据
        List<String[]> alx = new ArrayList<String[]>();
        //读取第一个工作表
        HSSFSheet sheetx = hssfWorkbook.getSheetAt(0);
        //System.out.println("sheet对象：" + sheetx);
        //获取最后一行的num，即总行数。此处从0开始计数
        int maxRow = sheetx.getLastRowNum();
        System.out.println("xls总行数为：" + maxRow);
        for (int row = 0; row <= maxRow; row++) {
            //获取最后单元格num，即总单元格数 ***注意：此处从1开始计数***
            int maxRol = sheetx.getRow(row).getLastCellNum();
            //创建一个String数组存放每行
            String[] rowDatax = new String[sheetx.getPhysicalNumberOfRows()];
            //System.out.println("--------第" + row + "行的数据如下--------");
            for (int rol = 0; rol < maxRol; rol++){
                //System.out.println(sheetx.getRow(row).getCell(rol) + " ");
                HSSFCell cellx = sheetx.getRow(row).getCell(rol);
                //将每行数据保存在String数组中
                rowDatax[rol] = cellx.getStringCellValue();
            }
            //将每行保存在列表中。
            alx.add(rowDatax);
        }
        hssfWorkbook.close();
        return alx;
    }
}




