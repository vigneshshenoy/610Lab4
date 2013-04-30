package vkshenoy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import vkshenoy.dao.DAOObject;
import vkshenoy.dao.PhotoDAO;
import vkshenoy.data.Photo;
import vkshenoy.form.IdForm;

public class ImageAction extends Action {
	
	FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);

	DAOObject daoObj;
	
	public ImageAction(DAOObject daoObj)	{
		this.daoObj = daoObj;
	}
	
	@Override
	public String getName() {
		return "image.do";
	}

	@Override
	public String perform(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		
		try {
			IdForm form = formBeanFactory.create(request);
			request.setAttribute("form",form);

	        if (!form.isPresent()) {
	        	errors.add("Invalid image ID");
	            return "error.jsp";
	        }
	        
	        errors.addAll(form.getValidationErrors());
	        if(errors.size() != 0)	{
	        	errors.add("Invalid image ID");
	        	return "error.jsp";
	        }
	        
	        int photoId = form.getIdAsInt();
	        PhotoDAO photoDao = daoObj.getPhotoDao();
	        Photo photo = photoDao.getPhotoByID(photoId);
	        
	        request.setAttribute("photo", photo);
	        
	        return "image";
			
		} catch (FormBeanException e) {
			errors.add("Internal error occurred.");
			return "error.jsp";
		}
	}

}
