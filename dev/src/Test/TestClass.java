package Test;
import Data.Data;
import bussinessLayer.Supplier;
import org.junit.jupiter.api.Test;

import ServiceLayer.OrderService;
import ServiceLayer.SupplierService;
import bussinessLayer.Order;

import  static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestClass {
	
	private static SupplierService supService = SupplierService.getInstance();
	private static OrderService oService = OrderService.getInstance();
	
	
	@BeforeAll
	public static void setup() {
		supService.loadFirstSuppliers();
		oService.loadFirstItems();
	}
	

    @Test
    public void CheckAddingItemsToCatalogItems(){
    }
    
    @Test
    public void createOrder(){
    	List<Order> orders = Data.getOrders();
    	int ordersSize = orders.size();
    	oService.createAnOrder(1);
    	assertEquals(ordersSize+1, orders.size(), "Size of orders wrong");
    }
    @Test
    public void creatSupplier(){
	    List<Supplier> suppliers = Data.getSuppliers();
	    int suppliersSize = suppliers.size();
	    supService.AddSupplier("d",0,1,"EOM30",true);
	    assertEquals(suppliersSize+1,suppliers.size(),"Size of suppliers wrong");
    }
    @Test
    public void c(){
	    List<Supplier> suppliers = Data.getSuppliers();
        supService.AddSupplier("d",0,1,"EOM30",true);
        int catalogSize = supService.getSupplierById(0).getContract().getCatalog().getItems().size();
        supService.addCatalogItemToCatalogInContract(0,1,10,6);
        assertEquals(catalogSize+1,supService.getSupplierById(0).getContract().getCatalog().getItems().size(),"Size of catalogItem wrong");
    }
    @Test
    public void d(){
    }
    @Test
    public void e(){
    }
    @Test
    public void f(){
    }
    @Test
    public void g(){
    }
}