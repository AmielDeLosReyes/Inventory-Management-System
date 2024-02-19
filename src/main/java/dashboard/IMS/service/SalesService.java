package dashboard.IMS.service;

import dashboard.IMS.dto.SalesDTO;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.repository.SalesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalesService {

    private final SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public SalesDTO createSales(SalesDTO salesDTO) {
        Sales Sales = new Sales();
        BeanUtils.copyProperties(salesDTO, Sales);
        Sales savedEntity = salesRepository.save(Sales);
        return toDTO(savedEntity);
    }

    public List<SalesDTO> getAllSales() {
        List<Sales> salesEntities = salesRepository.findAll();
        return salesEntities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public SalesDTO getSalesById(Integer id) {
        Optional<Sales> salesOptional = salesRepository.findById(id);
        return salesOptional.map(this::toDTO).orElse(null);
    }

    public SalesDTO updateSales(Integer id, SalesDTO salesDTO) {
        Optional<Sales> salesOptional = salesRepository.findById(id);
        if (salesOptional.isPresent()) {
            Sales existingEntity = salesOptional.get();
            BeanUtils.copyProperties(salesDTO, existingEntity);
            Sales updatedEntity = salesRepository.save(existingEntity);
            return toDTO(updatedEntity);
        }
        return null; // Or throw an exception indicating the sales record was not found
    }

    public void deleteSales(Integer id) {
        salesRepository.deleteById(id);
    }

    private SalesDTO toDTO(Sales entity) {
        SalesDTO dto = new SalesDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
