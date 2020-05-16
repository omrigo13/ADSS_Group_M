package DataAccessLaye;

import ServiceLayer.ServiceObjects.*;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduledDAOImpl implements IScheduledOrderDAO {
    private Connection conn;
    private CatalogItemDAOImpl catalogItemDAO;

    public ScheduledDAOImpl(Connection conn) {
        this.conn = conn;
        catalogItemDAO = new CatalogItemDAOImpl(conn);
    }

    @Override
    public List<ScheduledDTO> findAll() throws SQLException {
        List<ScheduledDTO> scheduledDTOS = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM ScheduledOrder  order by branchId,supplierId,Sday";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        int currentBranchId = rs.getInt("branchId");
        int currentSupplier = rs.getInt("supplierId");
        int currentDay = rs.getInt("Sday");
        int catalogItemId;
        int Sday;
        int branchId;
        int supplierID;
        List<Pair<CatalogItemDTO, Integer>> itemsToOrder = new ArrayList<Pair<CatalogItemDTO, Integer>>();
        while (true) {
            Sday = rs.getInt("Sday");
            supplierID = rs.getInt("supplierId");
            catalogItemId = rs.getInt("catalogItemId");
            int amount = rs.getInt("amount");
            branchId = rs.getInt("branchId");
            itemsToOrder.add(new Pair<>(new CatalogItemDTO(catalogItemDAO.find(catalogItemId, supplierID)), amount));
            boolean t = rs.next();
            if (currentSupplier != supplierID || currentDay != Sday || branchId != currentBranchId) {
                currentSupplier = supplierID;
                currentDay = Sday;
                currentBranchId = branchId;
                scheduledDTOS.add(new ScheduledDTO(Sday, supplierID, itemsToOrder,branchId));
            }
            if (!t) break;
        }
        return scheduledDTOS;
    }


    @Override
    public void insert(ScheduledDTO scheduledDTO) throws SQLException {
        String sql = "INSERT INTO ScheduledOrder(Sday, supplierId,catalogItemId,amount,branchId) VALUES(?,?,?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (Pair<CatalogItemDTO, Integer> pair : scheduledDTO.getItemsToOrder())
        {
            pstmt.setInt(1, scheduledDTO.getDay().getValue());
            pstmt.setInt(2, scheduledDTO.getSupplierId());
            pstmt.setInt(3, pair.getKey().getCatalogItemId());
            pstmt.setInt(4, pair.getValue());
            pstmt.setInt(5,scheduledDTO.getBranchId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteScheduledBySupplier(int supplierId) {

    }
}
