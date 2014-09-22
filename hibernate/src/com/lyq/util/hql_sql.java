package com.lyq.util;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lyq.model.Medicine;

/**
 * get()方法加载数据
 * 
 * @author Li Yong Qiang
 */
public class hql_sql {
	public static void main(String[] args) {
		// select 时候不能再配置文件中加入 自动建表 ,否则删除数据
		Session session = null; // 声明Session对象
		try {
			// 获取Session
			session = HibernateUtil.getSession();
			// 开启事物
			session.beginTransaction();
			// from 实体类
			// select
			String hql = "from  Medicine";
			Query query = session.createQuery(hql);
			List<Medicine> list = query.list();
			for (Medicine medicine : list) {
				// 输出药品信息
				System.out.println("药品ID：" + medicine.getId());
				System.out.println("药品名称：" + medicine.getName());
				System.out.println("药品价格：" + medicine.getPrice());
				System.out.println("出厂地址：" + medicine.getFactoryAdd());
				System.out.println("药品描述：" + medicine.getDescription());
			}
			System.out.println("....................");

			// 条件查询
			hql = "from  Medicine  where id=1";
			// from Medicine as m where m.id=1 //这样写也行
			query = session.createQuery(hql);
			list = query.list();
			for (Medicine medicine : list) {
				// 输出药品信息
				System.out.println("药品ID：" + medicine.getId());
				System.out.println("药品名称：" + medicine.getName());
				System.out.println("药品价格：" + medicine.getPrice());
				System.out.println("出厂地址：" + medicine.getFactoryAdd());
				System.out.println("药品描述：" + medicine.getDescription());
			}
			System.out.println("....................");

			// 条件查询
			hql = "from  Medicine  where id=1";
			// from Medicine as m where m.id=1 //这样写也行
			query = session.createQuery(hql);
			list = query.list();
			for (Medicine medicine : list) {
				// 输出药品信息
				System.out.println("药品ID：" + medicine.getId());
				System.out.println("药品名称：" + medicine.getName());
				System.out.println("药品价格：" + medicine.getPrice());
				System.out.println("出厂地址：" + medicine.getFactoryAdd());
				System.out.println("药品描述：" + medicine.getDescription());
			}
			System.out.println("....................");

			// 取一个对象 如果不唯一则抛出异常,不存在则为null
			hql = "from  Medicine  where id=?";
			query = session.createQuery(hql);
			query = query.setParameter(0, 1);
			Medicine medicineu = (Medicine) query.uniqueResult();
			System.out.println(medicineu);
			System.out.println("....................");

			// 动态赋值 自定义参数
			hql = "from  Medicine  where id=:id";
			query = session.createQuery(hql);
			query = query.setParameter("id", 1);
			list = query.list();
			for (Medicine medicine : list) {
				// 输出药品信息
				System.out.println("药品ID：" + medicine.getId());
				System.out.println("药品名称：" + medicine.getName());
				System.out.println("药品价格：" + medicine.getPrice());
				System.out.println("出厂地址：" + medicine.getFactoryAdd());
				System.out.println("药品描述：" + medicine.getDescription());
			}
			System.out.println("....................");

			// 排序
			hql = "from  Medicine  order by id";
			query = session.createQuery(hql);
			list = query.list();
			for (Medicine medicine : list) {
				// 输出药品信息
				System.out.println("药品ID：" + medicine.getId());
				System.out.println("药品名称：" + medicine.getName());
				System.out.println("药品价格：" + medicine.getPrice());
				System.out.println("出厂地址：" + medicine.getFactoryAdd());
				System.out.println("药品描述：" + medicine.getDescription());
			}
			System.out.println("....................");

			// 分组操作
			hql = "select count(*),id,name from  Medicine  ";
			query = session.createQuery(hql);
			List<Object[]> li = query.list();
			for (Object[] obj : li) {
				// 输出药品信息
				System.out.println("count:" + obj[0]);
				System.out.println("药品ID：" + obj[1]);
				System.out.println("药品名称：" + obj[2]);
			}
			System.out.println("....................");

			// update
			hql = "update Medicine  set name='kkklo' where id=1";
			query = session.createQuery(hql);
			System.out.println(query.executeUpdate());
			System.out.println("....................");

			// 分页
			hql = "from  Medicine  ";
			query = session.createQuery(hql);
			query.setFirstResult(2);// 从第几个开始 limit n,m
			query.setMaxResults(3);// 总条数 limit m
			list = query.list();
			for (Medicine medicine : list) {
				// 输出药品信息
				System.out.println("药品ID：" + medicine.getId());
				System.out.println("药品名称：" + medicine.getName());
				System.out.println("药品价格：" + medicine.getPrice());
				System.out.println("出厂地址：" + medicine.getFactoryAdd());
				System.out.println("药品描述：" + medicine.getDescription());
			}
			System.out.println("....................");

			// select字段 需要1个多字段的构造方法，最好再加上一个空的构造方法
			hql = "select new Medicine(id,name,price,factoryAdd,description) from  Medicine  ";
			query = session.createQuery(hql);
			list = query.list();
			for (Medicine medicine : list) {
				// 输出药品信息
				System.out.println("药品ID：" + medicine.getId());
				System.out.println("药品名称：" + medicine.getName());
				System.out.println("药品价格：" + medicine.getPrice());
				System.out.println("出厂地址：" + medicine.getFactoryAdd());
				System.out.println("药品描述：" + medicine.getDescription());
			}
			System.out.println("....................");

			// select Map或者List 不使用别名则自动使用数字作为key
			hql = "select new Map(id ,name ) from  Medicine  ";
			query = session.createQuery(hql);
			List<Map> list2 = query.list();
			for (Map m : list2) {
				System.out.println(m.get("0"));
				System.out.println(m.get("1"));
			}
			System.out.println("....................");

			// select Map或者List
			hql = "select new Map(id as id,name as name) from  Medicine  ";
			query = session.createQuery(hql);
			list2 = query.list();
			for (Map m : list2) {
				System.out.println(m.get("id"));
				System.out.println(m.get("name"));
			}
			System.out.println("....................");
			
			// 提交事物
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			// 出错将回滚事物
			session.getTransaction().rollback();
		} finally {
			// 关闭Session对象
			HibernateUtil.closeSession(session);
		}
	}
}
