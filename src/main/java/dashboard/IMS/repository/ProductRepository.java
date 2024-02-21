package dashboard.IMS.repository;

import dashboard.IMS.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Product entity.
 * Provides access to Product entities in the database.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // You can add custom query methods here if needed
    Product findByProductName(String productName);
}
