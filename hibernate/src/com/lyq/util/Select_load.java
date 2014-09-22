package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * load()方法加载数据
 * @author Li Yong Qiang
 */
public class Select_load {
	public static void main(String[] args) {
		//调用load时候，不会立即发送sql语句进行查询  只有在引用对象的时候才会发送sql语句
		Session session = null;			//声明Session对象
		try {
			//获取Session
			session = HibernateUtil.getSession();
			//开启事物
			session.beginTransaction();
			//查询药品ID为1的药品信息   若不存在则抛出异常
			Medicine medicine = (Medicine)session.load(Medicine.class, new Integer(1));
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
