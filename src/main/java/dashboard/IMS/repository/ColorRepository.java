package dashboard.IMS.repository;

import dashboard.IMS.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    // You can add custom query methods if needed
}

