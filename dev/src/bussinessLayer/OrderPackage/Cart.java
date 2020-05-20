package bussinessLayer.OrderPackage;


import java.util.ArrayList;
import java.util.List;

import bussinessLayer.DTOPackage.CartDTO;
import bussinessLayer.DTOPackage.LineCatalogItemDTO;

public class Cart {
    private List<LineCatalogItem> itemsToDelivery;
    private int totalAmount;
    private double totalPrice;

    public Cart() {
        this.totalAmount = 0;
        this.totalPrice = 0;
        itemsToDelivery = new ArrayList<LineCatalogItem>();
    }

    public Cart(CartDTO cart) {
    	this();
    	getLineItems(cart);
    	this.totalAmount = cart.getTotalAmount();
    	this.totalPrice = cart.getTotalPrice();
	}

	private void getLineItems(CartDTO cart) {
		for (LineCatalogItemDTO lineCatalogItem : cart.getLineItems()) {
			itemsToDelivery.add(new LineCatalogItem(lineCatalogItem.getCatalogItem(), lineCatalogItem.getAmount(), lineCatalogItem.getPriceAfterDiscount()));
		}
	}

	public void addItemToCart(bussinessLayer.SupplierPackage.CatalogItem catItem, int amount, double priceAfterDiscount) {
        itemsToDelivery.add(new LineCatalogItem(catItem, amount, priceAfterDiscount));
        this.totalAmount += amount;
        totalPrice += priceAfterDiscount * (double) amount;
    }

    public void removeFromCart(int catalogItemId) throws Exception {
        int i = 0;
        for (LineCatalogItem lineCatItem : itemsToDelivery) {
            if (lineCatItem.getCatalogItemId() == catalogItemId) {
                itemsToDelivery.remove(i);
                this.totalAmount -= lineCatItem.getAmount();
                totalPrice -= (double) lineCatItem.getAmount() * lineCatItem.getPriceAfterDiscount();
                return;
            }
            i += 1;
        }
        throw new Exception("Item does not Exist in the cart.");
    }

    /**
     * @return the totalAmount
     */
    public int getTotalAmount() {
        return totalAmount;
    }

    /**
     * @return the totalPrice
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        String s = "\nItem name:ID\tPrice\tAmount";
        for (LineCatalogItem item : itemsToDelivery) {
            s += "\n" + item.toString();
        }
        s += "\nTotal:\t" + totalPrice;
        return s;
    }

    public List<LineCatalogItem> getItemsToDelivery() {
        return itemsToDelivery;
    }

	public CartDTO converT7oDTO() {
        List<LineCatalogItemDTO> list = new ArrayList<LineCatalogItemDTO>();
        for (LineCatalogItem lineCatalogItem : itemsToDelivery) {
            list.add(lineCatalogItem.converToDTO());
        }
		return new CartDTO(list, totalAmount, totalPrice);
	}

	public double getPriceAfterDiscount(int catalogItemId) throws Exception {
		for (LineCatalogItem line : itemsToDelivery) {
            if(line.getCatalogItemId() == catalogItemId) return line.getPriceAfterDiscount();
        }

        throw new Exception("Item not found in cart");
	}

	public LineCatalogItemDTO getLineCatalogItemDTO(int catalogItemId) throws Exception {
        for (LineCatalogItem lineCatalogItem : itemsToDelivery) {
            if(lineCatalogItem.getCatalogItemId() == catalogItemId) return lineCatalogItem.converToDTO(); 
        }
        throw new Exception("Line Item not found!");
	}

	public void updateCartBeforeReturningToUser() {
		totalAmount = 0;
		totalPrice = 0;
		for (LineCatalogItem lineCatalogItem : itemsToDelivery) {
			totalAmount += lineCatalogItem.getAmount();
			totalPrice += lineCatalogItem.getAmount()*lineCatalogItem.getPriceAfterDiscount();
		}
		
	}


}
