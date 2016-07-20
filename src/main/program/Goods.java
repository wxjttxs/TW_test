package program;

public class Goods{

	private String barcode;
	private String name;
	private String unit;
	private String category;
	private String subcategory;
	private float price;
	
	Goods(){ }
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subcategory;
	}
	public void setSubCategory(String subcategory) {
		this.subcategory = subcategory;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String toString(){
		return barcode +" "+ name +" " + unit +" "+ category +" "+ subcategory +" "+ price;
		
	}
}