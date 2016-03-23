package com.example.offlinestore.main;

import java.util.List;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.offlinestore.interfaces.CacheModel;
import com.example.offlinestore.interfaces.OnlineListener;
import com.example.offlinestore.utils.CacheUtils;
/**
 * ���߻��������
 * @author Administrator
 *
 */
public class NetRecoveryHandler {

	private Context context;
	private volatile static NetRecoveryHandler NetRecHandlerInstance;
	private CacheUtils cacheUtils;
	private OnlineListener onlineListener;
	private ConnectivityManager manager;
	private NetStatusReceiver netStatusReceiver;
	
	
	/**
	 * ����ģʽ
	 * @param context
	 * @return
	 */
	public static NetRecoveryHandler getInstance(Context context,OnlineListener onlineListener)
	{
		if (NetRecHandlerInstance == null)
		{
			synchronized (NetRecoveryHandler.class)
			{
				if (NetRecHandlerInstance == null)
				{
					NetRecHandlerInstance = new NetRecoveryHandler(context,onlineListener);
					
				}
			}
		}
		return NetRecHandlerInstance;
	}
	
	
	private NetRecoveryHandler(Context context,OnlineListener onlineListener) {
		this.context = context;
		this.onlineListener = onlineListener;
		cacheUtils = CacheUtils.getInstance(context);
		netStatusReceiver = new NetStatusReceiver();
	}
	/**
	 * ��������Ҫ����Ķ�����ӽ���
	 */
	public void addModels(List<CacheModel> cacheModels) {
		cacheUtils.addCacheModels(cacheModels);
	}
	
	/**
	 * ��ʼִ�����߻��湦��
	 */
	public void execute() {
		netStatusReceiver.setOnlineListener(onlineListener);
		manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = manager.getActiveNetworkInfo();
		if (activeInfo != null) {
			if (activeInfo.isAvailable() && activeInfo.isConnected()) {
				//������״̬
				try {
					if(onlineListener != null){
						onlineListener.onWebConnect(cacheUtils.getAllCache());
						cacheUtils.clearAllCache();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				//������״̬
				doWhenOffLine();

			}
		}else {
			doWhenOffLine();
		}
	}
	
	private void doWhenOffLine() {
		// TODO Auto-generated method stub
		Toast.makeText(context, "������", 1).show();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		filter.setPriority(1000);
		context.registerReceiver(netStatusReceiver, filter);
	}
	
	/**
	 * ֹͣ��������״̬
	 * 
	 */
	public void stop() {
		// TODO Auto-generated method stub
		context.unregisterReceiver(netStatusReceiver);
		
	}

}
