package com.chat.controller;

import com.chat.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class AvatarController {         //头像上传

    @Value("${uploadPath}")
    private String uploadPath;  //文件存在的绝对路径

    @Value("${folderName}")
    private String folderName;  //文件存在所在的文件夹

    @PostMapping("/uploadAvatar")   //接收图片上传后处理并返回存放的相对路径
    public StringUtil uploadAvatar(@RequestParam("userImgUrl") MultipartFile file) throws IOException {

        Map<String, Object> result = new HashMap<>();   //编写一个Map集合便于存放对象

        String fileName = file.getOriginalFilename();   //获取上传文件的全名/全称

        if (fileName != null && !"".equalsIgnoreCase(fileName.trim()) && isImageFile(fileName)) {

            String fName = UUID.randomUUID() + fileName;

            File filePath = new File(uploadPath + fName);   //构建操作的文件

            if (!filePath.exists()) {   //判断路径是否存在

                filePath.mkdirs();  //创建文件夹
            }

            String proPath = System.getProperty("user.dir");    //获取项目的根路径

            System.out.println(proPath);

            file.transferTo(filePath);  //把文件写入硬盘

            result.put("url", "/" + folderName + fName);    //将拼接的图片地址存入Map集合

            return new StringUtil(200, "上传成功", result);  //把拼接的图片地址返回

        } else {

            return new StringUtil(400, "上传失败,格式错误", null);

        }
    }

    private Boolean isImageFile(String fName) {     //判断文件上传的格式 是否符合图片格式
        String[] img_type = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        if (fName == null) {
            return false;
        }
        fName = fName.toLowerCase();
        fName = fName.toLowerCase();
        for (String type : img_type) {
            if (fName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }

}
