package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * get()������������
 * @author Li Yong Qiang
 */
public class Select_get {
	public static void main(String[] args) {
		//select ʱ�����������ļ��м��� �Զ����� ,����ɾ������
		Session session = null;			//����Session����
		try {
			//��ȡSession
			session = HibernateUtil.getSession();
			//��������
			session.beginTransaction();
			//��ѯҩƷIDΪ1��ҩƷ��Ϣ
			Medicine medicine = (Medicine)session.get(Medicine.class, new Integer(1));
			System.out.println(medicine);
			//���ҩƷ��Ϣ
			System.out.println("ҩƷID��" + medicine.getId());
			System.out.println("ҩƷ���ƣ�" + medicine.getName());
			System.out.println("ҩƷ�۸�" + medicine.getPrice());
			System.out.println("������ַ��" + medicine.getFactoryAdd());
			System.out.println("ҩƷ������" + medicine.getDescription());
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
