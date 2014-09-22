package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;

/**
 * ɾ������ ����ɾ���������(ֻ��Ҫ����ID)�ͳ־û����� ���������׳��쳣
 * 
 * @author Li Yong Qiang
 */
public class Delete {
	public static void main(String[] args) {
		Session session = null; // ����Session����
		try {
			// ��ȡSession
			session = HibernateUtil.getSession();
			// ��������
			session.beginTransaction();
			// ���ض���
			Medicine medicine = (Medicine) session.load(Medicine.class,
					new Integer(7));
			// ���ҩƷ��Ϣ
			session.delete(medicine); // Ĭ��ɾ����medicineʵ��û���޸ģ�Medicine [id=7,
										// name=XXx,��������use_identifier_rollback����Ϊtrue
			System.out.println(medicine);
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
