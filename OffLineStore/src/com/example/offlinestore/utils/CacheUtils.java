package com.example.offlinestore.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.offlinestore.interfaces.CacheModel;

/**
 * ֻҪ������ʵ��CacheModel�Ķ����Ի�������
 * ��SharePreference���洢һЩ�����л��Ļ������key��1���𣩣���Ϊһ�ֻ��湤�ߣ������ƶ��еķ�ʽ�洢��
 * ��������һ���������ɾ��һ��������󣨶�����ʽ��������ĳ���������Ϊnull,��ȡ���л�����󣬻�ȡ��������������
 * @author yyn
 *
 */
public class CacheUtils {

	
	private Context context;
	private volatile static CacheUtils CacheUtilInstance;
	
	/**
	 * ����ģʽ
	 * @param context
	 * @return
	 */
	public static CacheUtils getInstance(Context context)
	{
		if (CacheUtilInstance == null)
		{
			synchronized (CacheUtils.class)
			{
				if (CacheUtilInstance == null)
				{
					CacheUtilInstance = new CacheUtils(context);
					
				}
			}
		}
		return CacheUtilInstance;
	}
	
	
	
	private CacheUtils(Context context) {
		
		//this.message = message;
		this.context = context;
		
		
	}

	/**
     * �Զ�����ʽ�����µĻ������
     * @param cacheModel �������
     * 
     */
    public void addCacheModel(CacheModel cacheModel){
    	
    	int count = getCacheCount();
    	//addSmsCount(++count);
    	saveCacheModel(cacheModel, ++count);
    }
	
    /**
     * ��ӻ�����󼯺�
     * @param cacheModels
     */
    public void addCacheModels(List<CacheModel> cacheModels) {
    	for (int i = 0; i < cacheModels.size(); i++) {
			addCacheModel(cacheModels.get(i));
		}
	}
	
	/**
	 * ���л�����
	 * @param message
	 * @return
	 * @throws IOException
	 */
	private String serialize(CacheModel cacheModel) throws IOException {  
       
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(  
                byteArrayOutputStream);  
        objectOutputStream.writeObject(cacheModel);  
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");  
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");  
        objectOutputStream.close();  
        byteArrayOutputStream.close();  
       
        return serStr;  
    }  
  
    /** 
     * �����л����� ,���ַ���ת��Ϊ����Ķ���
     *  
     * @param str ��Ҫת��Ϊ������ַ���
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     */  
	private  CacheModel deSerialization(String str) throws IOException,  
            ClassNotFoundException {  
        if(str == null || str == ""){
        	return null;
        }
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");  
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(  
                redStr.getBytes("ISO-8859-1"));  
        ObjectInputStream objectInputStream = new ObjectInputStream(  
                byteArrayInputStream);  
        CacheModel message = (CacheModel) objectInputStream.readObject();  
        objectInputStream.close();  
        byteArrayInputStream.close();  
        
        return message;  
    }  
    /**
     * ���滺�����,key��1����
     * @param strObject ���л���ת��ΪString�Ķ��Ŷ���
     * @param num  �������
     */
    private  void  saveCacheModel(CacheModel model,int num) {
    	String strObject;
		try {
			String i = String.valueOf(num);
			strObject = serialize(model);
			SharedPreferences sp = context.getSharedPreferences("Cache", 0);  
		    Editor edit = sp.edit();  
		    edit.putString(i, strObject);  
		    edit.commit(); 
		    
	    	setCacheCount(num);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }  
    
    /**
     * ���ö�Ӧ�����Ļ������Ϊ��
     * @param index
     */
    public synchronized void setCacheModelToNull(int index) {
		// TODO Auto-generated method stub
    	String strObject;
		try {
			String i = String.valueOf(index);
			strObject = serialize(null);
			SharedPreferences sp = context.getSharedPreferences("Cache", 0);  
		    Editor edit = sp.edit();  
		    edit.putString(i, strObject);  
		    edit.commit(); 
		    
	    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
  
    
    /**
     * �Զ�����ʽɾ��ĳ������������ں���Ķ�������ǰ�Ʋ�λ��
     * @throws Exception 
     */
    public synchronized void deleteCacheByQueue(int index) throws Exception {
    	int sum = getCacheCount();
    	if(index >  sum){
    		throw new Exception("index is greater than sum");
    		
    	}
    	
    	if(sum == index){
    		setCacheCount(--sum);
    		return;
    	}
    	for(int i = index;i < sum;i++ ){
    		CacheModel cacheModel = getCacheModel(i+1);
    		saveCacheModel(cacheModel, i);
    	}
    		
//    		int count = getSmsCount();
//    	    	setSmsCount(--count);
	}
    /**
     * ������һ��������󣨳��У�
     * @throws Exception 
     */
    public synchronized CacheModel pollCache() throws Exception {
		// TODO Auto-generated method stub
    	CacheModel cacheModel = getCacheModel(1);
    	deleteCacheByQueue(1);
    	return cacheModel;
	}
    
    /**
     * ���ĳ���������
     * @param index �����������
     * @return
     * @throws Exception 
     */
    public CacheModel getCacheModel(int index) throws Exception  {  
        SharedPreferences sp = context.getSharedPreferences("Cache", 0); 
        CacheModel cacheModel;
        int sum = getCacheCount();
        if(index > sum){
        	throw new Exception("index is greater than sum");
        }
        String i = String.valueOf(index);
		try {
			cacheModel = deSerialization(sp.getString(i, null));
			return cacheModel; 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}
    }  
    
    /**
     * ���ȫ���洢�Ķ���
     * @return
     * @throws Exception 
     */
    public List<CacheModel> getAllCache() throws Exception {
		List<CacheModel> cacheModels = new ArrayList<CacheModel>();
		for(int i = 1;i <= getCacheCount();i++){
			CacheModel cacheModel = getCacheModel(i);
			cacheModels.add(cacheModel);
		}
    	return cacheModels;
	}
    
    /**
     * ������л���
     * @throws Exception 
     */
    public synchronized void clearAllCache()  {
		
    	
    	SharedPreferences sp = context.getSharedPreferences("Cache", 0);  
	    Editor edit = sp.edit();
	    edit.clear().commit();
	    setCacheCount(0);
	}
    
    
    /**
     * ���û������ĸ���
     * @param count
     */
    private synchronized void setCacheCount(int count) {
    	SharedPreferences sp = context.getSharedPreferences("Cache", 0);  
	    Editor edit = sp.edit();  
	    edit.putInt("cacheCount", count);
	    edit.commit();  
	    

    }
    
    /**
     * ��ô洢�Ļ��������ܸ���
     * @return
     */
   public  int getCacheCount() {
	   SharedPreferences sp = context.getSharedPreferences("Cache", 0); 
	   int count = sp.getInt("cacheCount", 0);
	   return count;
	}
   
}
