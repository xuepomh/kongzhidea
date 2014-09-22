package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * get()方法加载数据
 * @author Li Yong Qiang
 */
public class Select_get {
	public static void main(String[] args) {
		//select 时候不能再配置文件中加入 自动建表 ,否则删除数据
		Session session = null;			//声明Session对象
		try {
			//获取Session
			session = HibernateUtil.getSession();
			//开启事物
			session.beginTransaction();
			//查询药品ID为1的药品信息
			Medicine medicine = (Medicine)session.get(Medicine.class, new Integer(1));
			System.out.println(medicine);
			//输出药品信息
			System.out.println("药品ID：" + medicine.getId());
			System.out.println("药品名称：" + medicine.getName());
			System.out.println("药品价格：" + medicine.getPrice());
			System.out.println("出厂地址：" + medicine.getFactoryAdd());
			System.out.println("药品描述：" + medicine.getDescription());
			//提交事物
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			//出错将回滚事物
			session.getTransaction().rollback();
		}finally{
			//关闭Session对象
			HibernateUtil.closeSession(session);
		}
	}
}
