package com.hoo.veiw;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * 生成excel视图，可用excel工具打开或者保存 由ViewController的return new ModelAndView(viewExcel,
 * model)生成
 * 
 * @author Tony Lin Created on 2008-10-22
 * @version Version 1.0
 */
public class ViewExcel extends AbstractExcelView {

	@Override
	public void buildExcelDocument(Map model, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HSSFSheet sheet = workbook.createSheet("list");
		sheet.setDefaultColumnWidth((short) 12);

		HSSFCell cell = getCell(sheet, 0, 0);
		setText(cell, "Spring Excel test");

		HSSFCellStyle dateStyle = workbook.createCellStyle();
		// dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("mm/dd/yyyy"));
		cell = getCell(sheet, 1, 0);
		cell.setCellValue("日期：2008-10-23");
		// cell.setCellStyle(dateStyle);
		getCell(sheet, 2, 0).setCellValue("测试1");
		getCell(sheet, 2, 1).setCellValue("测试2");

		HSSFRow sheetRow = sheet.createRow(3);
		for (short i = 0; i < 10; i++) {
			sheetRow.createCell(i).setCellValue(i * 10);
		}

		List list = (List) model.get("list");
		HSSFRow sheetRow2 = sheet.createRow(4);
		for (short i = 0; i < list.size(); i++) {
			sheetRow2.createCell(i).setCellValue(list.get(i).toString());
		}
	}

}