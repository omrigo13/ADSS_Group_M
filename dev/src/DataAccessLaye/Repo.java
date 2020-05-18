package DataAccessLaye;

import ServiceLayer.ServiceObjects.*;
import bussinessLayer.SupplierPackage.Supplier;
import javafx.util.Pair;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Repo {
    private static Repo repo = null;
    private static Connection con;
    private IBranchDAO branchDAO;
    private ICatalogItemDAO catalogItemDAO;
    private IContactDAO contactDao;
    private IDamagedControllerDAO damagedItemDAO;
    private IDeliveryDaysDAO deliveryDaysDAO;
    private IInventoryDAO inventoryDAO;
    private IItemDAO itemDAO;
    private IItemStatusDAO itemStatusDAO;
    private ILineCatalogItemInCartDAO lineCatalogItemInCartDAO;
    private IOrderDAO orderDAO;
    private IRangesDAO rangesDAODAO;
    private IScheduledOrderDAO scheduledDAO;
    private ISupplierDAO supplierDAO;
    private IContractDAO contractDAO;
    //private IOldCostPriceDAO oldCostPriceDAO;
    //private IOldSalePriceDAO oldSalePriceDAO;

    private Repo() throws SQLException {
    	try {
			con = getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        branchDAO = new BranchDAOImpl(con);
        catalogItemDAO = new CatalogItemDAOImpl(con);
        contactDao = new ContactDaoImpl(con);
        damagedItemDAO = new DamagedControllerDAOImpl(con);
        deliveryDaysDAO = new DeliveryDaysDAOImpl(con);
        inventoryDAO = new InventoryDAOImpl(con);
        itemDAO = new ItemDAOImpl(con);
        itemStatusDAO = new ItemStatusDAOImpl(con);
        lineCatalogItemInCartDAO = new LineCatalogItemInCartDAOImpl(con);
        orderDAO = new OrderDAOImpl(con);
        rangesDAODAO = new RangesDAODAOImpl(con);
        scheduledDAO = new ScheduledDAOImpl(con);
        supplierDAO = new SupplierDAOImpl(con);
        contractDAO = new ContractDAOImpl(con);
    }

    public static Repo getInstance() throws SQLException {
        if (repo == null) {
        	repo = new Repo();
        }
        return repo;
    }
    
    //public static final String DB_URL = "jdbc:sqlite:C:/Users/nivod/Desktop/ADSS_Group_M/Nituz.db";
    public static final String DB_URL = "jdbc:sqlite:Nituz.db";

    public static final String DRIVER = "org.sqlite.JDBC";  

    public static Connection getConnection() throws ClassNotFoundException {  
        Class.forName(DRIVER);  
        Connection connection = null;  
        try {  
            SQLiteConfig config = new SQLiteConfig();  
            config.enforceForeignKeys(true);  
            connection = DriverManager.getConnection(DB_URL,config.toProperties());  
        } catch (SQLException ex) {}  
        return connection;  
    }

    public void creatTables() throws SQLException {
    	

        String sqlQ = "CREATE TABLE IF NOT EXISTS Suppliers("
                + "supplierName varchar(50),"
                + "supplierId INTEGER NOT NULL,"
                + "bankAccountNumber INTEGER,"
                + "bilingOptions varchar(50),"
                + "CONSTRAINT PK_Supplier Primary KEY(supplierId)"
                + ");";
        
        Statement stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";
        
        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS Contacts("
                + "supplierId INTEGER ,"
                + "firstName varchar,"
                + "lastName varchar,"
                + "phoneNumber varchar,"
                + "address varchar,"
                + "CONSTRAINT PK_Contact Primary KEY(phoneNumber,supplierId),"
                + "CONSTRAINT FK_Contact FOREIGN KEY (supplierId) references  Suppliers(supplierId) on delete cascade"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";
        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS Contracts ("
                + "contractId INTEGER,"
                + "isDeliver Boolean,"
                + "CONSTRAINT PK_Contract Primary KEY(contractId),"
                + "CONSTRAINT FK_Contact FOREIGN KEY(contractId) references Suppliers(supplierId) on delete cascade"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";
        
        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS DeliveryDays("
                + "contractId INTEGER,"
                + "Deliday varchar,"
                + "CONSTRAINT PK_DeliDays primary KEY(Deliday,contractId),"
                + "CONSTRAINT  FK_DeliDays FOREIGN KEY (contractId) references Contracts(contractId) on delete cascade"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";
        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS Orders ("
                + "orderId INTEGER ,"
                + "branchId INTEGER ,"
                + "actualDeliverDate TIMESTAMP ,"
                + "status varchar, "
                + "supplierId INTEGER ,"
                + "creationTime TIMESTAMP ,"
                + "deliveryDate TIMESTAMP,"
                + "CONSTRAINT PK_Orders Primary KEY(orderId),"
                + "CONSTRAINT FK_Orders FOREIGN KEY (supplierId) references Suppliers(supplierId) on delete no action,"
                + "CONSTRAINT FK_ScheduledOrder3 FOREIGN KEY (branchId) references Branch(branchId) on delete no action"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";
        
        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS Ranges ("
                + "rangeId INTEGER,"
                + "catalogItemId INTEGER ,"
                + "contractId INTEGER ,"
                + "minimum INTEGER ,"
                + "maximum INTEGER ,"
                + "price REAL,"
                + "CONSTRAINT PK_Ranges Primary KEY(rangeId,catalogItemId,contractId),"
                + "CONSTRAINT FK_Ranges FOREIGN KEY(contractId) references Contracts(contractId) on delete cascade ,"
                + "CONSTRAINT FK_Ranges2 FOREIGN key(catalogItemId,contractId) references CatalogItem(catalogItemId,contractId) on delete cascade"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";
        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS CatalogItem ("
                + "catalogItemId INTEGER ,"
                + "contractId INTEGER ,"
                + "itemId INTEGER,"
                + "price REAL,"
                + "description varchar(50),"
                + "CONSTRAINT PK_CatalogItem Primary KEY(catalogItemId,contractId),"
                + "CONSTRAINT FK_CatalogItem FOREIGN KEY (contractId) references Contracts(contractId) on delete cascade"
                + ");";
        
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);

        sqlQ = "";
        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS LineCatalogItemInCart ("
                + "orderId INTEGER ,"
                + "catalogItemId INTEGER ,"
                + "amount INTEGER ,"
                + "priceAfterDiscount REAL ,"
                + "CONSTRAINT PK_LineCatalogItemInCart Primary KEY(orderId,catalogItemId),"
                + "CONSTRAINT FK_LineCatalogItemInCart FOREIGN KEY (orderId) references Orders(orderId)"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";
        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS ScheduledOrder ("
                + "Sday INTEGER ,"
                + "supplierId INTEGER ,"
                + "catalogItemId INTEGER ,"
                + "amount INTEGER ,"
                + "branchId INTEGER,"
                + "CONSTRAINT PK_ScheduledOrder Primary KEY(Sday,supplierID,catalogItemId,branchId),"
                + "CONSTRAINT  FK_ScheduledOrder FOREIGN KEY (supplierId) references Suppliers(supplierId) on delete cascade,"
                + "CONSTRAINT  FK_ScheduledOrder2 FOREIGN KEY (catalogItemId, supplierId) references CatalogItem(catalogItemId, contractId) on delete cascade,"
                + "CONSTRAINT  FK_ScheduledOrder3 FOREIGN KEY (branchId) references Branch(branchId) on delete cascade"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";
        sqlQ = sqlQ + "CREATE INDEX rangeIndex on Ranges(rangeId);"; //TODO NOT WORKING
        stmt = con.createStatement();
        try{stmt.execute(sqlQ);}catch(Exception e) {};
        sqlQ = "";

        //tables for Inventory module

        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS Branch ("
                + "branchId INTEGER ,"
                + "description varchar ,"
                + "CONSTRAINT PK_Branch Primary KEY(branchID)"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";

        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS DamagedItem ("
                + "branchId INTEGER ,"
                + "itemId INTEGER ,"
                + "quantityDamaged INTEGER ,"
                + "CONSTRAINT PK_DamagedItem Primary KEY(branchID, itemId),"
                + "CONSTRAINT FK_DamagedItem FOREIGN KEY (branchId) references Branch(branchId)"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";

        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS Inventory ("
                + "inventoryId INTEGER,"
                + "idCounter INTEGER ,"
                + "CONSTRAINT PK_Inventory Primary KEY(inventoryId)"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";

        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS Item ("
                + "itemId INTEGER ,"
                + "description varchar ,"
                + "costPrice REAL ,"
                + "salePrice REAL ,"
                + "minimumQuantity INTEGER ,"
                + "weight REAL ,"
                + "category varchar ,"
                + "subCategory varchar ,"
                + "sub2Category varchar ,"
                + "manufacturer varchar ,"
                + "costCounter INTEGER ,"
                + "saleCounter INTEGER ,"
                + "CONSTRAINT PK_Item Primary KEY(itemId)"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";

        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS ItemStatus ("
                + "branchId INTEGER ,"
                + "itemId INTEGER ,"
                + "quantityShelf INTEGER ,"
                + "quantityStock INTEGER ,"
                + "CONSTRAINT PK_ItemStatus Primary KEY(branchId, itemId),"
                + "CONSTRAINT FK_ItemStatus FOREIGN KEY (branchId) references Branch(branchId),"
                + "CONSTRAINT FK_ItemStatus2 FOREIGN KEY (itemId) references Item(itemId)"
                + ");";
        
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";
        

        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS OldCostPrice("
                + "itemId INTEGER ,"
                + "counter INTEGER ,"
                + "price INTEGER ,"
                + "CONSTRAINT PK_OldCostPrice Primary KEY(itemId, counter),"
                + "CONSTRAINT FK_OldCostPrice FOREIGN KEY (itemId) references Item(itemId)"
                + ");";
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";

        sqlQ = sqlQ + "CREATE TABLE IF NOT EXISTS OldSalePrice("
                + "itemId INTEGER ,"
                + "counter INTEGER ,"
                + "price INTEGER ,"
                + "CONSTRAINT PK_OldSalePrice Primary KEY(itemId, counter),"
                + "CONSTRAINT FK_OldSalePrice FOREIGN KEY (itemId) references Item(itemId)"
                + ");";
        stmt = con.createStatement();
        stmt.execute(sqlQ);
        sqlQ = "";

    }



    public CatalogItemDTO getCatalogItem(int catalogItemId, int contractId) throws SQLException {
        return this.catalogItemDAO.find(catalogItemId, contractId);
    }

    public void updateCatalogItem(int catalogItemId, int contractId, double price) throws SQLException {

        String sql = "UPDATE CatalogItem SET price = ? where catalogItemId = ? AND contractId = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setDouble(1, price);
        pstmt.setInt(2, catalogItemId);
        pstmt.setInt(3, contractId);
        pstmt.executeUpdate();

    }

    public void deleteCatalogItem(int contractId, int catalogItemId) throws SQLException {
        String sql = "DELETE FROM CatalogItem " +
                "WHERE contractId = ? AND  catalogItemId = ?";

        PreparedStatement stmp = con.prepareStatement(sql);
        stmp.setInt(1, contractId);
        stmp.setInt(2, catalogItemId);
        stmp.executeUpdate();

    }



    public ContactDTO getSpecificContact(int supplierId, String phoneNumber) throws SQLException {
        return this.contactDao.find(supplierId, phoneNumber);
    }

    public void updateContact(String phoneNumber, int supplierId, ContactDTO contactDTO) throws SQLException {
        String sql = "UPDATE Contacts SET phoneNumber = ? , firstName = ? ,lastName = ? , address = ? where supplierId = ? AND phoneNumber = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, phoneNumber);
        pstmt.setString(2, contactDTO.getFirstName());
        pstmt.setString(3, contactDTO.getLastName());
        pstmt.setString(4, contactDTO.getAddress());
        pstmt.setInt(5, supplierId);
        pstmt.setString(6, phoneNumber);
        pstmt.executeUpdate();
    }

    public void insertLineCatalogItem(LineCatalogItemDTO lineCatalogItemDTO, int orderId) throws SQLException {
        this.lineCatalogItemInCartDAO.insert(lineCatalogItemDTO, orderId);
    }

    public List<ContactDTO> getAllContactBySupplier(int supplierId) throws SQLException {

        return this.contactDao.findAllBySupplier(supplierId);
    }


    public ContractDTO getContract(int contractId) throws SQLException {

        return this.contractDAO.find(contractId);

    }


    public void updateContract(ContractDTO contractDTO) throws SQLException {
        String sql = "UPDATE Contracts SET isDeliver = ? where contractId = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setBoolean(1, contractDTO.getIsDeliver());
        pstmt.setInt(2, contractDTO.getSupplierId());
        pstmt.executeUpdate();
        this.deliveryDaysDAO.deleteEveryThingByContract(contractDTO.getSupplierId());
        this.deliveryDaysDAO.insertEveryTingByContract(contractDTO);

    }

    public void updateDeliveryDaysByContract(int contractId) {

    }

    public DeliveryDaysDTO getAllDeliveryDaysByContract(int contractId) throws SQLException {
        return this.deliveryDaysDAO.findAllByContract(contractId);
    }

    public void insertDeliveryDays(DeliveryDaysDTO deliveryDaysDTO , int contractId) throws SQLException {
        this.deliveryDaysDAO.insert(deliveryDaysDTO,contractId);

    }

    public void UpdateDeliveryDays(int contractId, LocalDateTime day) {

    }

    public List<LineCatalogItemDTO> getOrderItems(int orderId) {
        return null;
    }

    public void updateOrderItem(int orderId, int catalogItemId, int amount, double priceAfterDiscount) {

    }

    public void insertLineCatalogItem() {

    }

    public void deleteItemFromOrder(int catalogItemId, int orderId) throws SQLException {

        String sql = "DELETE FROM LineCatalogItemInCart " +
                "WHERE catalogItemId = ? AND orderId = ?";

        PreparedStatement stmp = con.prepareStatement(sql);
        stmp.setInt(1, catalogItemId);
        stmp.setInt(2, orderId);
        stmp.executeUpdate();

    }

    public OrderDTO getOrderByID(int orderId) throws SQLException {
        return this.orderDAO.find(orderId);
    }

    public void updateOrder(OrderDTO orderDTO) throws SQLException {
        String sql = "UPDATE Orders SET orderId = ? , branchId = ? ,actualDeliverDate = ? , status = ?, supplierId = ? , creationTime = ?,deliveryDate  where orderId = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, orderDTO.getOrderId());
        pstmt.setInt(2, orderDTO.getBranchId());
        pstmt.setTimestamp(3, Timestamp.valueOf(orderDTO.getActualDate()));
        pstmt.setString(4, orderDTO.getOrderStatus());
        pstmt.setInt(5, orderDTO.getSupplierId());
        pstmt.setTimestamp(6, Timestamp.valueOf(orderDTO.getCreationDate()));
        pstmt.setTimestamp(7, Timestamp.valueOf(orderDTO.getDeliveryDate()));
        pstmt.setInt(8, orderDTO.getOrderId());
        pstmt.executeUpdate();
        orderDTO.getCart().getLineItems();
        orderDTO.getCart().getTotalAmount();
        orderDTO.getCart().getTotalPrice();
        CartDTO cartDTO = orderDTO.getCart();
        for (LineCatalogItemDTO lineCatalogItem : cartDTO.getLineItems()) {
            LineCatalogItemDTO lineCatalogItemDTO = new LineCatalogItemDTO(lineCatalogItem.getCatalogItem(), lineCatalogItem.getAmount(), lineCatalogItem.getPriceAfterDiscount());
            this.UpdateLineCatalog(lineCatalogItemDTO, orderDTO.getOrderId());
        }

    }

    private void UpdateLineCatalog(LineCatalogItemDTO lineCatalogItemDTO, int orderId) throws SQLException {

        String sql = "UPDATE LineCatalogItemInCart SET orderId = ? , catalogItemId = ? ,amount = ? , priceAfterDiscount = ? where orderId = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        pstmt.setInt(2, lineCatalogItemDTO.getCatalogItemId());
        pstmt.setInt(3, lineCatalogItemDTO.getAmount());
        pstmt.setDouble(4, lineCatalogItemDTO.getPriceAfterDiscount());
        pstmt.setInt(5, orderId);

    }

    public CatalogDTO getCatalog(int supplierId) throws SQLException {
        List<CatalogItemDTO> catalogItemDTOS = this.catalogItemDAO.findAll(supplierId);
        return new CatalogDTO(catalogItemDTOS);
    }

    public List<OrderDTO> getSupplierOrders(int supplierId) throws SQLException {
        List<OrderDTO> allOrders = this.orderDAO.findAll();
        List<OrderDTO> supplierOrders = new ArrayList<>();
        for (OrderDTO orderDTO : allOrders) {
            if (orderDTO.getSupplierId() == supplierId) supplierOrders.add(orderDTO);
        }
        return supplierOrders;
    }

    public void insertOrder(OrderDTO orderDTO) throws SQLException {
        this.orderDAO.insert(orderDTO);
    }

    public HashMap<Integer, List<Pair<RangeDTO, Double>>> getAllRangesByContract(int contractId) throws SQLException {
        return this.rangesDAODAO.findAll(contractId);
    }

    public List<Pair<RangeDTO, Double>> getAllRangeForCatalogItemId(int contractId, int catalogItemId) throws SQLException {
        HashMap<Integer, List<Pair<RangeDTO, Double>>> allRangesByContract = this.rangesDAODAO.findAll(contractId);
        List<Pair<RangeDTO, Double>> allRangesForCatalogItem = new ArrayList<>();
        for (Pair<RangeDTO, Double> pair : allRangesByContract.get(catalogItemId)) {
            allRangesForCatalogItem.add(pair);
        }
        return allRangesForCatalogItem;
    }

    public void deleteAllRangesByContractId(int contractId, int catalogItemId) throws SQLException {


        String sql = "DELETE FROM Ranges " +
                "WHERE catalogItemId = ? AND contractId = ?";

        PreparedStatement stmp = con.prepareStatement(sql);
        stmp.setInt(1, catalogItemId);
        stmp.setInt(2, contractId);
        stmp.executeUpdate();

    }

  /*  public void updateRange(RangeDTO rangeDTO, ContractDTO contractDTO, double price) throws SQLException {
        String sql = "DELETE FROM LineCatalogItemInCart\n" +
                "WHERE catalogItemId = ? AND orderId = ?;";

        PreparedStatement stmp = con.prepareStatement(sql);
        stmp.setInt(1, catalogItemId);
        stmp.setInt(2, orderId);
        stmp.executeUpdate();
    }
*/
    public ScheduledDTO getSpecificScheduled(int branchId, int day, int supplierId) throws Exception {

        List<ScheduledDTO> scheduledDTOS = this.scheduledDAO.findAll();
        for (ScheduledDTO scheduledDTO : scheduledDTOS) {
            if (scheduledDTO.getSupplierId() == supplierId && scheduledDTO.getDay().getValue() == day && scheduledDTO.getBranchId() == branchId) {
                return scheduledDTO;
            }
        }

        throw new Exception("Scheduled order not found by input you provied");
    }

    public List<ScheduledDTO> getAllScheduled() throws SQLException {
        return this.scheduledDAO.findAll();
    }

    public void deleteScheduledBySupplier(int supplierId) {

    }

    public SupplierDTO getSupplierById(int supplierId) throws SQLException {
        return this.supplierDAO.find(supplierId);
    }

    public void updateSupplier(SupplierDTO supplierDTO) throws SQLException {
        String sql = "UPDATE Suppliers SET supplierId = ? ,supplierName = ?, bankAccountNumber = ? ,bilingOptions = ?  where supplierId = ? ";
        PreparedStatement stmp = con.prepareStatement(sql);
        stmp.setInt(1, supplierDTO.getSupplierId());
        stmp.setString(2, supplierDTO.getName());
        stmp.setInt(3, supplierDTO.getBankAccountNumber());
        stmp.setString(4, supplierDTO.getBillingOption().name());
        stmp.setInt(5, supplierDTO.getSupplierId());
        stmp.executeUpdate();
        this.updateContract(supplierDTO.getContractDTO());
        for (ContactDTO contactDTO : supplierDTO.getContactDTOS()) {
            this.updateContact(contactDTO.getPhoneNumber(), supplierDTO.getSupplierId(), contactDTO);
        }

    }

    public List<SupplierDTO> getAllSuppliers() throws SQLException {
        return this.supplierDAO.findAll();
    }

    public void insertSupplier(Supplier supplier) throws SQLException {
        this.supplierDAO.insertSupplier(supplier);
    }

    public int getSupplierIdByOrder(int orderId) throws SQLException {
        return this.orderDAO.find(orderId).getSupplierId();
    }

    public OrderDTO getOrderByDateSupplier(int supplierId, int branchId, LocalDateTime deliveryDate) throws Exception {
        List<OrderDTO> allOrders = this.orderDAO.findAll();
        for (OrderDTO orderDTO : allOrders) {
            if (orderDTO.getSupplierId() == supplierId && orderDTO.getDeliveryDate().equals(deliveryDate) && orderDTO.getBranchId() == branchId)
                return orderDTO;
        }
        throw new Exception("order dose not exsit by branch id , date and supplierId");
    }

    public List<OrderDTO> getAllOrderByBranchId(int barnchId) throws SQLException {
        List<OrderDTO> allOrders = this.orderDAO.findAll();
        List<OrderDTO> orderByBranchId = new ArrayList<>();
        for (OrderDTO orderDTO : allOrders) {
            if (orderDTO.getBranchId() == barnchId)
                orderByBranchId.add(orderDTO);
        }
        return orderByBranchId;
    }

    public void insertScheduled(ScheduledDTO schedule) throws SQLException {
        this.scheduledDAO.insert(schedule);
    }

    /**
     * Get item description of specific item
     *
     * @param itemId The item ID
     * @return item description
     */
    public String getItemDescription(int itemId) throws Exception {
		/*
		 * List<ItemDTO> itemDTOS = this.itemDAO.findAll(); for (ItemDTO itemDTO :
		 * itemDTOS) { if (itemDTO.getId() == itemId) return itemDTO.getDescription(); }
		 * throw new Exception("Item do not found!");
		 */
    	
    	//TODO DELETE AFTER
    	
    	String desc = "";

		switch(itemId) {
		case 1:
			desc = "milk";
			break;
		case 2:
			desc = "meat";
			break;
		case 3:
			desc = "cornflakes";
			break;
		case 4: 
			desc = "cigarretes";
			break;
		}
		return desc;
    }

    public void insertContact(int supplierId, ContactDTO contactDTO) throws SQLException {
        this.contactDao.insert(contactDTO, supplierId);
    }

    public void deleteContact(String phoneNumber, int supplierId) throws SQLException {
        String sql = "DELETE FROM Contacts " +
                "WHERE PhoneNumber = ? AND supplierId = ?";

        PreparedStatement stmp = con.prepareStatement(sql);
        stmp.setString(1, phoneNumber);
        stmp.setInt(2, supplierId);
        stmp.executeUpdate();

    }

    public BranchDTO getBranchById(int branchId) throws SQLException {
	    return this.branchDAO.find(branchId);
    }

    public void createBranch(BranchDTO branch) throws SQLException {
        this.branchDAO.insert(branch);
    }

    public List<BranchDTO> getAllBranches() throws SQLException {
	    return this.branchDAO.findAll();
    }


    public void updateBranchDescription(int branchId, String description) throws SQLException {
	    this.branchDAO.updateDescription(branchId, description);
    }

    public DamagedControllerDTO getDamagedControllerForBranch(int branchId) throws SQLException{
	    return this.damagedItemDAO.findDamageController(branchId);
    }

    public void insertNewDamagedItem(int branchID,int itemId, int quantity) throws SQLException{
	    damagedItemDAO.insertDamagedItem(branchID,itemId, quantity);
    }

    public List<DamagedControllerDTO> getAllDamagedControllers() throws SQLException{
	    return damagedItemDAO.findAll();
    }

    public void updateExistingDamagedItem(int branchId, int itemId, int newQuantity) throws SQLException{
	    damagedItemDAO.updateAnItem( branchId,  itemId,  newQuantity);
    }

    public InventoryDTO getInventory() throws SQLException{
	    return inventoryDAO.find();
    }

    public void createInventory(InventoryDTO inventoryDTO) throws SQLException{
	    inventoryDAO.insert(inventoryDTO);
    }

    public void updateInventoryIdCounter(int idCounter) throws SQLException{
	    inventoryDAO.updateIdCounter(idCounter);
    }

    public ItemDTO getItem(int itemId) throws SQLException{
        return itemDAO.find(itemId);
    }

    public void addNewItem(ItemDTO itemDTO) throws SQLException{
	    itemDAO.insert(itemDTO);
    }

    public List<ItemDTO> getAllItems() throws SQLException{
	    return itemDAO.findAll();
    }

    public void updateAnItemWithoutOldPrices(ItemDTO itemDTO) throws SQLException{
        itemDAO.updateWithoutOldPrices(itemDTO);
    }

    public void updateCostPriceForItem(int itemId, double newPrice,double oldPrice) throws SQLException{
	    itemDAO.updateCostPrice( itemId,  newPrice,oldPrice);
    }

    public void updateSalePriceForItem(int itemId, double newPrice,double oldPrice) throws SQLException{
	    itemDAO.updateSalePrice(itemId,  newPrice,oldPrice);
    }

    public ItemStatusDTO getItemStatus(int branchId, int itemId) throws SQLException{
	    return itemStatusDAO.find(branchId, itemId);
    }

    public void addItemStatus(ItemStatusDTO itemStatusDTO) throws SQLException{
	    itemStatusDAO.insert(itemStatusDTO);
    }

    public List<ItemStatusDTO> getAllItemStatusByBranch(int branchId) throws SQLException{
	    return itemStatusDAO.findAllByBranch(branchId);
    }

    public void updateAnItemStatus(ItemStatusDTO itemStatusDTO) throws SQLException{
	    itemStatusDAO.updateAStatus(itemStatusDTO);
    }



    public void insertRange(RangeDTO rangeDTO, int contractId, int catalogItemId, double price) throws SQLException {
        this.rangesDAODAO.insert(rangeDTO, contractId, catalogItemId, price);
    }

    public void deleteSupplierById(int supplierId) throws SQLException {

        String sql = "DELETE FROM Suppliers " +
                "WHERE supplierId = ?";

        PreparedStatement stmp = con.prepareStatement(sql);
        stmp.setInt(1, supplierId);
        stmp.executeUpdate();

    }

    public void insertCatalogItem(CatalogItemDTO catalogItemDTO, int contractId) throws SQLException {
        this.catalogItemDAO.insert(catalogItemDTO, contractId);
    }
    
    public void close() throws SQLException {
    	con.close();
    }

	public void deleteConstDelivery(int supplierId) throws SQLException {
		this.deliveryDaysDAO.deleteEveryThingByContract(supplierId);
		
	}

    public boolean isInventoryExist() throws SQLException {
        return this.inventoryDAO.isAlreadyExist();
    }
}
