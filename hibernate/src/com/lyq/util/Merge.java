package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;

/**
 * merge ��ʱ����ֱ��save������������������update(���������ͻ����в�һ��ʱ���update)����������save,
 * 
 * ��������һ�ݣ����ض��������,�뻺���еĶ�����ͬһ��ʵ��
 * 
 * @author Li Yong Qiang
 */
public class Merge {
	public static void main(String[] args) {
		Session session = null; // ����Session����
		try {
			// ��ȡSession
			session = HibernateUtil.getSession();
			// ��������
			session.beginTransaction();
			// ʵ����ҩƷ���󣬲��������Ը�ֵ
			Medicine medicine = new Medicine();

			medicine.setName("XXx");
			medicine.setPrice(7.00);
			medicine.setFactoryAdd("kk");
			medicine.setDescription("new");
			Medicine me = (Medicine) session.merge(medicine);
			System.out.println(medicine.getId() + ".." + me.getId());//null..9
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
