package com.renren.shopping.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.renren.shopping.conf.CommentConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.model.Comment;
import com.renren.shopping.model.Travel;
import com.renren.shopping.services.CommentService;
import com.renren.shopping.services.TravelService;

/**
 * 买手详情页
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("travel")
public class UserTravelController {
	@Autowired
	TravelService travelService;

	@Autowired
	CommentService commentService;

	public static Log logger = LogFactory.getLog(UserTravelController.class);

	/**
	 * 评论页面
	 * 
	 * @param request
	 * @param response
	 * @param tid
	 * @param type
	 *            评价得分
	 * @return
	 */
	@RequestMapping("comment/{tid}")
	public ModelAndView showComment(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("tid") long travelId,
			@RequestParam(value = "score", required = false) Integer score,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (page == null || page == 0) {
			page = 1;
		}
		if (limit == null) {
			limit = CommentConf.COMMENT_LOAD_DEFAULTNUM;
		}
		// 当前登陆用户,0表示未登录
		int currentUser = 0;
		ModelAndView mav = new ModelAndView("travel/comment");
		// 行程信息
		Travel travel = travelService.getTravel(travelId);
		mav.addObject("travel", travel);
		// 评论信息
		List<Comment> comments = new ArrayList<Comment>();
		if (score == null || score == 0) {
			comments = commentService.getCommentListDesc(travelId, (page - 1)
					* limit, limit);
		} else {
			comments = commentService.getCommentListByTypeDesc(travelId, score,
					(page - 1) * limit, limit);
		}
		JSONObject obj = new JSONObject();
		commentService.supplyJsonObject(obj, currentUser, comments);
		mav.addObject("comments", obj.optJSONArray(CommentConf.COMMENTKEY));
		return mav;
	}

	/**
	 * 发表评论 ，参数和comment类中属性相对应 TODO 用户登录验证
	 * 
	 * curl url -d "userId=1&travelId=1&content=评论7&score=3&toId=6"
	 * 
	 * @param comment
	 * @return
	 */
	@RequestMapping(value = "publishComment", method = RequestMethod.POST)
	@ResponseBody
	public Object publishComment(Comment comment) {
		if (comment.getScore() > 3 || comment.getScore() <= 0) {
			return ResultConf.PARAM_EXCEPTION;
		}
		commentService.addComment(comment);
		return ResultConf.getSuccessResult(comment.getId());
	}
}
