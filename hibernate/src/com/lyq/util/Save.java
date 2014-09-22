package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;

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
			// ʵ����ҩƷ���󣬲��������Ը�ֵ
			Medicine medicine = new Medicine();
			// saveʱ�� ��ʹ����IDҲ����Ч�� insert into
			// tb_medicine_save (name, price, factoryAdd, description)
			medicine.setName("XX");
			medicine.setPrice(5.00);
			medicine.setFactoryAdd("kk");
			medicine.setDescription("new");
			// ����ҩƷ����   save������setID,�����׳��쳣
			session.save(medicine);
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
