package com.jeeplus.modules.cv.web;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.modules.sys.entity.SysUpload;
import com.jeeplus.modules.sys.service.SysUploadService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "${adminPath}/api/file")
public class FileOptController {

    @Autowired
    private SysUploadService sysUploadService;


    @RequestMapping(value = "/uploadimg")
    public Map<String,String> uploadimg(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String,String> map = new HashMap<>();

        if (!file.isEmpty()) {
            //文件保存路径
            LocalDate localDate = LocalDate.now();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int dayOfMonth = localDate.getDayOfMonth();
            String tempPath = year+"/"+month+"/"+dayOfMonth +"/";

            String realPath = Global.getConfig("images.real.path")+"/work-images/"+tempPath;  //图片存放路径
            FileUtils.createDirectory(realPath);
            String imgName = file.getOriginalFilename();

            String filePath = realPath +"/"+  imgName;

            File newFile = FileUtils.getAvailableFile(filePath,0);
            file.transferTo(newFile);

            String weburl = Global.getConfig("images.web.url")+"/work-images/" + tempPath + newFile.getName();

            //存入表中的 路径
            String imgRealPath = "/work-images/" + tempPath + newFile.getName();

            //表操作(冗余操作)
            SysUpload sysUpload = new SysUpload();
            sysUpload.setCategory("work-images");
            sysUpload.setOriginName(imgName);
            sysUpload.setPath(imgRealPath);
            sysUpload.setUploadBy(UserUtils.getUser().getId());
            sysUpload.setUploadDate(new Date());
            sysUploadService.save(sysUpload);

            map.put("code","200");
            map.put("src",weburl);
            map.put("imgRealPath",imgRealPath);
            map.put("id",sysUpload.getId());
        }else{
            map.put("code","401");
            map.put("msg","没有文件上传");
        }

        return map;
    }



    @RequestMapping(value = "/deleteimg")
    public Map<String,String> deleteimg(@RequestParam("imgRealPath") String imgRealPath,
                                        @RequestParam("fileId") String fileId,
                                        HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String,String> map = new HashMap<>();


        String imgPath = Global.getConfig("images.real.path") + imgRealPath;

        //1.删除磁盘上文件
        boolean b = FileUtils.deleteFile(imgPath);
        //2删除表记录
        sysUploadService.delete(new SysUpload(fileId));

        if(b){
            map.put("result","success");
        }else{
            map.put("result","false");
        }

        return map;
    }
}
