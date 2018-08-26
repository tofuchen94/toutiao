package com.nowcoder.controller;

import com.nowcoder.model.Comment;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.*;
import com.nowcoder.util.EntityType;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tofuc on 2018/6/18.
 */
@Controller
public class NewsController {

   private static Logger logger=LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path="/news/{newsId}",method = RequestMethod.GET)
    public String newsDetail(Model model, @PathVariable("newsId") int newsId){
        try{
            News news=newsService.getNewsById(newsId);
            if(news!=null){

                int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
                if(localUserId!=0){
                    model.addAttribute("like",likeService.getLikeStatus(localUserId,EntityType.NEWS,newsId));
                }else{
                    model.addAttribute("like",0);
                }

                List<Comment> comments=commentService.getCommentsByEntity(newsId, EntityType.NEWS);
                List<ViewObject> vos=new ArrayList<>();
                for(Comment comment:comments){
                    ViewObject vo=new ViewObject();
                    vo.set("comment",comment);
                    vo.set("user",userService.getUserById(comment.getUserId()));
                    vos.add(vo);
                }
                model.addAttribute("vos",vos);
            }
            model.addAttribute("news",news);
            model.addAttribute("owner",userService.getUserById(news.getUserId()));
        }catch(Exception e){
            logger.error("获取资讯明细失败:"+e.getMessage());
        }
        return "newsDetail";
    }

    @RequestMapping(value="/uploadImage",method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
            //String fileUrl=newsService.saveImage(file);
            String fileUrl=qiniuService.uploadImage(file);
            if(fileUrl==null){
                return ToutiaoUtil.getJSONString(1,"上传失败");
            }
            return ToutiaoUtil.getJSONString(0,fileUrl);
        }catch(Exception e){
            logger.error("上传图片失败:"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败");
        }

    }

    @RequestMapping(value="/image",method = RequestMethod.GET)
    @ResponseBody
    public void getImage(@RequestParam("name") String name,
                           HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+name)),response.getOutputStream());
        }catch(Exception e){
            logger.error("获取图片失败:"+name+e.getMessage());
        }
    }

    @RequestMapping(value="/user/addNews",method = RequestMethod.POST)
    @ResponseBody
    public String addNews(News news){
        try {
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                news.setUserId(ToutiaoUtil.ANONYMITY_ID);
            }
            news.setCreatedDate(new Date());
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯错误"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布失败");
        }
    }

    @RequestMapping(value="/addComment",method = RequestMethod.POST)
    public String addComment(@RequestParam("newsId") int newsId,@RequestParam("content")String content){
        try {
            Comment comment=new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.NEWS);
            comment.setStatus(0);
            comment.setUserId(hostHolder.getUser().getId());
            commentService.addComment(comment);

            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateCommentCount(newsId,count);
        } catch (Exception e) {
            logger.error("添加评论错误"+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }



}
