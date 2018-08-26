package com.nowcoder.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by tofuc on 2018/6/19.
 */
public interface QiniuService {

    String uploadImage(MultipartFile file);
}
