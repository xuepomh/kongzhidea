package com.lyq.util;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lyq.model.Medicine;

/**
 * get()������������
 * 
 * @author Li Yong Qiang
 */
public class hql_sql {
	public static void main(String[] args) {
		// select ʱ�����������ļ��м��� �Զ����� ,����ɾ������
		Session session = null; // ����Session����
		try {
			// ��ȡSession
			session = HibernateUtil.getSession();
			// ��������
			session.beginTransaction();
			// from ʵ����
			// select
			String hql = "from  Medicine";
			Query query = session.createQuery(hql);
			List<Medicine> list = query.list();
			for (Medicine medicine : list) {
				// ���ҩƷ��Ϣ
				System.out.println("ҩƷID��" + medicine.getId());
				System.out.println("ҩƷ���ƣ�" + medicine.getName());
				System.out.println("ҩƷ�۸�" + medicine.getPrice());
				System.out.println("������ַ��" + medicine.getFactoryAdd());
				System.out.println("ҩƷ������" + medicine.getDescription());
			}
			System.out.println("....................");

			// ������ѯ
			hql = "from  Medicine  where id=1";
			// from Medicine as m where m.id=1 //����дҲ��
			query = session.createQuery(hql);
			list = query.list();
			for (Medicine medicine : list) {
				// ���ҩƷ��Ϣ
				System.out.println("ҩƷID��" + medicine.getId());
				System.out.println("ҩƷ���ƣ�" + medicine.getName());
				System.out.println("ҩƷ�۸�" + medicine.getPrice());
				System.out.println("������ַ��" + medicine.getFactoryAdd());
				System.out.println("ҩƷ������" + medicine.getDescription());
			}
			System.out.println("....................");

			// ������ѯ
			hql = "from  Medicine  where id=1";
			// from Medicine as m where m.id=1 //����дҲ��
			query = session.createQuery(hql);
			list = query.list();
			for (Medicine medicine : list) {
				// ���ҩƷ��Ϣ
				System.out.println("ҩƷID��" + medicine.getId());
				System.out.println("ҩƷ���ƣ�" + medicine.getName());
				System.out.println("ҩƷ�۸�" + medicine.getPrice());
				System.out.println("������ַ��" + medicine.getFactoryAdd());
				System.out.println("ҩƷ������" + medicine.getDescription());
			}
			System.out.println("....................");

			// ȡһ������ �����Ψһ���׳��쳣,��������Ϊnull
			hql = "from  Medicine  where id=?";
			query = session.createQuery(hql);
			query = query.setParameter(0, 1);
			Medicine medicineu = (Medicine) query.uniqueResult();
			System.out.println(medicineu);
			System.out.println("....................");

			// ��̬��ֵ �Զ������
			hql = "from  Medicine  where id=:id";
			query = session.createQuery(hql);
			query = query.setParameter("id", 1);
			list = query.list();
			for (Medicine medicine : list) {
				// ���ҩƷ��Ϣ
				System.out.println("ҩƷID��" + medicine.getId());
				System.out.println("ҩƷ���ƣ�" + medicine.getName());
				System.out.println("ҩƷ�۸�" + medicine.getPrice());
				System.out.println("������ַ��" + medicine.getFactoryAdd());
				System.out.println("ҩƷ������" + medicine.getDescription());
			}
			System.out.println("....................");

			// ����
			hql = "from  Medicine  order by id";
			query = session.createQuery(hql);
			list = query.list();
			for (Medicine medicine : list) {
				// ���ҩƷ��Ϣ
				System.out.println("ҩƷID��" + medicine.getId());
				System.out.println("ҩƷ���ƣ�" + medicine.getName());
				System.out.println("ҩƷ�۸�" + medicine.getPrice());
				System.out.println("������ַ��" + medicine.getFactoryAdd());
				System.out.println("ҩƷ������" + medicine.getDescription());
			}
			System.out.println("....................");

			// �������
			hql = "select count(*),id,name from  Medicine  ";
			query = session.createQuery(hql);
			List<Object[]> li = query.list();
			for (Object[] obj : li) {
				// ���ҩƷ��Ϣ
				System.out.println("count:" + obj[0]);
				System.out.println("ҩƷID��" + obj[1]);
				System.out.println("ҩƷ���ƣ�" + obj[2]);
			}
			System.out.println("....................");

			// update
			hql = "update Medicine  set name='kkklo' where id=1";
			query = session.createQuery(hql);
			System.out.println(query.executeUpdate());
			System.out.println("....................");

			// ��ҳ
			hql = "from  Medicine  ";
			query = session.createQuery(hql);
			query.setFirstResult(2);// �ӵڼ�����ʼ limit n,m
			query.setMaxResults(3);// ������ limit m
			list = query.list();
			for (Medicine medicine : list) {
				// ���ҩƷ��Ϣ
				System.out.println("ҩƷID��" + medicine.getId());
				System.out.println("ҩƷ���ƣ�" + medicine.getName());
				System.out.println("ҩƷ�۸�" + medicine.getPrice());
				System.out.println("������ַ��" + medicine.getFactoryAdd());
				System.out.println("ҩƷ������" + medicine.getDescription());
			}
			System.out.println("....................");

			// select�ֶ� ��Ҫ1�����ֶεĹ��췽��������ټ���һ���յĹ��췽��
			hql = "select new Medicine(id,name,price,factoryAdd,description) from  Medicine  ";
			query = session.createQuery(hql);
			list = query.list();
			for (Medicine medicine : list) {
				// ���ҩƷ��Ϣ
				System.out.println("ҩƷID��" + medicine.getId());
				System.out.println("ҩƷ���ƣ�" + medicine.getName());
				System.out.println("ҩƷ�۸�" + medicine.getPrice());
				System.out.println("������ַ��" + medicine.getFactoryAdd());
				System.out.println("ҩƷ������" + medicine.getDescription());
			}
			System.out.println("....................");

			// select Map����List ��ʹ�ñ������Զ�ʹ��������Ϊkey
			hql = "select new Map(id ,name ) from  Medicine  ";
			query = session.createQuery(hql);
			List<Map> list2 = query.list();
			for (Map m : list2) {
				System.out.println(m.get("0"));
				System.out.println(m.get("1"));
			}
			System.out.println("....................");

			// select Map����List
			hql = "select new Map(id as id,name as name) from  Medicine  ";
			query = session.createQuery(hql);
			list2 = query.list();
			for (Map m : list2) {
				System.out.println(m.get("id"));
				System.out.println(m.get("name"));
			}
			System.out.println("....................");
			
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
