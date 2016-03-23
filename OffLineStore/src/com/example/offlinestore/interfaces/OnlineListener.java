package com.example.offlinestore.interfaces;

import java.util.List;

/**
 *the method onWebConnect() will be invoke  when network connect as a callback. 
 * @author yanyinan
 *
 */
public interface OnlineListener {
	/**
	 * ������ʱ�޷��ύ�������������ύ��ȥ
	 * @param cacheModels  ���ظ��ͻ��˵Ļ�����󼯺�
	 */
	void onWebConnect(List<CacheModel> cacheModels);
}
