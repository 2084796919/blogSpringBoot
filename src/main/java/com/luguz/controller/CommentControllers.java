package com.luguz.controller;



import com.luguz.aspect.MyLog;
import com.luguz.entity.Blog;
import com.luguz.entity.Comment;
import com.luguz.service.IBlogService;
import com.luguz.service.ICommentService;
import com.luguz.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
public class CommentControllers {

    @Autowired
    private ICommentService iCommentService;


    @Autowired
    private IBlogService iBlogService;

    @MyLog
    @GetMapping("/comments/{blId}")
    public String comments(@PathVariable Long blId, Model model) {
        Blog blog = iBlogService.findFullById(blId);
        if (!blog.getPublished()) {
            throw new RuntimeException("无效资源！");
        }
        model.addAttribute("blog", blog);
        return "blog :: commentList";
    }

    @MyLog
    @PostMapping("/comments")
    public String postComments(Comment comment, HttpServletRequest request) {
        //保存评论
        if (comment.getParentId() <= -1)
            comment.setParentId(null);
        String ipAddress = IpUtil.getIpAddress(request);
        comment.setIpAddress(ipAddress);
        iCommentService.saveOrUpdate(comment);
        return "redirect:/comments/" + comment.getBlId();
    }

}
