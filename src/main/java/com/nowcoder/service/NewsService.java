package com.nowcoder.service;

import com.nowcoder.model.News;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by tofuc on 2018/6/13.
 */
public interface NewsService {
    int addNews(News news);
    List<News> selectByUserIdAndOffset(int userId, int offset, int limit);

    String saveImage(MultipartFile file)throws IOException;

    News getNewsById(int id);

    int updateCommentCount(int id,int commentCount);

    int updateLikeCount(int id,int likeCount);
}
