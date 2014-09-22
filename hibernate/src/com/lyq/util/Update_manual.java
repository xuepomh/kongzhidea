package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * update()������������
 * @author Li Yong Qiang
 */
public class Update_manual {
	public static void main(String[] args) {
		Session session = null;			//����Session����
		try {
			//��ȡSession
			session = HibernateUtil.getSession();
			//��������
			session.beginTransaction();
			//�ֶ������detached״̬��ҩƷ����
			Medicine medicine = new Medicine();
			medicine.setId(9);				//ҩƷID  ���������׳��쳣
			medicine.setName("xx01");	//ҩƷ����
			medicine.setDescription("dest");
			session.update(medicine);		//����ҩƷ��Ϣ
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
