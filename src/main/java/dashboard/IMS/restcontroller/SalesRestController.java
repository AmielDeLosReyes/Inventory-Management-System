package dashboard.IMS.restcontroller;

import dashboard.IMS.dto.SalesDTO;
import dashboard.IMS.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller class for handling sales-related endpoints.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/25/2024
 */
@RestController
@RequestMapping("/sales")
public class SalesRestController {

    @Autowired
    private SalesService salesService;

    /**
     * Endpoint to create a new sales record.
     *
     * @param salesDTO The DTO representing the sales record to be created.
     * @return ResponseEntity with HTTP status indicating success or failure.
     */
    @PostMapping("/create")
    public ResponseEntity<SalesDTO> createSales(@RequestBody SalesDTO salesDTO) {
        SalesDTO createdSales = salesService.createSales(salesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSales);
    }

    /**
     * Endpoint to retrieve a sales record by its ID.
     *
     * @param id The ID of the sales record to retrieve.
     * @return ResponseEntity with the retrieved sales record or not found status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalesDTO> getSalesById(@PathVariable Integer id) {
        SalesDTO salesDTO = salesService.getSalesById(id);
        if (salesDTO != null) {
            return ResponseEntity.ok(salesDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to retrieve all sales records.
     *
     * @return ResponseEntity with a list of all sales records.
     */
    @GetMapping("/all")
    public ResponseEntity<List<SalesDTO>> getAllSales() {
        List<SalesDTO> allSales = salesService.getAllSales();
        return ResponseEntity.ok(allSales);
    }

    /**
     * Endpoint to update a sales record.
     *
     * @param id       The ID of the sales record to update.
     * @param salesDTO The DTO representing the updated sales record.
     * @return ResponseEntity with the updated sales record or not found status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalesDTO> updateSales(@PathVariable Integer id, @RequestBody SalesDTO salesDTO) {
        SalesDTO updatedSales = salesService.updateSales(id, salesDTO);
        if (updatedSales != null) {
            return ResponseEntity.ok(updatedSales);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to delete a sales record by its ID.
     *
     * @param id The ID of the sales record to delete.
     * @return ResponseEntity with no content status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSales(@PathVariable Integer id) {
        salesService.deleteSales(id);
        return ResponseEntity.noContent().build();
    }
}
