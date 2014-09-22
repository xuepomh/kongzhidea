package com.hoo.veiw;

/**
 * 实体类字段加入@ExcelField
 * 在想要导出的实体类的字段加上@ExcelField注解，其中各个属性含义为value字段值、title字段标题、type为字段导入导出
 * 、align为对齐方式、sort为是否排序。
 * 
 * @author Administrator
 * 
 */
public class Test {

	@ExcelField(title = "ID", align = 0, sort = 0, value = "id")
	private Long id;

	@ExcelField(title = "消息", align = 0, sort = 0, value = "msg")
	private String msg;

	@ExcelField(title = "详细信息", align = 0, sort = 0, value = "detail")
	private String detail;

	public Test(Long id, String msg, String detail) {
		super();
		this.id = id;
		this.msg = msg;
		this.detail = detail;
	}

	public Test() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
