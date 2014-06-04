package org.rautasydan.mtx.common;

public class MTXItem {
	String id;
	String priceString;
	int quantity;
	float valuePerItem;

	public MTXItem(String itemID, String price){
		id = itemID;
		priceString = price;
	}
	public MTXItem(String itemID, float itemValue) {
		id = itemID;
		quantity = 1;
		valuePerItem = itemValue;
	}
	public MTXItem(String itemID, int itemQuantity, float itemValue){
		id = itemID;
		quantity = itemQuantity;
		valuePerItem = itemValue;
	}

	public String getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public float getValuePerItem() {
		return valuePerItem;
	}
	
	public float getTotalValue() {
		return valuePerItem * quantity;
	}
	public String getPriceString(){
		if(priceString == null || priceString.equals("")){
			return valuePerItem + "";
		} else {
			return priceString;
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setValuePerItem(float valuePerItem) {
		this.valuePerItem = valuePerItem;
	}
	

}
