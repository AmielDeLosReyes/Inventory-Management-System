package dashboard.IMS.repository;

import dashboard.IMS.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Sales entity.
 * Provides access to Sales entities in the database.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {
}
