package org.rautasydan.mtx.android;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.rautasydan.mtx.common.MTX;
import org.rautasydan.mtx.common.MTXItem;
import org.rautasydan.mtx.common.MTXItemTable;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

public class MTXTask extends AsyncTask<Bundle, Integer, Bundle> {
	GooglePlayVendor vendor;
	Context context;
	String tag = "MTXTask";
	int timeout = 20;

	public MTXTask(Context ctx, GooglePlayVendor service) {
		super();
		vendor = service;
		context = ctx;
	}

	@Override
	protected Bundle doInBackground(Bundle... querySkus) {
		Bundle skuDetails= new Bundle();
		skuDetails.putInt("RESPONSE_CODE", 1);
		try {
			skuDetails = vendor.getIabService(true).getSkuDetails(3, context.getPackageName(),
					"inapp", querySkus[0]);
			Log.i(tag, "Got skuDetails: " + skuDetails.toString());
		} catch (RemoteException e) {
			Log.e(tag, "Error retrieving sku details for product list" + e.toString());
		} catch (NullPointerException e){
			Log.e(tag, "iabService was not ready yet!" + e.toString());
		}
		return skuDetails;
	}

	@Override
	protected void onPostExecute(Bundle skuDetails) {
		super.onPostExecute(skuDetails);
		MTXItemTable items = new MTXItemTable();
		int response = skuDetails.getInt("RESPONSE_CODE");
		if(response == 0){
			ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
			for(String s : responseList){
				try {
					JSONObject obj = new JSONObject(s);
					String sku = obj.getString("productId");
					String price = obj.getString("price");
					items.add(new MTXItem(sku, price));
					Log.i(tag, "Item received: " + sku + ": " + price);
				} catch (JSONException e) {
					Log.e(tag, "Couldn't parse sku detail response: " + e.toString());
				}
			}
		} else {
			Log.e(tag, "Response not ok, was " + response);
		}
		MTX.receiveProductList(items);
	}

}
