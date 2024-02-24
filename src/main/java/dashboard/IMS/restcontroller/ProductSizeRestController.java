package dashboard.IMS.restcontroller;

import dashboard.IMS.entity.Size;
import dashboard.IMS.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sizes")
public class ProductSizeRestController {

    @Autowired
    private SizeService sizeService;

    @GetMapping
    public List<Size> getAllSizes() {
        // Retrieve all sizes from the database using the SizeService
        return sizeService.getAllSizes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Size> getSizeById(@PathVariable Integer id) {
        // Retrieve a size by its ID from the database using the SizeService
        Size size = sizeService.getSizeById(id);
        return size != null ? ResponseEntity.ok(size) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Size> createSize(@RequestBody Size size) {
        // Save the provided size in the database using the SizeService
        Size savedSize = sizeService.saveSize(size);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSize);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Size> updateSize(@PathVariable Integer id, @RequestBody Size sizeDetails) {
        // Update the existing size with the given ID using the SizeService
        Size updatedSize = sizeService.updateSize(id, sizeDetails);
        return updatedSize != null ? ResponseEntity.ok(updatedSize) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSizeById(@PathVariable Integer id) {
        // Delete the size by its ID from the database using the SizeService
        sizeService.deleteSizeById(id);
        return ResponseEntity.noContent().build();
    }
}
