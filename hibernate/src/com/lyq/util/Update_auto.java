package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * 修改药品
 * @author Li Yong Qiang
 */
public class Update_auto {
	public static void main(String[] args) {
		//自动更新
		Session session = null;			//声明Session对象
		try {
			//获取Session
			session = HibernateUtil.getSession();
			//开启事物
			session.beginTransaction();
			//加载药品对象
			Medicine medicine = (Medicine)session.get(Medicine.class, new Integer(1));
			medicine.setName("xxkl");	//修改药品名称
			medicine.setPrice(10.05);		//修改药品价格
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
