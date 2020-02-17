package com.lin.bulter.common.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;

import java.io.File;
import java.io.IOException;

public class FileUploadUtils {

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "91PPY7U14ICwEKYHT_rfe-hNpF9jKthwh9HvuP-I"; //这两个登录七牛 账号里面可以找到
    String SECRET_KEY = "ihvc6wmY7qEk3zPqnbOHGHLm9A7PnNCo08lYZQ8s";

    //要上传的空间
    String bucketname = "lin-pic"; //对应要上传到七牛上 你的那个路径（自己建文件夹 注意设置公开）
    //上传文件的路径
    String FilePath = "e:\\timg.jpg";  //本地要上传文件路径

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //构造一个带指定Zone对象的配置类
    Configuration cfg = new Configuration(Zone.zone0());
    //创建上传对象
    UploadManager uploadManager = new UploadManager(cfg);

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken(){
        return auth.uploadToken(bucketname);
    }

    //普通上传
    public String upload(File file, String key) throws IOException {
        try {
            //调用put方法上传
            Response res = uploadManager.put(file, key, getUpToken());
            //打印返回的信息
            StringMap decode = Json.decode(res.bodyString());
            String key1 = (String) decode.get("key");
            return key1;
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignoree
            }
        }
        return null;
    }

    public static void main(String args[]) throws IOException{
//        new FileUploadUtils().upload();
    }
}
