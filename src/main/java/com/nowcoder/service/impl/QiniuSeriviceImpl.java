package com.nowcoder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.service.QiniuService;
import com.nowcoder.util.ToutiaoUtil;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Created by tofuc on 2018/6/19.
 */
@Service
public class QiniuSeriviceImpl implements QiniuService {

    private static Logger logger= LoggerFactory.getLogger(QiniuSeriviceImpl.class);

    private static String AK="5xkk3BBQ8ZyIujf_a2JyDciSwmhxT5bKmm8wTUIt";
    private static String SK="vWclk2jBIRq7R8GKZNjws9J86ELjYsypP2rIyPrj";

    private static String bucketname="images";
    private Auth auth=Auth.create(AK,SK);
    private UploadManager uploadManager=new UploadManager();
    private static String QINIU_IMAGE_DOMAIN="http://pagnne7rb.bkt.clouddn.com/";

    public String getUpToken(){
        return auth.uploadToken(bucketname);
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try{
            int dotPos=file.getOriginalFilename().lastIndexOf(".");
            if(dotPos<0)return null;
            String fileExt=file.getOriginalFilename().substring(dotPos+1);
            if(!ToutiaoUtil.isAllowedFile(fileExt))return null;
            String fileName= UUID.randomUUID().toString().replace("-","")+"."+fileExt;
            Response response=uploadManager.put(file.getBytes(),fileName,getUpToken());
            if(response.isOK()&&response.isJson()){
                return QINIU_IMAGE_DOMAIN+ JSONObject.parseObject(response.bodyString()).get("key");
            }else{
                logger.error("七牛异常:"+response.bodyString());
                return null;
            }
        }catch(Exception e){
            logger.error("七牛异常:"+e.getMessage());
            return null;
        }
    }
}
