package com.example.offlinestore.main;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.offlinestore.interfaces.CacheModel;
import com.example.offlinestore.interfaces.OnlineListener;
import com.example.offlinestore.utils.CacheUtils;

public class NetStatusReceiver extends BroadcastReceiver {
	private CacheUtils cacheUtils;
	private Context context;
	private List<CacheModel> cacheModels;
	private OnlineListener onlineListener;
	/**
	 * �����ʶλ��true��ʾ�����磻false��ʾû������
	 * ��ֹ�������Ѿ����ϵ�����¶�ε���onReceive()
	 * �����Ǹ��������õģ���ʹ��static
	 */
	private static boolean isNetAvailable = false;
	private ConnectivityManager manager;
	

	
	
	public NetStatusReceiver() {
		super();
		
	}
	
	public void setOnlineListener(OnlineListener onlineListener) {
		this.onlineListener = onlineListener;
	}

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO �Զ����ɵķ������
	//	this.context = cont;
		cacheUtils = CacheUtils.getInstance(context);
// �ƶ���������
		manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
 
//		NetworkInfo mobileInfo = manager
//				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//	
//		// wifi����
//		NetworkInfo wifiInfo = manager
//				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo activeInfo = manager.getActiveNetworkInfo();

		// ��������ʱ��
		if (activeInfo != null) {
			if (activeInfo.isAvailable() && activeInfo.isConnected()) {
				//��������������磬����ִ��
Toast.makeText(context, "����ָ�", 1).show();
				if (isNetAvailable) {
					return;
				}
				isNetAvailable = true;
		// ���洢��cacheUtils�����
				
//Toast.makeText(context, "����������", 1).show();
//int count = cacheUtils.getCacheCount();
				if (cacheUtils.getCacheCount() > 0) {
					
					if(onlineListener != null){
						try {
							onlineListener.onWebConnect(cacheUtils.getAllCache());
							cacheUtils.clearAllCache();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				}

				
			}
		}else {
			//û����ʱ���ñ�ʶλΪfalse
			isNetAvailable = false;
		}
	}

}
