package DataAccessLaye;

import ServiceLayer.ServiceObjects.BranchDTO;
import ServiceLayer.ServiceObjects.CatalogItemDTO;

import java.sql.SQLException;
import java.util.List;

public interface IBranchDAO {
    BranchDTO find (int branchId) throws SQLException;

    void insert(BranchDTO branchDTO) throws SQLException;

    List<BranchDTO> findAll() throws SQLException;
}