package dashboard.IMS.repository;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    Optional<Sales> findByIdAndUserId(Integer salesId, Integer id);

    Optional<Sales> findByProductVariationIdAndQuantitySoldAndUserId(Integer productVariationId, Integer quantity, Integer id);

    List<Sales> findByProductVariationIdAndUserIdOrderByTransactionDateDesc(Integer productVariationId, Integer id);

    Page<Sales> findByUser(User user, Pageable pageable);
}
