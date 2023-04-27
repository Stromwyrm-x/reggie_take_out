//package com.weng.controller;
//
//import com.weng.common.Result;
//import jakarta.servlet.ServletOutputStream;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.UUID;
//
//@RestController
//@Slf4j
//@RequestMapping("/common")
//public class UploadController
//{
//    @Value("${reggie.path}")
//    private String basePath;
//
//    @PostMapping("/upload")
//    public Result<String> upload(MultipartFile file) throws IOException
//    {
//        log.info("上传的文件为:{}",file.toString());
//        String uuid = UUID.randomUUID().toString();
//        String originalFilename = file.getOriginalFilename();
//        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//
//        String filename = uuid + suffix;
//
//        File dir = new File(basePath);
//        log.info("basePath:{}",basePath);
//        if (!dir.exists())
//        {
//            dir.mkdirs();
//        }
//
//        file.transferTo(new File(basePath + filename));
//
//        return Result.success(filename);
//    }
//
//    //文件下载
//    @GetMapping("/download")
//    public void download(String name, HttpServletResponse response){
//        try {
//            //输入流，通过输入流读取文件内容
//            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
//            //输出流，通过输出流将文件写回浏览器，在浏览器中展示图片
//            ServletOutputStream outputStream = response.getOutputStream();
//
//            int len=0;
//            byte[] bytes = new byte[1024];
//            while ((len=fileInputStream.read(bytes))!=-1){
//                outputStream.write(bytes,0,len);
//                outputStream.flush();
//            }
//            outputStream.close();
//            fileInputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
