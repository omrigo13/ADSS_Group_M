package ServiceLayer;

import DataAccessLaye.*;

import MessageTypes.Damaged;
import MessageTypes.ItemWarning;
import MessageTypes.StockReport;
import MessageTypes.ToOrder;
import ServiceLayer.ServiceObjects.BranchDTO;
import ServiceLayer.ServiceObjects.OrderDTO;
import bussinessLayer.BranchPackage.Branch;
import bussinessLayer.BranchPackage.BranchController;
import bussinessLayer.OrderPackage.Order;
import bussinessLayer.SupplierPackage.Supplier;

import java.sql.SQLException;
import java.util.*;

public class BranchService {
    private BranchController branchController;

    public BranchService() {
        this.branchController = BranchController.getInstance();
    }

    public Response updateItemShelfQuantity(int branchId, int itemId, int delta) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
        try {
            this.branchController.getBranches().get(branchId).editShelfQuantity(itemId, delta);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        Response response = new Response();
        response.setMessage("Shelf quantity was edited, for branch id: " + branchId);
        return response;
    }

    public void updateBranchController() throws SQLException {
        List<BranchDTO> list = Repo.getInstance().getAllBranches();
        branchController.setIdCounter(list.size());
        Map<Integer, Branch> map = new HashMap<>();
        for (BranchDTO branchDTO: list)
        {
            map.put(branchDTO.getId(), branchDTO.convertFromDTO());
        }
    }

    public Response createBranch(String description) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
        int id = 0;
        try {
            id = this.branchController.createBranch(description);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        Response response = new Response();
        response.setMessage("Branch was created successfully, with id: " + id);
        return response;
    }

    public Response updateItemStockQuantity(int branchId, int itemId, int delta) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
        try {
            this.branchController.getBranches().get(branchId).editStockQuantity(itemId, delta);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        Response response = new Response();
        response.setMessage("Stock quantity was edited, for branch id: " + branchId);
        return response;
    }

    public Response cancelCard(int branchId, int itemId, int quantityToCancel) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
        try {
            this.branchController.getBranches().get(branchId).cancelCard(itemId, quantityToCancel);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        Response response = new Response();
        response.setMessage("Quantity was updated according to cancel card");
        return response;
    }

    public Response updateBranchDescription(int branchId, String description) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
        try {
            this.branchController.getBranches().get(branchId).setDescription(description);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        Response response = new Response();
        response.setMessage("Description was edited, for branch id: " + branchId);
        return response;
    }

    public Response updateDamagedItem(int branchId, int itemId, int delta) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
        try {
            this.branchController.getBranches().get(branchId).updateDamagedItem(itemId, delta);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        Response response = new Response();
        response.setMessage("Damaged quantity for item " + itemId + "was updated, at branchId " + branchId);
        return response;
    }

    /*
     * arguments: string of categories: category, subCategory, sub2Category. to
     * generate report for all of the items, input empty array.
     */
    public ResponseT<StockReport> generateStockReport(int branchId, String[] categories) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new ResponseT<StockReport>(e.getMessage());
        }

        StockReport report = null;
        try {
            report = this.branchController.getBranches().get(branchId).generateStockReport(categories);
        } catch (SQLException throwables) {
            return new ResponseT<StockReport>(throwables.getSQLState());
        }
        return new ResponseT<StockReport>(report);
    }

    public ResponseT<Damaged> generateDamagedReport(int branchId) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new ResponseT<Damaged>(e.getMessage());
        }
        Damaged report = new Damaged(new HashMap<>());
        try {
            report.setDamagedById(this.branchController.getBranches().get(branchId).generateDamagedReport());
        } catch (SQLException throwables) {
            return new ResponseT<Damaged>(throwables.getSQLState());
        }

        return new ResponseT<Damaged>(report);
    }

    public ResponseT<ItemWarning> generateWarningReport(int branchId) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new ResponseT<ItemWarning>(e.getMessage());
        }
        ItemWarning report = new ItemWarning(new HashMap<>());
        try {
            report.setWarningById(this.branchController.getBranches().get(branchId).generateWarningReport());
        } catch (SQLException throwables) {
            return new ResponseT<ItemWarning>(throwables.getSQLState());
        }

        return new ResponseT<ItemWarning>(report);
    }

    public ResponseT<ToOrder> generateToOrderReport(int branchId) {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new ResponseT<ToOrder>(e.getMessage());
        }
        ToOrder report = new ToOrder();
        try {
            report.setOrderById(this.branchController.getBranches().get(branchId).generateToOrderReport());
        } catch (SQLException throwables) {
            return new ResponseT<ToOrder>(throwables.getSQLState());
        }
        // DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        // System.out.println(df.format(report.dateProduced));

        return new ResponseT<ToOrder>(report);
    }

    public ResponseT<ToOrder> generateAndSendOrder(int branchId, List<Supplier> suppliersForBranchId) throws Exception {
        try {
            updateBranchController();
        }
        catch (Exception e) {
            return new ResponseT<ToOrder>(e.getMessage());
        }
        int chosenForAnItem = -1; //represents supplier id that is cheapest
        double priceAfterDiscount = -1;
        boolean foundExistOrderBySup = false;
        ResponseT<ToOrder> report = this.generateToOrderReport(branchId);
        double cheapestPriceForItem = -1;
        List<Order> orderList = new ArrayList<>();
        Supplier chosenSup = suppliersForBranchId.get(0);
        for (Integer itemId : report.getObj().getOrderById().keySet()) {
            for (Supplier sup : suppliersForBranchId) {
                try {
                    priceAfterDiscount = sup.getPriceForItemWithAmountAfterDiscount(itemId, report.getObj().getOrderById().get(itemId)); //arg. 2 returns amount to order
                } catch (Exception e) {
                    continue;
                }
                if (cheapestPriceForItem == -1 || priceAfterDiscount < cheapestPriceForItem) {
                    cheapestPriceForItem = priceAfterDiscount;
                    chosenForAnItem = sup.getSupplierId();
                    chosenSup = sup;
                }
            }
            if (orderList.size() > 0) {
                for (Order order : orderList) {
                    if (order.getSupplierId() == chosenForAnItem) {
                        order.addItemToCart(chosenSup.getCatalogItemIdByItem(itemId), report.getObj().getOrderById().get(itemId));
                        foundExistOrderBySup = true;
                        break;
                    }
                    if (foundExistOrderBySup) break;
                    orderList.add(new Order(chosenSup, branchId));
                    foundExistOrderBySup = false;
                }
            } else {
                orderList.add(new Order(chosenSup, branchId));
                orderList.get(0).addItemToCart(chosenSup.getCatalogItemIdByItem(itemId), report.getObj().getOrderById().get(itemId));
                foundExistOrderBySup = false;
            }

        }
        for (Order order : orderList) {
            order.sendOrder();
            OrderDTO orderDTO = order.converToDTO();
            Repo.getInstance().insertOrder(orderDTO);
        }
        return report;
    }

}
