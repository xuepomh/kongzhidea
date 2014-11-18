package com.ebay.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/test")
public class FileController {

	@RequestMapping("show")
	public String show() {
		return "test/show";
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file,
			@RequestParam("file2") MultipartFile file2) {
		System.out.println(name);
		if (!file.isEmpty()) {
			System.out.println(file.getOriginalFilename() + "..."
					+ file.getSize());
			System.out.println(file2.getOriginalFilename() + "..."
					+ file2.getSize());
		}
		return "ok";
	}

	@RequestMapping(value = "upload2", method = RequestMethod.POST)
	@ResponseBody
	public Object upload2(HttpServletRequest request,
			HttpServletResponse response) {
		// 转型为MultipartHttpRequest：
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得文件：
		List<MultipartFile> files = multipartRequest.getFiles("file");
		// MultipartFile file = multipartRequest.getFile("file");
		for (MultipartFile file : files) {
			System.out.println(file.getOriginalFilename() + ".."
					+ file.getSize());
		}
		return "ok";
	}

}
