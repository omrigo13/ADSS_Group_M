package DataAccessLaye;

import ServiceLayer.ServiceObjects.RangeDTO;
import javafx.util.Pair;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class RangesDAODAOImpl implements IRangesDAO {
    private Connection conn;
    public RangesDAODAOImpl(Connection conn)
    {
        this.conn = conn;
    }
    @Override
    public HashMap<Integer, List<Pair<RangeDTO, Double>>> getAllRangesByContract(int contractId) {
        return null;
    }

    @Override
    public List<Pair<RangeDTO, Double>> getAllRangeForCatalogItemId(int contractId, int catalogItemId) {
        return null;
    }

    @Override
    public void deleteAllRangesByContractId(int contractId, int catalogItemId) {

    }
}
