package ad.smartstudio.caea.controller;

import ad.smartstudio.caea.aspect.Audit;
import ad.smartstudio.caea.model.dto.CaeaLineExcel;
import ad.smartstudio.caea.model.entity.Activitat;
import ad.smartstudio.caea.model.entity.Grup;
import ad.smartstudio.caea.model.entity.Seccio;
import ad.smartstudio.caea.service.ActivityService;
import com.poiji.bind.Poiji;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/v1")
public class ActivityController {
	private ActivityService service;

	@Autowired
	public ActivityController(ActivityService service) {
		this.service = service;
	}

	@Audit(message = "Activities")
	@GetMapping(value = "/activity", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Seccio>> getAll() {
		return ResponseEntity.ok(service.findAllBySections());
	}

	@Audit(message = "Update activity")
	@PutMapping(value = "/activity", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Activitat> update(@RequestBody Activitat activity) {
		Activitat act = service.findById(activity.getId());
		act.setStatus(activity.getStatus());
		act.setLastUpdated(new Date().getTime());
		return ResponseEntity.ok(service.save(act));
	}

	@Audit(message = "Update activity")
	@GetMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> importExcel() {
		File f = new File("/Users/zer0/Desktop/caea.xlsx");
		List<CaeaLineExcel> lines = Poiji.fromExcel(f, CaeaLineExcel.class);
		Grup group = new Grup();
		Seccio seccio = new Seccio();
		for (CaeaLineExcel line : lines) {
			try {
				if (line.getGrup().contains(",")) {
					line.setGrup(line.getGrup().replace(",", "."));
				}
			} catch (Exception e) {
				System.out.println(line.toString());
			}
			try {
				if (line.getType().contains("7.")) {
					Activitat a = new Activitat();
					a.setLastUpdated(new Date().getTime());
					a.setClassification(line.getGrup());
					a.setDescription(line.getDescripcio());
					a.setGrupId(group.getId());
					try {
						a.setQuantity(Integer.parseInt(line.getNumero()));
					} catch (Exception e) {
						a.setQuantity(0);
					}
					a.setStatus("Obert".equals(line.getStatus()));
					this.service.save(a);
				} else if (line.getType().contains("4.")) {
					Grup g = new Grup();
					g.setClassification(line.getGrup());
					g.setDescription(line.getDescripcio());
					g.setSeccioId(seccio.getId());
					try {
						g.setQuantity(Integer.parseInt(line.getNumero()));
					} catch (Exception e) {
						g.setQuantity(0);
					}
					g = this.service.saveGroup(g);
					group = g;
				} else if (line.getType().contains("1.")) {
					Seccio s = new Seccio();
					s.setClassification(line.getGrup());
					s.setDescription(line.getDescripcio());
					try {
						s.setQuantity(Integer.parseInt(line.getNumero()));
					} catch (Exception e) {
						s.setQuantity(0);
					}
					s = this.service.saveSeccio(s);
					seccio = s;
				}
			} catch (Exception e) {
				System.out.println(line);
			}
		}
		return ResponseEntity.ok("Done");
	}

	@Audit(message = "Update activity")
	@GetMapping(value = "/fix", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> fixRows() {
		List<Seccio> seccios = this.service.findAllBySections();
		for (Seccio s : seccios) {
			if (s.getClassification().contains(",")) {
				s.setClassification(s.getClassification().replace(',', '.'));
				this.service.saveSeccio(s);
			}
			for (Grup g : s.getGrups()) {
				if (g.getClassification().contains(",")) {
					g.setClassification(g.getClassification().replace(',', '.'));
					this.service.saveGroup(g);
				}
				for (Activitat a : g.getActivitats()) {
					if (a.getClassification().contains(",")) {
						a.setClassification(a.getClassification().replace(',', '.'));
						this.service.save(a);
					}
				}
			}
		}
		return ResponseEntity.ok("Done");
	}
}
