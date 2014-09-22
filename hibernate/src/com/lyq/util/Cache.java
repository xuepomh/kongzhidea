package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;
/**
 * ���β�ѯͬһ����
 * @author Li Yong Qiang
 */
public class Cache {
	public static void main(String[] args) {
		//һ������
		Session session = null;			//����Session����
		try {
			//��ȡSession
			session = HibernateUtil.getSession();
			//��������
			session.beginTransaction();
			System.out.println("��һ�β�ѯ��");
			//��ѯҩƷ
			Medicine medicine1 = (Medicine)session.get(Medicine.class, new Integer(1));
			//���ҩƷ����
			System.out.println("ҩƷ���ƣ�" + medicine1.getName());
			
			//�ڶ��β�ѯʱ��  û�з���sql���
			System.out.println("\n�ڶ��β�ѯ��");
			//��ѯҩƷ
			Medicine medicine2 = (Medicine)session.get(Medicine.class, new Integer(1));
			//���ҩƷ����
			System.out.println("ҩƷ���ƣ�" + medicine2.getName());
			
			System.out.println(medicine1 == medicine2);//true  ��һ��ʵ��
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
