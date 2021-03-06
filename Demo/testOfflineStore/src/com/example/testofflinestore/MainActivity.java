package com.example.testofflinestore;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.offlinestore.interfaces.CacheModel;
import com.example.offlinestore.interfaces.OnlineListener;
import com.example.offlinestore.main.NetRecoveryHandler;
import com.example.testofflinestore.R;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		One one = new One();
		Two two = new Two();
		Three three = new Three();
		List<CacheModel> cacheModels = new ArrayList<CacheModel>();
		cacheModels.add(one);
		cacheModels.add(two);
		cacheModels.add(three);
		
		/***************************************************** 
		 * first,create a OnlineListener instance,override the method
		 * onWebConnect(),write down the how you want to do with the datas
		 * that will be stored when offline or directly do what you want when online.                                         
		 *                                                     
		 * ******************************************************
		 */
		OnlineListener onlineListener = new OnlineListener() {
			
			@Override
			public void onWebConnect(List<CacheModel> cacheModels) {
				for (int i = 0; i < cacheModels.size(); i++) {
					switch (cacheModels.get(i).getMark()) {
					case "one":
						//post cacheModel which return mark "one" to server
						break;
					case "two":
						//post cacheModel which return mark "two" to server
						break;
					case "three":
						//post cacheModel which return mark "three" to server
						break;
					default:
						break;
					}
				}
				
			}
		};
		/********************************************************************
		 * Secondly,create a NetRecoveryHandler instance.
		 * Pass onlineListener and Context as parameters in constructor.
		 * 
		 * ********************************************************************
		 */
		NetRecoveryHandler netRecoveryHandler = NetRecoveryHandler.getInstance(this, onlineListener);
		/********************************************************************
		 * thirdly,invoke method execute() through NetRecoveryHandler instance
		 * Pass cacheModels which hold all models that you want to post as parameter in execute().
		 * 
		 * ********************************************************************
		 */
		netRecoveryHandler.execute(cacheModels);
		
	}
	
	/**
	 * Three classes which implements CacheModel are examples of model which we want to post
	 *  in our test
	 * @author Administrator
	 *
	 */
	static class One implements CacheModel{

		@Override
		public String getMark() {
			// TODO Auto-generated method stub
			return "one";
		}
		
	}
	
	static class Two implements CacheModel{

		@Override
		public String getMark() {
			// TODO Auto-generated method stub
			return "two";
		}
		
	}
	static class Three implements CacheModel{

		@Override
		public String getMark() {
			// TODO Auto-generated method stub
			return "three";
		}
		
	}
		
	
}
