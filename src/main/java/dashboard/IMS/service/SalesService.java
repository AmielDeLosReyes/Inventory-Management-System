package dashboard.IMS.service;

import dashboard.IMS.dto.SalesDTO;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.repository.SalesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Sales entity.
 * Handles business logic related to Sales entities.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Service
public class SalesService {

    private final SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    /**
     * Creates a new Sales record.
     *
     * @param salesDTO The DTO representing the Sales record to be created.
     * @return The created Sales DTO.
     */
    public SalesDTO createSales(SalesDTO salesDTO) {
        Sales sales = new Sales();
        BeanUtils.copyProperties(salesDTO, sales);
        Sales savedEntity = salesRepository.save(sales);
        return toDTO(savedEntity);
    }

    /**
     * Retrieves all Sales records.
     *
     * @return A list of all Sales DTOs.
     */
    public List<SalesDTO> getAllSales() {
        List<Sales> salesEntities = salesRepository.findAll();
        return salesEntities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a Sales record by its ID.
     *
     * @param id The ID of the Sales record to retrieve.
     * @return The Sales DTO if found, otherwise null.
     */
    public SalesDTO getSalesById(Integer id) {
        Optional<Sales> salesOptional = salesRepository.findById(id);
        return salesOptional.map(this::toDTO).orElse(null);
    }

    /**
     * Updates a Sales record.
     *
     * @param id       The ID of the Sales record to update.
     * @param salesDTO The DTO representing the updated Sales record.
     * @return The updated Sales DTO if successful, otherwise null.
     */
    public SalesDTO updateSales(Integer id, SalesDTO salesDTO) {
        Optional<Sales> salesOptional = salesRepository.findById(id);
        if (salesOptional.isPresent()) {
            Sales existingEntity = salesOptional.get();
            BeanUtils.copyProperties(salesDTO, existingEntity);
            Sales updatedEntity = salesRepository.save(existingEntity);
            // Convert the updated entity to DTO
            SalesDTO updatedDto = toDTO(updatedEntity);

            // Check if all properties are copied correctly
            if(updatedDto.getId() != null && updatedDto.getProductVariationId() != null &&
                    updatedDto.getQuantitySold() != null && updatedDto.getTotalRevenue() != null &&
                    updatedDto.getTotalCost() != null && updatedDto.getTotalProfit() != null) {
                return updatedDto;
            } else {
                return null;
            }
        }
        return null; // Or throw an exception indicating the sales record was not found
    }


    /**
     * Deletes a Sales record by its ID.
     *
     * @param id The ID of the Sales record to delete.
     */
    public void deleteSales(Integer id) {
        salesRepository.deleteById(id);
    }

    /**
     * Converts a Sales entity to a DTO.
     *
     * @param entity The Sales entity.
     * @return The corresponding Sales DTO.
     */
    private SalesDTO toDTO(Sales entity) {
        if (entity == null) {
            return null; // Return null if entity is null
        }
        SalesDTO dto = new SalesDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public void deleteSalesByProductVariationId(Integer productVariationId) {
        salesRepository.deleteByProductVariationId(productVariationId);
    }
}
