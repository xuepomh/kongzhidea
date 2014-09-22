package com.lyq.annotion;

import org.hibernate.Session;

import com.lyq.model.User;

/**
 * �����ݿ����������
 * 
 * @author Li Yong Qiang
 */
public class Save {
	public static void main(String[] args) {
		Session session = null; // ����Session����
		try {
			// ��ȡSession
			session = HibernateUtil.getSession();
			// ��������
			session.beginTransaction();
			
			User user = new User();
			user.setName("kongzhidea");
			session.save(user);
			// �ύ����
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			// �����ع�����
			session.getTransaction().rollback();
		} finally {
			// �ر�Session����
			HibernateUtil.closeSession(session);
		}
	}
}
