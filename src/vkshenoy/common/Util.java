package vkshenoy.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import vkshenoy.dao.DAOObject;

public class Util {
	
	public static final String myUrl = System.getenv().get("myUrl");
	
	public static final String[] replicas = {System.getenv().get("replica1"), System.getenv().get("replica2")};
	
	private static long TID = Long.parseLong(System.getenv().get("init_tid"));
	
	private static DAOObject daoObj;
	
	public static void init(DAOObject daoObj)
	{
		Util.daoObj = daoObj;
	}
	
	public static boolean canEventBeCreated(String eventName)
	{
		if(daoObj.getEventDao().getByEventName(eventName) != null)	{
			return false;
		}
		
		if(daoObj.getTransactionDao().isTransactionPresent(eventName))	{
			return false;
		}
		
		return true;
	}
	
	
	public static void respond(HttpServletResponse response, String result)
	{
		try {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			out.write(result);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String serialize(Serializable obj)
	{
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(b);
			o.writeObject(obj);
			byte[] arr = b.toByteArray();
			return Base64.encodeBase64String(arr);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object deserialize(String str)
	{
		try {
			byte[] arr = Base64.decodeBase64(str);
			ByteArrayInputStream b = new ByteArrayInputStream(arr);
			ObjectInputStream o = new ObjectInputStream(b);
			return o.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized static String updateReplica(String url, String operation, String eventName, Serializable obj)
	{
		HttpResponse response = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair(Constants.TRANSACTION_ID, ""+TID));
		params.add(new BasicNameValuePair(Constants.OPERATION, operation));
		params.add(new BasicNameValuePair(Constants.EVENT_NAME, eventName));
		if(obj != null)	{
			params.add(new BasicNameValuePair(Constants.DATA, serialize(obj)));
		}	else	{
			params.add(new BasicNameValuePair(Constants.DATA, ""));
		}
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    return rd.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return Constants.FAILURE;
		}
	}
	
	private static void signalReplica(String url, long tid, String signal)
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair(Constants.TRANSACTION_ID, ""+tid));
		params.add(new BasicNameValuePair(Constants.OPERATION, signal));
		params.add(new BasicNameValuePair(Constants.EVENT_NAME, ""));
		params.add(new BasicNameValuePair(Constants.DATA, ""));
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpclient.execute(httppost);
			/*BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    return rd.readLine();*/
		} catch (Exception e) {
			e.printStackTrace();
			//return null;
		}
	}
	
	public static boolean updateReplicas2Phase(String operation, String eventName, Serializable obj)
	{
		String res1 = updateReplica(replicas[0], operation, eventName, obj);
		String res2 = updateReplica(replicas[1], operation, eventName, obj);
		long tid = TID;
		TID += (replicas.length + 1);
		if(Constants.SUCCESS.equals(res1) && Constants.SUCCESS.equals(res2))	{
			signalReplica(replicas[0], tid, Constants.COMMIT);
			signalReplica(replicas[1], tid, Constants.COMMIT);
			return true;
		}	else	{
			signalReplica(replicas[0], tid, Constants.ABORT);
			signalReplica(replicas[1], tid, Constants.ABORT);
			return false;
		}
	}

}
