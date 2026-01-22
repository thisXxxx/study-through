package team.weilai.studythrough.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.ability.listener.StudentListener;
import team.weilai.studythrough.pojo.main.User;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.AdminService;
import team.weilai.studythrough.service.UserService;
import team.weilai.studythrough.util.ApiAuthAlgorithm;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author gwj
 * @create 2024/10/9 21:42
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Resource
    private UserService userService;


    @Override
    public void getStuSample(HttpServletResponse response) throws IOException {
        //创建表
        List<List<String>> list = ListUtils.newArrayList();
        //创建表头
        List<String> head0 = ListUtils.newArrayList();
        List<String> head1 = ListUtils.newArrayList();
        List<String> head2 = ListUtils.newArrayList();
        List<String> head3 = ListUtils.newArrayList();
        head0.add("姓名");
        head1.add("用户名");
        head2.add("班级");
        head3.add("性别");

        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);

        //创建表内容
        List<List<String>> dataList = ListUtils.newArrayList();
        List<String> data = ListUtils.newArrayList();
        data.add("例：张三");
        data.add("20111514101");
        data.add("计科111");
        data.add("男");
        dataList.add(data);
        String fileName = "student-template.xlsx";
        //设置内容的颜色
        WriteCellStyle style = new WriteCellStyle();
        WriteFont writeFont = new WriteFont();
        writeFont.setColor(IndexedColors.RED.getIndex());
        style.setWriteFont(writeFont);
        HorizontalCellStyleStrategy cellStyleStrategy = new HorizontalCellStyleStrategy(new WriteCellStyle() , style);
        //设置response信息
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            EasyExcel.write(response.getOutputStream())
                    .registerWriteHandler(cellStyleStrategy)
                    .registerWriteHandler(new SimpleColumnWidthStyleStrategy(15))
                    .head(list)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("模板")
                    .doWrite(dataList);
        } catch (Exception e) {
            //重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            //输出错误信息
            Result<Object> fail = Result.fail("下载文件失败");
            response.getWriter().println(JSON.toJSONString(fail));
        }
    }

    @Override
    public Result<Void> uploadStu(MultipartFile file, Integer type) {
        try {
            EasyExcel.read(file.getInputStream(),
                    User.class,
                    new StudentListener(userService,type)).sheet().doRead();
        } catch (Exception e) {
            log.error("导入数据异常，{}",e.getMessage());
            return Result.fail("导入数据失败");
        }
        return Result.ok();
    }

}
