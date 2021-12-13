package com.cloud.product.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.cloud.product.entity.ProductCommentEntity;
import org.springframework.stereotype.Controller;

import com.cloud.product.service.ProductCommentService;


/**
 *
 * @ClassName: ProductCommentController
 * @Description: TODO(这里用一句话描述这个类的作用)商品评论表
 * @author wjg
 * @date 2021-08-05
 */
@Controller
@RequestMapping(value ="/productCommentEntity")
public class ProductCommentController  {
@Autowired
private ProductCommentService productCommentService;

}