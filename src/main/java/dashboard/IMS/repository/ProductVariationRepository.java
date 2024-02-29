package dashboard.IMS.repository;

import dashboard.IMS.entity.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ProductVariation entity.
 * Provides access to ProductVariation entities in the database.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Integer> {

    @Query("SELECT pv.product.id, SUM(pv.quantity) " +
            "FROM ProductVariation pv " +
            "GROUP BY pv.product.id")
    List<Object[]> getTotalQuantities();
}
