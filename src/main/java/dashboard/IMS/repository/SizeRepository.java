package dashboard.IMS.repository;

import dashboard.IMS.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Size entity.
 * Provides access to Size entities in the database.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
public interface SizeRepository extends JpaRepository<Size, Integer> {
    // Add custom queries if needed
}
