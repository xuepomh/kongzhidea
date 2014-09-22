package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * load()������������
 * @author Li Yong Qiang
 */
public class Select_load {
	public static void main(String[] args) {
		//����loadʱ�򣬲�����������sql�����в�ѯ  ֻ�������ö����ʱ��Żᷢ��sql���
		Session session = null;			//����Session����
		try {
			//��ȡSession
			session = HibernateUtil.getSession();
			//��������
			session.beginTransaction();
			//��ѯҩƷIDΪ1��ҩƷ��Ϣ   �����������׳��쳣
			Medicine medicine = (Medicine)session.load(Medicine.class, new Integer(1));
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
