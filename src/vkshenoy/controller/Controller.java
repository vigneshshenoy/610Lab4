package vkshenoy.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vkshenoy.common.Util;
import vkshenoy.dao.DAOObject;
import vkshenoy.data.Photo;


public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private DAOObject daoObj;

	public void init() throws ServletException {
		
		daoObj = new DAOObject();
		Action.init();
		Action.add(new CreateEvent(daoObj));
		Action.add(new UploadPhoto(daoObj));
		Action.add(new ImageAction(daoObj));
		Action.add(new EventVote(daoObj));
		Action.add(new EventLogin(daoObj));
		Action.add(new ViewEvent(daoObj));
		Action.add(new RefreshEvent(daoObj));
		Action.add(new SynchronizeReplica(daoObj));
		
		daoObj.getTransactionDao().deleteTransactions();
		Util.init(daoObj);
		
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextPage = performTheAction(request, response);
        sendToNextPage(nextPage,request,response);
    }
    
    private String performTheAction(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session     = request.getSession(true);
        String      servletPath = request.getServletPath();
        //User        user = (User) session.getAttribute("user");
        String      action = getActionName(servletPath);
        
      	// Let the logged in user run his chosen action
		return Action.perform(action, request, response);
    }
    
    /*
     * If nextPage is null, send back 404
     * If nextPage ends with ".do", redirect to this page.
     * If nextPage ends with ".jsp", dispatch (forward) to the page (the view)
     *    This is the common case
     */
    private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	if (nextPage == null) {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND,request.getServletPath());
    		return;
    	}
    	
    	if (nextPage.endsWith(".do")) {
			response.sendRedirect(nextPage);
			return;
    	}
    	
    	if(nextPage.equals("image"))	{
    		HttpSession session = request.getSession();
    		ArrayList<Photo> photos = null;
    		Photo p = (Photo)request.getAttribute("photo");
    		
    		if (p == null) {
    			response.sendError(HttpServletResponse.SC_NOT_FOUND);
    			return;
    		}
    		response.setContentType(p.getContentType());
    		ServletOutputStream out = response.getOutputStream();
    		out.write(p.getImageData());
    		return;
    	}
    	
    	if (nextPage.endsWith(".jsp")) {
	   		RequestDispatcher d = request.getRequestDispatcher(nextPage);
	   		d.forward(request, response);
    		//response.sendRedirect(nextPage);
	   		//request.getSession().setAttribute("errors", null);
	   		return;
    	}
    	
    	//throw new ServletException(Controller.class.getName()+".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
    }

	/*
	 * Returns the path component after the last slash removing any "extension"
	 * if present.
	 */
    private String getActionName(String path) {
    	// We're guaranteed that the path will start with a slash
        int slash = path.lastIndexOf('/');
        return path.substring(slash+1);
    }
}
