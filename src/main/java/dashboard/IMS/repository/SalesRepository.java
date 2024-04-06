package dashboard.IMS.repository;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> origin/main

/**
 * Repository interface for Sales entity.
 * Provides access to Sales entities in the database.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {
    List<Sales> findByUserId(Integer userId);

    List<Sales> findAllByUser(UserDTO loggedInUser);

    List<Sales> findAllByUserId(Integer id);

    void deleteByProductVariationId(Integer productVariationId);
<<<<<<< HEAD

    Optional<Sales> findByIdAndUserId(Integer salesId, Integer id);

    Optional<Sales> findByProductVariationIdAndQuantitySoldAndUserId(Integer productVariationId, Integer quantity, Integer id);

    List<Sales> findByProductVariationIdAndUserIdOrderByTransactionDateDesc(Integer productVariationId, Integer id);
=======
>>>>>>> origin/main
}
