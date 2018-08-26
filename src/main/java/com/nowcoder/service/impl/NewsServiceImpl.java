package com.nowcoder.service.impl;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.model.News;
import com.nowcoder.service.NewsService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by tofuc on 2018/6/13.
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    NewsDao newsDao;
    public int addNews(News news){
        return newsDao.addNews(news);
    }


    public List<News> selectByUserIdAndOffset(int userId, int offset, int limit){
        return newsDao.selectByUserIdAndOffset(userId,offset,limit);
    }

    @Override
    public String saveImage(MultipartFile file) throws IOException{
        String filename=file.getOriginalFilename();
        if(filename.indexOf('.')<0)return null;
        String ext=filename.substring(filename.indexOf('.')+1);
        if(!ToutiaoUtil.isAllowedFile(ext)){
            return null;
        }
        String newName= UUID.randomUUID().toString().replace("-","")+"."+ext;
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+newName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+newName;
    }

    @Override
    public News getNewsById(int id) {
        return newsDao.selectById(id);
    }

    @Override
    public int updateLikeCount(int id, int likeCount) {
        return newsDao.updateLikeCount(id,likeCount);
    }

    @Override
    public int updateCommentCount(int id, int commentCount) {
        return newsDao.updateCommentCount(id,commentCount);
    }
}
