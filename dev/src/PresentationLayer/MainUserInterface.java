package PresentationLayer;

import java.util.Scanner;

import ServiceLayer.*;
import sun.misc.OSEnvironment;

public class MainUserInterface {


    private OrderService oService;
    private SupplierService supService;

    public MainUserInterface() {
        this.supService = new SupplierService();
        this.oService = new OrderService();
    }

    public void start(){
        loadProgramDefault();
        int input=0;
        do{
            printMenu();
            input = Integer.valueOf(getUserInput());
            switch(input){
                case 1:
                    makeAnOrder(); //ORDER MENU
                    break;
                case 2:
                    printSuppliersDetails(); // TODO DOR PEREZ
                    break;
                case 3:
                    printOrdersFromSupplier(); // PRINTS ALL ORDERS FROM SUPPLIER
                    break;
                case 4:
                    printSupplierCatalog(); //TODO DOR PEREZ
                    break;
                case 5:
                    endOrder();
                    break;
                case 6:
                    getOrderStatus(); // TODO REDUNDANT DELETE (INSIDE CASE 7)
                    break;
                case 7:
                    getOrderDetails();//GET ORDER DETAILS OF A specific order
                    break;
                case 8:
                    Quit(); //QUIT
                    break;
                default:
                    break;
            }
        } while(input!=8);
    }

    private void Quit() {
        System.out.println("BYE!");
    }

    private void getOrderDetails() { // GET ORDER DETAILS OF ORDER NO.@orderId
        System.out.println("Enter order ID:\n");
        int orderId = Integer.valueOf(getUserInput());
        oService.getOrderDetails(orderId);
    }

    private void endOrder() { // CHANGES ORDER STATUS TO COMPLETE
        System.out.println("Enter order ID:\n");
        int orderId = Integer.valueOf(getUserInput());
        oService.endOrder(orderId);
    }

    private void printOrdersFromSupplier() { // PRINTS ALL ORDERS FROM SUPPLIER
        int supplierId = Integer.valueOf(getUserInput());
        System.out.println(oService.printOrdersFromSupplier(supplierId));
    }

    private void makeAnOrder() { // ORDER MENU
        int input=0;
        int supplierId=0;
        System.out.println("Enter supplier ID:\n");
        supplierId = Integer.valueOf(getUserInput());
        int orderId = oService.createAnOrder(supplierId);
        System.out.println("1) Add item\n2) Remove item\n3) Confirm order");
        
        do{
            input = Integer.valueOf(getUserInput());
            switch(input){
                case 1:
                    addItemToCart(orderId); //ADD ITEM TO CART
                    break;
                case 2:
                    removeItemFromCart(orderId); // REMOVES AN ITEM FROM CART
                    break;
                case 3:
                    oService.sendOrder(orderId); //CONFIRM ORDER(SEND ORDER())
                    break;
            }
        } while(input!=3);
    }

    private void removeItemFromCart(int orderId) { // REMOVES AN ITEM FROM CART
        System.out.println("Enter catalog item ID: itemId:Amount\n");
        String catalogItemId = getUserInput();
        oService.removeFromCart(orderId, Integer.valueOf(catalogItemId));
    }

    private void addItemToCart(int orderId) { //ADD ITEM TO CART
        System.out.println("Enter as Follow: CatalogItemId:Amount\n");
        String split[] = getUserInput().split(":");
        oService.addItemToCart(orderId, Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }

    public void loadProgramDefault() { //LOADING THE PROGRAM WITH BASIC OBJECTS OR CLEAN START
        System.out.println("1) Load with objects\n2) Clean start");
        if(!getUserInput().equals("1")) return;
        //TODO FILL THE PROGRAM WITH ESSENTIAL OBJECTS
    }

    public void printMenu(){ //PRINTS THE MENU
        System.out.println("1) Make an order\n2) Print suppliers details\n3) Print all orders from supplier\n4)Print supplier catalog");
        System.out.println("5) End order\n6) Get order status\n7) Get order details\n8) Quit");
    }
    
    public String getUserInput(){ //GET USER INPUT
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        sc.close();
        return input;
    }
}