package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * �޸�ҩƷ
 * @author Li Yong Qiang
 */
public class Update_auto {
	public static void main(String[] args) {
		//�Զ�����
		Session session = null;			//����Session����
		try {
			//��ȡSession
			session = HibernateUtil.getSession();
			//��������
			session.beginTransaction();
			//����ҩƷ����
			Medicine medicine = (Medicine)session.get(Medicine.class, new Integer(1));
			medicine.setName("xxkl");	//�޸�ҩƷ����
			medicine.setPrice(10.05);		//�޸�ҩƷ�۸�
			//�ύ����
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			//�����ع�����
			session.getTransaction().rollback();
		}finally{
			//�ر�Session����
			HibernateUtil.closeSession(session);
		}
	}
}
