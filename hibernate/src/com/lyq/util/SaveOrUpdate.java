package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;

/**
 * ��ʱ������save�����������udate,updateʱ��������������׳��쳣
 * 
 * @author Li Yong Qiang
 */
public class SaveOrUpdate {
	public static void main(String[] args) {
		Session session = null; // ����Session����
		try {
			// ��ȡSession
			session = HibernateUtil.getSession();
			// ��������
			session.beginTransaction();
			// ʵ����ҩƷ���󣬲��������Ը�ֵ
			Medicine medicine = new Medicine();

			// idĬ����null��ʱ���ʾ����ʱ����ִ��save����;��Ҫ��ID��unsaved-valueֵΪ0�ű�ʾID��0��ʱ������ʱ����
			// medicine.setId(0);
			medicine.setName("XX");
			medicine.setPrice(6.00);
			medicine.setFactoryAdd("kk");
			medicine.setDescription("new");
			// ID�������򱣴棬������update �����������ж�
			session.saveOrUpdate(medicine);
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
