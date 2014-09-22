package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * update()方法更新数据
 * @author Li Yong Qiang
 */
public class Update_manual {
	public static void main(String[] args) {
		Session session = null;			//声明Session对象
		try {
			//获取Session
			session = HibernateUtil.getSession();
			//开启事物
			session.beginTransaction();
			//手动构造的detached状态的药品对象
			Medicine medicine = new Medicine();
			medicine.setId(9);				//药品ID  不存在则抛出异常
			medicine.setName("xx01");	//药品名称
			medicine.setDescription("dest");
			session.update(medicine);		//更新药品信息
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
