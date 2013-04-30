package vkshenoy.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import vkshenoy.data.Photo;

public class PhotoDAO extends BaseDAO {
	
	public PhotoDAO()	{
		super();
	}
	
	public Photo getPhotoByID(int id)	{
		Session session = beginTransaction();
		Criteria criteria = session.createCriteria(Photo.class);
		criteria.add(Restrictions.eq("photoId", id));
		List <Photo> photos = criteria.list();
		Photo photo = null;
		if(photos != null && photos.size() > 0)	{
			photo = photos.get(0);
			photo.getEvent().load();
		}
		commitTransaction(session);
		return photo;
	}

	public void deletePhoto(Photo photo)	{
		Session session = beginTransaction();
		session.delete(photo);
		commitTransaction(session);
	}
	
}
