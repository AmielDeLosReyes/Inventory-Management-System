package dashboard.IMS.service;

import dashboard.IMS.entity.Size;
import dashboard.IMS.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SizeService {

    private final SizeRepository sizeRepository;

    @Autowired
    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    // Example service methods
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    public Size getSizeById(Integer id) {
        return sizeRepository.findById(id).orElse(null);
    }

    // Add other service methods as needed
}

