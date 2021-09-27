package com.example.demoexcel.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demoexcel.Entity.Center;
import com.example.demoexcel.Entity.Mission;
import com.example.demoexcel.Entity.Model;
import com.example.demoexcel.Service.CenterService;
import com.example.demoexcel.Service.MissionService;
import com.example.demoexcel.Service.ModelService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.*;


/**
 * (Mission)表控制层
 *
 * @author makejava
 * @since 2021-09-08 13:28:34
 */
@Controller
@RequestMapping("/Mission")
public class MissionController {
    /**
     * 服务对象
     */
    @Resource
    private MissionService missionService;
    @Resource
    private ModelService modelService;
    @Resource
    private CenterService centerService;


    static final HashMap<String,String> map=new HashMap<>();
    /**
     *
     * 新插入所有数据
     */
    public void exportData(HSSFSheet sheet,String excel_name){
        int row0=1;
        int row1=1;
        int row3=1;
        //聚合查询
        List<Center> centerList=centerService.queryAllCenter();
        for(int i=0;i<centerList.size();i++){
            List<Model> modelList=modelService.queryByCenter_id(centerList.get(i).getId());
            for(int j=0;j<modelList.size();j++){
               List<Mission> missionList=missionService.queryByModel_id(modelList.get(j).getId(),excel_name);
                row1+=missionList.size();
                for(int k=0;k<missionList.size();k++){
                    HSSFRow row = sheet.createRow(k+row0);
                    Mission mission=missionList.get(k);
                    row.createCell(0).setCellValue(centerList.get(i).getCenterName());

                    row.createCell(1).setCellValue(modelList.get(j).getModelName());

                    row.createCell(2).setCellValue(mission.getDirector());
                    row.createCell(3).setCellValue(mission.getOppositePerson());
                    row.createCell(4).setCellValue(mission.getMissionName());
                    row.createCell(5).setCellValue(mission.getDescribe());
                    row.createCell(6).setCellValue(mission.getStarttime());
                    row.createCell(7).setCellValue(mission.getEndtime());
                    row.createCell(8).setCellValue(mission.getTimechange());
                    row.createCell(9).setCellValue(mission.getChangreason());
                    row.createCell(10).setCellValue(mission.getSpeedOfProgress());
                    row.createCell(11).setCellValue(mission.getRelatedDocumentLinks());
                    row.createCell(12).setCellValue(mission.getAcceptancePassed());
                }
                if(row1-1>row0) {
                    CellRangeAddress region = new CellRangeAddress(row0,row1-1, 1, 1);
                    sheet.addMergedRegion(region);
                }
                row0+=missionList.size();
            }
            if(row1-1>row3) {
                CellRangeAddress region2 = new CellRangeAddress(row3,row1-1, 0, 0);
                sheet.addMergedRegion(region2);
            }
            row3+=row1-1;
        }
    }




    /**
     *
     * 插入每周数据
     */
    public void exportWeekData(HSSFSheet sheet,String excel_name){
        int row0=1;
        int row1=1;
        int row3=1;
        //聚合查询
        List<Center> centerList=centerService.queryAllCenter();
        for(int i=0;i<centerList.size();i++){
            List<Model> modelList=modelService.queryByCenter_id(centerList.get(i).getId());
            for(int j=0;j<modelList.size();j++){
                List<Mission> missionList=NowWeekMission(modelList.get(j).getId(),excel_name);
                row1+=missionList.size();
                for(int k=0;k<missionList.size();k++){
                    HSSFRow row = sheet.createRow(k+row0);
                    Mission mission=missionList.get(k);
                    row.createCell(0).setCellValue(centerList.get(i).getCenterName());

                    row.createCell(1).setCellValue(modelList.get(j).getModelName());

                    row.createCell(2).setCellValue(mission.getDirector());
                    row.createCell(3).setCellValue(mission.getOppositePerson());
                    row.createCell(4).setCellValue(mission.getMissionName());
                    row.createCell(5).setCellValue(mission.getDescribe());
                    row.createCell(6).setCellValue(mission.getStarttime());
                    row.createCell(7).setCellValue(mission.getEndtime());
                    row.createCell(8).setCellValue(mission.getTimechange());
                    row.createCell(9).setCellValue(mission.getChangreason());
                    row.createCell(10).setCellValue(mission.getSpeedOfProgress());
                    row.createCell(11).setCellValue(mission.getRelatedDocumentLinks());
                    row.createCell(12).setCellValue(mission.getAcceptancePassed());
                }
                if(row1-1>row0) {
                    CellRangeAddress region = new CellRangeAddress(row0,row1-1, 1, 1);
                    sheet.addMergedRegion(region);
                }
                row0+=missionList.size();
            }
            if(row1-1>row3) {
                CellRangeAddress region2 = new CellRangeAddress(row3,row1-1, 0, 0);
                sheet.addMergedRegion(region2);
            }
            row3+=row1-1;
        }
    }

