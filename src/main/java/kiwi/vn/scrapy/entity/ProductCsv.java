package kiwi.vn.scrapy.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class ProductCsv.
 */
public class ProductCsv {
	
	/** The site name. */
	private String siteName;
	
	/** The category. */
	private String category;
	
	/** The product. */
	private String product;
	
	/** The product model. */
	private String productModel;
	
	/** The description. */
	private String description;
	
	/** The price. */
	private double price;
	
	/** The quantity. */
	private int    quantity;
	
	/** The date create. */
	private String dateCreate;
	
	/** The product url. */
	private String productUrl;
	
	/** The more info. */
	private String moreInfo;

	/**
	 * Instantiates a new product csv.
	 *
	 * @param siteName the site name
	 */
	public ProductCsv(String siteName){
		this.siteName = siteName;
	}
	
	/**
	 * Gets the site name.
	 *
	 * @return the site name
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * Sets the site name.
	 *
	 * @param siteName the new site name
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * Gets the product.
	 *
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * Sets the product.
	 *
	 * @param product the new product
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * Gets the product model.
	 *
	 * @return the product model
	 */
	public String getProductModel() {
		return productModel;
	}

	/**
	 * Sets the product model.
	 *
	 * @param productModel the new product model
	 */
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Sets the price.
	 *
	 * @param price the new price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity.
	 *
	 * @param quantity the new quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the date create.
	 *
	 * @return the date create
	 */
	public String getDateCreate() {
		return dateCreate;
	}

	/**
	 * Sets the date create.
	 *
	 * @param dateCreate the new date create
	 */
	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}

	/**
	 * Gets the product url.
	 *
	 * @return the product url
	 */
	public String getProductUrl() {
		return productUrl;
	}

	/**
	 * Sets the product url.
	 *
	 * @param productUrl the new product url
	 */
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	/**
	 * Gets the more info.
	 *
	 * @return the more info
	 */
	public String getMoreInfo() {
		return moreInfo;
	}

	/**
	 * Sets the more info.
	 *
	 * @param moreInfo the new more info
	 */
	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}
	
	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}
