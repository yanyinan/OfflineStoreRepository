package com.example.offlinestore.interfaces;

import java.util.List;

/**
 * ��������ʱ�Ļص��ӿ�
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
