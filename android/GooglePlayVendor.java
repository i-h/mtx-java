package org.rautasydan.mtx.android;

import org.rautasydan.mtx.common.MTX;
import org.rautasydan.mtx.common.MTXItem;
import org.rautasydan.mtx.common.IMTXVendor;

import com.android.vending.billing.IInAppBillingService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public abstract class GooglePlayVendor implements IMTXVendor {
	final protected Class<? extends Activity> storeActivity;
	final String tag = getClass().getSimpleName();
	final int IAPversion = 3;
	static IInAppBillingService iabService;
	ServiceConnection iabServConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(tag, "iabService disconnected!");
			iabService = null;

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(tag, "iabService connected!");
			iabService = IInAppBillingService.Stub.asInterface(service);
		}
	};

	public GooglePlayVendor(Class<? extends Activity> storeActivityClass) {
		storeActivity = storeActivityClass;
		// Bind to IInAppBillingService
		Intent billingIntent = new Intent(
				"com.android.vending.billing.InAppBillingService.BIND");
		getContext().bindService(billingIntent, iabServConn,
				Context.BIND_AUTO_CREATE);
	}
	@Override
	public boolean buyItem(MTXItem itm) {
		try {
			Bundle buyIntentBundle = iabService.getBuyIntent(IAPversion,
					getContext().getPackageName(), itm.getId(), "inapp", "");
			Intent i = new Intent(getContext(), MTXPurchaseActivity.class);
			i.putExtra("buy", buyIntentBundle);
			getContext().startActivity(i);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public void getProductList() {
		Bundle querySkus = new Bundle();
		querySkus.putStringArrayList("ITEM_ID_LIST", MTX.getSkuList());

		MTXTask task = new MTXTask(getContext(), this);
		task.execute(querySkus);
	}

	@Override
	public void destroy() {
		if (iabService != null) {
			getContext().unbindService(iabServConn);
		}

	}

	public void displayStore() {
		Intent i = new Intent(getContext(), this.getClass());
		getContext().startActivity(i);
	}

	public void waitUntilConnected(float timeout) {
		if (iabService != null) {
			return;
		}
		float wait = 100.0f;
		float elapsed = 0;
		float maxWait;
		if (timeout <= 0) {
			maxWait = 1000;
		} else {
			maxWait = timeout;
		}
		Log.w(tag, "Starting wait for iabService...");
		while (iabService == null || elapsed < maxWait) {
			Log.i(tag, "Waiting..." + elapsed + "/" + maxWait);
			elapsed += wait;
			try {
				Thread.sleep((long) wait);
			} catch (InterruptedException e) {
				Log.e(tag, "Wait interrupted: " + e.toString());
			}
		}
		Log.w(tag, "Wait for iabService over.");
	}

	public IInAppBillingService getIabService(boolean wait) {
		if (wait) {
			waitUntilConnected(-1);
		}
		Log.i(tag, "iabService = " + iabService);
		return iabService;
	}

	protected abstract Context getContext();

	protected abstract int getStoreLayoutID();
}
