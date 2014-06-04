package org.rautasydan.mtx.android;

import org.json.JSONException;
import org.json.JSONObject;
import org.rautasydan.mtx.common.MTX;
import org.rautasydan.mtx.common.MTXItem;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MTXPurchaseActivity extends Activity {
	String tag = "PurchaseActivity";
	public static boolean isActive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().hasExtra("buy")) {
			Bundle buyIntentBundle = getIntent().getBundleExtra("buy");
			PendingIntent pendingIntent = buyIntentBundle
					.getParcelable("BUY_INTENT");
			try {
				startIntentSenderForResult(pendingIntent.getIntentSender(),
						1001, new Intent(), Integer.valueOf(0),
						Integer.valueOf(0), Integer.valueOf(0));
			} catch (SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			Log.e(tag, "No buyIntentBundle passed, not buying anything >:(");
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		isActive = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1001) {
			String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");

			if (resultCode == RESULT_OK) {
				try {
					JSONObject jo = new JSONObject(purchaseData);
					String sku = jo.getString("productId");
					int amount = 1;
					float price = 0.00f;
					Toast.makeText(getApplicationContext(),
							"You have bought the " + sku + ".",
							Toast.LENGTH_LONG).show();
					MTXItem boughtItem = new MTXItem(sku, amount, price);
					MTX.receivePurchase(boughtItem);
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(),
							"Failed to parse purchase data.", Toast.LENGTH_LONG)
							.show();
					e.printStackTrace();
				}
			}

		}
		finish();
	}

}
