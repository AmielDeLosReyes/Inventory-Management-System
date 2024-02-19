package dashboard.IMS.service;

import dashboard.IMS.entity.Color;
import dashboard.IMS.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService {
    private final ColorRepository colorRepository;

    @Autowired
    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public Color saveColor(Color color) {
        return colorRepository.save(color);
    }

    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    public Color getColorById(Integer id) {
        return colorRepository.findById(id).orElse(null);
    }

    public void deleteColorById(Integer id) {
        colorRepository.deleteById(id);
    }
}

