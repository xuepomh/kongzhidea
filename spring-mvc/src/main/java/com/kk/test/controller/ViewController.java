package com.kk.test.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hoo.veiw.DefaultExcelView;
import com.hoo.veiw.Test;
import com.hoo.veiw.ViewExcel;
import com.hoo.veiw.ViewPDF;

/**
 * 生成excel或PDF类型试图 根据参数进行数据组装，并跳转到相应的视图页面 View Controller Bean<br>
 * 
 * @author Tony Lin Created on 2008-10-22
 * @version Version 1.0
 */

@Controller
@RequestMapping("/view")
public class ViewController {

	/**
	 * 下载excel文档
	 * 
	 * view/?exceltest
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "exceltest", method = RequestMethod.GET)
	public ModelAndView viewExcel(HttpServletRequest request,
			HttpServletResponse response) {
		List list = new ArrayList();
		Map model = new HashMap();
		list.add("test1");
		list.add("test2");
		model.put("list", list);
		ViewExcel viewExcel = new ViewExcel();

		return new ModelAndView(viewExcel, model);
	}

	/**
	 * 将类中字段都打出来
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "exceltest2", method = RequestMethod.GET)
	public ModelAndView viewExcel2(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		List<Test> testList = new ArrayList<Test>();
		Test test = new Test(1l, "msg", "detail");
		Test test2 = new Test(2l, "msg2", "detail2");
		Test test3 = new Test(3l, "msg3", "detail3");
		testList.add(test);
		testList.add(test2);
		testList.add(test3);
		model.addAttribute("testList", testList);
		return new ModelAndView(new DefaultExcelView("导出Test信息", "testList",
				Test.class));
	}

	/**
	 * 阅读pdf view/?pdftest
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "pdftest", method = RequestMethod.GET)
	public ModelAndView viewPDF(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = new ArrayList();
		Map model = new HashMap();
		list.add("test1");
		list.add("test2");
		model.put("list", list);
		ViewPDF viewPDF = new ViewPDF();
		return new ModelAndView(viewPDF, model);
	}
}