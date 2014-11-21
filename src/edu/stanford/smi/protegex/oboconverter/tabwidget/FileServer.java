package edu.stanford.smi.protegex.oboconverter.tabwidget;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
//import org.eclipse.jetty.embedded.DumpServlet;

/** Simple Jetty FileServer.
 * This is a simple example of Jetty configured as a FileServer.
 */

@SuppressWarnings("serial")
class DumpServlet extends HttpServlet
{
	public DumpServlet()
	{
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>DumpServlet</h1><pre>");
		response.getWriter().println("requestURI=" + request.getRequestURI());
		response.getWriter().println("contextPath=" + request.getContextPath());
		response.getWriter().println("servletPath=" + request.getServletPath());
		response.getWriter().println("pathInfo=" + request.getPathInfo());
		response.getWriter().println("session=" + request.getSession(true).getId());
		response.getWriter().println("</pre>");
	}
}
public class FileServer {

	public static void main(String[] args) throws Exception {

		Server server = new Server(8081);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		//server.setHandler(context);
		context.addServlet(new ServletHolder(new DumpServlet()), "/swrl");

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		String file = new File(".").getAbsolutePath();
		file = file.substring(0, file.length()-1) + "war/";

		resource_handler.setResourceBase(file);
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, context, new DefaultHandler() });
		server.setHandler(handlers);

		//		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		//		context.setContextPath("/");
		//		server.setHandler(context);
		//		String file = new File(".").getAbsolutePath();
		//		file = file.substring(0, file.length()-1) + "war/*";
		//		System.out.println("Servlet: "+file);
		//
		//		ServletHolder holder = context.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/tmp/*");
		//		holder.setInitParameter("resourceBase", "/tmp");
		//		holder.setInitParameter("pathInfoOnly", "true");
		//context.addServlet(new ServletHolder(new DumpServlet()), "/*");
		//context.addServlet(new ServletHolder(new SWRLEditorServiceImpl()), "/swrlgwt/swrlEditor");

		//		Server server = new Server(8080);
		//		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		//		context.setContextPath("/");
		//		server.setHandler(context);
		//		String file = new File(".").getAbsolutePath()+ "/war/";
		//
		//		ServletHolder holder = context.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, file);//"/tmp/*");
		//		holder.setInitParameter("resourceBase", "/war");
		//		holder.setInitParameter("pathInfoOnly", "true");
		//
		context.addServlet(new ServletHolder(new DumpServlet()), "/*");

		server.start();
		server.join();
	}
	//	public static void main(String[] args) throws Exception {
	//		Server server = new Server(8080);
	//		ResourceHandler resource_handler = new ResourceHandler();
	//		resource_handler.setDirectoriesListed(true);
	//		resource_handler.setWelcomeFiles(new String[] { "index.html" });
	//		resource_handler.setResourceBase(".");
	//		HandlerList handlers = new HandlerList();
	//		handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
	//		server.setHandler(handlers);
	//		server.start();
	//		server.join();
	//	}
}