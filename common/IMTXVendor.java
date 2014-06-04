package org.rautasydan.mtx.common;


public interface IMTXVendor {
	public boolean buyItem(MTXItem itm);
	public void getProductList();
	public void receiveProductList(MTXItemTable items);
	public void receivePurchase(MTXItem item); 
	public void destroy();
	public String[] getSkuList();

}
