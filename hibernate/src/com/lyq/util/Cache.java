package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * 两次查询同一对象
 * @author Li Yong Qiang
 */
public class Cache {
	public static void main(String[] args) {
		//一级缓存
		Session session = null;			//声明Session对象
		try {
			//获取Session
			session = HibernateUtil.getSession();
			//开启事物
			session.beginTransaction();
			System.out.println("第一次查询：");
			//查询药品
			Medicine medicine1 = (Medicine)session.get(Medicine.class, new Integer(1));
			//输出药品名称
			System.out.println("药品名称：" + medicine1.getName());
			
			//第二次查询时候  没有发送sql语句
			System.out.println("\n第二次查询：");
			//查询药品
			Medicine medicine2 = (Medicine)session.get(Medicine.class, new Integer(1));
			//输出药品名称
			System.out.println("药品名称：" + medicine2.getName());
			
			System.out.println(medicine1 == medicine2);//true  是一个实例
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
