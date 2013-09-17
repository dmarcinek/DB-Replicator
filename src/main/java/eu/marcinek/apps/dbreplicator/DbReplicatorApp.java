package eu.marcinek.apps.dbreplicator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DbReplicatorApp {

	public static void main(String... args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		DbReplicator dbReplicator = context.getBean(DbReplicator.class);
		dbReplicator.replicate();
	}

}
