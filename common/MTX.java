package org.rautasydan.mtx.common;

import java.lang.reflect.Constructor;
import java.util.ArrayList;


public class MTX {
	static IMTXVendor vendor;
	private static ArrayList<String> skuList;
	public MTXItemTable products;
	
	public MTX(Class<? extends IMTXVendor> vendorType) {
		IMTXVendor newVendor;
		
		try {
			Constructor<? extends IMTXVendor> c = vendorType.getConstructor();
			newVendor = vendorType.cast(c.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
			newVendor = null;
		}
		vendor = newVendor;
		skuList = new ArrayList<String>();
		
		for(String s : vendor.getSkuList()){
			skuList.add(s);
		}
		
	}
	
	public static void setVendor(IMTXVendor v){
		vendor = v;
	}
	public MTXItem purchase(MTXItem item){
		if(!vendor.buyItem(item)){
			item.setQuantity(0);
		}		
		return item;
	}

	public static void receivePurchase(MTXItem item){
		vendor.receivePurchase(item);
	}
	public void destroy() {
		vendor.destroy();
		
	}
	public void getProductList(){
		vendor.getProductList();
	}
	public void setSkuList(ArrayList<String> itemSkuList){
		skuList = itemSkuList;
	}
	public static ArrayList<String> getSkuList(){
		if(skuList == null){
			skuList = new ArrayList<String>();
		} 
		return skuList;
	}
	public static void receiveProductList(MTXItemTable items){
		vendor.receiveProductList(items);
	}
}
