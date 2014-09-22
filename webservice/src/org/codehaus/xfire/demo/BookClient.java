package org.codehaus.xfire.demo;

import java.net.MalformedURLException;
import java.util.Map;

import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.demo.handlers.OutHeaderHandler;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;

/**
 * @author <a href="mailto:nathanyp@hotmail.com">Nathan Peles</a>
 * @author <a href="mailto:tsztelak@gmail.com">Tomasz Sztelak</a>
 */
public class BookClient {
	public static void main(String args[]) {
		String serviceURL = "http://localhost:8080/webservice/services/BookService";
		Service serviceModel = new ObjectServiceFactory()
				.create(BookService.class);

		XFireProxyFactory serviceFactory = new XFireProxyFactory();

		try {
			BookService service = (BookService) serviceFactory.create(
					serviceModel, serviceURL);
			Client client = Client.getInstance(service);
			client.addOutHandler(new OutHeaderHandler());
			// disable timeout
			client.setProperty(CommonsHttpMessageSender.HTTP_TIMEOUT, "0");

			Book[] books = service.getBooks();
			Map booksMap = service.getBooksMap();
			System.out.print("Received map with " + booksMap.size()
					+ " book(s) \n");
			System.out.println("BOOKS:");

			for (int i = 0; i < books.length; i++) {
				System.out.println(books[i].getTitle());
			}
			// Throw Exception
			service.findBook("");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (BookException e) {
			System.out.print(e.getFaultInfo().getDetailMessage());
		}
	}
}
