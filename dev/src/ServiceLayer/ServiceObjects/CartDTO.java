package ServiceLayer.ServiceObjects;

import java.util.List;

public class CartDTO {

	private List<LineCatalogItemDTO> lineItems;
	private int totalAmount;
	private double totalPrice;

	public CartDTO(List<LineCatalogItemDTO> lineItems, int totalAmount, double totalPrice) {
		this.lineItems = lineItems;
		this.totalAmount = totalAmount;
		this.totalPrice = totalPrice;
	}

	public List<LineCatalogItemDTO> getLineItems() {
		return lineItems;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	@Override
	public String toString() {
		String s = "\nCatalog item ID:\t\t";
		for (LineCatalogItemDTO lineItem : lineItems) {
			s += lineItem.toString() +"\n";
		}

		return s;
	}
    
}