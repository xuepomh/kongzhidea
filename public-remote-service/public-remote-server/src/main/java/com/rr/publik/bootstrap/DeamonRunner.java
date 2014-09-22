package com.rr.publik.bootstrap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author po.xu
 */
public class DeamonRunner {
	private static final Log LOG = LogFactory.getLog(DeamonRunner.class);

	/**
	 * 启动服务的入口程序
	 * 
	 * @param args
	 *            需要传入两个参数，<br>
	 *            args[0]是一个XML文件路径，如
	 *            "src/main/resources/beans/proxy-service.xml"
	 *            args[1] 是beanName，如serverStart
	 */
	public static void main(String[] args) {
		// PropertyConfigurator.configure("log4j.properties");
		if (args.length < 2) {
			LOG.error("Usage:config beanName!");
			return;
		}

		// let the server process shut down in case of any exception raised in
		// any threads. the process will not stop unless an uncaught-exception
		// handler is set. This can allow discovering of severe server crashes
		// such as OOM and null-pointers.
		Thread.setDefaultUncaughtExceptionHandler( // NL
		new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				LOG.error(e.getMessage(), e);
			}
		});

		String conf = args[0];
		String beanName = args[1];

		AppMain app = null;

		try {
			BeanFactory factory = new FileSystemXmlApplicationContext(conf);
			app = (AppMain) factory.getBean(beanName);
		} catch (Throwable e) {
			LOG.fatal("Error from Spring during initialization: "
					+ e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		app.doMain(args);

	}
}