    /**
     * 设置title
     */
    private void setTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        try {
            String[] str = new String[]{"中台模块","中台模块","负责人", "后端对接人", "任务名称"
                    , "描述", "开始时间", "结束时间", "计划截止时间变更", "变更原因", "进度", "相关文档", "验证通过"};
            HSSFRow row = sheet.createRow(0);
            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            for (int i = 0; i <= str.length; i++) {
                sheet.setColumnWidth(i, 15 * 256);
            }
            //设置为居中加粗,格式化时间格式
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
//            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
            //创建表头名称
            HSSFCell cell;

            for (int j = 0; j < str.length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(str[j]);
                cell.setCellStyle(style);
            }
        } catch (Exception e) {
            System.out.println("导出时设置表头失败！");
            e.printStackTrace();
        }
    }
    @RequestMapping("/showExcel")
    public String show(){
        return "/Excel.html";
    }

    @RequestMapping("/showNewExcel")
    public String showNew(){
        return "/ExcelNew.html";
    }

    @RequestMapping("/index")
    public String showIndex(org.springframework.ui.Model model){
        List<String> lists=missionService.queryFile();
        model.addAttribute("lists",lists);
        return "/index.html";
    }

    /**
     *
     *
     * 读取数据后 新建文件
     *
     */
    @RequestMapping(value = "/download",method =RequestMethod.GET)
    public void downloadFile(@RequestParam("filename")String filename,HttpServletResponse response){
        map.put("filename",filename);
        try {
            response.sendRedirect("/Mission/showExcel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//   @ApiOperation("导出所有任务")
   @RequestMapping(value = "/exportExcel",method =RequestMethod.GET)
    public void exportExcel(HttpServletResponse response) {
        try {
            String filename=map.get("filename");
            //实例化HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workbook.createSheet("sheet");
            //设置表头
            setTitle(workbook, sheet);
            //设置单元格并赋值
            exportData(sheet,filename);
            //设置浏览器下载
            try {
                //清空response
                response.reset();
                //设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new SimpleDateFormat("yyyyMMdd").format(new Date()));
                OutputStream os = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/vnd.ms-excel;charset=gb2312");
                //将excel写入到输出流中
                workbook.write(os);
                os.flush();
                os.close();
                System.out.println("设置浏览器下载成功！");
            } catch (Exception e) {
                System.out.println("设置浏览器下载失败！");
                e.printStackTrace();
            }
            System.out.println("导出解析成功!");
        } catch (Exception e) {
            System.out.println("导出解析失败!");
            e.printStackTrace();
        }
    }

//    @ApiOperation("导出本周任务")
    @RequestMapping(value = "/exportNowExcel",method =RequestMethod.GET)


    public void exportNowExcel(HttpServletResponse response) {
        try {
            String filename=map.get("filename");
            //实例化HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workbook.createSheet("每周周报");
            //设置表头
            setTitle(workbook, sheet);
            //设置单元格并赋值
            exportWeekData(sheet,filename);
            //设置浏览器下载
            try {
                //清空response
                response.reset();
                //设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + "sheet");
                OutputStream os = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/vnd.ms-excel;charset=gb2312");
                //将excel写入到输出流中
                workbook.write(os);
                os.flush();
                os.close();
                System.out.println("设置浏览器下载成功！");
            } catch (Exception e) {
                System.out.println("设置浏览器下载失败！");
                e.printStackTrace();
            }
            System.out.println("导出解析成功!");
        } catch (Exception e) {
            System.out.println("导出解析失败!");
            e.printStackTrace();
        }
    }


    //    @ApiOperation("新建任务")
    @RequestMapping(value = "/NewExcel",method =RequestMethod.GET)


    public void NewExcel(HttpServletResponse response) {
        try {
            map.put("filename","新建表格"+UUID.randomUUID().toString().replace("-", "").toUpperCase());
            //实例化HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workbook.createSheet("新建表格");
            //设置表头
            setTitle(workbook, sheet);
            //设置浏览器下载
            try {
                //清空response
                response.reset();
                //设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + "sheet");
                OutputStream os = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/vnd.ms-excel;charset=gb2312");
                //将excel写入到输出流中
                workbook.write(os);
                os.flush();
                os.close();
                System.out.println("设置浏览器下载成功！");
            } catch (Exception e) {
                System.out.println("设置浏览器下载失败！");
                e.printStackTrace();
            }
            System.out.println("导出解析成功!");
        } catch (Exception e) {
            System.out.println("导出解析失败!");
            e.printStackTrace();
        }
    }



    /**
     *
     * 导入数据
     */
    public List<List<String>> importExcel(HttpServletRequest request, HttpServletResponse response) {
        List<List<String>> list = new ArrayList<>();
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String body = "";

            try {
                Scanner scanner = new Scanner(request.getInputStream());
                scanner.useDelimiter("\\A");
                body = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
            } catch (Exception ex) {
                writer.write("get request.getInputStream error:" + ex.getMessage());
                return null;
            }

            if (body.isEmpty()) {
                writer.write("empty request.getInputStream");
                return null;
            }

            JSONObject jsonObj = JSON.parseObject(body);

            int status = (Integer) jsonObj.get("status");

            int saved = 0;
            if (status == 2 || status == 3) //MustSave, Corrupted
            {
                String downloadUri = (String) jsonObj.get("url");

                try {
                    URL url = new URL(downloadUri);
                    java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                    InputStream stream = connection.getInputStream();

                    if (stream == null) {
                        throw new Exception("Stream is null");
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    Date date=new Date();
                    File savedFile = new File("C:/Users/chenyanlong/Desktop/新建文件夹 (2)/NoException/demo10/src/main/resources/static" + "/" +sdf.format(date));
                    try (FileOutputStream out = new FileOutputStream(savedFile)) {
                        int read;
                        final byte[] bytes = new byte[1024*1024];
                        while ((read = stream.read(bytes)) != -1) {
                            out.write(bytes, 0, read);
                        }

                        out.flush();
                    }

                    connection.disconnect();
                    list=importData(savedFile);
                } catch (Exception ex) {
                    saved = 1;
                    System.out.println("导入文件解析失败！");
                    ex.printStackTrace();
                    return null;
                }

            }
            writer.write("{\"error\":" + saved + "}");
        } catch (IOException e) {
            writer.write("{\"error\":\"-1\"}");
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     *
     * 将文件导入数据库
     */
    @RequestMapping(value = "/importDb",method = RequestMethod.POST)
    public void importDb(@RequestParam("file") File file){
        String fileName = file.getName();//获取文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件的后缀名
        if(suffixName.equals("xls")||suffixName.equals("xlsx")) {
            List<List<String>> list = importData(file);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    Mission mission = new Mission();
                    Model model = modelService.queryBymodel_name(list.get(i).get(1));
                    mission.setModelId(String.valueOf(model.getId()));
                    mission.setModel(list.get(i).get(1));
                    mission.setDirector(list.get(i).get(2));
                    mission.setOppositePerson(list.get(i).get(3));
                    mission.setMissionName(list.get(i).get(4));
                    mission.setDescribe(list.get(i).get(5));
                    mission.setStarttime(list.get(i).get(6));
                    mission.setEndtime(list.get(i).get(7));
                    mission.setTimechange(list.get(i).get(8));
                    mission.setChangreason(list.get(i).get(9));
                    mission.setSpeedOfProgress(list.get(i).get(10));
                    mission.setRelatedDocumentLinks(list.get(i).get(11));
                    mission.setAcceptancePassed(list.get(i).get(12));
                    mission.setExcel_name(file.getName());
                    missionService.updateOrinsert(mission);
                }
            } else {
                System.out.println("文件为空");
            }
        }else {
            System.out.println("文件类型错误");
        }
    }
    /**
     *
     *
     * 解析文件
     */
     public  List<List<String>> importData (File file){
         List<List<String>> list = new ArrayList<>();
         FileInputStream fileInputStream= null;
         try {
             fileInputStream = new FileInputStream(file);
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
         XSSFWorkbook wb = null;
         try {
             wb = new XSSFWorkbook(fileInputStream);
         } catch (IOException e) {
             e.printStackTrace();
         }
         XSSFSheet sheet = wb.getSheetAt(0);
         //获取sheet的行数
         int rows = sheet.getPhysicalNumberOfRows();
         for (int i = 0; i < rows; i++) {
             //过滤表头行
             if (i == 0) {
                 continue;
             }
             //获取当前行的数据
             XSSFRow row = sheet.getRow(i);

             List<String> objects = new ArrayList<>();
             for (int j=0;j<13;j++) {
                 XSSFCell cell=row.getCell(j);
                 if(cell==null||cell.getCellType().equals(BLANK)){
                     objects.add(null);
                     continue;
                 }
                 if (cell.getCellType().equals(NUMERIC)) {
                     objects.add(String.valueOf(cell.getNumericCellValue()));
                 }
                 if (cell.getCellType().equals(_NONE)) {
                     objects.add(cell.getStringCellValue());
                 }
                 if (cell.getCellType().equals(BLANK)) {
                     objects.add(cell.getStringCellValue());
                 }
                 if (cell.getCellType().equals(STRING)) {
                     if(j==6||j==7|j==8){
                         String string=cell.getStringCellValue();
                         string=string.substring(0,string.length()-4);
                         string=string.replace("/","-");
                         objects.add(string);
                     }else {objects.add(cell.getStringCellValue());}
                 }
                 if (cell.getCellType().equals(BOOLEAN)) {
                     objects.add(String.valueOf(cell.getBooleanCellValue()));
                 }
                 if (cell.getCellType().equals(ERROR)) {
                     objects.add(String.valueOf(cell.getErrorCellValue()));
                 }
             }
             list.add(objects);
         }
         System.out.println("导入文件解析成功！");
         return list;
     }
    /**
     *
     * 更新数据库
     */
    @RequestMapping(value = "/importExcel",method =RequestMethod.POST)
    public void updateDb(HttpServletRequest request,HttpServletResponse response){
        List<List<String>> list=importExcel(request,response);
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Mission mission = new Mission();
                Model model = modelService.queryBymodel_name(list.get(i).get(1));
                mission.setModelId(String.valueOf(model.getId()));
                mission.setModel(list.get(i).get(1));
                mission.setDirector(list.get(i).get(2));
                mission.setOppositePerson(list.get(i).get(3));
                mission.setMissionName(list.get(i).get(4));
                mission.setDescribe(list.get(i).get(5));
                mission.setStarttime(list.get(i).get(6));
                mission.setEndtime(list.get(i).get(7));
                mission.setTimechange(list.get(i).get(8));
                mission.setChangreason(list.get(i).get(9));
                mission.setSpeedOfProgress(list.get(i).get(10));
                mission.setRelatedDocumentLinks(list.get(i).get(11));
                mission.setAcceptancePassed(list.get(i).get(12));
                mission.setExcel_name(map.get("filename"));
                missionService.updateOrinsert(mission);
            }
        }else{
            System.out.println("更新失败!");
        }
    }
    /**
     * 获取当前时间所在周的周一和周日的日期时间
     * @return
     */
    public  Map<String,String> getWeekDate() {
        Map<String,String> map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayWeek==1){
            dayWeek = 8;
        }

        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        Date mondayDate = cal.getTime();
        String weekBegin = sdf.format(mondayDate);

        cal.add(Calendar.DATE, 4 +cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        String weekEnd = sdf.format(sundayDate);

        map.put("mondayDate", weekBegin);
        map.put("sundayDate", weekEnd);
        return map;
    }


    /**
     *
     * 获取当前周任务
     */
    public List<Mission> NowWeekMission(Integer model_id, String excel_name){
        Map<String,String> map=getWeekDate();
        String date1=map.get("mondayDate");
        String date2=map.get("sundayDate");
        List<Mission> missionList=missionService.queryAllByDate(date1,date2,model_id,excel_name);
        return missionList;
    }
}


