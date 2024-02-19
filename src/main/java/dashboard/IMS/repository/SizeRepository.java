package dashboard.IMS.repository;

import dashboard.IMS.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    // Add custom queries if needed
}
