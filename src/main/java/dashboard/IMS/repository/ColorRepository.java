package dashboard.IMS.repository;

import dashboard.IMS.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Color entity.
 * Provides access to Color entities in the database.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    // You can add custom query methods if needed
}
